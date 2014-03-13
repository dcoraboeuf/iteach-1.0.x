angular.module('iteach.ui.teacher', [
        'iteach.config'
    ])
    .service('uiTeacher', function ($q, $http, config) {
        var self = {};

        self.getSchools = function () {
            var d = $q.defer();
            $http.get(config.api('teacher/school'))
                .success(function (schools) {
                    d.resolve(schools);
                })
            return d.promise;
        }

        return self;
    })
;