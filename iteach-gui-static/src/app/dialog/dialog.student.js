angular.module('iteach.dialog.student', [
    'iteach.ui.teacher'
])
    .controller('dialogStudent', function ($log, $scope, $modalInstance, modalController, initialStudent, notificationService, uiTeacher) {

        $scope.student = initialStudent;
        $scope.contracts = [];
        uiTeacher.getSchools().then(function (schools) {
            $scope.schools = schools;
            if ($scope.student.school && $scope.student.school.id) {
                loadContracts($scope.student.school.id);
            }
        });

        function loadContracts(schoolId) {
            uiTeacher.getContracts(schoolId).success(function (contractCollection) {
                $scope.contracts = contractCollection.resources;
            });
        }

        $scope.$watch('student.school.id', function (schoolId) {
            if (schoolId) {
                loadContracts(schoolId);
            } else {
                $scope.contracts = [];
            }
        });

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function (isValid) {
            if (isValid) {
                $scope.student.schoolId = $scope.student.school.id;
                modalController.onSubmit($scope.student).then(
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