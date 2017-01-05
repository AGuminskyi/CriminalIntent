package com.android.huminskiy1325.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by cubru on 05.01.2017.
 */


public class TimePickerFragment extends DialogFragment {
    @NonNull

    public static Date mTime;

    public static final String EXTRA_TIME = "com.android.huminskiy1325.criminalintent.time";

    public static TimePickerFragment newInstance(Date data) {
        
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_TIME, data);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTime = (Date)getArguments().getSerializable(EXTRA_TIME);
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTime(mTime);
        //DateFormat dateFormat = DateFormat.getTimeFormat(getDialog());

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_time,null);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
