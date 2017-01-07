package com.android.huminskiy1325.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * Created by cubru on 07.01.2017.
 */

public class ChoiceDialogFragment extends DialogFragment {

    public static final String EXTRA_CHOISE = "com.android.huminskiy1325.criminalintent.choise_picker_fragment_id";
    public static int mChoise;
    public static final int FIRST_CHOISE = 1;
    public static final int SECOND_CHOISE = 2;

    public void sendResult(int resultCode){
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_CHOISE,mChoise);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
     return  new AlertDialog.Builder(getActivity())
             .setTitle(R.string.choise_dialog_title)
                .setPositiveButton(R.string.choise_dialog_date, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoise = FIRST_CHOISE;
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(R.string.choise_dialog_time, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mChoise = SECOND_CHOISE;
                        sendResult(Activity.RESULT_OK);
                    }
                })

         .create();
    }
}
