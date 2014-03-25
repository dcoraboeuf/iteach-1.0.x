angular.module('iteach.view.admin.account', [
        'iteach.service.account',
        'iteach.service.core'
    ])
    .controller('AdminAccountCtrl', function ($location, $scope, $routeParams, $translate, accountService, notificationService) {

        var accountId = $routeParams.accountId;

        function loadAccount(message) {
            // TODO Loads details (number of schools, students & lessons)
            accountService.getAccount(accountId).success(function (account) {
                $scope.account = account;
                $scope.deleteAllowed = !account.administrator;
                if (message) {
                    notificationService.success($translate.instant(message));
                }
            })
        }

        loadAccount();

        $scope.deleteAccount = function () {
            accountService.deleteAccount(accountId).then(function () {
                $location.path('/admin/accounts')
            })
        };

        $scope.importAccount = function () {
            accountService.importAccount($scope.account).then(function () {
                loadAccount('admin.account.import.success');
            });
        };

    })
;