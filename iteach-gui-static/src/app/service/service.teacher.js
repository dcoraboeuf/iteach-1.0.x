angular.module('iteach.service.teacher', [
        'iteach.ui.teacher',
        'iteach.dialog.school',
        'iteach.dialog.student'
    ])
    .service('teacherService', function ($modal, uiTeacher) {
        var self = {};

        self.getSchools = function () {
            return uiTeacher.getSchools()
        }

        self.createSchool = function () {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.shool.tpl.html',
                controller: 'dialogSchool',
                resolve: {
                    initialSchool: function () {
                        return {}
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (school) {
                                return uiTeacher.createSchool(school)
                            }
                        }
                    }
                }
            }).result
        }

        self.updateSchool = function (school) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.shool.tpl.html',
                controller: 'dialogSchool',
                resolve: {
                    initialSchool: function () {
                        var form = angular.copy(school)
                        delete form.id
                        delete form.href
                        return form
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (schoolForm) {
                                return uiTeacher.updateSchool(school.id, schoolForm)
                            }
                        }
                    }
                }
            }).result
        }

        self.getSchool = uiTeacher.getSchool

        self.getStudents = function () {
            return uiTeacher.getStudents()
        }

        self.createStudent = function () {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.student.tpl.html',
                controller: 'dialogStudent',
                resolve: {
                    modalController: function () {
                        return {
                            onSubmit: function (student) {
                                return uiTeacher.createStudent(student)
                            }
                        }
                    }
                }
            }).result
        }

        return self;
    })
;