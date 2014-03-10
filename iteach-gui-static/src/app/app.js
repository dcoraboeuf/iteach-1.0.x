'use strict';

// Declare app level module which depends on filters, and services
var iteach = angular.module('iteach', [
            'ui.bootstrap',
            'ngRoute',
            'pascalprecht.translate',
            'iteach.templates',
            'iteach.config',
            // Directives
            'iteach.directive.view',
            // Services
            'iteach.service.account',
            // UI
            'iteach.ui.account'
        ])
        // TODO HTTP configuration
        // TODO Translation configuration
        // TODO Translation URL configuration
        // TODO Runs the initial security service (in case of refresh)
        .run(function AppRun(accountService) {
            accountService.init()
        })
        // TODO HTTP error interceptor
        // Routing configuration
        .config(function ($routeProvider) {
            $routeProvider
                // Login page
                .when('/login', {
                    templateUrl: 'app/template/login.tpl.html'
                })
                // Teacher main page
                .when('/teacher', {
                    templateUrl: 'app/template/teacher.tpl.html'
                }
            );
        })
        .controller('AppCtrl', function AppCtrl($scope, config) {
            $scope.version = config.version;
        })
    ;

// BOOTSTRAPING SECTION

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
                // FIXME Loading the translations
                /*
                 $http.get(config.api('localization/en/' + config.version))
                 .success(function (map) {
                 $log.info('Default translation map loaded.');
                 _translationMap = map;
                 */
                // Starting the application
                $log.info('Starting the application...');
                angular.bootstrap(document, ['iteach']);
                // OK
                $log.info('Bootstraping done.');
                deferred.resolve();
                /*
                 })
                 .error(function () {
                 angular.element(document.getElementById('ontrack-loading-message'))
                 .removeClass('alert-info')
                 .addClass('alert-danger')
                 .text('Could not initialize application, configuration could not be loaded.');
                 deferred.reject();
                 })
                 ;
                 */
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