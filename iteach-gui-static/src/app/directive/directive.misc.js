angular.module('iteach.directive.misc', [])
    .directive('itTodo', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.todo.tpl.html',
            transclude: true
        }
    })
;