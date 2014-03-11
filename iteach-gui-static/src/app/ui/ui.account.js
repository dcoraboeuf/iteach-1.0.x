angular.module('iteach.ui.account', [
        'iteach.config'
    ])
    .service('uiAccount', function ($q, $http, config) {

        var self = {};

        self.current = function () {
            /*
             return {
             authenticated: true,
             teacher: {
             id: 2,
             name: 'Damien',
             email: 'damien@test.com',
             administrator: false,
             authenticationMode: 'PASSWORD'
             }
             };
             */
            return {
                authenticated: false
            }
        };

        self.iteachLogin = function (email, password) {
            var deferred = $q.defer();
            $http.get(config.api('account/login'), {
                'Authorization': window.btoa(email + ':' + password)
            }).success(function (teacher) {
                    deferred.resolve({
                        authenticated: true,
                        teacher: teacher
                    })
                }
            );
            return deferred.promise;
        };

        self.registerWithPassword = function (name, email, password) {
            var deferred = $q.defer();
            $http.post(config.api('account/register'), {
                name: name,
                email: email,
                password: password
            }).success(function (id) {
                    deferred.resolve(id)
                });
            return deferred.promise;
        }

        return self;

    })
;