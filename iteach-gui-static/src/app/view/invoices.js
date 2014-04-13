angular.module('iteach.view.invoices', [
    'iteach.service.teacher'
])
    .controller('InvoicesCtrl', function ($scope, teacherService) {

        function loadInvoices() {
            teacherService.getInvoices().success(function (invoices) {
                $scope.invoices = invoices;
            })
        }

        loadInvoices();
    })
;