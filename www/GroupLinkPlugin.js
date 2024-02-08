var exec = require('cordova/exec');

var groupLink = function() {
};

var execMethod = function(method, param, success, error) {
    exec(success, error, 'GroupLinkPlugin', method, [param]);
};

groupLink.register = function (param, success, error) {
    execMethod('register', param, success, error);
};

if (!window.plugins) {
    window.plugins = {};
}
  
window.plugins.groupLink = groupLink;

module.exports = groupLink;
