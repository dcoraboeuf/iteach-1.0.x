angular.module('iteach.service.account', [
        'iteach.service.core',
        'iteach.ui.account'
    ])
    .service('accountService', function ($log, $location, $translate, notificationService, uiAccount) {

        var self = {

        };

        self.onAccount = function onAccount(account) {
            // Home page
            if (account.authenticated) {
                if (account.teacher.administrator) {
                    // TODO Admin page
                    $log.debug('Going to the admin page');
                } else {
                    // Teacher home page
                    $log.debug('Going to the teacher page');
                    $location.path('/teacher');
                }
            } else {
                // Login page
                $log.debug('Going to the login page');
                $location.path('/login');
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
                self.onAccount(account)
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