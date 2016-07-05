package at.fhooe.mc.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.json.JSONObject;

/**
 * Created by laureenschausberger on 17.06.16.
 */
public class QuitDialogFragment extends DialogFragment {

    OnHeadlineSelectedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quit Session");
        builder.setMessage(R.string.dialog_quit)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AdditionalMethods helper = AdditionalMethods.getInstance();
                        helper.quitGame(helper.getGameId(), new OnJSONResponseCallback() {
                            @Override
                            public void onJSONResponse(boolean success, JSONObject response) {
                                if(success) {
                                    mCallback.onArticleSelected(true);
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

    // Container Activity must implement this interface
    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(boolean quit);
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