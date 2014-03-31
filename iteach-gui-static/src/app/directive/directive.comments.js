angular.module('iteach.directive.comments', [
        'iteach.ui.comment'
    ])
    .directive('itComments', function ($log, uiComment) {
        return {
            restrict: 'E',
            scope: {
                entity: '@',
                entityId: '@'
            },
            templateUrl: 'app/directive/directive.comments.tpl.html',
            link: function (scope) {
                $log.debug('[comments] entity=' + scope.entity);
                $log.debug('[comments] entityId=' + scope.entityId);
                // Getting the list of comments
                uiComment.getComments(scope.entity, scope.entityId).success(function (comments) {
                    scope.comments = comments;
                });
            }
        }
    })
;