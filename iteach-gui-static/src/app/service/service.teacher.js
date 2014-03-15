angular.module('iteach.service.teacher', [
        'iteach.ui.teacher',
        'iteach.dialog.school'
    ])
    .service('teacherService', function ($q, $modal, uiTeacher, notificationService) {
        var self = {};

        self.getSchools = function () {
            return uiTeacher.getSchools()
        }

        self.createSchool = function () {
            // TODO Uses the notification service for the modal dialog
            $modal.open({
                templateUrl: 'app/dialog/dialog.shool.tpl.html',
                controller: 'dialogSchool',
                resolve: {
                    modalController: function () {
                        return {
                            onSubmit: function (school) {
                                return uiTeacher.createSchool(school)
                            }
                        }
                    }
                }
            }).result.finally(function () {
                    // TODO Stops using the notification service for the modal dialog
                })
        }

        return self;
    })
;