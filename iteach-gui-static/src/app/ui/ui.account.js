angular.module('iteach.ui.account', [
        'iteach.config'
    ])
    .service('uiAccount', function ($q, $http, config) {

        var self = {};

        self.current = function () {
            var deferred = $q.defer();
            $http.get(config.api('account/state'), {
                httpIgnoreError: true
            }).success(function (state) {
                    deferred.resolve(state)
                });
            return deferred.promise;
        };

        self.logout = function () {
            var deferred = $q.defer();
            $http.get(config.api('account/logout')).success(function () {
                deferred.resolve();
            });
            return deferred.promise;
        };

        self.iteachLogin = function (email, password) {
            var deferred = $q.defer();
            $http.get(config.api('account/login'), {
                httpIgnoreError: true,
                headers: {
                    'Authorization': 'Basic ' + window.btoa(email + ':' + password)
                }
            }).success(function (teacher) {
                    deferred.resolve({
                        authenticated: true,
                        teacher: teacher
                    })
                }
            ).error(function () {
                    deferred.reject();
                });
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
        };

        return self;

    })
;