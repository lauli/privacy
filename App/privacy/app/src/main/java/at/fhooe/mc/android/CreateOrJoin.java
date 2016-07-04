package at.fhooe.mc.android;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateOrJoin extends FragmentActivity implements View.OnClickListener{


    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;
    AdditionalMethods helper = AdditionalMethods.getInstance();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private static Context contextForCreateUser;
    private final String MyPREFERENCES = "myPref";
    FirstLoginDialogFragment dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join);
        Button b = null;
        b = (Button) findViewById(R.id.create_or_join_create);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.create_or_join_join);
        b.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.getBoolean("firstCall", true)) {
            editor.putBoolean("firstCall", false);
            editor.commit();
            FragmentManager fm = getFragmentManager();
            dialog = new FirstLoginDialogFragment();
            dialog.show(getSupportFragmentManager(), "Dialog");
        }
        else{
            AdditionalMethods helper = AdditionalMethods.getInstance();
            helper.setName(preferences.getString("username", ""));
            helper.setUserID(preferences.getInt("userId", -1));
            helper.setPoints(preferences.getInt("points", -1));
        }

        contextForCreateUser = getApplicationContext();


        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.create_or_join_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.create_or_join_drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        //------------------------------------------------------------------------ ListView in Actionbar
        String username = preferences.getString("username", "");
        int punkte = preferences.getInt("points", -1);

        TextView name = (TextView) findViewById(R.id.user_name);
        name.setText(username);
        TextView points = (TextView) findViewById(R.id.user_points);
        points.setText("Points: " + punkte);
        mNavItems.add(new NavItem("Skip", "skip this question", R.drawable.ic_menu_moreoverflow_normal_holo_dark));
        mNavItems.add(new NavItem("Quit", "Quit the game", R.drawable.ic_menu_moreoverflow_normal_holo_dark));
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.ic_menu_moreoverflow_normal_holo_dark));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.create_or_join_drawer_layout);

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
        //------------------------------------------------------------------------ ListView in Actionbar


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

        final TextView styleButton = (TextView) findViewById(R.id.create_or_join_indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.create_or_join)
                        : resources.getString(R.string.nice));

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
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.create_or_join_create : {
                Intent i = new Intent(this, HostCategory.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }break;
            case R.id.create_or_join_join : {
                Intent i = new Intent(this, ClientRegister.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }break;
            default :
        }

    }

    private void selectItemFromDrawer(int position, String title) {

        FragmentManager fm = getFragmentManager();

        if(title == "Credit") {
            CreditDialogFragment fragment = new CreditDialogFragment();
            fragment.show(getSupportFragmentManager(), "Dialog");
        }
//        else if(title == "Skip") {
//            SkipDialogFragment fragment = new SkipDialogFragment();
//            fragment.show(getSupportFragmentManager(), "Dialog");
//        }
//        else { //Quit
//            QuitDialogFragment fragment = new QuitDialogFragment();
//            fragment.show(getSupportFragmentManager(), "Dialog");
//        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    public static Context getContextOfApplication(){
        return contextForCreateUser;
    }


    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
            String username = preferences.getString("username", "");
            int punkte = preferences.getInt("points", -1);

            TextView name = (TextView) findViewById(R.id.user_name);
            name.setText(username);
            TextView points = (TextView) findViewById(R.id.user_points);
            points.setText("Points: " + punkte);
        }
    }
}
