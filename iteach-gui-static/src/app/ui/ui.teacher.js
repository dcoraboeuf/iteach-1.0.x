angular.module('iteach.ui.teacher', [
        'iteach.config'
    ])
    .service('uiTeacher', function ($q, $http, config) {
        var self = {};

        /**
         * Schools
         */

        self.getSchools = function () {
            var d = $q.defer();
            $http.get(config.api('teacher/school'))
                .success(function (schools) {
                    d.resolve(schools);
                })
            return d.promise;
        }

        self.createSchool = function (school) {
            var d = $q.defer();
            $http.post(config.api('teacher/school'), school)
                .success(function (school) {
                    d.resolve(school);
                })
            return d.promise;
        }

        self.getSchool = function (schoolId) {
            var d = $q.defer();
            $http.get(config.api('teacher/school/' + schoolId))
                .success(function (school) {
                    d.resolve(school);
                })
            return d.promise;
        }

        self.updateSchool = function (schoolId, schoolForm) {
            var d = $q.defer();
            $http.put(config.api('teacher/school/' + schoolId), schoolForm)
                .success(function (school) {
                    d.resolve(school);
                })
            return d.promise;
        }

        /**
         * Students
         */

        self.getStudents = function () {
            var d = $q.defer();
            $http.get(config.api('teacher/student'))
                .success(function (students) {
                    d.resolve(students);
                })
            return d.promise;
        }

        self.createStudent = function (student) {
            var d = $q.defer();
            $http.post(config.api('teacher/student'), student)
                .success(function (student) {
                    d.resolve(student);
                })
            return d.promise;
        }

        /**
         * Lessons
         */
        self.createLesson = function (lessonForm) {
            var d = $q.defer();
            $http.post(config.api('teacher/lesson'), lessonForm)
                .success(function (lesson) {
                    d.resolve(lesson);
                })
            return d.promise;
        }

        return self;
    })
;