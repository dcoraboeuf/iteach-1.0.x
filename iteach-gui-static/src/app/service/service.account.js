angular.module('iteach.service.account', [
        'iteach.ui.account'
    ])
    .service('accountService', function ($log, uiAccount) {

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
                    // TODO Teacher home page
                    $log.debug('Going to the teacher page');
                }
            } else {
                // TODO Login page
                $log.debug('Going to the login page');
            }
        };

        return self;

    })
;