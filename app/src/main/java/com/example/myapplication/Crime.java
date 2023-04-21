package com.example.myapplication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Crime {

    private int mId;
    private String mUUID = UUID.randomUUID().toString();
    private String mTitle = "BRAK";
    private String mDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private String mDetails = "NOPEE";
    private String mRecord_1 = "2000-12-12 12:12:12 15 15 15";
    private String mRecord_2 = "2000-12-12 12:12:12 15 15 15";
    private String mRecord_3 = "2000-12-12 12:12:12 15 15 15";
    private String mRecord_4 = "2000-12-12 12:12:12 15 15 15";
    private String mRecord_5 = "2000-12-12 12:12:12 15 15 15";


    public void setId(int mId) {
        this.mId = mId;
    }
    public int getId() {
        return mId;
    }

    public void setUUID(String mUUID) {
        this.mUUID = mUUID;
    }
    public String getUUID() {
        return mUUID;
    }

    public void setTitle(String s) {
        this.mTitle = s;
    }
    public String getTitle() { return this.mTitle; }

    public void setDate(String S) {this.mDate = S; }
    public String getDate() { return mDate; }

    public void setDetails(String S) {this.mDetails = S; }
    public String getDetails() { return mDetails; }

    public void setRecord_1(String S) {this.mRecord_1 = S; }
    public String getRecord_1() { return mRecord_1; }

    public void setRecord_2(String S) {this.mRecord_2 = S; }
    public String getRecord_2() { return mRecord_2; }

    public void setRecord_3(String S) {this.mRecord_3 = S; }
    public String getRecord_3() { return mRecord_3; }

    public void setRecord_4(String S) {this.mRecord_4 = S; }
    public String getRecord_4() { return mRecord_4; }

    public void setRecord_5(String S) {this.mRecord_5 = S; }
    public String getRecord_5() { return mRecord_5; }
}
