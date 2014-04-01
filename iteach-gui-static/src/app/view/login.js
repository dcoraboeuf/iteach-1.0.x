angular.module('iteach.view.login', [
        'iteach.service.core',
        'iteach.service.account'
    ])
    .controller('LoginCtrl', function ($scope, $routeParams, $translate, accountService, localDataService) {
        localDataService.clearCurrentDate(); // Resets the current date as now at login time
        $scope.login = {
            email: '',
            password: ''
        };
        if ($routeParams.error) {
            $scope.loginError = $translate.instant('login.error.' + $routeParams.error);
        }
        $scope.iteachLogin = function () {
            accountService.iteachLogin($scope.login.email, $scope.login.password).then(angular.noop, function () {
                $scope.loginError = $translate.instant('login.error');
            });
        }
    })
;