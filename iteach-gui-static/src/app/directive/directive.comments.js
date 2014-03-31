angular.module('iteach.directive.comments', [
        'iteach.ui.comment'
    ])
    .directive('itComments', function (uiComment) {
        return {
            restrict: 'E',
            scope: {
                entity: '@',
                entityId: '@'
            },
            templateUrl: 'app/directive/directive.comments.tpl.html',
            link: function (scope) {
                // Getting the list of comments
                uiComment.getComments(scope.entity, scope.entityId).success(function (comments) {
                    scope.comments = comments;
                });
            },
            controller: function ($scope) {
                $scope.commentContent = '';
                $scope.commentForm = false;
                $scope.showCommandForm = function (show) {
                    $scope.commentForm = show;
                };
                $scope.submitComment = function () {
                    uiComment.postComment($scope.entity, $scope.entityId, $scope.commentContent).success(function (comment) {
                        $scope.commentContent = '';
                        $scope.commentForm = false;
                        // TODO $scope.comments.unshift(comment);
                    });
                };
            }
        }
    })
;