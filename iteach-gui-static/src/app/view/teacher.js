angular.module('iteach.view.teacher', [
        'iteach.service.teacher'
    ])
    .controller('TeacherCtrl', function ($scope, $translate, teacherService) {

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
        }

        /**
         * Students
         */

        function loadStudents() {
            teacherService.getStudents().then(function (students) {
                $scope.students = students
            })
        }

        // Loads the list of students
        loadStudents();

        // Creating a student
        $scope.createStudent = function() {
            teacherService.createStudent().then(loadStudents)
        }

        /**
         * Planning
         */

        // Intl
        $scope.calendarI18n = function () {
            return _calendar_i18n[$translate.use()]
        }

        // TODO Current date from the session
        $scope.currentDate = new Date();

        $scope.calendarConfig = {
            calendar:{
                height: 450,
                editable: true,
                header:{
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                // Dimensions
                aspectRatio: 0.5,
                // TODO viewDisplay: onViewDisplay,
                // Current date
                year: $scope.currentDate.getFullYear(),
                month: $scope.currentDate.getMonth(),
                date: $scope.currentDate.getDate(),
                // i18n
                firstDay: $scope.calendarI18n().firstDay,
                dayNames: $scope.calendarI18n().dayNames,
                dayNamesShort: $scope.calendarI18n().dayNamesShort,
                monthNames: $scope.calendarI18n().monthNames,
                monthNamesShort: $scope.calendarI18n().monthNamesShort,
                buttonText: $scope.calendarI18n().buttonText,
                timeFormat: $scope.calendarI18n().timeFormat,
                columnFormat: $scope.calendarI18n().columnFormat,
                titleFormat: $scope.calendarI18n().titleFormat,
                axisFormat: $scope.calendarI18n().axisFormat,
                // General appearance
                allDaySlot: false,
                // TODO Configurable min/max time
                minTime: '07:00',
                maxTime: '21:00',
                // TODO Configurable week-ends
                weekends: false,
                // Default view
                defaultView: 'agendaWeek',
                // Allowing selection (-> creation)
                selectable: true,
                selectHelper: true,
                // TODO select: onSelect,
                // Loading of events
                // TODO events: fetchEvents,
                // Resizing of an event
                editable: true
                // TODO eventResize: onEventChange,
                // eventDrop: function (event, dayDelta, minuteDelta, allDay, revertFunc) {
                // TODO    onEventMoved(event, dayDelta, minuteDelta, revertFunc);
                //}
            }
        };
        $scope.lessons = [];

        // TODO Loads the planning
    })
;