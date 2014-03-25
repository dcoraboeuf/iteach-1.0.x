angular.module('iteach.view.student', [
        'iteach.service.teacher'
    ])
    .controller('StudentCtrl', function ($scope, $routeParams, teacherService) {

        var studentId = $routeParams.studentId;

        function loadStudent() {
            teacherService.getStudent(studentId).success(function (student) {
                $scope.student = student;
            })
        }

        // Loads the school
        loadStudent();

        // TODO Updating the student
        $scope.update = function () {
            teacherService.updateStudent($scope.student).then(loadStudent);
        };

        // Deleting the student
        $scope.delete = function () {
            teacherService.deleteStudent(studentId);
        };

    })
;