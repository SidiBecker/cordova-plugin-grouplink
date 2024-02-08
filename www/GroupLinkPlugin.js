var exec = require('cordova/exec');

var groupLink = function() {
};

groupLink.register = function (arg0, success, error) {
    exec(success, error, 'GroupLinkPlugin', 'register', [arg0]);
};

if (!window.plugins) {
    window.plugins = {};
}
  
window.plugins.groupLink = groupLink;

module.exports = groupLink;
