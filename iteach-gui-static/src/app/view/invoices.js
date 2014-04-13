angular.module('iteach.view.invoices', [
    'iteach.service.core',
    'iteach.service.teacher',
    'iteach.dialog.invoice.error'
])
    .controller('InvoicesCtrl', function ($scope, $modal, teacherService, calendarService) {

        function loadInvoices() {
            teacherService.getInvoices().success(function (invoices) {
                $scope.invoices = invoices.resources;

                angular.forEach($scope.invoices, function (invoice) {
                    invoice.period.monthName = calendarService.getMonthName(invoice.period.month);
                });
            })
        }

        loadInvoices();

        $scope.displayError = function (invoice) {
            if (invoice.errorMessage || invoice.errorUuid) {
                return $modal.open({
                    templateUrl: 'app/dialog/dialog.invoice.error.tpl.html',
                    controller: 'DialogInvoiceErrorCtrl',
                    resolve: {
                        invoiceConfig: function () {
                            return invoice;
                        }
                    }
                }).result;
            }
        };
    })
;