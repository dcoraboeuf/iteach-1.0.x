angular.module('iteach.view.passwordChangeRequest', [
    ])
    .controller('PasswordChangeRequestCtrl', function ($scope) {
        $scope.oldPassword = '';
        $scope.newPassword = '';
        $scope.newPasswordConfirm = '';
        $scope.submit = function (valid) {
            if (!valid) return;
            if ($scope.newPassword != $scope.newPasswordConfirm) return;
            // FIXME Sending the form
        }
    })
;