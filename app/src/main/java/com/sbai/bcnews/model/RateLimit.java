package com.sbai.bcnews.model;

public class RateLimit {

    //算力当时是否达到上限

    private long day;
    private boolean isLimit;//是否已达当日上限
    private int mUserId;

    public RateLimit(int userId) {
        mUserId = userId;
    }

    public RateLimit() {
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public boolean isLimit() {
        return isLimit;
    }

    public void setLimit(boolean limit) {
        isLimit = limit;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
