package com.android.huminskiy1325.criminalintent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    private Crime mCrime;

    private EditText mTitleField;
    private CheckBox mSolvedCheckBox;

    //    private Button mDateButton;
//    private Button mTimeButton;
    private Button mChoiseButton;
    private Button mSuspectButton;
    private Button mCallButton;
    private Button mChoiseObjectButton;
    private Button mChoisePerson;
    private Button mChoisePair;


    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private static final String TAG = "CrimeFragment";

    public static final String EXTRA_CRIME_ID = "com.android.huminskiy1325.criminalintent.crime_id";

    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private static final String DIALOG_IMAGE = "image";

    private static final String ALERT_MESSAGE_TITLE = "Please, enter a tittle for the crime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUST_TIME = 1;
    private static final int REQUEST_CHOISE = 2;
    private static final int REQUEST_PHOTO = 3;
    private static final int REQUEST_CONTACT = 4;
    private static final int REQUEST_OBJECT = 5;
    private static final int REQUEST_PERSON = 6;
    private static final int REQUEST_PAIR = 7;


    private Callbacks mCallbacks;

    private Context context;

    public interface Callbacks {
        void OnCrimeUpdate(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID cimeId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CRIME_ID, cimeId);
        CrimeFragment fragment = new CrimeFragment();
        //Чтобы присоединить пакет аргументов к фрагменту, вызовите метод Fragment.setArguments(Bundle).
        fragment.setArguments(args);  //Присоединение должно быть выполнено после созданияфрагмента, но до его добавления в активность.
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).saveCrimes();
    }

    @Override
    public void onStart() {
        super.onStart();
//        showPhoto();

        if (mCrime.getSuspect() != null) {
            String text = "Call " + mCrime.getSuspect();
            mCallButton.setText(text);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        PictureUtils.cleanImageView(mPhotoView);
    }
//    public void updateDateOnButton() {
//        mDateButton.setText(DateFormat.format("EEEE, LLL d, yyyy", mCrime.getDate()));
//        // mDateButton.setText(mCrime.getDate().toString());
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            mCallbacks.OnCrimeUpdate(mCrime);
            // updateDateOnButton();
        } else if (requestCode == TimePickerFragment.TIME_RESULT_CODE) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            mCallbacks.OnCrimeUpdate(mCrime);
            //mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getDate()));
        } else if (requestCode == REQUEST_PHOTO) {

            String fileName = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
            if (fileName != null) {
                Photo photo = new Photo(fileName);
                mCrime.setPhoto(photo);
                mCallbacks.OnCrimeUpdate(mCrime);
//                showPhoto();
            }
        } else if (requestCode == REQUEST_CHOISE) {
            int choise = data.getIntExtra(ChoiceDialogFragment.EXTRA_CHOISE, 0);
            if (choise == 0)
                return;
            if (choise == 1) {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                // DatePickerFragment dialog = new DatePickerFragment();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
            if (choise == 2) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
                timeDialog.setTargetFragment(CrimeFragment.this, REQUST_TIME);
                timeDialog.show(fm, DIALOG_TIME);
            }
        } else if (requestCode == REQUEST_CONTACT) {
            Uri contactUri = data.getData();
            // Определение полей, значения которых должны быть
            // возвращены запросом.
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME
            };
            // Выполнение запроса - contactUri здесь выполняет функции
            // условия "where"
            Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = getActivity().getContentResolver().
                    query(contactUri, queryFields, null, null, null);

            Cursor cursor1 = getActivity().getContentResolver().query(phoneUri, queryFields, null, null, null);

            if (cursor.getCount() == 0) {
                cursor.close();
                return;
            }

            if (cursor1.getCount() == 0) {
                cursor1.close();
                return;
            }
            // Извлечение первого столбца данных - имени подозреваемого.
            cursor.moveToFirst();
            String suspect = cursor.getString(0);
            mCrime.setSuspect(suspect);
            mSuspectButton.setText(suspect);
            mCallbacks.OnCrimeUpdate(mCrime);
            cursor.close();

            cursor1.moveToFirst();
            String contactNumber = cursor1.getString(0);
            mCallButton.setText(contactNumber);
            mCallbacks.OnCrimeUpdate(mCrime);
            cursor1.close();
        } else if (requestCode == REQUEST_OBJECT) {
            String choise = data.getStringExtra(ChooseObjectDialogFragment.CHOISE_OBJECT);
            mChoiseObjectButton.setText(choise);
            mCrime.setmObject(choise);
            mCallbacks.OnCrimeUpdate(mCrime);

        } else if (requestCode == REQUEST_PERSON) {
            String choise = data.getStringExtra(ChooseObjectDialogFragment.CHOISE_OBJECT);
            mChoisePerson.setText(choise);
            mCrime.setmPerson(choise);
            mCallbacks.OnCrimeUpdate(mCrime);
        } else if (requestCode == REQUEST_PAIR) {
            String choise = data.getStringExtra(ChooseObjectDialogFragment.CHOISE_OBJECT);
            mChoisePair.setText(choise);
            mCrime.setmPair(Integer.parseInt(choise));
            mCallbacks.OnCrimeUpdate(mCrime);
        }
        mChoiseButton.setText(DateFormat.format("EEEE, LLL d, yyyy, HH:mm", mCrime.getDate()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.crime_fragment_command_menu, menu);
    }

    public boolean backButtonWasPressed() {
        if (NavUtils.getParentActivityName(getActivity()) != null) {
            if (mCrime.getTitle() == null) {
                Toast.makeText(getActivity(), ALERT_MESSAGE_TITLE, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
        }
        return true;

//        if(mCrime.getTitle() == null){
//            Toast.makeText(getActivity(), ALERT_MESSAGE_TITLE, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else{
//            getActivity().onBackPressed();
//            return true;
//        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
        switch (v.getId()) {
            case R.id.crime_imageView:
                getActivity().getMenuInflater().inflate(R.menu.crime_fragment_image_view_context, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_item_delete_image_view:
                Photo oldPhoto = mCrime.getPhoto();
                if (oldPhoto != null) {
                    File mFile = new File(getActivity().getExternalFilesDir(null), oldPhoto.getFileName());
                    mFile.delete();
                    Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                    startActivityForResult(i, REQUEST_PHOTO);
                }
                return true;
            case R.id.menu_item_take_image_view:
                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null)
                    if (mCrime.getTitle() == null) {
                        Toast.makeText(getActivity(), ALERT_MESSAGE_TITLE, Toast.LENGTH_SHORT).show();
                        return false;
                    } else {
                        if (CrimeLab.get(getActivity()).checkSchedulers()) {
                            NavUtils.navigateUpFromSameTask(getActivity());
                        }
                        else {
                            Toast.makeText(getActivity(),"This object already booked on this pair. Choose other time or pair", Toast.LENGTH_LONG).show();
                        }

                    }
                return true;
            case R.id.command_menu_delete_crimeFragment:
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.deleteCrime(mCrime);
                if (NavUtils.getParentActivityName(getActivity()) != null)
                    NavUtils.navigateUpFromSameTask(getActivity());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
        // UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        UUID crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);

        setHasOptionsMenu(true);
    }

    // @Nullable
    @TargetApi(11)
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null)
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
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
                mCallbacks.OnCrimeUpdate(mCrime);
                getActivity().setTitle(mCrime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        mDateButton = (Button) v.findViewById(R.id.crime_date);
//        mDateButton.setText(DateFormat.format("EEEE, LLL d, yyyy", mCrime.getDate()));
//        //mDateButton.setEnabled(false);
//        mDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
//                // DatePickerFragment dialog = new DatePickerFragment();
//                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
//                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
//                dialog.show(fm, DIALOG_DATE);
//
//            }
//        });
//
//        mTimeButton = (Button) v.findViewById(R.id.crime_time);
//        mTimeButton.setText(DateFormat.format("HH:mm", mCrime.getDate()));
//        // mTimeButton.setEnabled(false);
//        mTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                TimePickerFragment timeDialog = TimePickerFragment.newInstance(mCrime.getDate());
//                timeDialog.setTargetFragment(CrimeFragment.this, REQUST_TIME);
//                timeDialog.show(fm, DIALOG_TIME);
//            }
//        });
        mChoiseObjectButton = (Button) v.findViewById(R.id.crime_objectButton);
        if (mCrime.getmObject() != null)
            mChoiseObjectButton.setText(mCrime.getmObject());
        mChoiseObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String[] objects = getResources().getStringArray(R.array.objects);
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(objects));
                ChooseObjectDialogFragment chooseObjectDialogFragment = ChooseObjectDialogFragment.newInstance(list, "Objects");
                chooseObjectDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_OBJECT);
                chooseObjectDialogFragment.show(fragmentManager, ChooseObjectDialogFragment.CHOISE_OBJECT);
            }
        });


        mChoisePerson = (Button) v.findViewById(R.id.crime_personButton);
        if (mCrime.getmPerson() != null)
            mChoisePerson.setText(mCrime.getmPerson());
        mChoisePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String[] objects = getResources().getStringArray(R.array.persons);
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(objects));
                ChooseObjectDialogFragment chooseObjectDialogFragment = ChooseObjectDialogFragment.newInstance(list, "Persons");
                chooseObjectDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_PERSON);
                chooseObjectDialogFragment.show(fragmentManager, ChooseObjectDialogFragment.CHOISE_OBJECT);
            }
        });

        mChoisePair = (Button) v.findViewById(R.id.choose_pair);
        if (mCrime.getmPair() != 0) {
            Integer pair = mCrime.getmPair();
            String pairs = pair.toString();
            mChoisePair.setText(pairs);
        }
        mChoisePair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                String[] objects = getResources().getStringArray(R.array.pairs);
                ArrayList<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(objects));
                ChooseObjectDialogFragment chooseObjectDialogFragment = ChooseObjectDialogFragment.newInstance(list, "Pairs");
                chooseObjectDialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_PAIR);
                chooseObjectDialogFragment.show(fragmentManager, ChooseObjectDialogFragment.CHOISE_OBJECT);
            }
        });

        mChoiseButton = (Button) v.findViewById(R.id.choice_button);
//        mChoiseButton.setText(DateFormat.format("EEEE, LLL d, yyyy, HH:mm", mCrime.getDate()));
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

//        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
//        mSolvedCheckBox.setChecked(mCrime.isSolved());
//        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked) {
//                mCrime.setSolved(isCheked);
//                mCallbacks.OnCrimeUpdate(mCrime);
//            }
//        });

//        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
//        mPhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
//                //startActivity(i);
//                startActivityForResult(i, REQUEST_PHOTO);
//            }
//        });
//
//        mPhotoView = (ImageView) v.findViewById(R.id.crime_imageView);
//        registerForContextMenu(mPhotoView);
//        mPhotoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Photo photo = mCrime.getPhoto();
//                if (photo == null)
//                    return;
//
//                FragmentManager fm = getActivity()
//                        .getSupportFragmentManager();
////                String path = getActivity()
////                        .getExternalFilesDir(photo.getFileName()).getAbsolutePath();
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() +
//                        "/Android/data/com.android.huminskiy1325.criminalintent/files/" + photo.getFileName();
//                ImageFragment.newInstance(path)
//                        .show(fm, DIALOG_IMAGE);
//            }
//        });

//        Button reportButton = (Button) v.findViewById(R.id.crime_reportButton);
//        reportButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
//                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
//                intent = Intent.createChooser(intent, getString(R.string.send_report));
//                startActivity(intent);
//            }
//        });

//        mSuspectButton = (Button) v.findViewById(R.id.crime_suspectButton);
//        mSuspectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent, REQUEST_CONTACT);
//            }
//        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        mCallButton = (Button) v.findViewById(R.id.crime_callButton);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:12345"));
                startActivity(intent);
            }
        });

        return v;
    }

    private String getCrimeReport() {
        String solvedString = null;
//        if (mCrime.isSolved()) {
//            solvedString = getString(R.string.crime_report_solved);
//        } else {
//            solvedString = getString(R.string.crime_report_unsolved);
//        }
        String dateFormat = "EEEE, LLL d, yyyy, HH:mm";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }

//    private void showPhoto() {
//        // Назначение изображения, полученного на основе фотографии
//        Photo photo = mCrime.getPhoto();
//        BitmapDrawable bitmapDrawable = null;
//        if (photo != null) {
////            String path = getActivity().getExternalFilesDir(photo.getFileName()).getAbsolutePath();
//            String path = Environment.getExternalStorageDirectory().getAbsolutePath().toString() +
//                    "/Android/data/com.android.huminskiy1325.criminalintent/files/" + photo.getFileName();
//            bitmapDrawable = PictureUtils.getScaledDrawable(getActivity(), path);
//        }
//        mPhotoView.setImageDrawable(bitmapDrawable);
//    }


}
