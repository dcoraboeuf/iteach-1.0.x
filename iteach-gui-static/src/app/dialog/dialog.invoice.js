angular.module('iteach.dialog.invoice', [
        'iteach.ui.teacher'
    ])
    .controller('dialogInvoice', function ($log, $scope, $modalInstance, modalController, invoiceForm, notificationService, uiTeacher) {

        $scope.invoice = invoiceForm;

        uiTeacher.getSchools().then(function (schools) {
            $scope.schools = schools.resources;
        });

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel')
        };

        $scope.submit = function (isValid) {
            if (isValid) {
                modalController.onSubmit($scope.invoiceForm).then(
                    function () {
                        $modalInstance.close('ok')
                    },
                    function (message) {
                        $scope.error = message
                    }
                )
            }
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;