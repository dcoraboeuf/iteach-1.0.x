angular.module('iteach.dialog.calendar.preferences', [
        'iteach.ui.teacher'
    ])
    .controller('dialogCalendarPreferences', function ($scope, $modalInstance, modalController, notificationService, uiTeacher) {

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