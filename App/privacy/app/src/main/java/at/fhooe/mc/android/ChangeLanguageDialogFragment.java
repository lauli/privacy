package at.fhooe.mc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 17.06.16 for Privacy.
 */
public class ChangeLanguageDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Language");
        builder.setMessage(R.string.dialog_language)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdditionalMethods helper = AdditionalMethods.getInstance();
                        int language;
                        if(helper.getLang() == 1)
                            language = 2;
                        else
                            language = 1;
                        helper.changeLanguage(helper.getUserID(), language,new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, JSONObject response) {
                                if(success) {
                                }
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

}