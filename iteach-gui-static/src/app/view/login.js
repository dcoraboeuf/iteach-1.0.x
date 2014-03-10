angular.module('iteach.view.login', [
        'iteach.service.account'
    ])
    .controller('LoginCtrl', function ($scope, accountService) {
        $scope.email = '';
        $scope.password = '';
        $scope.iteachLogin = function () {
            accountService.iteachLogin($scope.email, $scope.password)
        }
    })
;