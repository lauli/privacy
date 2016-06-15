package at.fhooe.mc.android;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 14.06.16.
 */
public interface OnJSONResponseCallback {
    public void onJSONResponse(boolean success, JSONObject response);
}
