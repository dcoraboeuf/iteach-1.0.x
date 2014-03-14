angular.module('iteach.service.teacher', [
        'iteach.ui.teacher',
        'iteach.dialog.school'
    ])
    .service('teacherService', function ($q, $modal, uiTeacher) {
        var self = {};

        self.getSchools = function () {
            return uiTeacher.getSchools()
        }

        self.createSchool = function () {
            $modal.open({
                templateUrl: 'app/dialog/dialog.shool.tpl.html',
                controller: 'dialogSchool'
            })
        }

        return self;
    })
;