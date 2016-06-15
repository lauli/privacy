package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ClientQuestion extends Activity implements View.OnClickListener{

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;
    TextView question;

    AdditionalMethods helper = AdditionalMethods.getInstance();


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
        question.setText("Here you will see your question.. ");

        final Handler handler = new Handler();
        helper.getQuestionByUserAndGameId(helper.getUserID(), helper.getGameId(), new OnJSONResponseCallback() {
            @Override
            public void onJSONResponse(boolean success, JSONObject response) {
                showQuestion(helper.getQuestion());
            }
        });


        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.client_question_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.client_question_drawer_indicator);
        final Resources resources = getResources();
        final ListView drawerList = (ListView) findViewById(R.id.client_question_drawer_list);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"Name", "Points", "Picture", "", "Skip", "Quit"});
//        drawerList.setAdapter(adapter);


        String[] oben = {"# " + helper.getGameIdString(), "Name", "Points",
                "Language", "Quit", "Credits"};

        String[] unten = {"", helper.getName(),  helper.getPointsString(), helper.getLanguage(), "quit this game", "thanks for help"};
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

    private void showQuestion(String _question) {
        question.setText(_question);
    }

    @Override
    public void onClick(View _view) {
        switch (_view.getId()){
            case R.id.client_question_yes : {
                helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.getQuestionId(), 1, 0, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        Intent i = new Intent(ClientQuestion.this, ClientGuess.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
            } break;
            case R.id.client_question_no : {
                helper.answerQuestion(helper.getUserID(), helper.getGameId(), helper.getQuestionId(), 2, 0, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        Intent i = new Intent(ClientQuestion.this, ClientGuess.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
            } break;
            default : {}
        }
    }
}
