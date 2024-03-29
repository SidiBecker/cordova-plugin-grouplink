package cordova.plugin.grouplink;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.grouplinknetwork.GroupLink;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupLinkPlugin extends CordovaPlugin {

    private static final int REQUEST_PERMISSION_CODE = 420;
    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 0;

    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final String[] REQUIRED_PERMISSIONS_Q = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @RequiresApi(api = Build.VERSION_CODES.S)
    private static final String[] REQUIRED_PERMISSIONS_S = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ? new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.POST_NOTIFICATIONS
    }
            : new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT
    };

    private int count = 0;
    private int countAutoStart = 0;
    private CallbackContext permissionStatusHandler;

    private Context getContext() {
        return this.cordova.getActivity().getApplicationContext();
    }

    private Activity getActivity() {
        return this.cordova.getActivity();
    }

    private interface Actions {
        public static final String REGISTER = "register";
        public static final String REQUEST_PERMISSIONS = "requestPermissions";
        public static final String GET_USER_ID = "getUserId";
        public static final String CHECK_PERMISSIONS = "checkPermissions";
        public static final String UNSUBSCRIBE_PERMISSION_STATUS = "unsubscribePermissionsStatus";
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        switch (action) {

            case Actions.REGISTER:
                this.register(args, callbackContext);
                return true;

            case Actions.REQUEST_PERMISSIONS:
                permissionStatusHandler = callbackContext;
                this.requestGlPermissions();
                return true;

            case Actions.GET_USER_ID:
                this.getUserId(callbackContext);
                return true;

            case Actions.CHECK_PERMISSIONS:
                this.checkGlPermissions(callbackContext);
                return true;

            case Actions.UNSUBSCRIBE_PERMISSION_STATUS:
                permissionStatusHandler = null;
                callbackContext.success("true");
                return true;

            default:
                callbackContext.error("Method " + action + " not found");
                break;
        }
        return false;
    }

    private void sendPermissionsStatus() {
        if (permissionStatusHandler == null) {
            return;
        }

        sendMessageToJs(checkGlPermissions(null), permissionStatusHandler);
    }

    private void sendMessageToJs(Boolean message, CallbackContext callback) {
        final PluginResult result = new PluginResult(PluginResult.Status.OK, message);
        result.setKeepCallback(true);
        callback.sendPluginResult(result);
    }

    private void register(JSONArray args, CallbackContext callbackContext) throws JSONException {

        final JSONObject options = args.getJSONObject(0);

        String token;
        Boolean test;

        try {
            token = options.getString("token");
            test = options.getBoolean("test");
        } catch (JSONException e) {
            callbackContext.error("The object is invalid.");
            return;
        }

        GroupLink.register(
                getContext(),
                token,
                test // true if you want to test if the implementation is working.
        );

        callbackContext.success("true");

    }

    private Boolean checkGlPermissions(CallbackContext callbackContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Boolean status = hasNeededPermissionsS();
            if (callbackContext != null) {
                callbackContext.success(status.toString());
            }
            return status;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            Boolean status = hasNeededPermissionsQ();
            if (callbackContext != null) {
                callbackContext.success(status.toString());
            }
            return status;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            Boolean status = hasNeededPermissions();
            if (callbackContext != null) {
                callbackContext.success(status.toString());
            }
            return status;
        }

        return false;
    }

    private void requestGlPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!hasNeededPermissionsS()) {
                requestPermissionsS();
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            if (!hasNeededPermissionsQ()) {
                requestPermissionsQ();
                return;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (!hasNeededPermissions()) {
                requestPermissions();
                return;
            }
        }
        if (countAutoStart < 1) {
            countAutoStart++;
        }
    }

    private void getUserId(CallbackContext callbackContext) {
        String userId = GroupLink.getUserId(this.getContext());

        callbackContext.success(userId);
    }

    private boolean hasNeededPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (!cordova.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNeededPermissionsQ() {
        for (String permission : REQUIRED_PERMISSIONS_Q) {
            if (!cordova.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private boolean hasNeededPermissionsS() {
        for (String permission : REQUIRED_PERMISSIONS_S) {
            if (!cordova.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        cordova.requestPermissions(this, REQUEST_PERMISSION_CODE, REQUIRED_PERMISSIONS);
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private void requestPermissionsQ() {
        cordova.requestPermissions(this, REQUEST_PERMISSION_CODE, REQUIRED_PERMISSIONS_Q);
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private void requestPermissionsS() {
        cordova.requestPermissions(this, REQUEST_PERMISSION_CODE, REQUIRED_PERMISSIONS_S);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            requestGlPermissions();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions,
                                          int[] grantResults) throws JSONException {

        sendPermissionsStatus();

        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (count < REQUIRED_PERMISSIONS_Q.length) {
                count++;
                requestGlPermissions();
                return;
            }
            if (countAutoStart < 1) {
                countAutoStart++;
            }
        }
        this.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
