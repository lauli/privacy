package at.fhooe.mc.android;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 14.06.16.
 * callback interface for AdditionalMethods calls
 */
public interface OnJSONResponseCallback {
     void onJSONResponse(boolean success, JSONObject response);
}
