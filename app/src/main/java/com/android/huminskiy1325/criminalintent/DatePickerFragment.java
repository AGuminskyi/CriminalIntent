package com.android.huminskiy1325.criminalintent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by cubru on 27.10.2016.
 */

//Диалоговое окно на рис. 12.1 является экземпляром AlertDialog — субкласса Dialog.


public class DatePickerFragment extends DialogFragment {
    // в качестве хоста выступает CrimePagerActivity
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //создает AlertDialog с заголовком и одной кнопкой OK. (
        return new AlertDialog.Builder(getActivity()) // класс AlertDialog.Builder, предоставляющий dинамичный интерфейс для конструирования экземпляров AlertDialog.
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, null) //получает строковый ресурс и объект, реализующий DialogInterface.OnClickListener.
                .create(); //возвращает настроенный экземпляр AlertDialog.
    }
}
