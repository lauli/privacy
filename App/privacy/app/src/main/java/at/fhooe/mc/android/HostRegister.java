package at.fhooe.mc.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via name/ID.
 */
public class HostRegister extends FragmentActivity implements  LoaderCallbacks<Cursor>, OnClickListener  {

    /**
     * saves the username, which the user can put into the edittext field
     */
    private EditText mUsername;

    /**
     * shows progress of login via progressview
     * will be shown as soon as user clicks host button
     */
    private View mProgressView;

    /**
     * login form and button for user interaction
     * will be hidden when login tasks are running
     */
    private View mJoinFormView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.host_register);
        // Set up the login form.
        mUsername = (EditText) findViewById(R.id.host_register_username);
        mUsername.setText(helper.getName());

        Button mName = (Button) findViewById(R.id.host_register_join_button);
        mName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mJoinFormView = findViewById(R.id.host_register_scroll);
        mProgressView = findViewById(R.id.host_register_progress);
        mName.setOnClickListener(HostRegister.this);


        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.host_register_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.host_register_drawer_indicator);
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
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.credits));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.host_register_drawer_layout);

        // Populate the Navigtion Drawer with options
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.navList);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setAdapter(adapter);

        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
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

        imageView.setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.create)
                        : resources.getString(R.string.cool));

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
     * Onclicklistener, which attempts Logintask as soon as you click the host Button
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (attemptLogin()) {
            final AdditionalMethods helper = AdditionalMethods.getInstance();
            helper.newGame(helper.getUserID(), helper.getQuestionId(), new OnJSONResponseCallback() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    if (success) {
                        Intent i = new Intent(HostRegister.this, HostLobby.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }


    /**
     * Attempts to create a new session.
     * If there are form errors (missing fields, too long name etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {
        // Reset errors.
        mUsername.setError(null);

        // Store values at the time of the login attempt.
        final String mName = mUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Checks for a valid input/name
        if (TextUtils.isEmpty(mName)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        } else if (!isUsernameValid(mName)) {
            mUsername.setError(getString(R.string.error_invalid_name));
            focusView = mUsername;
            cancel = true;
        }
        else{
            if(!helper.getName().equals(mName)){
                helper.changeUserName(helper.getUserID(), mName, new OnJSONResponseCallback() {
                    @Override
                    public void onJSONResponse(boolean success, JSONObject response) {
                        if (success) {
                            SharedPreferences preferences = getSharedPreferences("myPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("username", mName);
                            editor.commit();
                        }
                    }
                });

            }
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to perform the user login attempt.
            showProgress(true);
            return true;
        }
    }

    /**
     *
     * Checks if name is valid (no more than 25 chars)
     * @param name  name that needs to be checked if valid
     * @return
     */
    private boolean isUsernameValid(String name) {
        if(name.length() > 25) return false;
        return true;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mJoinFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            TextView view = (TextView) findViewById(R.id.host_register_textview);
            view.setText("Please wait.\nWe are currently trying to create your game.");
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mJoinFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void selectItemFromDrawer(int position) {

        FragmentManager fm = getFragmentManager();
        CreditDialogFragment credit = new CreditDialogFragment();
        credit.show(getSupportFragmentManager(), "Dialog");

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
}

