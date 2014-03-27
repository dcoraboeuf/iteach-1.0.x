angular.module('iteach.view.student', [
        'iteach.service.core',
        'iteach.service.teacher'
    ])
    .controller('StudentCtrl', function ($scope, $routeParams, teacherService, localDataService) {

        var studentId = $routeParams.studentId;

        function loadLessonReport() {
            // Gets the current date
            var date = localDataService.getCurrentDate();
            // Gets the year and the month only
            var period = {
                year: date.getFullYear(),
                month: date.getMonth() + 1
            };
            // Loads the report for this period
            teacherService.getLessonReport(studentId, period.year, period.month).success(function (report) {
                $scope.report = report;
            });
        }

        function loadStudent() {
            teacherService.getStudent(studentId).success(function (student) {
                $scope.student = student;
            });
            loadLessonReport();
        }

        // Loads the school
        loadStudent();

        // Updating the student
        $scope.update = function () {
            teacherService.updateStudent($scope.student).then(loadStudent);
        };

        // Deleting the student
        $scope.delete = function () {
            teacherService.deleteStudent(studentId);
        };

        // Disabling the student
        $scope.disable = function () {
            teacherService.disableStudent(studentId).then(loadStudent);
        };

        // Enabling the student
        $scope.enable = function () {
            teacherService.enableStudent(studentId).then(loadStudent);
        };

    })
;