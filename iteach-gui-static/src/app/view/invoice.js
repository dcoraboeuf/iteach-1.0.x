angular.module('iteach.view.invoice', [
        'iteach.service.teacher'
    ])
    .controller('InvoiceCtrl', function ($scope, $routeParams, teacherService) {

        var schoolId = $routeParams.schoolId;
        var year = $routeParams.year;
        var month = $routeParams.month;
        var number = $routeParams.number;

        teacherService.loadInvoice(schoolId, year, month, number).success(function (invoice) {
            $scope.invoice = invoice;
        });

    })
;