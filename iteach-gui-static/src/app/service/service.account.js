angular.module('iteach.service.account', [
        'iteach.service.core',
        'iteach.ui.account'
    ])
    .service('accountService', function ($log, $location, $route, $translate, notificationService, uiAccount) {

        var self = {

        };

        self.onAccount = function onAccount(account, logging) {
            $log.debug('onAccount', account);
            $log.debug('onAccount. logging = ' + logging);
            $log.debug('onAccount. route = ' + $route.current);
            $log.debug('onAccount. path = ' + $location.path());
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
                if ($route.current) {
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
                if (current == '/register' || current == '/login' || current.match(/\/registration\/.*/)) {
                    // Stays on the page
                } else {
                    // Login page
                    $log.debug('Going to the login page');
                    $location.path('/login');
                }
            }
        }

        self.init = function init() {
            $log.debug('Initializing the account');
            var account = uiAccount.current();
            $log.debug('Current account', account);
            self.onAccount(account);
        };

        self.iteachLogin = function iteachLogin(email, password) {
            uiAccount.iteachLogin(email, password).then(function (account) {
                self.onAccount(account, true)
            })
        };

        self.registerWithPassword = function (name, email, password) {
            uiAccount.registerWithPassword(name, email, password).then(function (id) {
                if (id.success) {
                    notificationService.success($translate.instant('register.success'))
                } else {
                    notificationService.error($translate.instant('register.failure'))
                }
            })
        }

        return self;

    })
;