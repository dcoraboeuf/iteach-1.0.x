var _version = 'DEV';
angular.module('iteach.config', [])
    .service('config', function ($interpolate) {
        return {
            version: _version,
            api: function (path, context) {
                return $interpolate('./api/' + path)(context);
            }
        }
    })
;