package at.fhooe.mc.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by laureenschausberger.
 * A login screen that offers login via name/ID.
 */
public class ClientRegister extends FragmentActivity implements  LoaderCallbacks<Cursor>, OnClickListener  {


    /**
     * EditText field for username-input
     */
    private EditText mUsername;

    /**
     * EditText field for gameIde input
     */
    private EditText mSessionId;

    /**
     * View for Progress
     */
    private View mProgressView;

    /**
     * View to hide JoinForm
     */
    private View mJoinFormView;

    /**
     * sessionId
     */
    private int sessionId;

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
     * created activity for clientregister
     * login UI for client
     * needs name and sessionid to be told
     * @param savedInstanceState    .
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_register);
        // Set up the login form.
        mUsername = (EditText) findViewById(R.id.client_register_username);
        /*
      SharedPreferences name
     */
        String myPREFERENCES = "myPref";
        SharedPreferences preferences = getSharedPreferences(myPREFERENCES, Context.MODE_PRIVATE);
        String name = preferences.getString("username", "");
        mUsername.setText(name);

        mSessionId = (EditText) findViewById(R.id.client_register_sessionID);
        mSessionId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                return id == R.id.login || id == EditorInfo.IME_ACTION_DONE;
            }
        });

        Button mName = (Button) findViewById(R.id.client_register_join_button);

        mName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mJoinFormView = findViewById(R.id.client_register_scroll);
        mProgressView = findViewById(R.id.client_register_progress);
        mName.setOnClickListener(ClientRegister.this);


        // --------------------------------------------------------------------------------------------  actionbar Start!
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.client_register_drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.client_register_drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        //------------------------------------------------------------------------ ListView in Actionbar
        String username = preferences.getString("username", "");
        int punkte = preferences.getInt("points", -1);

        TextView nameTextfield = (TextView) findViewById(R.id.user_name);
        nameTextfield.setText(username);
        TextView points = (TextView) findViewById(R.id.user_points);
        points.setText("Points: " + punkte);
        mNavItems.add(new NavItem("Credit", "thank you!", R.drawable.ic_menu_moreoverflow_normal_holo_dark));

        // DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.client_register_drawer_layout);

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

        final TextView styleButton = (TextView) findViewById(R.id.indicator_style);
        styleButton.setOnClickListener(new View.OnClickListener() {
            boolean rounded = false;

            @Override public void onClick(View v) {
                styleButton.setText(rounded //
                        ? resources.getString(R.string.join)
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
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {

        // Reset errors.
        mUsername.setError(null);
        mSessionId.setError(null);

        // Store values at the time of the login attempt.
        final String mName = mUsername.getText().toString();
        String mID = mSessionId.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid name.
        if (TextUtils.isEmpty(mName)) {
            mUsername.setError(getString(R.string.error_field_required));
            focusView = mUsername;
            cancel = true;
        }
        else if (TextUtils.isEmpty(mID)) {
            mSessionId.setError(getString(R.string.error_field_required));
            focusView = mSessionId;
            cancel = true;
        }
        else if (!isUsernameValid(mName)) {
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            sessionId = Integer.parseInt(mID);
            showProgress(true);
            return true;
        }
    }

    /**
     * check if username is valid
     * @param name  username
     * @return      true or false
     */
    private boolean isUsernameValid(final String name) {
        return name.length() <= 25;
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

    /**
     * onclick calls attemptLogin
     * if input is valid, calls joinGame and ClientLobby
     * finishes
     * @param view  .
     */
    @Override
    public void onClick(View view) {
        helper = AdditionalMethods.getInstance();
        if(attemptLogin()) {
            helper.joinGame(helper.getUserID(), sessionId, new OnJSONResponseCallback() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    if(success) {
                        if (helper.getGameId() != -1) {
                            Intent i = new Intent(ClientRegister.this, ClientLobby.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(ClientRegister.this, "Oops. That did not work.\n Your Session ID was wrong.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ClientRegister.this, "Oops. That did not work.\n Your Session ID was wrong.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /**
     * ProfileQuery interface
     */
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
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
}

