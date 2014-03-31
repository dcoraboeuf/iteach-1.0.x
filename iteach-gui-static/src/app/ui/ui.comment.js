angular.module('iteach.ui.comment', [])
    .service('uiComment', function ($http, config) {
        var self = {};

        self.getComments = function (entity, entityId) {
            return $http.get(config.api('comment/{{entity}}/{{entityId}}', {
                entity: entity,
                entityId: entityId
            }));
        };

        return self;
    })
;