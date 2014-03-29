angular.module('iteach.dialog.invoice', [
        'iteach.service.core',
        'iteach.ui.teacher'
    ])
    .controller('dialogInvoice', function ($log, $scope, $translate, $modalInstance, calendarService, modalController, invoiceForm, notificationService, uiTeacher) {

        $scope.invoice = invoiceForm;
        if (invoiceForm.period) {
            $scope.invoice.year = invoiceForm.period.year;
            $scope.invoice.month = invoiceForm.period.month;
        }

        uiTeacher.getSchools().then(function (schools) {
            $scope.schools = schools.resources;
        });

        $scope.months = calendarService.getMonths();

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