angular.module('iteach.view.report', [
        'iteach.service.core',
        'iteach.service.teacher'
    ])
    .controller('ReportCtrl', function ($scope, teacherService, localDataService) {

        function loadReportForPeriod(period) {
            teacherService.getReport(period.year, period.month).success(function (report) {
                $scope.report = report;
            });
        }

        function loadReport() {
            // Gets the current date
            var date = localDataService.getCurrentDate();
            // Gets the year and the month only
            var period = {
                year: date.getFullYear(),
                month: date.getMonth() + 1
            };
            // Loads the report for this period
            loadReportForPeriod(period);
        }

        loadReport();

    })
;