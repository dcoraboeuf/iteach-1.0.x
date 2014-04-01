angular.module('iteach.ui.comment', [])
    .service('uiComment', function ($http, config) {
        var self = {};

        self.getComments = function (entity, entityId) {
            return $http.get(config.api('comment/{{entity}}/list/{{entityId}}', {
                entity: entity,
                entityId: entityId
            }));
        };

        self.postComment = function (entity, entityId, content) {
            return $http.post(config.api('comment/{{entity}}/{{entityId}}', {
                entity: entity,
                entityId: entityId
            }), {
                content: content
            });
        };

        self.deleteComment = function (entity, id) {
            return $http.delete(config.api('comment/{{entity}}/{{id}}', {
                entity: entity,
                id: id
            }));
        };

        self.updateComment = function (entity, id, content) {
            return $http.put(config.api('comment/{{entity}}/{{id}}', {
                entity: entity,
                id: id
            }), {
                content: content
            });
        };

        return self;
    })
;