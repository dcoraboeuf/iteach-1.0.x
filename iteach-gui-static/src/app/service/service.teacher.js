angular.module('iteach.service.teacher', [
        'iteach.service.core',
        'iteach.ui.teacher',
        'iteach.dialog.school',
        'iteach.dialog.student',
        'iteach.dialog.lesson'
    ])
    .service('teacherService', function ($q, $log, $modal, $translate, $location, alertService, uiTeacher) {
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

        self.deleteSchool = function (schoolId) {
            return self.getSchool(schoolId).success(function (school) {
                alertService.confirm({
                    title: school.name,
                    message: $translate.instant('school.delete.prompt')
                }).then(function () {
                        return uiTeacher.deleteSchool(schoolId).success(function () {
                            $location.path('/');
                        });
                    });
            });
        };

        self.getSchool = uiTeacher.getSchool;

        self.getStudents = uiTeacher.getStudents;

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

        self.deleteStudent = function (studentId) {
            return self.getStudent(studentId).success(function (student) {
                alertService.confirm({
                    title: student.name,
                    message: $translate.instant('student.delete.prompt')
                }).then(function () {
                        return uiTeacher.deleteStudent(studentId).success(function () {
                            $location.path('/');
                        });
                    });
            });
        };

        self.disableStudent = function (studentId) {
            var d = $q.defer();
            self.getStudent(studentId).success(function (student) {
                if (student.disabled) {
                    d.reject();
                } else {
                    alertService.confirm({
                        title: student.name,
                        message: $translate.instant('student.disable.prompt')
                    }).then(function () {
                            uiTeacher.disableStudent(studentId).success(function () {
                                d.resolve()
                            })
                        })
                }
            });
            return d.promise;
        };

        self.enableStudent = uiTeacher.enableStudent;

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

        self.updateLesson = function (lesson) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.lesson.tpl.html',
                controller: 'dialogLesson',
                resolve: {
                    input: function () {
                        return {
                            start: lesson.from,
                            end: lesson.to,
                            location: lesson.location,
                            studentId: lesson.student.id
                        };
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (lessonForm) {
                                return uiTeacher.updateLesson(lesson.id, {
                                    studentId: lessonForm.studentId,
                                    location: lessonForm.location,
                                    from: lessonForm.from,
                                    to: lessonForm.to
                                })
                            }
                        }
                    }
                }
            }).result
        };

        function adjustTime(date, dayDelta, minuteDelta) {
            var time = new Date(date).getTime();
            time += 24 * 60 * 60000 * dayDelta;
            time += 60000 * minuteDelta;
            return new Date(time);
        }

        self.updateLessonWithDelta = function (lesson, resize, dayDelta, minuteDelta) {
            var from;
            var to;
            if (resize) {
                from = lesson.from;
                to = adjustTime(lesson.to, dayDelta, minuteDelta)
            } else {
                from = adjustTime(lesson.from, dayDelta, minuteDelta);
                to = adjustTime(lesson.to, dayDelta, minuteDelta);
            }
            return uiTeacher.updateLesson(lesson.id, {
                studentId: lesson.student.id,
                location: lesson.location,
                from: from,
                to: to
            })
        };

        return self;
    })
;