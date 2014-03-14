angular.module('iteach.directive.form', [])
    .directive('itForm', function () {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.form.tpl.html',
            transclude: true
        }
    })
;