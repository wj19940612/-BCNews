package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 登录后的用户信息数据
 */
public class UserInfo implements Parcelable {

    private String userPortrait;

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    public String getUserPhone() {
        //TODO 需要字段
        return null;
    }

    protected UserInfo(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
}


