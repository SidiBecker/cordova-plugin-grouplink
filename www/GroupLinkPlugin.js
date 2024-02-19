var exec = require('cordova/exec');

var groupLink = function() {
};

var execMethod = function(method, param, success, error) {
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
    execMethod('checkPermissions', param, success, error);
};


if (!window.plugins) {
    window.plugins = {};
}
  
window.plugins.groupLink = groupLink;

module.exports = groupLink;
