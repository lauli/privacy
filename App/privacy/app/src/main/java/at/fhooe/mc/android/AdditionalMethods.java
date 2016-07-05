package at.fhooe.mc.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by laureenschausberger on 14.06.16.
 * class that holds every method related to database
 * contains instance method, with which one can access same data in every activity via singleton
 */
public class AdditionalMethods {

    /**
     * Tag for Log.i
     */
    private static final String LOG_TAG = "AdditionalMethods";

    /**
     * instance variable, witch which one can access same data in every activity via singleton
     */
    private static AdditionalMethods mInstance = null;

    /**
     * phpurl for createUser
     * @return php link for createUser
     */
    public String createUserURL()                   {return "http://privacygame.soft-tec.net/create_user.php";}

    /**
     * phpurl for changeLanguage
     * @return php link for changeLanguage
     */
    public String changeLanguageURL()               {return "http://privacygame.soft-tec.net/change_language.php";}

    /**
     * phpurl for changeUserName
     * @return php link for changeUserName
     */
    public String changeUserNameURL()               {return "http://privacygame.soft-tec.net/change_user_name.php";}
//    public String getQuestionGroupsByUserIdURL()    {return "http://privacygame.soft-tec.net/get_question_groups_by_user_id.php";}

    /**
     * phpurl for getQuestionsIdsByGrpId
     * @return php link for getQuestionsIdsByGrpId
     */
    public String getQuestionsIdsByGrpIdURL ()      {return "http://privacygame.soft-tec.net/get_question_ids_by_grp_id.php";}

    /**
     * phpurl for getQuestionByUserAndGameId
     * @return php link for getQuestionByUserAndGameId
     */
    public String getQuestionByUserAndGameIdURL ()  {return "http://privacygame.soft-tec.net/get_question_by_user_and_game_id.php";}

    /**
     * phpurl for newGame
     * @return php link for newGame
     */
    public String newGameURL ()                     {return "http://privacygame.soft-tec.net/new_game.php";}

    /**
     * phpurl for joinGame
     * @return php link for joinGame
     */
    public String joinGameURL ()                    {return "http://privacygame.soft-tec.net/join_game.php";}

    /**
     * phpurl for answerQuestion
     * @return php link for answerQuestion
     */
    public String answerQuestionURL ()              {return "http://privacygame.soft-tec.net/answer_question.php";}

    /**
     * phpurl for getAnsweredUsers
     * @return php link for getAnsweredUsers
     */
    public String getAnsweredUsersURL ()            {return "http://privacygame.soft-tec.net/get_answered_users.php";}

    /**
     * phpurl for getStatisticsbyGameId
     * @return php link for getStatisticsbyGameId
     */
    public String getStatisticsbyGameIdURL()        {return "http://privacygame.soft-tec.net/get_statistic_by_game_id.php";}

    /**
     * phpurl for forceNextQuestion
     * @return php link for forceNextQuestion
     */
    public String forceNextQuestionURL()            {return "http://privacygame.soft-tec.net/force_next_question.php";}

    /**
     * phpurl for getPlayersInGame
     * @return php link for getPlayersInGame
     */
    public String getPlayersInGameURL()             {return "http://privacygame.soft-tec.net/get_players_in_game.php";}

    /**
     * phpurl for allowContinue
     * @return php link for allowContinue
     */
    public String allowContinueURL()                {return "http://privacygame.soft-tec.net/allow_continue.php";}

    /**
     * phpurl for isCountinueAllowed
     * @return php link for isCountinueAllowed
     */
    public String isCountinueAllowedURL()           {return "http://privacygame.soft-tec.net/is_continue_allowed.php";}

    /**
     * phpurl for quitGame
     * @return php link for quitGame
     */
    public String quitGameURL()                     {return "http://privacygame.soft-tec.net/quit_game.php";}

    /**
     * phpurl for isGameExisting
     * @return php link for isGameExisting
     */
    public String isGameExistingURL()               {return "http://privacygame.soft-tec.net/is_game_existing.php";}


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
    private int points;
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

    public int getAnswer(){ return answer;}

    public int getGuess(){ return guess;}

    public int getHowManyYes(){ return howManyYes;}

    public Player[] getStatistic() { return statistic;}


    protected void createUser(int language, String name, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        this.lang = language;
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
                SharedPreferences preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
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
                points = 0;
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

    protected void changeLanguage (int userId, final int language, final OnJSONResponseCallback callback) {
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
                lang = language;
                Context context = CreateOrJoin.getContextOfApplication();
                SharedPreferences preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("language", getLang());
                editor.commit();

                callback.onJSONResponse(true, null);
            }
        });
    }

    protected void changeUserName (int userId, final String name, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("name", name);
        client.post(changeUserNameURL(), params, new TextHttpResponseHandler() {
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

//    protected void getQuestionGroupsByUserId(int userId, final OnJSONResponseCallback callback) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        counter = 0;
//        RequestParams params = new RequestParams();
//        params.put("user_id", userId);
//        client.post(getQuestionGroupsByUserIdURL(), params, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
//                callback.onJSONResponse(false, null);
//            }
//
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
//                Log.i(LOG_TAG,"getQuestionGroupsByUserId was a success.");
//                callback.onJSONResponse(true, null);
//            }
//        });
//    }

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

        points = 0;
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

        points = 0;
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("game_id", id);



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


    protected void getStatisticsByGameIdHost(int gameId, final OnJSONResponseCallback callback) {
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
                Log.i(LOG_TAG, "getStatisticsByGameIdHost was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);
                    JSONArray array = json.getJSONArray("STAT");

                    //get the total amount of yeses
                    JSONObject obj = array.getJSONObject(0);
                    howManyYes= Integer.parseInt(obj.getString("yesses"));

                    //get my points from this round and add it to my points from the game
                    pointsFromThisRound = getAnsweredPlayers().length - Math.abs(getHowManyYes() - getGuess());
                    points += pointsFromThisRound;

                    Context context = HostGuess.getContextOfApplication();
                    SharedPreferences preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                    int p = preferences.getInt("points", -1);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("points", (p+pointsFromThisRound));
                    editor.commit();

                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void getStatisticsByGameIdClient(int gameId,  final OnJSONResponseCallback callback) {
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
                Log.i(LOG_TAG, "getStatisticsByGameIdHost was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);
                    JSONArray array = json.getJSONArray("STAT");

                    //get the total amount of yeses
                    JSONObject obj = array.getJSONObject(0);
                    howManyYes= Integer.parseInt(obj.getString("yesses"));

                    //get my points from this round and add it to my points from the game
                    pointsFromThisRound = getAnsweredPlayers().length - Math.abs(getHowManyYes() - getGuess());
                    points += pointsFromThisRound;

                    Context context = ClientGuess.getContextOfApplication();
                    SharedPreferences preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
                    int p = preferences.getInt("points", -1);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("points", (p+pointsFromThisRound));
                    editor.commit();

                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void getStatisticsByGameId2(int gameId,  final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("game_id", gameId);
        client.post(getStatisticsbyGameIdURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG, "getStatisticsByGameIdHost was a success.");

                JSONObject json;

                try {
                    json = new JSONObject(responseString);
                    JSONArray array = json.getJSONArray("STAT");

                    statistic = new Player[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        statistic[i] = new Player();
                        statistic[i].setName(array.getJSONObject(i).getString("name"));
                        statistic[i].setPoints(Integer.parseInt(array.getJSONObject(i).getString("points")));
                        statistic[i].setDifference(Integer.parseInt(array.getJSONObject(i).getString("difference")));
                    }
                    callback.onJSONResponse(true, null);
                } catch (JSONException _e) {
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void forceNextQuestion(int userId, int gameId, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();

        counter++;
        if(counter >= ids.length){
            counter = 0;
        }
        int questionId = ids[counter];

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
                    _e.printStackTrace();
                }
            }
        });
    }

    protected void getQuestionByUserAndGameId2(int userId, int gameId, final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();


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
                    _e.printStackTrace();
                }
            }
        });
    }


    protected void answerQuestion(int user_id, int game_id,int question_id, int yn_answer, int cnt_answer, final OnJSONResponseCallback callback) {
       // yn_answer ---->     1 == true , 0 == false

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

    protected void allowContinue(int user_id, int game_id, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("user_id", user_id);
        params.put("game_id", game_id);
        client.post(allowContinueURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG,"allowContinue was a success.");
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
                    Log.i(LOG_TAG, "isContinueAllowed is true.");
                }
                else Log.i(LOG_TAG, "isContinueAllowed is not false.");
                //returns true when allowed
                if(allowed)
                    callback.onJSONResponse(true, null);
                else
                    callback.onJSONResponse(false, null);
            }
        });
    }

    protected void quitGame(int game_id, final OnJSONResponseCallback callback) {
        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("game_id", game_id);
        client.post(quitGameURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG, "isContinueAllowed was a success.");
                    callback.onJSONResponse(true, null);
            }
        });
    }

    protected void isGameExisting(int game_id, final OnJSONResponseCallback callback) {
        SyncHttpClient client = new SyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("game_id", game_id);
        client.post(isGameExistingURL(), params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                callback.onJSONResponse(false, null);
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                Log.i(LOG_TAG, "isGameExisting was a success.");
                boolean allowed = false;
                if(responseString.equals("true")) {
                    allowed = true;
                }

                //returns true when allowed
                if(allowed)
                    callback.onJSONResponse(true, null);
                else
                    callback.onJSONResponse(false, null);
            }
        });
    }

}
