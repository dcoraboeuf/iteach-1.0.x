angular.module('iteach.view.account', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('AccountCtrl', function ($rootScope, $scope, $translate, accountService, notificationService) {

        $scope.passwordChange = function () {
            console.log($rootScope.account);
            accountService.passwordChangeRequest($rootScope.account.teacher.id).success(function () {
                notificationService.success($translate.instant('account.passwordChange.requested'))
            })
        };

    })
;