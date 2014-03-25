angular.module('iteach.view.admin.account.import', [
        'iteach.service.account'
    ])
    .controller('AdminAccountImportCtrl', function ($scope, $routeParams, $interpolate, $http, accountService) {

        var accountId = $routeParams.accountId;

        function loadAccount() {
            accountService.getAccount(accountId).success(function (account) {
                $scope.account = account;
            })
        }

        loadAccount();

        $scope.error = false;
        $scope.import = function () {
            if (!$scope.inputFile) return;
            var file = $scope.inputFile;
            var fd = new FormData();
            fd.append('file', file);
            $http.post(
                // TODO Uses the service
                $interpolate('api/account/{{id}}/import')($scope.account),
                fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }
            )
                .success(function () {

                })
                .error(function () {
                    $scope.error = true;
                })
        };

    })
;