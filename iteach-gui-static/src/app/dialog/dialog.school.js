angular.module('iteach.dialog.school', [])
    .controller('dialogSchool', function ($log, $scope, $modalInstance) {

        $scope.school = {
            name: 'test'
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function () {
            $log.debug('name=', $scope.school.name);
            // $modalInstance.close('ok')
        }

    })
;