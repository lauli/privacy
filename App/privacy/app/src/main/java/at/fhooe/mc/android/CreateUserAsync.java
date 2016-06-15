package at.fhooe.mc.android;

import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by laureenschausberger on 14.06.16.
 */
public class CreateUserAsync extends AsyncTask<User, String, Boolean>{

    @Override
    protected Boolean doInBackground(User... params) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams parameter = new RequestParams();
        parameter.put("lang_id", params[0].name);
        parameter.put("name", params[0].languageId);

        client.post("http://emagycavirpeht.esy.es/create_user.php", parameter, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: do error handling here
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject json;

                try {
                    json = new JSONObject(responseString);



                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }

            }
        });
        return true;
    }
}
