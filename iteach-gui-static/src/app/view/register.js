angular.module('iteach.view.register', [
        'iteach.service.core'])
    .controller('RegisterCtrl', function ($scope, $translate, notificationService) {
        $scope.name = '';
        $scope.email = '';
        $scope.password = '';
        $scope.password_confirm = '';
        $scope.registerSubmit = function () {
            // Checks the password confirmation
            if ($scope.password != $scope.password_confirm) {
                notificationService.error($translate.instant('register.incorrect_password_confirmation'));
            }
            // TODO Sends the form
        }
    })
;