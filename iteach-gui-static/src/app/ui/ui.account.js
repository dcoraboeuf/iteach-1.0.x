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

        self.getAccounts = function () {
            return $http.get(config.api('account'));
        };

        self.getAccount = function (accountId) {
            return $http.get(config.api('account/' + accountId))
        };

        self.deleteAccount = function (accountId) {
            return $http.delete(config.api('account/' + accountId))
        };

        self.importAccount = function (accountId, file) {
            var fd = new FormData();
            fd.append('file', file);
            return $http.post(
                config.api('account/{{id}}/import', {id: accountId}),
                fd, {
                    transformRequest: angular.identity,
                    headers: {'Content-Type': undefined}
                }
            );
        };

        self.disableAccount = function (accountId) {
            return $http.put(config.api('account/{{id}}/disable', {id: accountId}));
        };

        self.getAccountProfile = function () {
            return $http.get(config.api('account/profile'));
        };

        self.updateAccountProfile = function (profile) {
            return $http.put(config.api('account/profile'), profile);
        };

        self.loadSetup = function () {
            return $http.get(config.api('account/setup'));
        };

        self.saveSetup = function (form) {
            return $http.put(config.api('account/setup'), form);
        };

        return self;

    })
;