angular.module('iteach.config', [])
    .service('config', function () {
        var _version = '1.0.0-SNAPSHOT';
        var _server = '.';
        return {
            version: _version,
            api: function (path) {
                return _server + '/api/' + path
            }
        }
    })
;