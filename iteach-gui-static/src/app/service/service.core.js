angular.module('iteach.service.core', [
    'iteach.alert.confirm'
])
    .service('calendarService', function ($translate) {
        var self = {};
        self.calendarI18n = function () {
            return _calendar_i18n[$translate.use()];
        };
        self.getMonths = function () {
            var months = [];
            var monthNames = self.calendarI18n().monthNames;
            for (var i = 0; i < 12; i++) {
                months.push({
                    index: i + 1,
                    name: monthNames[i]
                });
            }
            return months;
        };
        self.getMonthName = function (i) {
            var monthNames = self.calendarI18n().monthNames;
            return monthNames[i - 1];
        };
        return self;
    })
    .service('routeService', function ($rootScope, $location, notificationService) {
        var self = {};
        self.routeWithSuccess = function (path, message) {
            $rootScope.preserveMessage = message;
            $location.path(path);
            notificationService.success(message);
        };
        return self;
    })
    .service('notificationService', function ($log, $rootScope) {
        var self = {
            scopes: [ $rootScope ]
        };
        self.clear = function () {
            $log.debug('Notification', 'clear');
            self.getScope().message = undefined;
            self.getScope().messageType = 'success';
        };
        self.success = function (message) {
            $log.debug('Notification', 'success', message);
            self.message('success', message);
        };
        self.warning = function (message) {
            $log.debug('Notification', 'warning', message);
            self.message('warning', message);
        };
        self.error = function (message) {
            $log.debug('Notification', 'error', message);
            self.message('danger', message);
        };
        self.message = function (messageType, message) {
            var scope = self.getScope();
            scope.message = message;
            scope.messageType = messageType;
        };
        self.getScope = function () {
            return self.scopes[self.scopes.length - 1]
        };
        self.pushScope = function (scope) {
            // Clears the previous scope
            self.clear();
            // Adds the new scope
            self.scopes.push(scope)
        };
        self.popScope = function () {
            self.scopes.pop()
        };
        return self;
    })
    .service('errorService', function ($interpolate, $log, notificationService) {
        var self = {};
        self.errorMsg = function (status, data) {
            // TODO Uses translations
            if (status == 401 || status == 403 || status == 404) {
                return '';
            } else if (status == 400) {
                return data;
            } else {
                return 'Connection problem';
            }
        };
        self.process = function (response) {
            if (response.config.httpIgnoreError) return;
            var status = response.status;
            var method = response.config.method;
            var url = response.config.url;
            // Logging
            // TODO Uses translations
            var log = $interpolate('HTTP error {{status}} for {{method}} {{url}}')({
                status: status,
                method: method,
                url: url
            });
            $log.error('[app] ' + log);
            // Displays a notification
            var errorMessage = self.errorMsg(status, response.data);
            if (errorMessage) {
                notificationService.error(errorMessage);
            }
        };
        return self;
    })
    .service('alertService', function ($modal) {
        var self = {};
        /**
         * Displays a confirmation box.
         * @param config.title Title for the alert
         * @param config.message Message content for the alert
         * @returns Promise on the confirmation result.
         */
        self.confirm = function (config) {
            //noinspection JSUnusedGlobalSymbols
            return $modal.open({
                templateUrl: 'app/dialog/alert.confirm.tpl.html',
                controller: 'alertConfirm',
                resolve: {
                    alertConfig: function () {
                        return config;
                    }
                }
            }).result;
        };
        return self;
    })
/**
 * Access to the local storage in a centralized way
 */
    .service('localDataService', function () {
        var self = {};

        self.getCurrentDate = function () {
            var currentDate = localStorage['iteachCurrentDate'];
            return currentDate ? new Date(currentDate) : new Date();
        };

        self.setCurrentDate = function (date) {
            localStorage['iteachCurrentDate'] = date;
        };

        self.clearCurrentDate = function () {
            localStorage.removeItem('iteachCurrentDate');
        };

        self.getCurrentPlanningViewMode = function () {
            var value = localStorage['iteachCurrentPlanningViewMode'];
            return value ? value : "agendaWeek";
        };

        self.setCurrentPlanningViewMode = function (value) {
            localStorage['iteachCurrentPlanningViewMode'] = value;
        };

        return self;
    })
;