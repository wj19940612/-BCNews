package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登录后的用户信息数据
 */
public class UserInfo implements Parcelable {

    private String userPortrait;

    private String userName;

    private int collectCount;

    private int readCount;

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userPortrait);
        dest.writeString(this.userName);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.userPortrait = in.readString();
        this.userName = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}


