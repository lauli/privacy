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
 * DialogFragment to show Quit Dialog
 * game can be closed when one clicks positive button
 */
public class GameDoesntExistDialogFragment extends DialogFragment {

    /**
     * callback for Activity to handle choices made in dialog
     */
    OnHeadlineSelectedListener mCallback;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Game has been terminated");
        builder.setMessage(R.string.dialog_game_doesntexist)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCallback.onArticleSelected(true);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /**
     * OnHeadlineSelectedListener interface
     */
    public interface OnHeadlineSelectedListener {
         void onArticleSelected(boolean quit);
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