'use strict';

// Initial configuration
var _calendar_i18n = [];

// Local storage for the locale
if (console) console.log('[language] Checking language on load');
var language = localStorage['iteachLanguage'];
if (!language) {
    if (console) console.log('[language] No language specified, using "en"');
    language = 'en';
}
if (console) console.log('[language] Using ' + language);

// Declare app level module which depends on filters, and services
var iteach = angular.module('iteach', [
            'ui.bootstrap',
            'ui.calendar',
            'ngRoute',
            'ngSanitize',
            'ngLocale_' + language,
            'pascalprecht.translate',
            'iteach.templates',
            'iteach.config',
            // Directives
            'iteach.directive.view',
            'iteach.directive.comments',
            'iteach.directive.misc',
            // Views
            'iteach.view.error',
            'iteach.view.login',
            'iteach.view.register',
            'iteach.view.registrationResult',
            'iteach.view.passwordChangeRequest',
            'iteach.view.account',
            'iteach.view.admin',
            'iteach.view.teacher',
            'iteach.view.school',
            'iteach.view.student',
            'iteach.view.lesson',
            'iteach.view.admin.setup',
            'iteach.view.admin.accounts',
            'iteach.view.admin.account',
            'iteach.view.report',
            'iteach.view.invoices',
            // Services
            'iteach.service.core',
            'iteach.service.account',
            'iteach.service.teacher',
            // UI
            'iteach.ui.account',
            'iteach.ui.teacher'
        ])
        //HTTP configuration
        .config(function ($httpProvider) {
            // Default error management
            $httpProvider.interceptors.push('httpErrorInterceptor');
            // Authentication using cookies and CORS protection
            $httpProvider.defaults.withCredentials = true;
        })
        // Translation configuration
        .config(function ($translateProvider) {
            $translateProvider.translations('en', map_en);
            $translateProvider.translations('fr', map_fr);
            $translateProvider.preferredLanguage(language);
        })
        // Runs the initial security service (in case of refresh)
        .run(function AppRun(accountService) {
            if (console) console.log('Loading the context');
            accountService.init()
        })
        // HTTP error interceptor
        .factory('httpErrorInterceptor', function ($q, $log, $interpolate, errorService) {
            return {
                'responseError': function (rejection) {
                    errorService.process(rejection);
                    // Standard behaviour
                    return $q.reject(rejection);
                }
            }
        })
        // Routing configuration
        .config(function ($routeProvider) {
            // TODO Try to register routes in the modules themselves
            $routeProvider
                // Error page
                .when('/error', {
                    templateUrl: 'app/view/error.tpl.html',
                    controller: 'ErrorCtrl'
                })
                // Login page
                .when('/login', {
                    templateUrl: 'app/view/login.tpl.html',
                    controller: 'LoginCtrl'
                })
                // Registration page
                .when('/register', {
                    templateUrl: 'app/view/register.tpl.html',
                    controller: 'RegisterCtrl'
                })
                // Registration OK page
                .when('/registration/:result', {
                    templateUrl: 'app/view/registrationResult.tpl.html',
                    controller: 'RegistrationResultCtrl'
                })
                // Password change form
                .when('/passwordChangeRequest/:token', {
                    templateUrl: 'app/view/passwordChangeRequest.tpl.html',
                    controller: 'PasswordChangeRequestCtrl'
                })
                // Account page
                .when('/account', {
                    templateUrl: 'app/view/account.tpl.html',
                    controller: 'AccountCtrl'
                })
                // Admin main page
                .when('/admin', {
                    templateUrl: 'app/view/admin.tpl.html',
                    controller: 'AdminCtrl'
                })
                // Teacher main page
                .when('/teacher', {
                    templateUrl: 'app/view/teacher.tpl.html',
                    controller: 'TeacherCtrl'
                })
                // School page
                .when('/school/:schoolId', {
                    templateUrl: 'app/view/school.tpl.html',
                    controller: 'SchoolCtrl'
                })
                // Student page
                .when('/student/:studentId', {
                    templateUrl: 'app/view/student.tpl.html',
                    controller: 'StudentCtrl'
                })
                // Lesson page
                .when('/lesson/:lessonId', {
                    templateUrl: 'app/view/lesson.tpl.html',
                    controller: 'LessonCtrl'
                })
                // Admin set-up
                .when('/admin/setup', {
                    templateUrl: 'app/view/admin.setup.tpl.html',
                    controller: 'AdminSetupCtrl'
                })
                // Admin accounts
                .when('/admin/accounts', {
                    templateUrl: 'app/view/admin.accounts.tpl.html',
                    controller: 'AdminAccountsCtrl'
                })
                // Admin account
                .when('/admin/account/:accountId', {
                    templateUrl: 'app/view/admin.account.tpl.html',
                    controller: 'AdminAccountCtrl'
                })
                // Admin account import
                .when('/admin/account/:accountId/import', {
                    templateUrl: 'app/view/admin.account.import.tpl.html',
                    controller: 'AdminAccountImportCtrl'
                })
                // Monthly report
                .when('/report', {
                    templateUrl: 'app/view/report.tpl.html',
                    controller: 'ReportCtrl'
                })
                // Management of invoices
                .when('/invoices', {
                    templateUrl: 'app/view/invoices.tpl.html',
                    controller: 'InvoicesCtrl'
                })
                // Default
                .otherwise({
                    template: '<div></div>',
                    controller: 'HomeCtrl'
                })
            ;
        })
        .controller('HomeCtrl', function (accountService) {
            accountService.init();
        })
        .controller('AppCtrl', function AppCtrl($rootScope, $scope, $translate, config, accountService, teacherService) {
            $scope.version = config.version;
            // Language management
            $scope.language = function () {
                return $translate.use();
            };
            $scope.languageList = [
                {
                    id: 'en',
                    name: 'language.en'
                },
                {
                    id: 'fr',
                    name: 'language.fr'
                }
            ];
            $scope.changeLanguage = function (lang) {
                localStorage['iteachLanguage'] = lang;
                location.reload();
            };
            // Notifications
            $scope.hasNotification = function () {
                return angular.isDefined($rootScope.message);
            };
            $scope.notification = function () {
                return $rootScope.message;
            };
            $scope.notificationType = function () {
                return $rootScope.messageType;
            };
            $scope.closeNotification = function () {
                $rootScope.message = undefined;
            };
            // Connection
            $scope.logged = function () {
                return angular.isDefined($rootScope.account) && $rootScope.account.authenticated;
            };
            $scope.accountLogout = function () {
                accountService.logout()
            };
            $scope.accountProfile = accountService.accountProfile;
            // User menu actions
            $scope.createInvoice = teacherService.createInvoice;
            // On state change
            $scope.$on('$routeChangeSuccess', function () {
                if ($rootScope.preserveMessage) {
                    // We want to preserve the message only for this time
                    $rootScope.preserveMessage = undefined;
                } else {
                    $rootScope.message = undefined;
                }
            });
        })
    ;