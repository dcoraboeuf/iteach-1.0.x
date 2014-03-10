angular.module('iteach.service.account', [
        'iteach.ui.account'
    ])
    .service('accountService', function ($log, $location, uiAccount) {

        var self = {

        };

        self.init = function init() {
            $log.debug('Initializing the account');
            var account = uiAccount.current();
            $log.debug('Current account', account);
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
        };

        return self;

    })
;