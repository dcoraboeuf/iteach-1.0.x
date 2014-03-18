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

        // Updating the school
        $scope.update = function () {
            teacherService.updateSchool($scope.school).then(loadSchool)
        }

        // TODO Deleting the school

    })
;