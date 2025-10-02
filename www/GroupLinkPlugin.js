var exec = require("cordova/exec");

var groupLink = function () {};

var ensureBoolean = function (callback) {
  return function (result) {
    if (typeof result == "boolean") {
      callback(result);
      return;
    }

    callback(result == "true");
  };
};

var execMethod = function (method, param, success, error) {
  exec(success, error, "GroupLinkPlugin", method, [param]);
};

// Android only
groupLink.register = function (param, success, error) {
  execMethod("register", param, success, error);
};

// Android only
groupLink.requestPermissions = function (param, success, error) {
  execMethod("requestPermissions", param, success, error);
};

// Android only
groupLink.checkPermissions = function (param, success, error) {
  execMethod("checkPermissions", param, ensureBoolean(success), error);
};

// Android only
groupLink.unsubscribePermissionsStatus = function (param, success, error) {
  execMethod("unsubscribePermissionsStatus", param, success, error);
};

// iOS only
groupLink.init = function (param, success, error) {
  execMethod("init", param, success, error);
};

// iOS and Android
groupLink.getUserId = function (param, success, error) {
  execMethod("getUserId", param, success, error);
};

// iOS and Android
groupLink.getUIBackgroundModes = function (param, success, error) {
  execMethod("getUIBackgroundModes", param, success, error);
};

if (!window.plugins) {
  window.plugins = {};
}

window.plugins.groupLink = groupLink;

module.exports = groupLink;
