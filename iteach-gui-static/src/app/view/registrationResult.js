angular.module('iteach.view.registrationResult', [])
    .controller('RegistrationResultCtrl', function ($log, $routeParams) {
        console.log('REGISTRATION');
        $log.info('Registration result', $routeParams.result)
    })
;