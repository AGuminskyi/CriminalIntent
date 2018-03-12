package com.android.huminskiy1325.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ChooseObjectDialogFragment extends DialogFragment {
    public static final String CHOISE_OBJECT = "com.android.huminskiy1325.criminalintent.object";
    private static final String ARRAY1 = "array1";
    private static final String ARRAY2 = "array2";

    private String object = null;
    private ArrayList<String> objects = null;


    public static ChooseObjectDialogFragment newInstance(ArrayList<String> objects, String title){
        Bundle args = new Bundle();
        args.putSerializable(ARRAY1, objects);
        args.putSerializable(ARRAY2, title);
        ChooseObjectDialogFragment fragment = new ChooseObjectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        objects = (ArrayList<String>)getArguments().getSerializable(ARRAY1);
        String title = (String) getArguments().getSerializable(ARRAY2);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_empty_list, null, false);
        ListView listView = (ListView) view.findViewById(R.id.empty_list);
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, objects);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                object = objects.get(position).toString();
                sendResult(Activity.RESULT_OK);
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setView(view);
        return builder.create();
    }

    private void sendResult(int result) {
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(CHOISE_OBJECT, object);
        getTargetFragment().onActivityResult(getTargetRequestCode(), result, intent);
    }

}
