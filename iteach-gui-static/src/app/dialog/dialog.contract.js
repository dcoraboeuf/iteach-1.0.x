angular.module('iteach.dialog.contract', [
        'iteach.ui.teacher'
    ])
    .controller('dialogContract', function ($log, $scope, $modalInstance, modalController, school, contract, notificationService) {

        // TODO hourlyRatePattern and vatRatePattern are also declared in dialog.school.js
        $scope.hourlyRatePattern = /^([A-Z]{3} )?\d+(\.\d+)?$/;
        $scope.vatRatePattern = /^([0-9]+([\.,][0-9]+)?)$/;
        $scope.school = school.data;
        $scope.contract = contract;

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function (isValid) {
            if (isValid) {
                modalController.onSubmit($scope.contract).then(
                    function () {
                        $modalInstance.close('ok')
                    },
                    function (message) {
                        $scope.error = message
                    }
                )
            }
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;