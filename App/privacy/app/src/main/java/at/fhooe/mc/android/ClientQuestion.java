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
import java.util.Timer;
import java.util.TimerTask;

public class ClientQuestion extends FragmentActivity implements View.OnClickListener, QuitDialogFragment.OnHeadlineSelectedListener{


    TextView question;
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
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    /**
     * SharedPreferences name
     */
    private final String MyPREFERENCES = "myPref";

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_question);

        Button b = null;
        b = (Button) findViewById(R.id.client_question_yes);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.client_question_no);
        b.setOnClickListener(this);

        question = (TextView) findViewById(R.id.question);
        String quest = helper.getQuestion();
        question.setText(quest);


        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.client_question_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.client_question_drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        //------------------------------------------------------------------------ ListView in Actionbar
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
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
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
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

    private void selectItemFromDrawer(int position, String title) {

        FragmentManager fm = getFragmentManager();
        if(title == "Credit") {
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

        final View mProgressView = (View) findViewById(R.id.client_question_progress);
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

    @Override
    public void onArticleSelected(boolean quit) {
        timer.cancel();
        timer.purge();
        timer = null;
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
