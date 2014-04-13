angular.module('iteach.view.invoices', [
    'iteach.service.core',
    'iteach.service.teacher',
    'iteach.dialog.invoice.error'
])
    .controller('InvoicesCtrl', function ($scope, $modal, $translate, teacherService, calendarService, alertService) {

        function loadInvoices(filter) {
            teacherService.getInvoices(filter).success(function (invoices) {
                $scope.invoiceCollection = invoices;
                $scope.invoices = invoices.resources;

                angular.forEach($scope.invoices, function (invoice) {
                    invoice.period.monthName = calendarService.getMonthName(invoice.period.month);
                    invoice.selected = false;
                });
            })
        }

        loadInvoices({});

        // Filter data
        $scope.filter = {};
        // Filter > school
        teacherService.getSchools().then(function (schools) {
            $scope.schools = schools.resources;
        });
        // Filter > year
        var years = [];
        for (var i = 2014; i < 2050; i++) years.push(i);
        $scope.years = years;
        // Filter > status
        $scope.statuses = [
            {
                id: 'CREATED',
                name: $translate.instant('invoice.status.CREATED')
            },
            {
                id: 'GENERATING',
                name: $translate.instant('invoice.status.GENERATING')
            },
            {
                id: 'READY',
                name: $translate.instant('invoice.status.READY')
            },
            {
                id: 'ERROR',
                name: $translate.instant('invoice.status.ERROR')
            }
        ];
        // Filter > downloaded
        $scope.downloadStatuses = [
            {
                id: true,
                name: $translate.instant('invoice.downloadStatus.yes')
            },
            {
                id: false,
                name: $translate.instant('invoice.downloadStatus.no')
            }
        ];

        // Reloads the filter
        var reload = function () {
            loadInvoices($scope.filter);
        };

        // Filter watches
        $scope.$watch('filter.schoolId', reload);
        $scope.$watch('filter.year', reload);
        $scope.$watch('filter.status', reload);
        $scope.$watch('filter.downloaded', reload);

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
                teacherService.deleteInvoices(getSelection()).success(reload);
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