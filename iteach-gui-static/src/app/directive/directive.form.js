angular.module('iteach.directive.form', [])
    .directive('itForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.form.tpl.html',
            transclude: true
        }
    })
    .directive('itFormText', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                name: '@name',
                label: '@label',
                size: '@size'
            },
            templateUrl: 'app/directive/directive.form.text.tpl.html'
        }
    })
    .directive('itFormColour', function () {
        return {
            restrict: 'E',
            replace: true,
            scope: {
                name: '@name',
                label: '@label'
            },
            templateUrl: 'app/directive/directive.form.colour.tpl.html'
        }
    })
;