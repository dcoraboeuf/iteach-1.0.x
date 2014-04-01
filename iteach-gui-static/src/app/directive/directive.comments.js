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
            controller: function ($scope) {
                // Getting the list of comments
                uiComment.getComments($scope.entity, $scope.entityId).success(function (comments) {
                    $scope.comments = comments.resources;
                });
                // Form
                $scope.commentContent = '';
                $scope.commentForm = false;
                $scope.showCommandForm = function (show) {
                    $scope.commentForm = show;
                    for (var i = 0; i < $scope.comments.length; i++) {
                        $scope.comments[i].edited = false;
                    }
                };
                $scope.submitComment = function () {
                    uiComment.postComment($scope.entity, $scope.entityId, $scope.commentContent).success(function (comment) {
                        $scope.commentContent = '';
                        $scope.commentForm = false;
                        $scope.comments.unshift(comment);
                    });
                };
                // Updating a comment
                $scope.showUpdateForm = function (comment) {
                    $scope.showCommandForm(false);
                    comment.edited = true;
                };
                // Deleting a comment
                $scope.deleteComment = function (id) {
                    uiComment.deleteComment($scope.entity, id).success(function () {
                        // Deletes the item from the list using its ID
                        var index = -1;
                        for (var i = 0; i < $scope.comments.length; i++) {
                            if ($scope.comments[i].id == id) {
                                index = i;
                            }
                        }
                        if (index >= 0) {
                            $scope.comments.splice(index, 1);
                        }
                    });
                };
            }
        }
    })
;