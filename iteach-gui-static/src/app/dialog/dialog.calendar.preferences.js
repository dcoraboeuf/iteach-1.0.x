angular.module('iteach.dialog.calendar.preferences', [
])
    .controller('dialogCalendarPreferences', function ($scope, $modalInstance, calendarPreferences, modalController, notificationService) {

        // Hours only
        $scope.calendarPreferences = {
            minHour: Number.parseInt(calendarPreferences.data.minTime.substring(0, 2), 10),
            maxHour: Number.parseInt(calendarPreferences.data.maxTime.substring(0, 2), 10),
            weekEnds: calendarPreferences.data.weekEnds
        };

        console.log('calendarPreferences=', $scope.calendarPreferences);

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        function toTime(hour) {
            var time = '';
            if (hour < 10) {
                time += '0' + hour;
            } else {
                time += hour;
            }
            return time + ':00';
        }

        $scope.submit = function () {
            var preferences = {
                minTime: toTime($scope.calendarPreferences.minHour),
                maxTime: toTime($scope.calendarPreferences.maxHour),
                weekEnds: $scope.calendarPreferences.weekEnds
            };
            modalController.onSubmit(preferences).then(
                function () {
                    $modalInstance.close('ok')
                },
                function (message) {
                    $scope.error = message
                }
            );
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;