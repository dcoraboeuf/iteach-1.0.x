angular.module('iteach.service.teacher', [
        'iteach.ui.teacher'
    ])
    .service('teacherService', function ($q, uiTeacher) {
        var self = {};

        self.getSchools = function () {
            var d = $q.defer();

            return d.promise;
        }

        return self;
    })
;