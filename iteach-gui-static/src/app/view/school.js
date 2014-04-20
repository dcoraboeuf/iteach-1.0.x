angular.module('iteach.view.school', [
        'iteach.service.teacher'
    ])
    .controller('SchoolCtrl', function ($scope, $routeParams, teacherService) {

        var schoolId = $routeParams.schoolId;
        $scope.schoolId = schoolId;

        function loadSchool() {
            teacherService.getSchool(schoolId).success(function (school) {
                $scope.school = school;
            });
            teacherService.getSchoolReport(schoolId, {}).success(function (report) {
                $scope.report = report;
            });
            loadContracts();
        }

        // Loads the school
        loadSchool();

        // Loads the contracts for the school
        function loadContracts() {
            teacherService.getContracts(schoolId).success(function (contractCollection) {
                $scope.contracts = contractCollection.resources;
            })
        }

        // Updating the school
        $scope.update = function () {
            teacherService.updateSchool($scope.school).then(loadSchool)
        };

        // Deleting the school
        $scope.delete = function () {
            teacherService.deleteSchool(schoolId)
        };

    })
;