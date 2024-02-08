package cordova.plugin.grouplink;

import android.content.Context;
import android.util.Log;

import com.grouplinknetwork.GroupLink;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class GroupLinkPlugin extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Log.w("GROUPLINK", "action: " + action);

        switch (action) {
            case "register":
                this.register(args, callbackContext);
                return true;
            default:
                callbackContext.error("Method " + action + " not found");
                break;
        }
        return false;
    }

    private void register(JSONArray args, CallbackContext callbackContext)  throws JSONException {

        final JSONObject options = args.getJSONObject(0);

        Log.w("OBJECT", options.toString());

        Context context = this.cordova.getActivity().getApplicationContext();

        String token =  options.getString("token");
        Boolean test =  options.getBoolean("test");

        Log.w("token", token);
        Log.w("test", test.toString());

        GroupLink.register(
                context,
                options.getString("token"),
                options.getBoolean("test") //true if you want to test if the implementation is working.
        );

    }
}
