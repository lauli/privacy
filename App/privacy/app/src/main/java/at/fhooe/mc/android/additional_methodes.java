package at.fhooe.mc.android;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by laureenschausberger on 14.06.16.
 */
public class additional_methodes extends Activity{

    private static final String LOG_TAG = "PostSample";

    public RequestHandle executeSample(AsyncHttpClient client,
                                       String URL,
                                       Header[] headers,
                                       HttpEntity entity,
                                       ResponseHandlerInterface responseHandler) {
        return client.post(additional_methodes.this, URL, headers, entity, null, responseHandler);
    }

    public boolean isRequestBodyAllowed() {
        return true;
    }

    public boolean isRequestHeadersAllowed() {
        return true;
    }

    public String getDefaultURL() {
        return "http://emagycavirpeht.esy.es/create_user.php";
    }


    public ResponseHandlerInterface getResponseHandler() {
        return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        };
    }


    protected void registerClient() {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("lang_id", 1);
        params.put("name", "Laureen");
        client.post(getDefaultURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {

            }
        });


        // client.post(getDefaultURL(), getResponseHandler());

    }
}
