angular.module('iteach.view.school', [
        'iteach.service.teacher'
    ])
    .controller('SchoolCtrl', function ($scope, $routeParams, teacherService) {

        var schoolId = $routeParams.schoolId;

        function loadSchool() {
            teacherService.getSchool(schoolId).then(function (school) {
                $scope.school = school
            })
        }

        // Loads the school
        loadSchool()

    })
;