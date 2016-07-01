package at.fhooe.mc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 17.06.16.
 */
public class FirstLoginDialogFragment extends DialogFragment {

    private final String MyPREFERENCES = "myPref";
    AdditionalMethods helper;
    Handler handler = new Handler();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        final EditText edittext = new EditText(this.getContext());
        View view = View.inflate(getActivity(),R.layout.dialog_first_login_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("User Registration")
                .setMessage("Welcome to PrivacyGame!\nYou have to register once, so that other players can identify you by your username.\nNotice that this name will be displayed everytime you play!\nTherefore, choose wisely.. ")
                .setView(edittext)
                .setPositiveButton("register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = edittext.getText().toString();
                        helper = AdditionalMethods.getInstance();
                        helper.createUser(1, name, new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, JSONObject response) {
                                if (success) {
                                }
                            }
                        });
                    }
                })
                .setIcon(R.drawable.privacy_icon)
                .setView(view)
                .create();
        return  builder.create();
    }

    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)activity).handleDialogClose(dialog);
    }

    public interface MyDialogCloseListener {
        public void handleDialogClose(DialogInterface dialog);
    }

}
