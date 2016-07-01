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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.dialog_fragment);
        builder.setTitle("Credits");
        builder.setMessage(R.string.dialog_credits)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // get the listview
        expListView = (ExpandableListView) getView().findViewById(R.id.dialog_listview);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });

        return builder.create();
    }

    /*
   * Preparing the list data
   */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("DrawerArrowDrawable");
        listDataHeader.add("FloatLabeledEditText");
        listDataHeader.add("Material");

        // Adding child data
        List<String> DrawerArrowDrawable = new ArrayList<String>();
        DrawerArrowDrawable.add(String.valueOf(R.string.credits_chrisrenke));

        List<String> FloatLabeledEditText = new ArrayList<String>();
        DrawerArrowDrawable.add(String.valueOf(R.string.credits_chrisrenke));

        List<String> Material = new ArrayList<String>();
        DrawerArrowDrawable.add(String.valueOf(R.string.credits_chrisrenke));

        listDataChild.put(listDataHeader.get(0), DrawerArrowDrawable); // Header, Child data
        listDataChild.put(listDataHeader.get(1), FloatLabeledEditText);
        listDataChild.put(listDataHeader.get(2), Material);
    }
}