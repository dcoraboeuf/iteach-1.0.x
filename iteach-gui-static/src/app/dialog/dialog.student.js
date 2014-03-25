angular.module('iteach.dialog.student', [
        'iteach.ui.teacher'
    ])
    .controller('dialogStudent', function ($log, $scope, $modalInstance, modalController, initialStudent, notificationService, uiTeacher) {

        $scope.student = initialStudent;
        uiTeacher.getSchools().then(function (schools) {
            $scope.schools = schools
        })

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function (isValid) {
            if (isValid) {
                modalController.onSubmit($scope.student).then(
                    function () {
                        $modalInstance.close('ok')
                    },
                    function (message) {
                        $scope.error = message
                    }
                )
            }
        }

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;