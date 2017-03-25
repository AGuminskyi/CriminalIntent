package com.android.huminskiy1325.criminalintent;


import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by cubru on 15.07.2016.
 */
public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_PHOTO = "photo";

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;

    Crime(){
        mID = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(JSONObject jsonObject) throws  JSONException{
        mID = UUID.fromString(jsonObject.getString(JSON_ID));
        mTitle = jsonObject.getString(JSON_TITLE);
        mDate = new Date(jsonObject.getLong(JSON_DATE));
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        if(jsonObject.has(JSON_PHOTO)){
            mPhoto = new Photo(jsonObject.getString(JSON_PHOTO));
        }
    }

    public UUID getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, mID.toString());
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_DATE, mDate.getTime());
        jsonObject.put(JSON_SOLVED, mSolved);
        if(mPhoto != null){
            jsonObject.put(JSON_PHOTO, mPhoto.toJSON());
        }
        return jsonObject;
    }

    public Photo getPhoto(){
        return mPhoto;
    }

    public void setPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    @Override
    public String toString() {
        return mTitle;
    }

}

