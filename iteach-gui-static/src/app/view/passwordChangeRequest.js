angular.module('iteach.view.passwordChangeRequest', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('PasswordChangeRequestCtrl', function ($scope, $routeParams, $translate, accountService, routeService) {
        $scope.oldPassword = '';
        $scope.newPassword = '';
        $scope.newPasswordConfirm = '';
        $scope.submit = function (valid) {
            if (!valid) return;
            if ($scope.newPassword != $scope.newPasswordConfirm) return;
            // Sending the form
            accountService.passwordChange($routeParams.token, $scope.oldPassword, $scope.newPassword)
                .success(function () {
                    routeService.routeWithSuccess('/teacher', $translate.instant('account.passwordChange.success'))
                })
        }
    })
;