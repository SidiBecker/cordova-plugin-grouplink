var exec = require('cordova/exec');

var groupLink = function() {
};

groupLink.register = function (arg0, success, error) {
    window.alert("register");
    window.alert(JSON.stringify(arg0));
    exec(success, error, 'GroupLinkPlugin', 'coolMethod', [arg0]);
};

if (!window.plugins) {
    window.plugins = {};
}
  
window.plugins.groupLink = groupLink;

module.exports = groupLink;
