angular.module('iteach.dialog.account.import', [
    ])
    .controller('dialogAccountImport', function ($scope, $modalInstance, modalController, account, notificationService) {

        $scope.account = account;

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function (inputFile) {
            console.log('inputFile', inputFile);
            modalController.onSubmit(inputFile).then(
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