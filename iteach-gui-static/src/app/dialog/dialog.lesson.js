angular.module('iteach.dialog.lesson', [
        'iteach.ui.teacher'
    ])
    .controller('dialogLesson', function ($log, $scope, $modalInstance, modalController, input, notificationService, uiTeacher) {

        $scope.lesson = {};
        uiTeacher.getStudents().then(function (students) {
            $scope.students = students
        })

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        }

        $scope.submit = function (isValid) {
            if (isValid) {
                modalController.onSubmit($scope.lesson).then(
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