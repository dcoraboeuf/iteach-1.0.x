angular.module('iteach.dialog.lesson', [
        'iteach.ui.teacher'
    ])
    .controller('dialogLesson', function ($log, $scope, $translate, $modalInstance, modalController, input, notificationService, uiTeacher) {

        $scope.lesson = {
            date: input.start,
            from: input.start,
            to: input.end
        };
        uiTeacher.getStudents().then(function (students) {
            $scope.students = students
        });

        // Calendar mngt
        $scope.dateFormat = $translate.instant('calendar.dateFormat');

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

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
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;