angular.module('iteach.view.contract', [
    'iteach.service.teacher'
])
    .controller('ContractCtrl', function ($scope, $routeParams, teacherService) {

        var contractId = $routeParams.contractId;

        function loadContract() {
            teacherService.getContract(contractId).success(function (contract) {
                $scope.contract = contract;
                // Loads the school details as well
                teacherService.getSchool(contract.school.id).success(function (school) {
                    $scope.school = school;
                });
                // Loads the students in the contract
                teacherService.getStudentsInContract(contractId).success(function (studentCollection) {
                    $scope.students = studentCollection.resources;
                });
            })
        }

        loadContract();

        $scope.modifyContract = function () {
            teacherService.modifyContract($scope.contract).then(loadContract);
        };

        $scope.deleteContract = function () {
            teacherService.deleteContract(contractId);
        };

    })
;