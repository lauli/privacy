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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HostStatistics extends FragmentActivity implements View.OnClickListener{

    private ArrayList<String> listItems = new ArrayList<String>();
    private ListView list;

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
    private ArrayAdapter<String> adapter;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_statistics);



        TextView score = (TextView) findViewById(R.id.host_statistic_score_view);
        score.setText("guess/total = " + helper.getGuess() + "/" + helper.getHowManyYes());
        score.append("\n\ndifference: " + (helper.getAnsweredPlayers().length - helper.getPointsFromThisRound()));

//        list = (ListView) findViewById(R.id.host_statistics_players_list);
//        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
//        list.setAdapter(this.adapter);

//        final Handler handler = new Handler();

//        int delay = 2000;   // delay for 5 sec.
//        int interval = 5000;  // iterate every sec.
//        timerPlayer = new Timer();
//
//        timerPlayer.scheduleAtFixedRate(new TimerTask() {
//            public void run() {
//                final AdditionalMethods helper = AdditionalMethods.getInstance();
//                helper.getPlayersInGame(helper.getGameId(), new OnJSONResponseCallback() {
//                    @Override
//                    public void onJSONResponse(boolean success, JSONObject response) {
//                        if(success) {
//                            for (int i = 0; i < helper.getPlayers().length; i++) {
//                                final int finalI = i;
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        addItem(helper.getPlayers()[finalI],1,1);
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });
//            }
//        }, delay, interval);



        try {
            Toast.makeText(getApplicationContext(), "your current score = " + helper.getPoints(), Toast.LENGTH_SHORT).show();
        }catch (RuntimeException _e){
            return;
        }



        Button b = null;
        b = (Button) findViewById(R.id.host_statistics_continue);
        b.setOnClickListener(this);

        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.host_statistics_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.host_statistics_drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        //------------------------------------------------------------------------ ListView in Actionbar
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String username = preferences.getString("username", "");
        int punkte = preferences.getInt("points", -1);

        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(username);
        TextView points = (TextView) findViewById(R.id.user_points);
        points.setText("Points: " + punkte);
        mNavItems.add(new NavItem("Quit", "quit game", R.drawable.ic_menu_moreoverflow_normal_holo_dark));
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.ic_menu_moreoverflow_normal_holo_dark));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.host_statistics_drawer_layout);

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

        final TextView styleButton = (TextView) findViewById(R.id.host_statistics_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.score)
                        : resources.getString(R.string.lucky));

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
    public void onClick(View view) {
//        timerPlayer.cancel();
        showProgress(true);
        Log.i("", "onclick");
        helper.forceNextQuestion(helper.getUserID(), helper.getGameId(), helper.getQuestionId(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                if(success) {
                    helper.getQuestionByUserAndGameId(helper.getUserID(), helper.getGameId(), new OnJSONResponseCallback() {
                        @Override
                        public void onJSONResponse(boolean success, JSONObject response) {
                            if(success) {
                                Intent i = new Intent(HostStatistics.this, HostQuestion.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }

//    public void addItem(String name, int points, int difference){
//        boolean foundEqual = false;
//        if(!adapter.isEmpty()) {
//            for (int i = 0; (i < adapter.getCount() && !foundEqual); i++) {
//
//                if (adapter.getItem(i).equals(name + " (" + points + ") " + difference)) {
//                    foundEqual = true;
//                }
//            }
//        }
//        if (!foundEqual) {
//            adapter.add(name + " (" + points + ") " + difference);
//            adapter.notifyDataSetChanged();
//        }
//    }

//    private class callPlayers extends TimerTask {
//        @Override
//        public void run() {
//            final AdditionalMethods helper = AdditionalMethods.getInstance();
//            helper.getPlayersInGame(helper.getGameId(), new OnJSONResponseCallback() {
//                @Override
//                public void onJSONResponse(boolean success, JSONObject response) {
//                    if(success) {
//                        for (int i = 0; i < helper.getPlayers().length; i++) {
//                            addItem(helper.getPlayers()[i],1,1);
//                        }
//                    }
//                }
//            });
//        }
//    }

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

        final View mProgressView = (View) findViewById(R.id.host_statistics_progress);
        Button b = (Button) findViewById(R.id.host_statistics_continue);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            b.setVisibility(View.GONE);
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
            b.setVisibility(View.GONE);

        }
    }

}

