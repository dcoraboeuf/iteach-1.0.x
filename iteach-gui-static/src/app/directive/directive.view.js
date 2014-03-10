angular.module('iteach.directive.view', [])
    .directive('it-view', function itView() {
        return {
            restrict: 'E',
            templateUrl: 'app/directive/directive.view.tpl.html',
            transclude: true
        }
    })
;