package cordova.plugin.grouplink;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.grouplinknetwork.GroupLink;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

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

    private interface Actions {
        public static final String REGISTER = "register";
        public static final String REQUEST_PERMISSIONS = "requestPermissions";
    }

    private Context getContext() {
        return this.cordova.getActivity().getApplicationContext();
    }

    private Activity getActivity() {
        return this.cordova.getActivity();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        switch (action) {
            case Actions.REGISTER:
                this.register(args, callbackContext);
                return true;
            case Actions.REQUEST_PERMISSIONS:
                this.requestGlPermissions();
                callbackContext.success("Permissions requested");
                return true;
            default:
                callbackContext.error("Method " + action + " not found");
                break;
        }
        return false;
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

        callbackContext.success("GroupLink registered");

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

    private boolean hasNeededPermissions() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this.getContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNeededPermissionsQ() {
        for (String permission : REQUIRED_PERMISSIONS_Q) {
            if (ActivityCompat.checkSelfPermission(this.getContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private boolean hasNeededPermissionsS() {
        for (String permission : REQUIRED_PERMISSIONS_S) {
            if (ActivityCompat.checkSelfPermission(this.getContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        this.getActivity().requestPermissions(REQUIRED_PERMISSIONS, REQUEST_PERMISSION_CODE);
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private void requestPermissionsQ() {
        this.getActivity().requestPermissions(REQUIRED_PERMISSIONS_Q, REQUEST_PERMISSION_CODE);
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private void requestPermissionsS() {
        this.getActivity().requestPermissions(REQUIRED_PERMISSIONS_S, REQUEST_PERMISSION_CODE);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
