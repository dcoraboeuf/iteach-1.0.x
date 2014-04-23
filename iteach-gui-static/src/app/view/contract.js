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
            })
        }

        loadContract();

    })
;