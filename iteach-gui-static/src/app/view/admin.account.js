angular.module('iteach.view.admin.account', [
        'iteach.service.account'
    ])
    .controller('AdminAccountCtrl', function ($location, $scope, $routeParams, accountService) {

        var accountId = $routeParams.accountId;

        function loadAccount() {
            // TODO Loads details (number of schools, students & lessons)
            accountService.getAccount(accountId).success(function (account) {
                $scope.account = account;
                $scope.deleteAllowed = !account.administrator;
            })
        }

        loadAccount();

        $scope.deleteAccount = function () {
            accountService.deleteAccount(accountId).then(function () {
                $location.path('/admin/accounts')
            })
        };

    })
;