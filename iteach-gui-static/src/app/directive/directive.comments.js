angular.module('iteach.directive.comments', [])
    .directive('itComments', function () {
        return {
            restrict: 'E',
            scope: {
                entity: '@',
                entityId: '@'
            },
            templateUrl: 'app/directive/directive.comments.tpl.html'
        }
    })
;