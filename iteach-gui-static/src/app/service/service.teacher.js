angular.module('iteach.service.teacher', [
        'iteach.service.core',
        'iteach.ui.teacher',
        'iteach.dialog.school',
        'iteach.dialog.student',
        'iteach.dialog.lesson'
    ])
    .service('teacherService', function ($modal, $translate, $location, alertService, uiTeacher) {
        var self = {};

        self.getSchools = function () {
            return uiTeacher.getSchools()
        };

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
        };

        self.updateSchool = function (school) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.shool.tpl.html',
                controller: 'dialogSchool',
                resolve: {
                    initialSchool: function () {
                        var form = angular.copy(school);
                        delete form.id;
                        delete form.href;
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
        };

        self.getSchool = uiTeacher.getSchool;

        self.getStudents = function () {
            return uiTeacher.getStudents()
        };

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
        };

        self.getStudent = uiTeacher.getStudent;

        self.createLesson = function (start, end) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.lesson.tpl.html',
                controller: 'dialogLesson',
                resolve: {
                    input: function () {
                        return {
                            start: start,
                            end: end
                        }
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (lessonForm) {
                                delete lessonForm.date;
                                return uiTeacher.createLesson(lessonForm)
                            }
                        }
                    }
                }
            }).result
        };

        self.getLessons = uiTeacher.getLessons;

        self.getLesson = uiTeacher.getLesson;

        self.deleteLesson = function (lessonId) {
            self.getLesson(lessonId).success(function (lesson) {
                alertService.confirm({
                    title: lesson.title,
                    message: $translate.instant('lesson.delete.prompt')
                }).then(function () {
                        uiTeacher.deleteLesson(lessonId).success(function () {
                            $location.path('/');
                        })
                    })
            })
        };

        return self;
    })
;