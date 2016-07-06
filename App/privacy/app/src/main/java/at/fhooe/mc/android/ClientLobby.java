package at.fhooe.mc.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by laureenschausberger.
 * Activity for Client to see how many players have joined the game
 * will be updated by an interval
 */
public class ClientLobby extends FragmentActivity implements View.OnClickListener, QuitDialogFragment.OnHeadlineSelectedListener, GameDoesntExistDialogFragment.OnHeadlineSelectedListener{


    /**
     * timer checks if game still exists
     * call isGameExisting
     */
    Timer timerDoesGameExist;

    /**
     * String items for listview
     */
    private ArrayList<String> listItems = new ArrayList<>();

    /**
     * adapter for listview
     */
    private ArrayAdapter<String> adapter;

    /**
     * timer for listview
     * call getplayersingame
     */
    Timer timerPlayer;

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
     * creates activity for clientlobby
     * interface shows listview with all players which are currently in the game
     * calls getPlayersInGame by timer to update listview every now and then
     * @param savedInstanceState    .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_lobby);


        ListView list = (ListView) findViewById(R.id.client_lobby_players_list);
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(this.adapter);

        final Handler handler = new Handler();

        int delay = 0;   // delay for 0sec.
        int interval = 4000;  // iterate every 3rd sec.
        timerPlayer = new Timer();


        timerPlayer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                helper.getPlayersInGame(helper.getGameId(), new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success) {
                            for (int i = 0; i < helper.getPlayers().length; i++) {
                                final int finalI = i;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addItem(helper.getPlayers()[finalI]);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, delay, interval);



        try {
            Toast.makeText(getApplicationContext(), "Welcome " + helper.getName() + " to game #" + helper.getGameId() + "!", Toast.LENGTH_SHORT).show();
        }catch (RuntimeException _e){
            return;
        }

        Button b =  (Button) findViewById(R.id.client_lobby_continue);
        b.setOnClickListener(this);

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
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.client_lobby_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.client_lobby_drawer_indicator);
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
        int punkte = preferences.getInt("points", -1);

        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(username);
        TextView points = (TextView) findViewById(R.id.user_points);
        points.setText("Points: " + punkte);
        mNavItems.add(new NavItem("Quit", "quit game", R.drawable.quit));
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.credits));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.client_lobby_drawer_layout);

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

        final TextView styleButton = (TextView) findViewById(R.id.client_lobby_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.lobby)
                        : resources.getString(R.string.handsome));

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
     * adds a (players) name to listview
     * @param name  name of player
     */
    public void addItem(String name){
        boolean foundEqual = false;
        if(!adapter.isEmpty()) {
            for (int i = 0; (i < adapter.getCount() && !foundEqual); i++) {

                if (adapter.getItem(i).equals(name)) {
                    foundEqual = true;
                }
            }
        }
        if (!foundEqual) {
            adapter.add(name);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * onclick progressview will be showhn by showProgress
     * timerPlayer will be canceled
     * getQuestionByUserAndGameId will be called
     * calls ClientQuestion
     * finishes
     * @param v .
     */
    @Override
    public void onClick(View v) {
        showProgress(true);
        timerPlayer.cancel();
        timerPlayer.purge();
        timerPlayer = null;
        timerDoesGameExist.cancel();
        timerDoesGameExist.purge();
        timerDoesGameExist = null;
        helper.getQuestionByUserAndGameId(helper.getUserID(), helper.getGameId(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                if (success) {
                    Intent i = new Intent(ClientLobby.this, ClientQuestion.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
            }
        });
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

        final View mProgressView = findViewById(R.id.client_lobby_progress);
        Button b = (Button) findViewById(R.id.client_lobby_continue);

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

    /**
     * finishes activity and canceled timerPlayer if quit is true
     * is true if user clicked positive button and quitGame was a success
     * @param quit true if quitGame
     */
    @Override
    public void onArticleSelected(boolean quit) {
        timerPlayer.cancel();
        timerPlayer.purge();
        timerPlayer = null;
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

