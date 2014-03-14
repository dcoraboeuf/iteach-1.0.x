angular.module('iteach.dialog.form', [])
    .service('formService', function ($q, $http, config) {

        var self = {};

        self.load = function (uri) {
            var d = $q.defer();
            $http.get(config.api(uri)).success(function (formDefinition) {
                d.resolve(formDefinition)
            });
            return d.promise;
        }

        return self;

    })

;