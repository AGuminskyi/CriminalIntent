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

    private UUID mID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;

    Crime(){
        mID = UUID.randomUUID();
        mDate = new Date();
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

    public boolean isSolved() {
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
        return jsonObject;
    }

    public Crime(JSONObject jsonObject) throws  JSONException{
        mID = UUID.fromString(jsonObject.getString(JSON_ID));
        mTitle = jsonObject.getString(JSON_TITLE);
        mDate = new Date(jsonObject.getLong(JSON_DATE));
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
    }

    @Override
    public String toString() {
        return mTitle;
    }

}

