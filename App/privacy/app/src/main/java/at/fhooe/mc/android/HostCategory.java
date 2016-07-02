package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

public class HostCategory extends Activity implements View.OnClickListener{

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;
    TextView question;

    AdditionalMethods helper = AdditionalMethods.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_category);

        Button b = null;
        b = (Button) findViewById(R.id.host_category_private);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.host_category_under18);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.host_category_over18);
        b.setOnClickListener(this);

        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.host_category_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.host_category_drawer_indicator);
        final Resources resources = getResources();
        final ListView drawerList = (ListView) findViewById(R.id.host_category_drawer_list);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"Name", "Points", "Picture", "", "Skip", "Quit"});
//        drawerList.setAdapter(adapter);

        String[] oben = {"Name", "Credits"};

        String[] unten = {helper.getName(), "thanks for help"};
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

        final TextView styleButton = (TextView) findViewById(R.id.host_category_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.category) //
                        : resources.getString(R.string.choose));

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
        switch (view.getId()){
            case R.id.host_category_private : {
                helper.getQuestionIdsByGroupId(3, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success){
                            Intent i = new Intent(HostCategory.this, HostRegister.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }
                });
            } break;
            case R.id.host_category_under18 : {
                helper.getQuestionIdsByGroupId(4, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success){
                                        Intent i = new Intent(HostCategory.this, HostRegister.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                        }
                    }
                });
            } break;
            case R.id.host_category_over18 : {
                helper.getQuestionIdsByGroupId(5, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if(success){
                            Intent i = new Intent(HostCategory.this, HostRegister.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }
                });
            } break;
            default : {}
        }
    }

    private void showQuestion(String questionString) {
        question.setText(questionString);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AdditionalMethods helper = AdditionalMethods.getInstance();
        SharedPreferences preferences = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userId", helper.getUserID());
        editor.commit();
        editor.putInt("points", helper.getPoints());
        editor.commit();
    }
}
