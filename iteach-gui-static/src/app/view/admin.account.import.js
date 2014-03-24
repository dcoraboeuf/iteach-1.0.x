angular.module('iteach.view.admin.account.import', [
        'iteach.service.account'
    ])
    .controller('AdminAccountImportCtrl', function ($scope, $routeParams, accountService) {

        var accountId = $routeParams.accountId;

        function loadAccount() {
            accountService.getAccount(accountId).success(function (account) {
                $scope.account = account;
            })
        }

        loadAccount();

    })
;