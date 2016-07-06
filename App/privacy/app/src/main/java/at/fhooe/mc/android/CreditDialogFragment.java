package at.fhooe.mc.android;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by laureenschausberger on 17.06.16.
 * shows dialog to see credits
 */
public class CreditDialogFragment extends DialogFragment {

    /**
     * instance of AdditionalMethode
     */
    AdditionalMethods helper = new AdditionalMethods();

    /**
     * listadapter
     */
    ExpandableListAdapter listAdapter;

    /**
     * listview
     */
    ExpandableListView expListView;

    /**
     * title
     */
    List<String> listDataHeader;

    /**
     * text under title
     * should be shown when clicked on title (listDataHeader)
     */
    HashMap<String, List<String>> listDataChild;

    /**
     * * shows dialog to see credits
     * @param savedInstanceState    .
     * @return                      builder.create()
     */
    @NonNull
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
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Special thanks to");
        listDataHeader.add("DrawerArrowDrawable");
        listDataHeader.add("FloatLabeledEditText");
        listDataHeader.add("Material");

        // Adding child data
        List<String> special = new ArrayList<>();
        special.add("FH Hagenberg and Fabian Bouchal");

        List<String> DrawerArrowDrawable = new ArrayList<>();
        DrawerArrowDrawable.add(getString(R.string.credits_chrisrenke));

        List<String> FloatLabeledEditText = new ArrayList<>();
        FloatLabeledEditText.add(getString(R.string.credits_henriksandstroem));

        List<String> Material = new ArrayList<>();
        Material.add(getString(R.string.credits_reypham));

        listDataChild.put(listDataHeader.get(0), special); // Header, Child data
        listDataChild.put(listDataHeader.get(1), DrawerArrowDrawable);
        listDataChild.put(listDataHeader.get(2), FloatLabeledEditText);
        listDataChild.put(listDataHeader.get(3), Material);
    }
}