package at.fhooe.mc.android;

import android.content.Context;
import android.content.SharedPreferences;
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

    public String createUserURL()                   {return "http://privacygame.soft-tec.net/create_user.php";}
    public String changeLanguageURL()               {return "http://privacygame.soft-tec.net/change_language.php";}
    public String changeUserName()                  {return "http://privacygame.soft-tec.net/change_user_name.php";}
    public String getQuestionGroupsByUserIdURL()    {return "http://privacygame.soft-tec.net/get_question_groups_by_user_id.php";}
    public String getQuestionsIdsByGrpIdURL ()      {return "http://privacygame.soft-tec.net/get_question_ids_by_grp_id.php";}
    public String getQuestionByUserAndGameIdURL ()  {return "http://privacygame.soft-tec.net/get_question_by_user_and_game_id.php";}
    public String newGameURL ()                     {return "http://privacygame.soft-tec.net/new_game.php";}
    public String joinGameURL ()                    {return "http://privacygame.soft-tec.net/join_game.php";}
    public String answerQuestionURL ()              {return "http://privacygame.soft-tec.net/answer_question.php";}
    public String getAnsweredUsersURL ()            {return "http://privacygame.soft-tec.net/get_answered_users.php";}
    public String allowStatisticsURL()              {return "http://privacygame.soft-tec.net/allow_statistics.php";}
    public String countPlayersByGameIdURL()         {return "http://privacygame.soft-tec.net/count_players_by_game_id.php";}
    public String getStatisticsbyGameIdURL()        {return "http://privacygame.soft-tec.net/get_statistic_by_game_id.php";}
    public String pushPointsToProfileURL ()         {return "http://privacygame.soft-tec.net/push_points_to_profile.php";}
    public String forceNextQuestionURL()            {return "http://privacygame.soft-tec.net/force_next_question.php";}
    public String getPlayersInGameURL()             {return "http://privacygame.soft-tec.net/get_players_in_game.php";}
    public String allowCountingURL()                {return "http://privacygame.soft-tec.net/allow_counting.php";}
    public String isCountinueAllowedURL()             {return "http://privacygame.soft-tec.net/is_continue_allowed.php";}

    private int lang;
    private String name;
    private int userId;
    private int gameId;
    private int questionId;
    private int counter;
    private String[] players;
    private String[] answeredPlayers;
    private Player[] statistic;
    private String question;
    private int points = 0;
    private int pointsFromThisRound;
    private int answer;
    private int guess;
    private int howManyYes;
    private int[] ids;


    public static AdditionalMethods getInstance() {
        if (mInstance == null)
            mInstance = new AdditionalMethods();
        return mInstance;
    }

    public int getUserID(){
        return userId;
    }

    public void setUserID(int id){
        this.userId = id;
    }

    public int getGameId(){
        return gameId;
    }

    public String getGameIdString(){return Integer.toString(gameId);}

    public int getQuestionId(){ return questionId; }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getLanguage(){
        if(lang == 1) return "eng";
        if(lang == 2) return "de";
        else return null;
    }

    public int getLang(){
        return lang;
    }

    public String[] getPlayers(){ return players;}

    public String[] getAnsweredPlayers(){ return answeredPlayers;}

    public String getQuestion(){ return question;}

    public int getPoints(){ return points;}

    public void setPoints(int points){ this.points = points;}

    public int getPointsFromThisRound(){ return pointsFromThisRound;}

    public String getPointsString(){ return Integer.toString(points);}

    public int getAnswer(){ return answer;}

    public int getGuess(){ return guess;}

    public int getHowManyYes(){ return howManyYes;}

    public Player[] getStatistic() { return statistic;}

    public int[] getQuestionIds() { return ids;}

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


    protected void createUser(int language, String name, final OnJSONResponseCallback callback) {
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
                Context context = CreateOrJoin.getContextOfApplication();
                SharedPreferences preferences = context.getSharedPreferences("myPref", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("firstCall", false);
                    editor.commit();
                    editor.putString("username", getName());
                    editor.commit();
                    editor.putInt("language", getLang());
                    editor.commit();
                    editor.putInt("points", 0);
                    editor.commit();

                JSONObject json;

                Log.i(LOG_TAG,"createUser was a success.");

                try {
                    json = new JSONObject(responseString);
                    userId = json.getInt("id");
                    editor.putInt("userId", userId);
                    editor.commit();
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

    protected void changeUserName (int userId, final String name, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("name", name);
        client.post(changeLanguageURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
                Log.i(LOG_TAG,"changeName was a failure.");
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"changeName was a success.");
                setName(name);
                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void getQuestionGroupsByUserId(int userId, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

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

                    ids = new int[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        ids[i] = obj.getInt("id");
                    }

                    counter = 0;
                    questionId = ids[counter];

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
        params.put("question_id", questionId);
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

                Log.i(LOG_TAG, "createUser was a success.");

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
        SyncHttpClient client = new SyncHttpClient();


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
                JSONObject json;

                try {
//                    responseString = "{ \"Players\" : " + responseString;
//                    responseString = responseString + "}";
                    json = new JSONObject(responseString);

                    JSONArray array = json.getJSONArray("Players");


                    String[] name = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        name[i] = obj.getString("title");
                    }

                    answeredPlayers = new String[name.length];
                    answeredPlayers = name;
                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    // TODO: error handling
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void allowStatistics(int userId, int gameId,  final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();

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


        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(getStatisticsbyGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG, "getStatisticsByGameId was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);
                    JSONArray array = json.getJSONArray("STAT");

                    //get the total amount of yeses
                    JSONObject obj = array.getJSONObject(0);
                    howManyYes= Integer.parseInt(obj.getString("yeses"));

                    //get my points from this round and add it to my points from the game
                    pointsFromThisRound = getAnsweredPlayers().length - Math.abs(getHowManyYes() - getGuess());
                    points += pointsFromThisRound;

                    Context context = CreateOrJoin.getContextOfApplication();
                    SharedPreferences preferences = context.getSharedPreferences("myPref", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("points", points);
                    editor.commit();

                    //TODO: implement statistic form all users with Player[] statistic
                    statistic = new Player[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        statistic[i] = new Player();
                        statistic[i].guessed = Integer.parseInt(array.getJSONObject(i).getString("guessed"));
                        statistic[i].name = array.getJSONObject(i).getString("name");
                        statistic[i].points = Integer.parseInt(array.getJSONObject(i).getString("points"));
                        statistic[i].mistake = (Math.abs(getHowManyYes() - statistic[i].guessed));
                    }
                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void pushPointsToProfile(int userId, int points,  final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();


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

        counter++;
        if(counter >= ids.length){
            //TODO: endGame Funktion
        }
        questionId = ids[counter];

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
                Log.i(LOG_TAG,"forceNextQuestion was a success." + responseString);
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
        client.post(getQuestionByUserAndGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                Log.i(LOG_TAG,"getQuestionByUserAndGameId was a failure.");
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


    protected void answerQuestion(int user_id, int game_id,int question_id, int yn_answer, int cnt_answer, final OnJSONResponseCallback callback) {
       // yn_answer ---->     1 == true , 2 == false

        AsyncHttpClient client = new AsyncHttpClient();

        this.answer = yn_answer;
        this.guess = cnt_answer;

        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("game_id", game_id);
        params.put("question_id", question_id);
        params.put("yn_answer", yn_answer);
        params.put("cnt_answer", cnt_answer);
        client.post(answerQuestionURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"answerQuestion was a success.");
                //returns true when success
                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void allowCounting(int user_id, int game_id, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("game_id", game_id);
        client.post(allowCountingURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"allowCounting was a success.");
                //returns true when success
                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void isContinueAllowed(int game_id, final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("game_id", game_id);
        client.post(isCountinueAllowedURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG, "isContinueAllowed was a success.");
                boolean allowed = false;
                if(responseString.equals("true")) {
                    allowed = true;
                    Log.i(LOG_TAG, "isContinueAllowed is allowed.");
                }
                else Log.i(LOG_TAG, "isContinueAllowed is not allowed.");
                //returns true when allowed
                if(allowed)
                    callback.onJSONResponse(true, null);
                else
                    callback.onJSONResponse(false, null);
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
