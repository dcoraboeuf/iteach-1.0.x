angular.module('iteach.dialog.calendar.preferences', [
])
    .controller('dialogCalendarPreferences', function ($scope, $modalInstance, calendarPreferences, modalController, notificationService) {

        // Hours only
        $scope.calendarPreferences = {
            minHour: Number.parseInt(calendarPreferences.data.minTime.substring(0, 2), 10),
            maxHour: Number.parseInt(calendarPreferences.data.maxTime.substring(0, 2), 10)
        };

        console.log('calendarPreferences=', $scope.calendarPreferences);

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function () {
            modalController.onSubmit($scope.profile).then(
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