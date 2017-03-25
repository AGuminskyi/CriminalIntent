package com.android.huminskiy1325.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by cubru on 02.02.2017.
 */

public class CriminalIntentJSONSerializer extends Object {
    private Context mContext;
    private String mFileName;

    public CriminalIntentJSONSerializer(Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public void saveCrimes(ArrayList<Crime> crimes) throws IOException, JSONException {
        // build an array in JSON
        JSONArray array = new JSONArray();
        for (Crime crime : crimes)
            array.put(crime.toJSON());

        // write the file to disk
        Writer writer = null;
        try {
            OutputStream out = null;
            if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED))
                out = mContext.openFileOutput(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Android/data/com.android.huminskiy1325.criminalintent/" + mFileName, Context.MODE_PRIVATE);
            else
                out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            InputStream in = null;
            if (Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED))
                in = mContext.openFileInput(Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Android/data/com.android.huminskiy1325.criminalintent/" + mFileName);
            else
                in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            // Разбор JSON с использованием JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            // Построение массива объектов Crime по данным JSONObject
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException ignored) {

        } finally {
            if (reader != null)
                reader.close();
        }
        return crimes;
    }

}
