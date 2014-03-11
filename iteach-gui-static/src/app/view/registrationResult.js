angular.module('iteach.view.registrationResult', [])
    .controller('RegistrationResultCtrl', function ($log, $routeParams, $scope) {
        var success = $routeParams.result;
        $log.info('Registration result', success)
        $scope.success = success;
    })
;