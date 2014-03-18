angular.module('iteach.view.teacher', [
        'iteach.service.teacher'
    ])
    .controller('TeacherCtrl', function ($scope, teacherService) {

        /**
         * Schools
         */

        function loadSchools() {
            teacherService.getSchools().then(function (schools) {
                $scope.schools = schools
            })
        }

        // Loads the list of schools
        loadSchools();

        // Creating a school
        $scope.createSchool = function () {
            teacherService.createSchool().then(loadSchools)
        }

        /**
         * Students
         */

        function loadStudents() {
            teacherService.getStudents().then(function (students) {
                $scope.students = students
            })
        }

        // Loads the list of students
        loadStudents();

        // Creating a student
        $scope.createStudent = function() {
            teacherService.createStudent().then(loadStudents)
        }

        /**
         * Planning
         */
        $scope.calendarConfig = {
            calendar:{
                height: 450,
                editable: true,
                header:{
                    left: 'month basicWeek basicDay agendaWeek agendaDay',
                    center: 'title',
                    right: 'today prev,next'
                },
                dayClick: $scope.alertEventOnClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize
            }
        };
        $scope.lessons = [];

        // TODO Loads the planning
    })
;