angular.module('iteach.view.teacher', [
        'iteach.service.teacher'
    ])
    .controller('TeacherCtrl', function ($scope, teacherService) {
        // Loads the list of schools
        teacherService.getSchools().then(function (schools) {
            $scope.schools = schools;
        });
        // Creating a school
        $scope.createSchool = teacherService.createSchool;
        // TODO Loads the list of students
        // TODO Loads the planning
    })
;