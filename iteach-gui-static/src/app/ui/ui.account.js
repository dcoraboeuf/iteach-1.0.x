angular.module('iteach.ui.account', [])
    .service('uiAccount', function () {

        var self = {};

        self.current = function () {
            return {
                authenticated: true,
                teacher: {
                    id: 2,
                    name: 'Damien',
                    email: 'damien@test.com',
                    administrator: false,
                    authenticationMode: 'PASSWORD'
                }
            };
        }

        return self;

    })
;