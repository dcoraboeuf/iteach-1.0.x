angular.module('iteach.view.account', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('AccountCtrl', function ($scope, $translate, accountService, notificationService) {

        $scope.passwordChange = function () {
            accountService.passwordChangeRequest().success(function () {
                notificationService.success($translate.instant('account.passwordChange.requested'))
            })
        };

    })
;