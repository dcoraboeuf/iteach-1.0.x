angular.module('iteach.view.teacher', [
    'iteach.service.core',
    'iteach.service.teacher'
])
    .controller('TeacherCtrl', function ($log, $scope, $translate, teacherService, localDataService, calendarService) {

        /**
         * Schools
         */

        function loadSchools() {
            teacherService.getSchools().then(function (schools) {
                $scope.schools = schools
            })
        }

        // Loads the list of schools
        loadSchools();

        // Creating a school
        $scope.createSchool = function () {
            teacherService.createSchool().then(loadSchools)
        };

        /**
         * Students
         */

        function loadStudents() {
            teacherService.getStudents(true).success(function (students) {
                $scope.students = students
            })
        }

        // Loads the list of students
        loadStudents();

        // Creating a student
        $scope.createStudent = function () {
            teacherService.createStudent().then(loadStudents)
        };

        /**
         * Planning
         */

            // Calendar - selection
        $scope.onCalendarSelect = function (start, end, allDay) {
            if (allDay) {
                $scope.mainCalendar.fullCalendar('unselect');
            } else {
                // Creates the lesson
                teacherService.createLesson(start, end).then(
                    function () {
                        $scope.mainCalendar.fullCalendar('fetchEvents')
                    }, function () {
                        $scope.mainCalendar.fullCalendar('unselect')
                    });
            }
        };

        // Planning: ratio according to the view mode
        $scope.onViewDisplay = function onViewDisplay(view) {
            $log.debug('View display', view.name);
            localDataService.setCurrentPlanningViewMode(view.name);
            if ('month' == view.name) {
                $scope.mainCalendar.fullCalendar('option', 'aspectRatio', 1.25);
            } else {
                $scope.mainCalendar.fullCalendar('option', 'aspectRatio', 0.5);
            }
        };

        // Planning: collection of lessons
        function adaptLesson(lesson) {
            // Dates
            lesson.start = new Date(lesson.from);
            lesson.end = new Date(lesson.to);
            // Colour
            lesson.backgroundColor = lesson.schoolColour;
            // Link
            lesson.url = "#/lesson/" + lesson.id;
            // Title with icon if comments
            if (lesson.hasComments) {
                lesson.className = 'it-lesson-with-comments';
            }
        }

        $scope.fetchEvents = function fetchEvents(start, end, callback) {
            $log.debug('Getting lessons');
            teacherService.getLessons({from: start, to: end}).then(
                function (collection) {
                    var lessons = collection.resources;
                    for (var i = 0; i < lessons.length; i++) {
                        var lesson = lessons[i];
                        adaptLesson(lesson);
                    }
                    callback(lessons);
                }
            )
        };

        $scope.onLessonResize = function (lesson, dayDelta, minuteDelta, revertFunc) {
            teacherService.updateLessonWithDelta(lesson, true, dayDelta, minuteDelta).error(revertFunc);
        };

        $scope.onLessonDrop = function (lesson, dayDelta, minuteDelta, allDay, revertFunc) {
            teacherService.updateLessonWithDelta(lesson, false, dayDelta, minuteDelta).error(revertFunc);
        };

        $scope.onViewRender = function () {
            var date = $scope.mainCalendar.fullCalendar('getDate');
            localDataService.setCurrentDate(date);
        };

        // Current date from the local storage
        $scope.currentDate = localDataService.getCurrentDate();

        teacherService.getCalendarPreferences().success(function (calendarPreferences) {
            $scope.calendarConfig.calendar.minTime = calendarPreferences.minTime;
            $scope.calendarConfig.calendar.maxTime = calendarPreferences.maxTime;
        });

        $scope.calendarConfig = {
            calendar: {
                height: 600,
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                // Dimensions
                aspectRatio: 0.5,
                viewDisplay: $scope.onViewDisplay,
                // Current date
                year: $scope.currentDate.getFullYear(),
                month: $scope.currentDate.getMonth(),
                date: $scope.currentDate.getDate(),
                // Date change
                viewRender: $scope.onViewRender,
                // i18n
                firstDay: calendarService.calendarI18n().firstDay,
                dayNames: calendarService.calendarI18n().dayNames,
                dayNamesShort: calendarService.calendarI18n().dayNamesShort,
                monthNames: calendarService.calendarI18n().monthNames,
                monthNamesShort: calendarService.calendarI18n().monthNamesShort,
                buttonText: calendarService.calendarI18n().buttonText,
                timeFormat: calendarService.calendarI18n().timeFormat,
                columnFormat: calendarService.calendarI18n().columnFormat,
                titleFormat: calendarService.calendarI18n().titleFormat,
                axisFormat: calendarService.calendarI18n().axisFormat,
                // General appearance
                allDaySlot: false,
                allDayDefault: false,
                // Configurable min/max time
                minTime: '07:00',
                maxTime: '21:00',
                // TODO Configurable week-ends
                weekends: false,
                // Default view
                defaultView: localDataService.getCurrentPlanningViewMode(),
                // Allowing selection (-> creation)
                selectable: true,
                selectHelper: true,
                select: $scope.onCalendarSelect,
                // Loading of events
                events: $scope.fetchEvents,
                // Resizing of an event
                editable: true,
                eventResize: $scope.onLessonResize,
                eventDrop: $scope.onLessonDrop
            }
        };
        $scope.lessons = [];

        // Calendar preferences
        $scope.calendarPreferences = function () {
            teacherService.calendarPreferences().then(function () {
                location.reload();
            });
        };
    })
;