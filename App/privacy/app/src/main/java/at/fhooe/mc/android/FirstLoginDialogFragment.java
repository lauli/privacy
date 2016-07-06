package at.fhooe.mc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 17.06.16.
 * Dialog for first time app call to createUser with input name
 */
public class FirstLoginDialogFragment extends DialogFragment {

    /**
     * callback for Activity to handle choices made in dialog
     */
    OnHeadlineSelectedListener mCallback;

    /**
     * name, changes when editText is filled
     */
    String name;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        final EditText edittext = new EditText(getActivity());
        edittext.setSingleLine(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("User Registration")
                .setMessage("Welcome to PrivacyGame!\nYou have to register once, so that other players can identify you by your username.\nNotice that this name will be displayed everytime you play!\nTherefore, choose wisely.. ")
                .setView(edittext)
                .setPositiveButton("register", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        name = edittext.getText().toString();
                        AdditionalMethods helper = AdditionalMethods.getInstance();
                        if(!name.equals("")) {
                            helper.createUser(1, name, new OnJSONResponseCallback() {
                                @Override
                                public void onJSONResponse(boolean success, JSONObject response) {
                                    if (success) {
                                        try {
                                            mCallback.onArticleSelected(true);
                                        } catch (Throwable _throwable) {
                                            _throwable.printStackTrace();
                                        }
                                    }
                                }
                            });
                        }
                        else{
                            try {
                                mCallback.onArticleSelected(true);
                            } catch (Throwable _throwable) {
                                _throwable.printStackTrace();
                            }
                        }
                    }
                })
                .setIcon(R.drawable.privacy_icon)
                .setCancelable(false)
                .create();


        return  builder.create();
    }

    /**
     * on dismiss dialog closes
     * @param dialog    .
     */
    public void onDismiss(DialogInterface dialog) {
        Activity activity = getActivity();
        if(activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)activity).handleDialogClose(dialog);
    }

    /**
     * MyDialogCloseListener interface
     */
    public interface MyDialogCloseListener {
         void handleDialogClose(DialogInterface dialog);
    }

    /**
     * OnHeadlineSelectedListener interface
     */
    public interface OnHeadlineSelectedListener {
         void onArticleSelected(boolean done);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}
