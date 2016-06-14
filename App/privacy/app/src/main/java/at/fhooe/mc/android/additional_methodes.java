package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.nio.ByteBuffer;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by laureenschausberger on 14.06.16.
 */
public class additional_methodes{

    private static final String LOG_TAG = "PostSample";

    private static additional_methodes mInstance = null;

    public boolean isRequestBodyAllowed() {
        return true;
    }

    public boolean isRequestHeadersAllowed() {
        return true;
    }

    public String createUserURL()                   {return "http://emagycavirpeht.esy.es/create_user.php";}
    public String changeLanguageURL()               {return "http://emagycavirpeht.esy.es/change_language.php";}
    public String getQuestionGroupsByUserIdURL()    {return "http://emagycavirpeht.esy.es/get_question_groups_by_user_id.php";}
    public String getQuestionsIdsByGrpIdURL ()      {return "http://emagycavirpeht.esy.es/get_question_id_by_grp_id.php";}
    public String getQuestionByUserAndGameIdURL ()  {return "http://emagycavirpeht.esy.es/get_question_by_user_and_game_id";}
    public String newGameURL ()                     {return "http://emagycavirpeht.esy.es/new_game.php";}
    public String joinGameURL ()                    {return "http://emagycavirpeht.esy.es/join_game.php";}
    public String answerQuestionURL ()              {return "http://emagycavirpeht.esy.es/answer_question.php";}
    public String getAnsweredUsersURL ()            {return "http://emagycavirpeht.esy.es/get_answered_users.php";}
    public String allowStatisticsURL()              {return "http://emagycavirpeht.esy.es/allow_statistics.php";}
    public String countPlayersByGameIdURL()         {return "http://emagycavirpeht.esy.es/count_players_by_game_id.php";}
    public String getStatisticsbyGameIdURL()        {return "http://emagycavirpeht.esy.es/get_statistics_by_game_id.php";}
    public String pushPointsToProfileURL ()         {return "http://emagycavirpeht.esy.es/push_points_to_profile.php";}
    public String forceNextQuestion()               {return "http://emagycavirpeht.esy.es/force_next_question.php";}

    protected int lang;
    protected String name;
    protected int userId;
    protected int gameId;
    protected int questionId;


    public static additional_methodes getInstance() {
        if (mInstance == null)
            mInstance = new additional_methodes();
        return mInstance;
    }

    public int getUserID(){
        return userId;
    }

    public int getGameId(){
        return gameId;
    }

    public int getQuestionId(){
        return questionId;
    }

    public String getName(){
        return name;
    }

    public String getLanguage(){
        if(lang == 0) return "eng";
        if(lang == 1) return "de";
        else return null;
    }

    public RequestHandle executeSample(AsyncHttpClient client,
                                       String URL,
                                       Header[] headers,
                                       HttpEntity entity,
                                       ResponseHandlerInterface responseHandler,
                                       Context _c) {
        return client.post(_c, URL, headers, entity, null, responseHandler);
    }

    public ResponseHandlerInterface getResponseHandler() {
        return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {}

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {}
        };
    }


    protected void registerClient(int language, String name) {
        SyncHttpClient client = new SyncHttpClient();

        this.lang = language;
        language = 1;
        this.name = name;

        final int[] ID = new int[1];

        RequestParams params = new RequestParams();
        params.put("lang_id", language);
        params.put("name", name);
        client.post(createUserURL(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Log.i(LOG_TAG,"registerClient was a success.");
                ID[0] = toInt(bytes);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        userId = ID[0];
    }

    protected void changeLanguage (int userId, int language) {
        AsyncHttpClient client = new AsyncHttpClient();

        language = 1;
        userId = 10;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("lang_id", language);
        client.post(changeLanguageURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"changeLanguage was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void getQuestionGroupsByUserId(int userId) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        client.post(getQuestionGroupsByUserIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getQuestionGroupsByUserId was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void getQuestionIdsByUserId(int grpId) {
        AsyncHttpClient client = new AsyncHttpClient();

        grpId = 1;

        RequestParams params = new RequestParams();
        params.put("grp_id", grpId);
        client.post(getQuestionsIdsByGrpIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getQuestionIdsByUserId was a success.");
                String firstEvent = responseString;
            }
        });
    }


    protected void newGame(int userId, int questionId) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        questionId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("question_id", questionId);
        client.post(newGameURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"newGame was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void joinGame(int userId, String id) {
        AsyncHttpClient client = new AsyncHttpClient();

        int sessionId = Integer.parseInt(id);
        final int[] ID = new int[1];

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", sessionId);
        client.post(joinGameURL(), params, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Log.i(LOG_TAG,"joinGame was a success.");
                ID[0] = toInt(bytes);
            }
        });

        this.gameId = ID[0];
    }

    protected void getAnsweredUsers(int gameId) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);

        client.post(getAnsweredUsersURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getAnsweredUsers was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void allowStatistics(int userId, int gameId) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", gameId);
        client.post(allowStatisticsURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"allowStatistics was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void countPlayersByGameId(int gameId) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(countPlayersByGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"countPlayersByGameId was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void getStatisticsByGameId(int gameId) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(getStatisticsbyGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getStatisticsByGameId was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void pushPointsToProfile(int userId, int points) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        points = 100;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("points", points);
        client.post(pushPointsToProfileURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"pushPointsToProfile was a success.");
                String firstEvent = responseString;
            }
        });
    }

    protected void forceNextQuestion(int userId, int gameId, int questionId) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        gameId = 1;
        questionId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", gameId);
        params.put("question_id", questionId);
        client.post(forceNextQuestion(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"forceNextQuestion was a success.");
                String firstEvent = responseString;
            }
        });
    }


    public int toInt( byte[] bytes ) {
        int result = 0;
        for (int i=0; i<4; i++) {
            result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
        }
        return result;
    }

}
