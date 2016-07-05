package at.fhooe.mc.android;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ExpandableListView.OnChildClickListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laureenschausberger on 17.06.16.
 */
public class CreditDialogFragment extends DialogFragment {

    AdditionalMethods helper = new AdditionalMethods();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        LayoutInflater factory = LayoutInflater.from(getActivity());
        View dialogLayout = factory.inflate(R.layout.dialog_fragment, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogLayout);
        builder.setTitle("Credits")
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        expListView = (ExpandableListView) dialogLayout.findViewById(R.id.dialog_list);

        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        return builder.create();
    }

    /*
   * Preparing the list data
   */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Special thanks to");
        listDataHeader.add("DrawerArrowDrawable");
        listDataHeader.add("FloatLabeledEditText");
        listDataHeader.add("Material");

        // Adding child data
        List<String> special = new ArrayList<String>();
        special.add("FH Hagenberg and Fabian Bouchal");

        List<String> DrawerArrowDrawable = new ArrayList<String>();
        DrawerArrowDrawable.add(getString(R.string.credits_chrisrenke));

        List<String> FloatLabeledEditText = new ArrayList<String>();
        FloatLabeledEditText.add(getString(R.string.credits_henriksandstroem));

        List<String> Material = new ArrayList<String>();
        Material.add(getString(R.string.credits_reypham));

        listDataChild.put(listDataHeader.get(0), special); // Header, Child data
        listDataChild.put(listDataHeader.get(1), DrawerArrowDrawable);
        listDataChild.put(listDataHeader.get(2), FloatLabeledEditText);
        listDataChild.put(listDataHeader.get(3), Material);
    }
}