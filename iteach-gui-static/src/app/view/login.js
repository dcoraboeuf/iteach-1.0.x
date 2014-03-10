angular.module('iteach.view.login', [])
    .controller('LoginCtrl', function ($scope) {
        $scope.email = '';
        $scope.password = '';
        $scope.iteachLogin = function () {
            console.log ("iteachLogin", $scope.email, $scope.password);
        }
    })
;