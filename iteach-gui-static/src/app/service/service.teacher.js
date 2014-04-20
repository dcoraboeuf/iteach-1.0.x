angular.module('iteach.service.teacher', [
    'iteach.service.core',
    'iteach.ui.teacher',
    'iteach.dialog.school',
    'iteach.dialog.student',
    'iteach.dialog.lesson',
    'iteach.dialog.invoice',
    'iteach.dialog.contract'
])
    .service('teacherService', function ($q, $log, $modal, $translate, $interpolate, $location, alertService, uiTeacher, localDataService) {
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

        self.getSchoolReport = uiTeacher.getSchoolReport;

        self.getStudents = uiTeacher.getStudents;

        self.createStudent = function () {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.student.tpl.html',
                controller: 'dialogStudent',
                resolve: {
                    initialStudent: function () {
                        return {}
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (studentForm) {
                                var form = {
                                    schoolId: studentForm.schoolId,
                                    contractId: studentForm.contractId,
                                    name: studentForm.name,
                                    subject: studentForm.subject,
                                    postalAddress: studentForm.postalAddress,
                                    phone: studentForm.phone,
                                    mobilePhone: studentForm.mobilePhone,
                                    email: studentForm.email
                                };
                                if (studentForm.contractId) {
                                    form.contractId = studentForm.contractId;
                                }
                                return uiTeacher.createStudent(form);
                            }
                        }
                    }
                }
            }).result
        };

        self.getStudent = uiTeacher.getStudent;

        self.updateStudent = function (student) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.student.tpl.html',
                controller: 'dialogStudent',
                resolve: {
                    initialStudent: function () {
                        return angular.copy(student)
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (studentForm) {
                                var form = {
                                    schoolId: studentForm.schoolId,
                                    name: studentForm.name,
                                    subject: studentForm.subject,
                                    postalAddress: studentForm.postalAddress,
                                    phone: studentForm.phone,
                                    mobilePhone: studentForm.mobilePhone,
                                    email: studentForm.email
                                };
                                if (studentForm.contractId) {
                                    form.contractId = studentForm.contractId;
                                }
                                return uiTeacher.updateStudent(student.id, form)
                            }
                        }
                    }
                }
            }).result
        };

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
                                return uiTeacher.createLesson({
                                    studentId: lessonForm.student.id,
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

        self.getLessonReport = uiTeacher.getLessonReport;

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
                            student: lesson.student
                        };
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (lessonForm) {
                                return uiTeacher.updateLesson(lesson.id, {
                                    studentId: lessonForm.student.id,
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

        self.getReport = uiTeacher.getReport;

        self.createInvoice = function (schoolId, period) {
            $log.debug('Creating invoice', schoolId, period);
            var thePeriod;
            if (period) {
                thePeriod = period;
            } else {
                var date = localDataService.getCurrentDate();
                thePeriod = {
                    year: date.getFullYear(),
                    month: date.getMonth() + 1
                };
            }
            var d = $q.defer();
            uiTeacher.getInvoiceFormData().success(function (data) {
                d.resolve($modal.open({
                    templateUrl: 'app/dialog/dialog.invoice.tpl.html',
                    controller: 'dialogInvoice',
                    resolve: {
                        invoiceForm: function () {
                            return {
                                schoolId: schoolId,
                                period: thePeriod,
                                number: data.nextInvoiceNumber,
                                detailPerStudent: data.detailPerStudent
                            };
                        }
                    }
                }).result);
            });
            return d.promise;
        };

        self.loadInvoice = uiTeacher.loadInvoice;

        self.getInvoices = uiTeacher.getInvoices;

        self.deleteInvoices = uiTeacher.deleteInvoices;

        // Contracts

        self.getContracts = uiTeacher.getContracts;

        self.createContract = function (schoolId) {
            return $modal.open({
                templateUrl: 'app/dialog/dialog.contract.tpl.html',
                controller: 'dialogContract',
                resolve: {
                    school: function () {
                        return self.getSchool(schoolId)
                    },
                    contract: function () {
                        return {};
                    },
                    modalController: function () {
                        return {
                            onSubmit: function (contractForm) {
                                return uiTeacher.createContract(schoolId, contractForm);
                            }
                        }
                    }
                }
            }).result
        };

        // OK

        return self;
    })
;