var exec = require('cordova/exec');

var GroupLink = function() {
};

GroupLink.register = function (arg0, success, error) {
    window.alert("register");
    window.alert(JSON.stringify(arg0));
    exec(success, error, 'GroupLinkPlugin', 'coolMethod', [arg0]);
};

module.exports = GroupLink;
