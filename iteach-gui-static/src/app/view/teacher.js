angular.module('iteach.view.teacher', [
        'iteach.service.teacher'
    ])
    .controller('TeacherCtrl', function ($scope, teacherService) {

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
        // TODO Loads the list of students
        // TODO Loads the planning
    })
;