package at.fhooe.mc.android;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.Future;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;

/**
 * Created by laureenschausberger on 14.06.16.
 */
public class AdditionalMethods {

    private static final String LOG_TAG = "PostSample";

    private static AdditionalMethods mInstance = null;

    public boolean isRequestBodyAllowed() {
        return true;
    }

    public boolean isRequestHeadersAllowed() {
        return true;
    }

    public String createUserURL()                   {return "http://emagycavirpeht.esy.es/create_user.php";}
    public String changeLanguageURL()               {return "http://emagycavirpeht.esy.es/change_language.php";}
    public String getQuestionGroupsByUserIdURL()    {return "http://emagycavirpeht.esy.es/get_question_groups_by_user_id.php";}
    public String getQuestionsIdsByGrpIdURL ()      {return "http://emagycavirpeht.esy.es/get_question_ids_by_grp_id.php";}
    public String getQuestionByUserAndGameIdURL ()  {return "http://emagycavirpeht.esy.es/get_question_by_user_and_game_id";}
    public String newGameURL ()                     {return "http://emagycavirpeht.esy.es/new_game.php";}
    public String joinGameURL ()                    {return "http://emagycavirpeht.esy.es/join_game.php";}
    public String answerQuestionURL ()              {return "http://emagycavirpeht.esy.es/answer_question.php";}
    public String getAnsweredUsersURL ()            {return "http://emagycavirpeht.esy.es/get_answered_users.php";}
    public String allowStatisticsURL()              {return "http://emagycavirpeht.esy.es/allow_statistics.php";}
    public String countPlayersByGameIdURL()         {return "http://emagycavirpeht.esy.es/count_players_by_game_id.php";}
    public String getStatisticsbyGameIdURL()        {return "http://emagycavirpeht.esy.es/get_statistics_by_game_id.php";}
    public String pushPointsToProfileURL ()         {return "http://emagycavirpeht.esy.es/push_points_to_profile.php";}
    public String forceNextQuestionURL()            {return "http://emagycavirpeht.esy.es/force_next_question.php";}
    public String getPlayersInGameURL()             {return "http://emagycavirpeht.esy.es/get_players_in_game.php";}

    protected int lang;
    protected String name;
    protected int userId;
    protected int gameId;
    protected int questionId;
    protected String[] players;
    protected String question;


    public static AdditionalMethods getInstance() {
        if (mInstance == null)
            mInstance = new AdditionalMethods();
        return mInstance;
    }

    public int getUserID(){
        return userId;
    }

    public int getGameId(){
        return gameId;
    }

    public int getQuestionId(){ return questionId; }

    public String getName(){
        return name;
    }

    public String getLanguage(){
        if(lang == 1) return "eng";
        if(lang == 2) return "de";
        else return null;
    }

    public String[] getPlayers(){ return players;}

    public String getQuestion(){ return question;}

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


    protected void registerClient(int language, String name, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        this.lang = language;
        language = 1;
        this.name = name;

        RequestParams params = new RequestParams();
        params.put("lang_id", language);
        params.put("name", name);



        client.post(createUserURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: do error handling here
                Log.i(LOG_TAG,"onFailure");
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject json;

                Log.i(LOG_TAG,"registerClient was a success.");

                try {
                    json = new JSONObject(responseString);
                    userId = json.getInt("id");
                    callback.onJSONResponse(true, null);

                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void changeLanguage (int userId, int language,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        language = 1;
        userId = 10;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("lang_id", language);
        client.post(changeLanguageURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"changeLanguage was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void getQuestionGroupsByUserId(int userId, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        client.post(getQuestionGroupsByUserIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getQuestionGroupsByUserId was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void getQuestionIdsByGroupId(int category, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("grp_id", category);
        client.post(getQuestionsIdsByGrpIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getQuestionIdsByUserId was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);

                    JSONArray array = json.getJSONArray("IDS");

                    int[] ids = new int[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ids[i] = obj.getInt("id");
                    }

                    int index = 0 + (int)(Math.random() * ((array.length()-1 - 0) + 1));
                    questionId = ids[index];

                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }

            }
        });
    }


    protected void newGame(int userId, int questionId, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("q_id", questionId);
        client.post(newGameURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                JSONObject json;

                Log.i(LOG_TAG, "newGame was a success.");

                try {
                    json = new JSONObject(responseString);

                    gameId = json.getInt("id");

                    callback.onJSONResponse(true, null);

                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void joinGame(int userId, int id, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        int sessionId = id;


        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", sessionId);



        client.post(joinGameURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // TODO: do error handling here
                Log.i(LOG_TAG, "onFailure");
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                JSONObject json;

                Log.i(LOG_TAG, "registerClient was a success.");

                try {
                    json = new JSONObject(responseString);

                    gameId = json.getInt("id");

                    callback.onJSONResponse(true, null);

                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
            }
        });

    }

    protected void getAnsweredUsers(int gameId,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);

        client.post(getAnsweredUsersURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getAnsweredUsers was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void allowStatistics(int userId, int gameId,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", gameId);
        client.post(allowStatisticsURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"allowStatistics was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void countPlayersByGameId(int gameId,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(countPlayersByGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"countPlayersByGameId was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void getStatisticsByGameId(int gameId,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        gameId = 1;

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(getStatisticsbyGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getStatisticsByGameId was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void pushPointsToProfile(int userId, int points,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        points = 100;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("points", points);
        client.post(pushPointsToProfileURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"pushPointsToProfile was a success.");
                String firstEvent = responseString;


                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void forceNextQuestion(int userId, int gameId, int questionId,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        userId = 1;
        gameId = 1;
        questionId = 1;

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", gameId);
        params.put("question_id", questionId);
        client.post(forceNextQuestionURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"forceNextQuestion was a success.");
                String firstEvent = responseString;

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void getPlayersInGame(int gameId, final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("game_id", gameId);


        client.post(getPlayersInGameURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getPlayersInGame was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);

                    JSONArray array = json.getJSONArray("Players");

                    String[] ids = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ids[i] = obj.getString("title");
                    }

                    players = new String[ids.length];
                    players = ids;

                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void getQuestionByUserAndGameId(int userId, int gameId, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", gameId);
        client.post(forceNextQuestionURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"getQuestionByUserAndGameId was a success.");
                JSONObject json;

                try {
                    json = new JSONObject(responseString);
                    questionId = json.getInt("id");
                    question = json.getString("title");
                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
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
