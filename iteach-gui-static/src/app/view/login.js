angular.module('iteach.view.login', [
        'iteach.service.account'
    ])
    .controller('LoginCtrl', function ($scope, $routeParams, $translate, accountService) {
        $scope.email = '';
        $scope.password = '';
        if ($routeParams.error) {
            $scope.loginError = $translate.instant('login.error.' + $routeParams.error);
        }
        $scope.iteachLogin = function () {
            accountService.iteachLogin($scope.email, $scope.password).then(angular.noop, function () {
                $scope.loginError = $translate.instant('login.error');
            });
        }
    })
;