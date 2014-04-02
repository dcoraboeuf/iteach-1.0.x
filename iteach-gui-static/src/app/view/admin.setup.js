angular.module('iteach.view.admin.setup', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('AdminSetupCtrl', function ($scope, $location, $translate, notificationService, accountService) {
        accountService.loadSetup().success(function (setup) {
            $scope.setup = setup;
        });

        $scope.submit = function (valid) {
            if (valid) {
                // Collects the data
                var email = $scope.setup.email;
                var password = $scope.setup.password;
                var passwordChange = $scope.setup.passwordChange;
                var passwordChangeConfirm = $scope.setup.passwordChangeConfirm;
                // Checks confirmation
                if (passwordChange && (passwordChange != passwordChangeConfirm)) return;
                // Form to send
                var form = {
                    email: email,
                    password: password,
                    passwordChange: passwordChange
                };
                accountService.saveSetup(form).success(function () {
                    $location.path('/admin');
                    var message;
                    if (passwordChange) {
                        message = $translate.instant('admin.setup.saved.passwordChanged');
                    } else {
                        message = $translate.instant('admin.setup.saved');
                    }
                    notificationService.success(message);
                })
            }
        };
    })
;