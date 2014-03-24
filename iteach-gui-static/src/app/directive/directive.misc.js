angular.module('iteach.directive.misc', [])
    .directive('itTodo', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.todo.tpl.html',
            transclude: true
        }
    })
    .directive('itYesNo', function ($translate) {
        return {
            restrict: 'E',
            scope: {
                value: '='
            },
            templateUrl: 'app/directive/directive.yesno.tpl.html'
        }
    })
;