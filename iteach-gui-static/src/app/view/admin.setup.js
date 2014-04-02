angular.module('iteach.view.admin.setup', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('AdminSetupCtrl', function ($scope, $location, $translate, notificationService, accountService) {
        accountService.loadSetup().success(function (setup) {
            $scope.setup = setup;
        });

        $scope.submit = function (valid) {
            if (valid) {
                accountService.saveSetup($scope.setup).success(function () {
                    $location.path('/admin');
                    notificationService.success($translate.instant('admin.setup.saved'));
                })
            }
        };
    })
;