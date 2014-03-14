angular.module('iteach.service.teacher', [
        'iteach.ui.teacher'
    ])
    .service('teacherService', function ($q, uiTeacher) {
        var self = {};

        self.getSchools = function () {
            return uiTeacher.getSchools()
        }

        return self;
    })
;