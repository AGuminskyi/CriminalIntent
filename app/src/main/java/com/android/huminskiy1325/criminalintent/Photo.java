package com.android.huminskiy1325.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cubru on 22.03.2017.
 */

public class Photo extends Object {
    private static final String JSON_FILENAME = "filename";

    private String mFileName;

    public Photo(String mFileName) {
        this.mFileName = mFileName;
    }

//    public Photo(JSONObject jsonObject) throws JSONException{
//        mFileName = jsonObject.getString(JSON_FILENAME);
//    }
//
//    public JSONObject toJSON() throws JSONException{
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put(JSON_FILENAME, mFileName);
//        return jsonObject;
//    }

    public Photo(JSONObject jsonObject) throws JSONException{
        mFileName = jsonObject.getString(JSON_FILENAME);
    }

    public JSONObject toJSON(JSONObject jsonObject) throws JSONException{
        jsonObject.put(JSON_FILENAME,mFileName);
        return jsonObject;
    }

    public String getFileName(){
        return mFileName;
    }
}
