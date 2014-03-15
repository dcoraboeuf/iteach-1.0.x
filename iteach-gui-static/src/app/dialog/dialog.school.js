angular.module('iteach.dialog.school', [])
    .controller('dialogSchool', function ($log, $scope, $modalInstance) {

        $scope.school = {};

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function (isValid) {
            if (isValid) {
                $log.debug('name=', $scope.school.name);
                $log.debug('hourlyRate=', $scope.school.hourlyRate);
            }
            // $modalInstance.close('ok')
        }

    })
;