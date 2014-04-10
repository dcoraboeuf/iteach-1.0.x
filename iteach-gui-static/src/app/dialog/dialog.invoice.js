angular.module('iteach.dialog.invoice', [
        'iteach.service.core',
        'iteach.ui.teacher'
    ])
    .controller('dialogInvoice', function ($log, $scope, $translate, $interpolate, $location, $modalInstance, calendarService, invoiceForm, notificationService, uiTeacher) {

        $scope.invoice = invoiceForm;
        if (invoiceForm.period) {
            $scope.invoice.year = invoiceForm.period.year;
            $scope.invoice.month = invoiceForm.period.month;
        }

        uiTeacher.getSchools().then(function (schools) {
            $scope.schools = schools.resources;
        });

        $scope.months = calendarService.getMonths();

        $scope.cancel = function () {
            $scope.closed = true;
            $modalInstance.dismiss('cancel');
        };

        $scope.generate = function () {
            // Invoice form
            var invoice = {
                schoolId: $scope.invoice.schoolId,
                year: $scope.invoice.year,
                month: $scope.invoice.month,
                number: $scope.invoice.number
            };
            // Launching the generation
            $scope.generating = true;
            $scope.launched = true;
            uiTeacher.generateInvoice(invoice).success(function (invoiceInfo) {
                var invoiceId = invoiceInfo.id;
                // Control function
                var controlFn = function () {
                    uiTeacher.getInvoice(invoiceId).success(function (info) {
                        if (info.status == 'READY') {
                            // Generation OK
                            $scope.generating = false;
                            $scope.ready = true;
                            $scope.invoiceInfo = info;
                        } else if (info.status == 'ERROR') {
                            // Error during the generation
                            $scope.generating = false;
                            // Error messaging
                            $scope.errorMessage = info.errorMessage;
                            $scope.errorUuid = info.errorUuid;
                        } else {
                            // Going on with the generation
                            if (!$scope.closed) window.setTimeout(controlFn, 500)
                        }
                    });
                };
                // Launches the control
                controlFn();
            });
        };

        $scope.invoiceMgt = function () {
            // Closes the dialog first
            $scope.cancel();
            // Goes to the invoice mgt page
            $location.path('/invoices');
        };

        $scope.download = function () {
            // Closes the dialog first
            $scope.cancel();
            // Download request
            // TODO Moves to service/ui
            location.href = $interpolate('api/teacher/invoice/{{id}}/download/attached')({id: $scope.invoiceInfo.id});
        };

        $modalInstance.opened.finally(function () {
            notificationService.pushScope($scope)
        });

        $modalInstance.result.finally(function () {
            notificationService.popScope()
        });

    })
;