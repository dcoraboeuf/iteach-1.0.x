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
    .service('errorService', function ($interpolate, $log, notificationService) {
        var self = {};
        self.errorMsg = function (text, status) {
            // TODO Uses translations
            if (status == 401) {
                return 'Not authenticated';
            } else if (status == 403) {
                return 'Forbidden access';
            } else if (status == 404) {
                return 'Resource not found';
            } else {
                return text;
            }
        };
        self.process = function (response) {
            var status = response.status;
            var method = response.config.method;
            var url = response.config.url;
            // Logging
            // TODO Uses translations
            var log = $interpolate('[app] HTTP error {{status}} for {{method}} {{url}}')({
                status: status,
                method: method,
                url: url
            });
            $log.error(log);
            // Displays a notification
            notificationService.error(
                self.errorMsg(
                    response.data,
                    status
                )
            );
        };
        return self;
    })
;