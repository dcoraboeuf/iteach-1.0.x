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
                });
            return d.promise;
        };

        self.createSchool = function (school) {
            var d = $q.defer();
            $http.post(config.api('teacher/school'), school)
                .success(function (school) {
                    d.resolve(school);
                });
            return d.promise;
        };

        self.getSchool = function (schoolId) {
            return $http.get(config.api('teacher/school/' + schoolId));
        };

        self.updateSchool = function (schoolId, schoolForm) {
            var d = $q.defer();
            $http.put(config.api('teacher/school/' + schoolId), schoolForm)
                .success(function (school) {
                    d.resolve(school);
                });
            return d.promise;
        };

        /**
         * Students
         */

        self.getStudents = function () {
            var d = $q.defer();
            $http.get(config.api('teacher/student'))
                .success(function (students) {
                    d.resolve(students);
                });
            return d.promise;
        };

        self.createStudent = function (student) {
            var d = $q.defer();
            $http.post(config.api('teacher/student'), student)
                .success(function (student) {
                    d.resolve(student);
                });
            return d.promise;
        };

        self.getStudent = function (studentId) {
            return $http.get(config.api('teacher/student/' + studentId));
        };

        /**
         * Lessons
         */

        self.createLesson = function (lessonForm) {
            var d = $q.defer();
            $http.post(config.api('teacher/lesson'), lessonForm)
                .success(function (lesson) {
                    d.resolve(lesson);
                });
            return d.promise;
        };

        self.getLessons = function (filter) {
            var d = $q.defer();
            $http.post(config.api('teacher/lesson/filter'), filter)
                .success(function (collection) {
                    d.resolve(collection);
                });
            return d.promise;
        };

        self.getLesson = function (lessonId) {
            return $http.get(config.api('teacher/lesson/' + lessonId));
        };

        self.deleteLesson = function (lessonId) {
            return $http.delete(config.api('teacher/lesson/' + lessonId));
        };

        self.updateLesson = function (lessonId, lessonForm) {
            return $http.put(config.api('teacher/lesson/' + lessonId), lessonForm);
        };

        return self;
    })
;