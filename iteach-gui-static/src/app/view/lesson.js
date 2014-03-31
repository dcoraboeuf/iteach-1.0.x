angular.module('iteach.view.lesson', [
        'iteach.service.teacher'
    ])
    .controller('LessonCtrl', function ($scope, $filter, $routeParams, teacherService) {

        var lessonId = $routeParams.lessonId;
        $scope.lessonId = lessonId;

        function loadLesson() {
            teacherService.getLesson(lessonId).success(function (lesson) {
                $scope.lesson = lesson;
                // Loads the student
                teacherService.getStudent(lesson.student.id).success(function (student) {
                    $scope.student = student;
                });
                // Loads the school
                teacherService.getSchool(lesson.student.school.id).success(function (school) {
                    $scope.school = school;
                });
            })
        }

        // Loads the lesson
        loadLesson();

        // Updating the lesson
        $scope.updateLesson = function () {
            teacherService.updateLesson($scope.lesson).then(loadLesson);
        };

        // Deleting the lesson
        $scope.deleteLesson = function () {
            teacherService.deleteLesson(lessonId);
        };

    })
;