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

        // TODO Loads the list of students

        // Creating a student
        $scope.createStudent = function() {
            teacherService.createStudent()
            // TODO Reloads list after creation
        }


        // TODO Loads the planning
    })
;