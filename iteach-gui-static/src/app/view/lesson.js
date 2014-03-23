angular.module('iteach.view.lesson', [
        'iteach.service.teacher'
    ])
    .controller('LessonCtrl', function ($scope, $routeParams, teacherService) {

        var lessonId = $routeParams.lessonId;

        function loadLesson() {
            teacherService.getLesson(lessonId).success(function (lesson) {
                console.log('lesson', lesson);
                $scope.lesson = lesson;
            })
        }

        // Loads the lesson
        loadLesson();

        // TODO Updating the lesson

        // TODO Deleting the school

    })
;