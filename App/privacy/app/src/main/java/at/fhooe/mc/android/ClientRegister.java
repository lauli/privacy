package at.fhooe.mc.android;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via name/ID.
 */
public class ClientRegister extends Activity implements  LoaderCallbacks<Cursor>, OnClickListener  {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
//    private static final String[] DUMMY_CREDENTIALS = new String[]{
//            "23", "24", "25", "26", "27"
//            //sessionID Database
//    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mUsername;
    private EditText mSessionId;
    private View mProgressView;
    private View mJoinFormView;

    private String name;
    private int sessionId;

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private ListView drawerList;

    AdditionalMethods helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_register);
        // Set up the login form.
        mUsername = (EditText) findViewById(R.id.client_register_username);


        mSessionId = (EditText) findViewById(R.id.client_register_sessionID);
        mSessionId.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_ACTION_DONE) {
                    //attemptLogin();
                    return true;
                }
                return false;
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
        final ListView drawerList = (ListView) findViewById(R.id.client_register_drawer_list);

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                new String[]{"Name: -", "Points: -", "Picture", "", "Skip", "Quit"});
//        drawerList.setAdapter(adapter);

        String[] oben = {"# SessionId", "Credits"};

        String[] unten = {"", "thanks for help"};
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
        if (mAuthTask != null) {
            return true;
        }

        // Reset errors.
        mUsername.setError(null);
        mSessionId.setError(null);

        // Store values at the time of the login attempt.
        String mName = mUsername.getText().toString();
        String mID = mSessionId.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(mID) && !isSessionIdValid(mID)) {
//            mSessionId.setError(getString(R.string.error_invalid_password));
//            focusView = mSessionId;
//            cancel = true;
//        }

        // Check for a valid email address.
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
        else if (!isUsernameValid(mName, mID)) {
            mUsername.setError(getString(R.string.error_invalid_email));
            focusView = mUsername;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            name = mName;
            sessionId = Integer.parseInt(mID);
            showProgress(true);
            mAuthTask = new UserLoginTask(mName, mID);
//            mAuthTask.execute((Void) null);
            return true;
        }
    }

    private boolean isUsernameValid(final String _name, String _id) {
//        int mId = Integer.valueOf(_id);
//        final boolean[] foundEqual = {false};
//        helper.getPlayersInGame(mId, new OnJSONResponseCallback() {
//            @Override
//            public void onJSONResponse(boolean success, JSONObject response) {
//                if(success) {
//                    for (int i = 0; i < helper.getPlayers().length; i++) {
//                        if(helper.getPlayers()[i].equals(_name))
//                            foundEqual[0] = true;
//                    }
//                }
//            }
//        });
//        if(foundEqual[0] == true) return false;
//        else
            return true;
    }

//    private boolean isSessionIdValid(String _id) {
//        //TODO: Replace this with your own logic
//        for(int i = 0; i < DUMMY_CREDENTIALS.length; i++){
//            if(_id.equals(DUMMY_CREDENTIALS[i])) return true;
//        }
//        return false;
//    }

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

    @Override
    public void onClick(View view) {
        helper = AdditionalMethods.getInstance();
        Handler handler = new Handler();
        if(attemptLogin()) {
            helper.registerClient(1, name, new OnJSONResponseCallback() {
                @Override
                public void onJSONResponse(boolean success, JSONObject response) {
                    if (success) {
                        helper.joinGame(helper.getUserID(), sessionId, new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, JSONObject response) {
                                if(success) {
                                    if (helper.getGameId() != -1) {
                                        Intent i = new Intent(ClientRegister.this, ClientLobby.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(ClientRegister.this, "Oops. That did not work.\n Your Session ID was wrong.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
                    }
                }
            });


        }
        /*
        if(attemptLogin()){
            Intent i = new Intent(this, ClientLobby.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        };
        */

    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mSessionID;

        UserLoginTask(String _name, String _id) {
            mName = _name;
            mSessionID = _id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


/*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
*/
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mName)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mSessionID);
//                }
//            }
//
//
//            Looper.prepare();
//            AdditionalMethods helper = AdditionalMethods.getInstance();
//            try {
//                helper.registerClient(1, mName, false);
//                //Thread.sleep(5000);
//            } catch (RuntimeException _e) {
//                    Log.i("", "A failure accured while trying to register. Plz try again");
//            }
//            /*catch (InterruptedException _e){
//                Log.i("", "A failure accured while trying to register. Plz try again");
//            } */
//
//            try {
//                helper.joinGame(helper.getUserID(), mSessionID);
//                //Thread.sleep(5000);
//            }
//            catch(RuntimeException _e){
//                    Log.i("", "A failure accured while trying to join a session. Plz try again");
//            }
//            /*catch (InterruptedException _e){
//                Log.i("", "A failure accured while trying to register. Plz try again");
//            }
//            */
//
//
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mSessionId.setError(getString(R.string.error_incorrect_password));
                mSessionId.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }
}

