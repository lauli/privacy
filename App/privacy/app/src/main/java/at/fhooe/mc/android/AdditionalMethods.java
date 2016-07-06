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

    // -----------------------------------------------------------------------------start URL variables
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
     * @return php link for getStatisticsbyGameIdHost/Client/2
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







    // -----------------------------------------------------------------------------end URL variables


    // -----------------------------------------------------------------------------start variables






    /**
     * Tag for Log.i
     */
    private static final String LOG_TAG = "AdditionalMethods";

    /**
     * instance variable, witch which one can access same data in every activity via singleton
     */
    private static AdditionalMethods mInstance = null;

    /**
     * language, on time it can only be 1(eng) or 2(ger)
     */
    private int lang;

    /**
     * name of user
     */
    private String name;

    /**
     * userId, given by database
     */
    private int userId;

    /**
     * gameId/sessionId, given by database after new Game was created or one joined an already existing session
     */
    private int gameId;

    /**
     * questionId, given by database
     * changes when forceNextQuestion is called
     * must be one element from ids[]
     */
    private int questionId;

    /**
     * counter, which determines questionId
     * increments when forceNextQuestion is called
     */
    private int counter;

    /**
     * String Array, which contains name of players in game
     * shown at lobby
     */
    private String[] players;

    /**
     * String Array, which contains name of players in game, which have answered a Question
     * shown at voted and guessed
     */
    private String[] answeredPlayers;

    /**
     * Array from type Player (class)
     * contains statistic given by database
     * every player item has String name, int points, int difference
     * shown at statistics
     */
    private Player[] statistic;

    /**
     * String question, given by database via questionId from ids[]
     */
    private String question;

    /**
     * points from session
     * increased by pointsFromThisRound after getStatisticsbyGameIdHost/Client/2
     */
    private int points;

    /**
     * points from this round
     * used to increase points from session
     */
    private int pointsFromThisRound;

    /**
     * answer to question
     * can be 1 yes, or 2 no
     */
    private int answer;

    /**
     * guess how many players have answered with yes
     * min 0
     * max number of answered players
     */
    private int guess;

    /**
     * yeses from this round
     */
    private int howManyYes;

    /**
     * question ids from database
     * have already been shuffled by database
     */
    private int[] ids;




// -----------------------------------------------------------------------------end variables

// -----------------------------------------------------------------------------start functions for variables


    /**
     * used to create instance from AdditionalMethods
     * Singleton
     * @return instance of AdditionalMethods
     */
    public static AdditionalMethods getInstance() {
        if (mInstance == null)
            mInstance = new AdditionalMethods();
        return mInstance;
    }

    /**
     *
     * @return userId
     */
    public int getUserID(){
        return userId;
    }

    /**
     * used to change userId
     * @param id integer to which userId should be changed
     */
    public void setUserID(int id){
        this.userId = id;
    }

    /**
     *
     * @return gameId
     */
    public int getGameId(){
        return gameId;
    }

    /**
     *
     * @return questionId
     */
    public int getQuestionId(){ return questionId; }

    /**
     *
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * used to change name
     * @param name  String to which name should be changed
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * get language as string
     * @return eng or de or null
     */
    public String getLanguage(){
        if(lang == 1) return "eng";
        if(lang == 2) return "de";
        else return null;
    }

    /**
     *
     * @return lang
     */
    public int getLang(){
        return lang;
    }

    /**
     *
     * @return players
     */
    public String[] getPlayers(){ return players;}

    /**
     *
     * @return answeredPlayers
     */
    public String[] getAnsweredPlayers(){ return answeredPlayers;}

    /**
     *
     * @return question
     */
    public String getQuestion(){ return question;}

    /**
     *
     * @return points
     */
    public int getPoints(){ return points;}

    /**
     * used to change points
     * @param points    integer to which points should be changed
     */
    public void setPoints(int points){ this.points = points;}

    /**
     *
     * @return pointsFromThisRound
     */
    public int getPointsFromThisRound(){ return pointsFromThisRound;}

    /**
     *
     * @return answer
     */
    public int getAnswer(){ return answer;}

    /**
     *
     * @return guess
     */
    public int getGuess(){ return guess;}

    /**
     *
     * @return howManyYes
     */
    public int getHowManyYes(){ return howManyYes;}

    /**
     *
     * @return statistic
     */
    public Player[] getStatistic() { return statistic;}



    // -----------------------------------------------------------------------------end functions for variables

    // -----------------------------------------------------------------------------start functions for database


    /**
     * POST REQUEST, that creates user by language and name
     * uses createUserURL
     * result: userId or -1
     * saves firstCall, username, language and points in sharedpreferences
     * saves lang, name and userId in AdditionalMethods
     * @param language      int id of language
     * @param name          String name of user
     * @param callback      responseCallback - success or not
     */
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
                    _e.printStackTrace();
                }
            }
        });
    }

    /**
     * POST REQUEST, that changes language in AdditionalMethods and database for questions to come in right language
     * uses URL changeLanguageURL
     * @param userId        userId
     * @param language      language
     * @param callback      callback
     */
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

    /**
     * POST REQUEST, that changes name in AdditionalMethods and database
     * uses URL changeUserNameURL
     * @param userId    userId
     * @param name      name
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets shuffled questionpool (ids) by groud id
     * saves questionIds in ids[]
     * saves question by first indes in ids[]
     * uses getQuestionIdsByGrpIdURL
     * result: shuffled ids
     * @param category  question category (3,4,5)
     * @param callback  callback
     */
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
                    _e.printStackTrace();
                }

            }
        });
    }

    /**
     * POST REQUEST, that creates new Game and saves gameId
     * uses newGameURL
     * result: gameId
     * @param userId        userId
     * @param questionId    questionId
     * @param callback      callback
     */
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

    /**
     * POST REQUEST, that allows user to join an already existing game by its gameId
     * uses joineGameURL
     * saves user and game id in AM
     * result: gameId
     * @param userId    userId
     * @param id        gameIde
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets all players, which have answered the current question
     * saves playernames in answeredPlayers[]
     * uses getAnsweredUsersURL
     * result: names of answered players
     * @param gameId    gameId
     * @param callback  callback
     */
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


    /**
     * POST REQUEST, that gets statistic of game
     * used only by host
     * saves howManyYes, pointsFromThisRound, points
     * uses getStatisticsbyGameIdURL
     * result: statistic of game, player: id, name, points, difference, yesses
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets statistic of game
     * used only by client
     * saves howManyYes, pointsFromThisRound, points
     * uses getStatisticsbyGameIdURL
     * result: statistic of game, player: id, name, points, difference, yesses
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets statistic of game
     * used only when others didn't work
     * saves howManyYes, pointsFromThisRound, points
     * uses getStatisticsbyGameIdURL
     * result: statistic of game, player: id, name, points, difference, yesses
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that forces next question in line (from ids[])
     * increases counter
     * uses counter to get next questionId from ids[]
     * uses forceNextQuestionURL
     * @param gameId    old gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that get players in game by gameId
     * saves names of all players in players[]
     * uses getPlayersInGameURL
     * result: players in game
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets question by user an game id
     * saves question in question and questionId in questionId
     * uses getQuestionbyUserAndGameURL
     * result: one question (string) and question id (int)
     * @param userId    userId
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that gets question by user an game id
     * saves question in question and questionId in questionId
     * uses getQuestionbyUserAndGameURL
     * result: one question (string) and question id (int)
     * @param userId    userId
     * @param gameId    gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that saves answer of user
     * saves answer(yn_answer -> 0 no / 1 yes) and guess (cnt_answer -> how many players answered with yes)
     * must be called 2 times in one session round, first time with default cnt_answer, later with real value
     * saves answer and guess
     * uses answerQuestionURL
     * @param user_id       userId
     * @param game_id       gameId
     * @param question_id   questionId
     * @param yn_answer     yes or no answer
     * @param cnt_answer    guess
     * @param callback      callback
     */
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

    /**
     * POST REQUEST, that allows client to continue
     * is only called by host
     * uses allowContinueURL
     * result: true or false
     * @param user_id   userId
     * @param game_id   gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that allows client to continue after host called allowContinue
     * uses isCountinueAllowedURL
     * @param game_id   gameId
     * @param callback  callback
     */
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

    /**
     * POST REQUEST, that quits game
     * quits game completely if called by host
     * user leaves game if called by client
     * uses quitGameURL
     * @param game_id   gameId
     * @param callback  callback
     */
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

    /**
     * checks if game still exists
     * game will be deleted if host called quitGame
     * uses isGameExistingURL
     * @param game_id   gameId
     * @param callback  callback
     */
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
