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
                label: '@label',
                help: '@help'
            },
            templateUrl: 'app/directive/directive.form.field.tpl.html'
        }
    })
    .directive('itFormText', function () {
        return {
            restrict: 'E',
            scope: {
                ngModel: '=',
                name: '@name',
                label: '@label',
                size: '@size',
                help: '@help',
                required: '@required'
            },
            templateUrl: 'app/directive/directive.form.text.tpl.html'
        }
    })
    .directive('itFormEmail', function () {
        return {
            restrict: 'E',
            scope: {
                name: '@name',
                label: '@label',
                help: '@help',
                required: '@required'
            },
            templateUrl: 'app/directive/directive.form.email.tpl.html'
        }
    })
    .directive('itFormColour', function () {
        return {
            restrict: 'E',
            scope: {
                name: '@name',
                label: '@label',
                help: '@help',
                required: '@required'
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
                rows: '@rows',
                help: '@help'
            },
            templateUrl: 'app/directive/directive.form.memo.tpl.html'
        }
    })
;