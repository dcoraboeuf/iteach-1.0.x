angular.module('iteach.view.invoices', [
    'iteach.service.core',
    'iteach.service.teacher',
    'iteach.dialog.invoice.error'
])
    .controller('InvoicesCtrl', function ($scope, $modal, $translate, teacherService, calendarService, alertService) {

        function loadInvoices() {
            teacherService.getInvoices().success(function (invoices) {
                $scope.invoices = invoices.resources;

                angular.forEach($scope.invoices, function (invoice) {
                    invoice.period.monthName = calendarService.getMonthName(invoice.period.month);
                });
            })
        }

        loadInvoices();

        $scope.selectInvert = function () {
            angular.forEach($scope.invoices, function (invoice) {
                invoice.selected = !invoice.selected;
            });
        };

        $scope.selectAll = function () {
            angular.forEach($scope.invoices, function (invoice) {
                invoice.selected = true;
            });
        };

        $scope.selectNone = function () {
            angular.forEach($scope.invoices, function (invoice) {
                invoice.selected = false;
            });
        };

        $scope.selectionEmpty = function () {
            var count = 0;
            angular.forEach($scope.invoices, function (invoice) {
                if (invoice.selected) count++;
            });
            return count == 0;
        };

        function getSelection() {
            var selection = [];
            angular.forEach($scope.invoices, function (invoice) {
                if (invoice.selected) {
                    selection.push(invoice.id);
                }
            });
            return selection;
        }

        $scope.deleteSelection = function () {
            alertService.confirm({
                title: $translate.instant('invoice.delete'),
                message: $translate.instant('invoice.delete.prompt')
            }).then(function () {
                teacherService.deleteInvoices(getSelection()).success(loadInvoices);
            });
        };

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