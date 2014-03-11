'use strict';

// Initial configuration
var _translationMap;

// Declare app level module which depends on filters, and services
var iteach = angular.module('iteach', [
            'ui.bootstrap',
            'ngRoute',
            'pascalprecht.translate',
            'iteach.templates',
            'iteach.config',
            // Directives
            'iteach.directive.view',
            // Views
            'iteach.view.login',
            'iteach.view.register',
            // Services
            'iteach.service.core',
            'iteach.service.account',
            // UI
            'iteach.ui.account'
        ])
        // TODO HTTP configuration
        // Translation configuration
        .config(function ($translateProvider) {
            $translateProvider.translations('en', map_en);
            $translateProvider.translations('fr', map_fr);
            $translateProvider.preferredLanguage('en');
        })
        // Runs the initial security service (in case of refresh)
        .run(function AppRun(accountService) {
            if (console) console.log('Loading the context')
            accountService.init()
        })
        // TODO HTTP error interceptor
        // Routing configuration
        .config(function ($routeProvider) {
            // TODO Try to register routes in the modules themselves
            $routeProvider
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
                // Teacher main page
                .when('/teacher', {
                    templateUrl: 'app/view/teacher.tpl.html'
                }
            );
        })
        .controller('AppCtrl', function AppCtrl($scope, $translate, config, notificationService) {
            $scope.version = config.version;
            // Language management
            $scope.language = function () {
                return $translate.use();
            };
            $scope.languageList = [{
                id: 'en',
                name: 'language.en'
            }, {
                id: 'fr',
                name: 'language.fr'
            }];
            $scope.changeLanguage = function (lang) {
                $translate.use(lang);
            };
            // Notifications
            $scope.hasNotification = function () {
                return angular.isDefined(notificationService.message);
            };
            $scope.notification = function () {
                return notificationService.message;
            };
            $scope.notificationType = function () {
                return notificationService.messageType;
            };
            $scope.closeNotification = function () {
                notificationService.clear();
            };
            // On state change
            $scope.$on('$routeChangeSuccess', function () {
                // Clears any notification
                notificationService.clear();
            });
        })
    ;

// BOOTSTRAPING SECTION
// TODO Maybe not needed any longer

angular.element(document).ready(function () {
    // Bootstrap element
    var bootstrapElement = document.getElementById('iteach-loading');
    // Bootstrap module
    var bootstrapModule = angular.module('iteach.bootstrap', ['iteach.config']);
    // Loading the default language localization map
    bootstrapModule.factory('bootstrapper', function ($http, $q, $log, config) {
        return {
            bootstrap: function () {
                $log.info('Initializing the application...');
                var deferred = $q.defer();
                // Starting the application
                $log.info('Starting the application...');
                angular.bootstrap(document, ['iteach']);
                // OK
                $log.info('Bootstraping done.');
                deferred.resolve();
                // OK
                return deferred.promise;
            }
        }
    });
    // Running the application after the bootstrap is complete
    bootstrapModule.run(function (bootstrapper, $log) {
        bootstrapper.bootstrap().then(function () {
            $log.info('Bootstraping finished. Removing the loading page.');
            // Removing the container will destroy the bootstrap app
            angular.element(bootstrapElement).remove();
        });

    });
    // Actual bootstraping
    angular.bootstrap(bootstrapElement, ['iteach.bootstrap']);
});