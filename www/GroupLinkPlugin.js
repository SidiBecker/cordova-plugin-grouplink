cordova.define("cordova-plugin-grouplink.groupLink", function (require, exports, module) {
    var exec = require('cordova/exec');

    var groupLink = function () {
    };

    var ensureBoolean = function (callback) {
        return function (result) {

            if (typeof result == "boolean") {
                callback(result);
                return;
            }

            callback(result == "true");
        }
    };

    var ensureArray = function (callback) {
        return function (result) {

            if (result == null) {
                return [];
            }

            if (Array.isArray(result)) {
                callback(result);
                return;
            }

            callback(JSON.parse(result));
        }
    };

    var execMethod = function (method, param, success, error) {
        exec(success, error, 'GroupLinkPlugin', method, [param]);
    };

    groupLink.register = function (param, success, error) {
        execMethod('register', param, success, error);
    };

    groupLink.requestPermissions = function (param, success, error) {
        execMethod('requestPermissions', param, success, error);
    };

    groupLink.getUserId = function (param, success, error) {
        execMethod('getUserId', param, success, error);
    };

    groupLink.checkPermissions = function (param, success, error) {
        execMethod('checkPermissions', param, ensureBoolean(success), error);
    };

    groupLink.subscribePermissionsStatus = function (param, success, error) {
        execMethod('subscribePermissionsStatus', param, ensureBoolean(success), error);
    };

    groupLink.unsubscribePermissionsStatus = function (param, success, error) {
        execMethod('unsubscribePermissionsStatus', param, success, error);
    };

    groupLink.getUnauthorizedPermissions = function (param, success, error) {
        execMethod('getUnauthorizedPermissions', param, ensureArray(success), error);
    };


    if (!window.plugins) {
        window.plugins = {};
    }

    window.plugins.groupLink = groupLink;

    module.exports = groupLink;

});
