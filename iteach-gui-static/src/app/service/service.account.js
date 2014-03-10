angular.module('iteach.service.account', [
        'iteach.ui.account'
    ])
    .service('accountService', function ($log, uiAccount) {

        var self = {

        };

        self.init = function init() {
            $log.info('Initializing the account');
            var account = uiAccount.current();
            $log.debug('Current account', account);
        };

        return self;

    })
;