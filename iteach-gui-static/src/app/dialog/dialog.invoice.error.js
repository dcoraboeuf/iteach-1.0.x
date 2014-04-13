angular.module('iteach.dialog.invoice.error', [])
    .controller('DialogInvoiceErrorCtrl', function ($scope, invoiceConfig) {
        $scope.invoice = invoiceConfig;
    })
;