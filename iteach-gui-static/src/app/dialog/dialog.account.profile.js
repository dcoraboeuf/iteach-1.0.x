angular.module('iteach.dialog.account.profile', [
        'iteach.ui.account'
    ])
    .controller('dialogAccountProfile', function ($scope, $modalInstance, modalController, notificationService, uiAccount) {

        uiAccount.getAccountProfile().success(function (profile) {
            $scope.profile = profile;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function () {
            modalController.onSubmit($scope.profile).then(
                function () {
                    $modalInstance.close('ok')
                },
                function (message) {
                    $scope.error = message
                }
            );
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;