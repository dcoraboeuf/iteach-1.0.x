angular.module('iteach.dialog.school', [
        'iteach.dialog.form'
    ])
    .controller('dialogSchool', function ($scope, $modalInstance, formService) {

        $scope.form = formService.load('teacher/school/form');

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

    })
;