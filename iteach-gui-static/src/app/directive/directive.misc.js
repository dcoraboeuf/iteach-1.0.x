angular.module('iteach.directive.misc', [])
    .directive('itTodo', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.todo.tpl.html',
            transclude: true
        }
    })
    .directive('itYesNo', function () {
        return {
            restrict: 'E',
            scope: {
                value: '='
            },
            templateUrl: 'app/directive/directive.yesno.tpl.html'
        }
    })
    .directive('fileModel', function ($parse) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                var model = $parse(attrs.fileModel);
                var modelSetter = model.assign;
                element.bind('change', function () {
                    scope.$apply(function () {
                        modelSetter(scope, element[0].files[0]);
                    });
                });
            }
        }
    })
;