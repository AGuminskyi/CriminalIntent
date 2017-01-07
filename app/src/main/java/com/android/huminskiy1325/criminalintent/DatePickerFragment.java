package com.android.huminskiy1325.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ShareCompat;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by cubru on 27.10.2016.
 */

//Диалоговое окно на рис. 12.1 является экземпляром AlertDialog — субкласса Dialog.


public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "com.android.huminskiy1325.criminalintent.date";

    private Date mDate;

    public static  DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    // в качестве хоста выступает CrimePagerActivity
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_date,null);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_datePicker);
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int day, int month) {
                mDate = new GregorianCalendar(year,month,day).getTime();

                //Обновление аргумента для сохранения
                //выбранного значения при повороте
                getArguments().putSerializable(EXTRA_DATE,mDate);
            }
        });
        //создает AlertDialog с заголовком и одной кнопкой OK. (
        return new AlertDialog.Builder(getActivity()) // класс AlertDialog.Builder, предоставляющий dинамичный интерфейс для конструирования экземпляров AlertDialog.
                .setView(view)
                .setTitle(R.string.date_picker_title)
               // .setPositiveButton(android.R.string.ok, null) //получает строковый ресурс и объект, реализующий DialogInterface.OnClickListener.
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sendResult(Activity.RESULT_OK);
                            }
                        })
                .create(); //возвращает настроенный экземпляр AlertDialog.
    }
}
