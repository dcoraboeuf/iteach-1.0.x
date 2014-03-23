angular.module('iteach.view.lesson', [
        'iteach.service.teacher'
    ])
    .controller('LessonCtrl', function ($scope, $routeParams, teacherService) {

        var lessonId = $routeParams.lessonId;

        function loadLesson() {
            teacherService.getLesson(lessonId).then(function (lesson) {
                $scope.lesson = lesson;
            })
        }

        // TODO Loads the lesson
        // loadLesson();

        // TODO Updating the lesson

        // TODO Deleting the school

    })
;