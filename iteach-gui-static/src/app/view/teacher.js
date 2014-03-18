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


        // TODO Loads the planning
    })
;