package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.widget.Slider;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HostGuess extends Activity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private ArrayList<String> listItems = new ArrayList<String>();
    private ListView list;

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;

    AdditionalMethods helper = AdditionalMethods.getInstance();
    private ArrayAdapter<String> adapter;
    final int[] guess = {helper.getAnsweredPlayers().length};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_guess);

        list = null;
        list = (ListView) findViewById(R.id.host_guess_players_list);
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        list.setAdapter(this.adapter);


        for (int i = 0; i < helper.getAnsweredPlayers().length; i++) {
            addItem(helper.getAnsweredPlayers()[i]);
        }

        com.rey.material.widget.Slider slider = (com.rey.material.widget.Slider) findViewById(R.id.host_guess_slider);
        slider.setValueRange(0, helper.getAnsweredPlayers().length, true);

//        slider.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.ACTION_DOWN && guess[0] != -1) {
//                    helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.questionId, helper.getAnswer(), guess[0], new OnJSONResponseCallback() {
//                        @Override
//                        public void onJSONResponse(boolean success, JSONObject response) {
//                            helper.allowStatistics(helper.userId, helper.getGameId(), new OnJSONResponseCallback() {
//                                @Override
//                                public void onJSONResponse(boolean success, JSONObject response) {
//                                    helper.getStatisticsByGameId(helper.getGameId(), new OnJSONResponseCallback() {
//                                        @Override
//                                        public void onJSONResponse(boolean success, JSONObject response) {
//                                            helper.pushPointsToProfile(helper.getUserID(), helper.getPointsFromThisRound(), new OnJSONResponseCallback() {
//                                                @Override
//                                                public void onJSONResponse(boolean success, JSONObject response) {
//                                                    Intent i = new Intent(HostGuess.this, HostStatistics.class);
//                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                    startActivity(i);
//                                                }
//                                            });
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    });
//                }
//                return false;
//            }
//        });
        slider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                guess[0] = newValue;
            }
        });

        Button b = null;
        b = (Button) findViewById(R.id.host_guess_continue);
        b.setOnClickListener(this);

        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.host_guess_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.host_guess_drawer_indicator);
        final Resources resources = getResources();
        final ListView drawerList = (ListView) findViewById(R.id.host_guess_drawer_list);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

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

        final TextView styleButton = (TextView) findViewById(R.id.host_guess_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.guess) //
                        : resources.getString(R.string.funny));

                rounded = !rounded;

                drawerArrowDrawable = new DrawerArrowDrawable(resources, rounded);
                drawerArrowDrawable.setParameter(offset);
                drawerArrowDrawable.setFlip(flipped);
                drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));

                imageView.setImageDrawable(drawerArrowDrawable);
            }
        });
        // --------------------------------------------------------------------------------------------  actionbar End!


//        Spinner spinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> adapt = ArrayAdapter.createFromResource(this, R.array.spinner_elements, android.R.layout.simple_spinner_item);
//        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapt);
//        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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

    @Override
    public void onClick(View _view) {
        helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.questionId, helper.getAnswer(), guess[0], new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
               if(success) helper.allowStatistics(helper.userId, helper.getGameId(), new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success) helper.getStatisticsByGameId(helper.getGameId(), new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, JSONObject response) {
                                if(success) helper.pushPointsToProfile(helper.getUserID(), helper.getPointsFromThisRound(), new OnJSONResponseCallback() {
                                    @Override
                                    public void onJSONResponse(boolean success, JSONObject response) {
                                        if(success)  helper.allowCounting(helper.getUserID(), helper.getGameId(), new OnJSONResponseCallback() {
                                            @Override
                                            public void onJSONResponse(boolean success, JSONObject response) {
                                                if(success){
                                                    Intent i = new Intent(HostGuess.this, HostStatistics.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
