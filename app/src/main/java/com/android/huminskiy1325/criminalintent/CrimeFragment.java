package com.android.huminskiy1325.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by cubrubarts on 15.07.2016.
 */
public class CrimeFragment extends Fragment {
    private Crime mCrime;
    private EditText mTitleField;
    private CheckBox mSolvedCheckBox;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mChoiseButton;

    public static final String EXTRA_CRIME_ID = "com.android.huminskiy1325.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final int REQUEST_DATE = 0;
    private static final int REQUST_TIME = 1;
    private static final int REQUEST_CHOISE = 2;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        // UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    public static CrimeFragment newInstance(UUID cimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, cimeId);
        CrimeFragment fragment = new CrimeFragment();//Чтобы присоединить пакет аргументов к фрагменту, вызовите метод Fragment.setArguments(Bundle).
        fragment.setArguments(args); //Присоединение должно быть выполнено после созданияфрагмента, но до его добавления в активность.
        return fragment;
    }

    public void updateDateOnButton(){
        mDateButton.setText(DateFormat.format("EEEE, LLL d, yyyy", mCrime.getDate()));
       // mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK)
            return;

        if(requestCode == REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDateOnButton();
        }

        if(requestCode == TimePickerFragment.TIME_RESULT_CODE){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getDate()));
        }

        if(requestCode == REQUEST_CHOISE){
            int choise = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOISE, 0);
            if(choise == 0)
                return;
            if(choise == 1)
            {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                // DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
            if (choise == 2)
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUST_TIME);
                timeDialog.show(fm, DIALOG_TIME);
            }
        }
    }

    // @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        //mTitleField.setText(DateFormat.format("EEEE, LLL d, yyyy", mCrime.getTitle()));
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        mDateButton.setText(DateFormat.format("EEEE, LLL d, yyyy", mCrime.getDate()));
        //mDateButton.setEnabled(false);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
               // DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);

            }
        });

        mTimeButton = (Button)v.findViewById(R.id.crime_time);
        mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getDate()));
       // mTimeButton.setEnabled(false);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUST_TIME);
                timeDialog.show(fm, DIALOG_TIME);
            }
        });

        mChoiseButton = (Button)v.findViewById(R.id.choice_button);
        mChoiseButton.setText(DateFormat.format("EEEE, LLL d, yyyy, HH:mm", mCrime.getDate()));
        mChoiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ChoiceDialogFragment choiceDialogFragment = new ChoiceDialogFragment();
                choiceDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_CHOISE);
                choiceDialogFragment.show(fm, ChoiceDialogFragment.EXTRA_CHOISE);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked) {
                mCrime.setSolved(isCheked);
            }
        });

        return v;
    }
}
