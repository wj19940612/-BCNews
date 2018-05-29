package com.sbai.bcnews.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.sbai.bcnews.Preference;

public class LocalUser {
    private static LocalUser sLocalUser;

    private UserInfo mUserInfo;
    private String mPhone;

    public static LocalUser getUser() {
        if (sLocalUser == null) {
            sLocalUser = loadFromPreference();
        }
        return sLocalUser;
    }

    private static LocalUser loadFromPreference() {
        String userJson = Preference.get().getUserJson();
        if (!TextUtils.isEmpty(userJson)) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, LocalUser.class);
        }

        return new LocalUser();
    }

    public void saveToPreference() {
        String userJson = new Gson().toJson(this);
        Preference.get().setUserJson(userJson);
    }

    public void setUserInfo(UserInfo userInfo, String phone) {
        mUserInfo = userInfo;
        mPhone = phone;
        saveToPreference();
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        saveToPreference();
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public String getPhone() {
        return mPhone;
    }

    public boolean isLogin() {
        return mUserInfo != null;
    }

    public void logout() {
        mUserInfo = null;
        Preference.get().setReadTime(0);
        saveToPreference();
    }

    @Override
    public String toString() {
        return "LocalUser{" +
                "mUserInfo=" + mUserInfo +
                ", mPhone='" + mPhone + '\'' +
                '}';
    }
}
