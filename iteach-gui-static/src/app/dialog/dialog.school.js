angular.module('iteach.dialog.school', [])
    .controller('dialogSchool', function ($scope, $modalInstance) {

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function () {
            alert("Submit")
            // $modalInstance.close('ok')
        }

    })
;