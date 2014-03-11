angular.module('iteach.view.register', [
        'iteach.service.core',
        'iteach.service.account'
    ])
    .controller('RegisterCtrl', function ($scope, $translate, notificationService, accountService) {
        $scope.name = '';
        $scope.email = '';
        $scope.password = '';
        $scope.password_confirm = '';
        $scope.registerSubmit = function () {
            // Checks the password confirmation
            if ($scope.password != $scope.password_confirm) {
                notificationService.error($translate.instant('register.incorrect_password_confirmation'));
            }
            // Sends the form
            accountService.registerWithPassword($scope.name, $scope.email, $scope.password)
        }
    })
;