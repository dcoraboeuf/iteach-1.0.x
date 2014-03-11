angular.module('iteach.view.register', [])
    .controller('RegisterCtrl', function ($scope) {
        $scope.name = '';
        $scope.email = '';
        $scope.password = '';
        $scope.password_confirm = '';
        $scope.registerSubmit = function () {
            // Checks the password confirmation
            if ($scope.password != $scope.password_confirm) {
                // TODO Notification service
                alert('Incorrect password confirmation')
            }
            // TODO Sends the form
        }
    })
;