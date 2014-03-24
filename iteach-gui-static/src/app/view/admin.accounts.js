angular.module('iteach.view.admin.accounts', [
        'iteach.service.account'
    ])
    .controller('AdminAccountsCtrl', function ($scope, accountService) {

        // Loads all the accounts
        function loadAccounts() {
            accountService.getAccounts().success(function (accounts) {
                $scope.accounts = accounts;
            })
        }

        loadAccounts();

        // Deleting an account
        $scope.deleteAccount = function (accountId) {
            accountService.deleteAccount(accountId).success(loadAccounts);
        };

    })
;