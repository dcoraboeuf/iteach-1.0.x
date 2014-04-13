angular.module('iteach.view.invoices', [
    'iteach.service.core',
    'iteach.service.teacher'
])
    .controller('InvoicesCtrl', function ($scope, teacherService, calendarService) {

        function loadInvoices() {
            teacherService.getInvoices().success(function (invoices) {
                $scope.invoices = invoices.resources;

                angular.forEach($scope.invoices, function (invoice) {
                    invoice.period.monthName = calendarService.getMonthName(invoice.period.month);
                });
            })
        }

        loadInvoices();
    })
;