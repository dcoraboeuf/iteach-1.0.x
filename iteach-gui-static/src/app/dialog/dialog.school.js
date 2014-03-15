angular.module('iteach.dialog.school', [])
    .controller('dialogSchool', function ($log, $scope, $modalInstance) {

        $scope.name = 'test';

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function () {
            $log.debug('name=', $scope.name);
            // $modalInstance.close('ok')
        }

    })
;