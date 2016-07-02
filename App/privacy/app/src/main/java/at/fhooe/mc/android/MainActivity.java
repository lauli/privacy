package at.fhooe.mc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static Context contextForCreateUser;
    private final String MyPREFERENCES = "myPref";

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout b = null;
        b = (FrameLayout) findViewById(R.id.main_start);
        b.setOnClickListener(this);

        SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.getBoolean("firstCall", true)) {
            editor.putBoolean("firstCall", false);
            editor.commit();
            FragmentManager fm = getFragmentManager();
            FirstLoginDialogFragment dialog = new FirstLoginDialogFragment();
            dialog.show(getSupportFragmentManager(), "Dialog");
        }
        else{
            AdditionalMethods helper = AdditionalMethods.getInstance();
            helper.setName(preferences.getString("username", ""));
            helper.setUserID(preferences.getInt("userId", -1));
            helper.setPoints(preferences.getInt("points", -1));
        }

        contextForCreateUser = getApplicationContext();
    }

    @Override
    public void onClick(View view) {
        Log.i("", "something is happening..");
        Intent i = new Intent(this, CreateOrJoin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public static Context getContextOfApplication(){
        return contextForCreateUser;
    }

}

