angular.module('iteach.view.lesson', [
        'iteach.service.teacher'
    ])
    .controller('LessonCtrl', function ($scope, $filter, $routeParams, teacherService) {

        var lessonId = $routeParams.lessonId;

        function loadLesson() {
            teacherService.getLesson(lessonId).success(function (lesson) {
                lesson.formatted = {
                    date: $filter('date')(new Date(lesson.from), 'fullDate'),
                    from: $filter('date')(new Date(lesson.from), 'HH:mm'),
                    to: $filter('date')(new Date(lesson.to), 'HH:mm')
                };
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

        // TODO Updating the lesson

        // TODO Deleting the school

    })
;