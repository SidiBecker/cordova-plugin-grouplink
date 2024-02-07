var exec = require('cordova/exec');

var GroupLink = function() {
};

GroupLink.coolMethod = function (arg0, success, error) {
    exec(success, error, 'GroupLinkPlugin', 'coolMethod', [arg0]);
    window.alert('TESTE');
};



module.exports = GroupLink;
