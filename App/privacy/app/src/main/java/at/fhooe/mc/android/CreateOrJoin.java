package at.fhooe.mc.android;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Objects;

public class CreateOrJoin extends FragmentActivity implements View.OnClickListener, FirstLoginDialogFragment.OnHeadlineSelectedListener{

    /**
     * Context from Activity
     * needed for Fragments & AdditionalMethods
     */
    private static Context contextForCreateUser;

    /**
     * Fragment for first time call
     */
    FirstLoginDialogFragment dialog;

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
     * creates activity for user to join or create a session
     * also shows FirstLoginDialogFragment if its open for the first time
     * @param savedInstanceState    .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join);
        Button b = (Button) findViewById(R.id.create_or_join_create);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.create_or_join_join);
        b.setOnClickListener(this);

        /*
      SharedPreferences name
     */
        String myPREFERENCES = "myPref";
        SharedPreferences preferences = getSharedPreferences(myPREFERENCES, MODE_PRIVATE);
        if (preferences.getString("username", "").equals("")) {
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
        mNavItems.add(new NavItem("Language", "change question Language", R.drawable.language));
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.credits));

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

    /**
     * onclick calls HostCategory or ClientRegister
     * @param v .
     */
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
        else {
            ChangeLanguageDialogFragment fragment = new ChangeLanguageDialogFragment();
            fragment.show(getSupportFragmentManager(), "Dialog");
        }

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    /**
     * context of application
     * @return  context
     */
    public static Context getContextOfApplication(){
        return contextForCreateUser;
    }

    /**
     * overridden because back should not be able to be pressed
     */
    @Override
    public void onBackPressed() {

    }

    /**
     * finishes activity  if user changedLanguage
     * is true if user clicked positive button and quitGame was a success
     * @param done true if user changed language
     */
    @Override
    public void onArticleSelected(boolean done) {
        finish();
        startActivity(getIntent());
    }
}
