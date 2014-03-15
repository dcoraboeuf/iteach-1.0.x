angular.module('iteach.dialog.school', [])
    .controller('dialogSchool', function ($log, $scope, $modalInstance, modalController) {

        $scope.colourPattern = /#[0-9A-Fa-f]{6}/;
        $scope.hourlyRatePattern = /^([A-Z]{3} )?\d+(\.\d+)?$/;
        $scope.school = {};

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function (isValid) {
            if (isValid) {
                modalController.onSubmit($scope.school).then(
                    function () {
                        $modalInstance.close('ok')
                    },
                    function () {
                        alert('error')
                    }
                )
            }
        }

    })
;