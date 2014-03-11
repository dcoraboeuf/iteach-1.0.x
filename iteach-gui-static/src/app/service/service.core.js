angular.module('iteach.service.core', [])
    .service('notificationService', function ($log) {
        var self = {
            message: undefined,
            messageType: 'default'
        };
        self.clear = function () {
            $log.debug('Notification', 'clear');
            self.message = undefined;
            self.messageType = 'success';
        };
        self.success = function (message) {
            $log.debug('Notification', 'success', message);
            self.message = message;
            self.messageType = 'success';
        };
        self.warning = function (message) {
            $log.debug('Notification', 'warning', message);
            self.message = message;
            self.messageType = 'warning';
        };
        self.error = function (message) {
            $log.debug('Notification', 'error', message);
            self.message = message;
            self.messageType = 'danger';
        };
        return self;
    })
;