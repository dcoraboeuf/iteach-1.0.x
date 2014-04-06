angular.module('iteach.service.account', [
        'iteach.service.core',
        'iteach.ui.account',
        'iteach.dialog.account.import',
        'iteach.dialog.account.profile'
    ])
    .service('accountService', function ($q, $modal, $rootScope, $log, $location, $translate, notificationService, alertService, uiAccount) {

        var self = {

        };

        self.onAccount = function onAccount(account, logging) {
            $log.debug('onAccount', account);
            $log.debug('onAccount. logging = ' + logging);
            var currentPath = $location.path();
            $log.debug('onAccount. path = ' + currentPath);
            $rootScope.account = account;
            // Logging
            if (logging) {
                if (account.authenticated) {
                    if (account.teacher.administrator) {
                        $log.debug('Going to the admin page');
                        $location.path('/admin');
                    } else {
                        $log.debug('Going to the teacher page');
                        $location.path('/teacher');
                    }
                } else {
                    notificationService.error($translate.instant('login.error'))
                }
            }
            // Refresh
            else if (account.authenticated) {
                if (currentPath != '' && currentPath != '/') {
                    // Stays on the page if a route is already defined
                } else if (account.teacher.administrator) {
                    $log.debug('Going to the admin page');
                    $location.path('/admin');
                } else {
                    // Teacher home page
                    $log.debug('Going to the teacher page');
                    $location.path('/teacher');
                }
            } else {
                var current = $location.path();
                if (current == '/register'
                    || current == '/login'
                    || current.match(/\/registration\/.*/)
                    || current.match(/\/passwordChangeRequest\/.*/)
                ) {
                    // Stays on the page
                } else {
                    // Login page
                    $log.debug('Going to the login page');
                    $location.path('/login');
                }
            }
        };

        self.init = function init() {
            $log.debug('Initializing the account');
            uiAccount.current().then(function (account) {
                self.onAccount(account, false);
            });
        };

        self.logout = function () {
            uiAccount.logout().then(function () {
                $log.debug('User disconnected. Going back to the login page.');
                $rootScope.account = undefined;
                $location.path('/login');
            });
        };

        self.iteachLogin = function iteachLogin(email, password) {
            var d = $q.defer();
            uiAccount.iteachLogin(email, password).then(function (account) {
                self.onAccount(account, true);
                d.resolve();
            }, function () {
                d.reject();
            });
            return d.promise;
        };

        self.registerWithPassword = function (name, email, password) {
            uiAccount.registerWithPassword(name, email, password).then(function (id) {
                if (id.success) {
                    notificationService.success($translate.instant('register.success'))
                } else {
                    notificationService.error($translate.instant('register.failure'))
                }
            })
        };

        self.getAccounts = uiAccount.getAccounts;

        self.getAccount = uiAccount.getAccount;

        self.deleteAccount = function (accountId) {
            return self.getAccount(accountId).success(function (account) {
                alertService.confirm({
                    title: account.name,
                    message: $translate.instant('admin.account.delete.prompt')
                }).then(function () {
                        return uiAccount.deleteAccount(accountId)
                    })
            })
        };

        self.importAccount = function (account) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.account.import.tpl.html',
                controller: 'dialogAccountImport',
                resolve: {
                    account: function () {
                        return account;
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (file) {
                                return uiAccount.importAccount(account.id, file);
                            }
                        }
                    }
                }
            }).result;
        };

        self.disableAccount = function (accountId) {
            var d = $q.defer();
            self.getAccount(accountId).success(function (account) {
                if (account.disabled) {
                    d.reject();
                } else {
                    alertService.confirm({
                        title: account.name,
                        message: $translate.instant('admin.account.disable.prompt')
                    }).then(function () {
                            uiAccount.disableAccount(accountId).success(function () {
                                d.resolve()
                            })
                        })
                }
            });
            return d.promise;
        };

        self.enableAccount = uiAccount.enableAccount;

        self.accountProfile = function () {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.account.profile.tpl.html',
                controller: 'dialogAccountProfile',
                resolve: {
                    modalController: function () {
                        return {
                            onSubmit: function (profile) {
                                return uiAccount.updateAccountProfile(profile);
                            }
                        }
                    }
                }
            }).result;
        };

        self.loadSetup = uiAccount.loadSetup;
        self.saveSetup = uiAccount.saveSetup;

        self.passwordChangeRequest = uiAccount.passwordChangeRequest;
        self.passwordChange = uiAccount.passwordChange;

        return self;

    })
;