package at.fhooe.mc.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 *  Created by laureenschausberger.
 *  Activity for Client to see question and answer it
 *  will call ClientGuess only if Host has called allowContinuing before
 *  checked by timer and isContinueAllowed
 */
public class ClientQuestion extends FragmentActivity implements View.OnClickListener, QuitDialogFragment.OnHeadlineSelectedListener, GameDoesntExistDialogFragment.OnHeadlineSelectedListener{


    /**
     * timer checks if game still exists
     * call isGameExisting
     */
    Timer timerDoesGameExist;

    /**
     * textview that shows question
     */
    TextView question;

    /**
     * timer for checking if ClientGuess can be called
     */
    Timer timer = new Timer();

    /**
     * menu and actionbar
     */
    private DrawerArrowDrawable drawerArrowDrawable;

    /**
     * for drawerArrowDrawable
     */
    private float offset;

    /**
     * for drawerArrowDrawable
     * used to show two different messages in menu
     */
    private boolean flipped;

    /**
     * Instance of AdditionalMethods
     * with this Instance it's possible to save data and call methods in AdditionalMethods that will be the same in every activity
     */
    AdditionalMethods helper = AdditionalMethods.getInstance();

    /**
     * Items in Actionbar
     */
    ArrayList<NavItem> mNavItems = new ArrayList<>();

    /**
     * ListView for Actionbar
     */
    ListView mDrawerList;

    /**
     * RelativeLayout for Actionbar
     */
    RelativeLayout mDrawerPane;

    /**
     * DrawerLayout for Actionbar
     */
    private DrawerLayout mDrawerLayout;


    /**
     * creates activity for clientquestion
     * shows question
     * @param savedInstanceState    .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_question);

        Button b =  (Button) findViewById(R.id.client_question_yes);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.client_question_no);
        b.setOnClickListener(this);

        question = (TextView) findViewById(R.id.question);
        String quest = helper.getQuestion();
        question.setText(quest);


        timerDoesGameExist = new Timer();
        timerDoesGameExist.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                helper.isGameExisting(helper.getGameId(), new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success) {
                            GameDoesntExistDialogFragment dialog = new GameDoesntExistDialogFragment();
                            dialog.setCancelable(false);
                            dialog.show(getSupportFragmentManager(), "Dialog");
                        }
                    }
                });
            }
        }, 0, 5000);

        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.client_question_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.client_question_drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        //------------------------------------------------------------------------ ListView in Actionbar
        /*
      SharedPreferences name
     */
        String myPREFERENCES = "myPref";
        SharedPreferences preferences = getSharedPreferences(myPREFERENCES, MODE_PRIVATE);
        String username = preferences.getString("username", "");

        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(username);
        TextView points = (TextView) findViewById(R.id.user_points);
        points.setText("Points: " + helper.getPoints());
        mNavItems.add(new NavItem("Quit", "quit game", R.drawable.quit));
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.credits));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.client_question_drawer_layout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        final DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = (adapter.getTitleFromItemAtPosition(position));
                selectItemFromDrawer(position, title);
            }
        });
        //------------------------------------------------------------------------ End ListView

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(true);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(false);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        final TextView styleButton = (TextView) findViewById(R.id.client_question_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.question) //
                        : resources.getString(R.string.amazing));

                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });
        // --------------------------------------------------------------------------------------------  actionbar End!
    }

    /**
     * onclick answersQuestion with chosen yes/no and default value for guess
     * afterwards start timer to check if istContinueAllowed
     * when true, calls ClientGuess
     * @param _view
     */
    @Override
    public void onClick(View _view) {
        showProgress(true);
        switch (_view.getId()){
            case R.id.client_question_yes : {
                helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.getQuestionId(), 1, 0, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success){
                            timer.scheduleAtFixedRate(new TimerTask() {
                                public void run() {
                                    helper.isContinueAllowed(helper.getGameId(), new OnJSONResponseCallback() {
                                        @Override
                                        public void onJSONResponse(boolean success, JSONObject response) {
                                            if (success) {
                                                helper.getAnsweredUsers(helper.getGameId(), new OnJSONResponseCallback() {
                                                    @Override
                                                    public void onJSONResponse(boolean success, JSONObject response) {
                                                        if (success) {
                                                            timer.cancel();
                                                            timer.purge();
                                                            timer = null;
                                                            timerDoesGameExist.cancel();
                                                            timerDoesGameExist.purge();
                                                            timerDoesGameExist = null;
                                                            Intent i = new Intent(ClientQuestion.this, ClientGuess.class);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }, 2000, 5000);
                        }

                    }
                });
            } break;
            case R.id.client_question_no : {
                helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.getQuestionId(), 0, 0, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success){
                            timer.scheduleAtFixedRate(new TimerTask() {
                                public void run() {
                                    helper.isContinueAllowed(helper.getGameId(), new OnJSONResponseCallback() {
                                        @Override
                                        public void onJSONResponse(boolean success, JSONObject response) {
                                            if (success) {
                                                helper.getAnsweredUsers(helper.getGameId(), new OnJSONResponseCallback() {
                                                    @Override
                                                    public void onJSONResponse(boolean success, JSONObject response) {
                                                        if (success) {
                                                            timer.cancel();
                                                            timer.purge();
                                                            timer = null;
                                                            timerDoesGameExist.cancel();
                                                            timerDoesGameExist.purge();
                                                            timerDoesGameExist = null;
                                                            Intent i = new Intent(ClientQuestion.this, ClientGuess.class);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }, 2000, 5000);
                        }

                    }
                });
            } break;
            default : {}
        }
    }

    /**
     * used for menu
     * when one item is selected, it will react considering which item was clicked
     * @param position  .
     * @param title     name of title
     */
    private void selectItemFromDrawer(int position, String title) {

        if(Objects.equals(title, "Credit")) {
            CreditDialogFragment fragment = new CreditDialogFragment();
            fragment.show(getSupportFragmentManager(), "Dialog");
        }
        else { //Quit
            QuitDialogFragment fragment = new QuitDialogFragment();
            fragment.show(getSupportFragmentManager(), "Dialog");
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    /**
     * Shows the progress UI and hides form to continue
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.

        final View mProgressView = findViewById(R.id.client_question_progress);
        Button y = (Button) findViewById(R.id.client_question_yes);
        Button n = (Button) findViewById(R.id.client_question_no);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            y.setVisibility(View.GONE);
            n.setVisibility(View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            TextView view = (TextView) findViewById(R.id.textView);
            view.setText("Please wait.");
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            y.setVisibility(View.GONE);
            n.setVisibility(View.GONE);

        }
    }

    /**
     * finishes activity and cancels timer if quit is true
     * is true if user clicked positive button and quitGame was a success
     * @param quit true if quitGame
     */
    @Override
    public void onArticleSelected(boolean quit) {
        timer.cancel();
        timer.purge();
        timer = null;
        timerDoesGameExist.cancel();
        timerDoesGameExist.purge();
        timerDoesGameExist = null;
        finish();
    }

    /**
     * overridden because back should not be able to be pressed
     */
    @Override
    public void onBackPressed() {
    }
}
