package com.android.huminskiy1325.criminalintent;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class CrimeLab {

    private static final String TAG = "CrimeLab";
    private static final String FileName = "crimes.json";

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    private ArrayList<Crime> mCrimes;
    private CriminalIntentJSONSerializer mSerializer;

    private CrimeLab(Context appContext) {
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FileName);
        try {
            mCrimes = mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.d(TAG,"Error loading crimes " + e);
            Toast.makeText(mAppContext,"Error loading crimes", Toast.LENGTH_SHORT);
        }

       // mCrimes = new ArrayList<Crime>();

//        for(int i=0; i<100;i++){
//            Crime crime = new Crime();
//            crime.setTitle("Crime #" + i);
//            crime.setSolved(i%2 == 0);
//                mCrimes.add(crime);
//            }
    }

    public void addCrime(Crime crime){
        mCrimes.add(crime);
    }

    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG, "Crimes saves to file");
            Toast.makeText(mAppContext,"Crimes saves to file", Toast.LENGTH_SHORT);
            return true;
        }catch (Exception e){
            Log.e(TAG, "Error saving crimes: " + e);
            Toast.makeText(mAppContext,"Error saving crimes", Toast.LENGTH_SHORT);
            return false;
        }
    }

    public static CrimeLab get(Context c) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(c.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes)
            if(crime.getID().equals(id))
                return crime;

        return null;
    }
}
