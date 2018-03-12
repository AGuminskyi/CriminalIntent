package com.android.huminskiy1325.criminalintent;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;


public class Crime {

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON__FILENAME = "filename";
    private static final String JSON__SUSPECT = "suspect";
    private static final String JSON__OBJECT = "object";
    private static final String JSON__PAIR = "pair";
    private static final String JSON__PERSON = "person";

    private UUID mID;
    private String mTitle;
    private String mSuspect;
    private Date mDate;
    private boolean mSolved;
    private Photo mPhoto;
    private String mObject;
    private int mPair = 0;
    private String mPerson;

    Crime() {
        mID = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(JSONObject jsonObject) throws JSONException {
        mID = UUID.fromString(jsonObject.getString(JSON_ID));
        mTitle = jsonObject.getString(JSON_TITLE);
        mDate = new Date(jsonObject.getLong(JSON_DATE));
        mSolved = jsonObject.getBoolean(JSON_SOLVED);
        mObject = jsonObject.getString(JSON__OBJECT);
        mPair = jsonObject.getInt(JSON__PAIR);
        mPerson = jsonObject.getString(JSON__PERSON);

        if (jsonObject.has(JSON__FILENAME)) {
            mPhoto = new Photo(jsonObject);
        }
        if (jsonObject.has(JSON__SUSPECT)) {
            mSuspect = jsonObject.getString(JSON__SUSPECT);
        }
    }

    public String getmObject() {
        return mObject;
    }

    public void setmObject(String mObject) {
        this.mObject = mObject;
    }

    public String getmPerson() {
        return mPerson;
    }

    public void setmPerson(String mPerson) {
        this.mPerson = mPerson;
    }

    public int getmPair() {
        return mPair;
    }

    public void setmPair(int mPair) {
        this.mPair = mPair;
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

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, mID.toString());
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_DATE, mDate.getTime());
        jsonObject.put(JSON_SOLVED, mSolved);
        jsonObject.put(JSON__PAIR, mPair);
        jsonObject.put(JSON__OBJECT, mObject);
        jsonObject.put(JSON__PERSON, mPerson);
        if (mPhoto != null) {
//            jsonObject.put(JSON_PHOTO, mPhoto.toJSON());
            jsonObject = mPhoto.toJSON(jsonObject);
            jsonObject.put(JSON__SUSPECT,mSuspect);
        }
        return jsonObject;
    }

    public Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Photo mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String mSuspect) {
        this.mSuspect = mSuspect;
    }

    @Override
    public String toString() {
        return mTitle;
    }

}

