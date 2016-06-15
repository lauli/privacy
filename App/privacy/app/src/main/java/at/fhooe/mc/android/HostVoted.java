package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HostVoted extends Activity implements View.OnClickListener{

    private ArrayList<String> listItems = new ArrayList<String>();
    private ListView list;

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;

    private ArrayAdapter<String> adapter;
    AdditionalMethods helper = AdditionalMethods.getInstance();
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_voted);


        list = null;
        list = (ListView) findViewById(R.id.host_voted_players_list);
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(this.adapter);

        final Handler handler = new Handler();
        int delay = 2000;   // delay for 2 sec.
        int interval = 5000;  // iterate every sec.
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                final AdditionalMethods helper = AdditionalMethods.getInstance();
                helper.getAnsweredUsers(helper.getGameId(), new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success) {
                            for (int i = 0; i < helper.getAnsweredPlayers().length; i++) {
                                final int finalI = i;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        addItem(helper.getAnsweredPlayers()[finalI]);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, delay, interval);

        Button b = null;
        b = (Button) findViewById(R.id.host_voted_continue);
        b.setOnClickListener(this);

        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.host_voted_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.host_voted_drawer_indicator);
        final Resources resources = getResources();
        final ListView drawerList = (ListView) findViewById(R.id.host_voted_drawer_list);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"Name", "Points", "Picture", "", "Skip", "Quit"});
//        drawerList.setAdapter(adapter);


        String[] oben = {"# " + helper.getGameIdString(), "Name", "Points",
                "Language", "Skip", "Quit", "Credits"};

        String[] unten = {  "", helper.getName(), helper.getPointsString(), helper.getLanguage(),
                "skip this question", "quit this game", "thanks for help"};
        MyAdapter myAdapter = new MyAdapter(this, oben, unten);
        drawerList.setAdapter(myAdapter);

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

        final TextView styleButton = (TextView) findViewById(R.id.host_voted_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.voted)
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
    public void onClick(View view) {

        helper.allowStatistics(helper.userId, helper.getGameId(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                timer.cancel();
                Intent i = new Intent(HostVoted.this, HostGuess.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

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
}

