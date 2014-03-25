angular.module('iteach.config', [])
    .service('config', function ($interpolate) {
        var _version = '1.0.0-SNAPSHOT';
        var _server = '.';
        return {
            version: _version,
            api: function (path, context) {
                return $interpolate(_server + '/api/' + path)(context);
            }
        }
    })
;