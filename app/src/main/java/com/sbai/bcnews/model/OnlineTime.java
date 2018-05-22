package com.sbai.bcnews.model;

public class OnlineTime {

    public OnlineTime() {
    }

    public OnlineTime(long day, int onlineTime, int userId) {
        this.day = day;
        this.onlineTime = onlineTime;
        mUserId = userId;
    }

    private long day;
    private int onlineTime;//记录前台停留的时间，单位秒
    private int mUserId;

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public int getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(int onlineTime) {
        this.onlineTime = onlineTime;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
