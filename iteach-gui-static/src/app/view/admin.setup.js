angular.module('iteach.view.admin.setup', [
        'iteach.service.account'
    ])
    .controller('AdminSetupCtrl', function ($scope, accountService) {
        accountService.loadSetup().success(function (setup) {
            $scope.setup = setup;
        });
    })
;