angular.module('iteach.directive.form', [])
    .directive('itForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.form.tpl.html',
            transclude: true
        }
    })
    .directive('itFormField', function () {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                name: '@name',
                label: '@label'
            },
            templateUrl: 'app/directive/directive.form.field.tpl.html'
        }
    })
    .directive('itFormText', function () {
        return {
            restrict: 'E',
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
            scope: {
                name: '@name',
                label: '@label'
            },
            templateUrl: 'app/directive/directive.form.colour.tpl.html'
        }
    })
    .directive('itFormMemo', function () {
        return {
            restrict: 'E',
            scope: {
                name: '@name',
                label: '@label',
                size: '@size',
                rows: '@rows'
            },
            templateUrl: 'app/directive/directive.form.memo.tpl.html'
        }
    })
;