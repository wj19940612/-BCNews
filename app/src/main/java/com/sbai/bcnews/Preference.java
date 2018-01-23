package com.sbai.bcnews;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.AppInfo;

import java.util.ArrayList;
import java.util.List;


public class Preference {
    private static final String SHARED_PREFERENCES_NAME = BuildConfig.FLAVOR + "_prefs";
    //更新引导页 GUIDE_UPDATE_VERSION增加1
    private static final int GUIDE_UPDATE_VERSION = 0;

    //上次打开页面超过当前页面多久提交一次  60*60*1000
    private static final int UPDATE_OPEN_APP_TIME = 60 * 60 * 1000;

    interface Key {
        String FOREGROUND = "foreground";
        String SERVICE_PHONE = "servicePhone";
        String SERVICE_QQ = "serviceQQ";
        String USER_JSON = "userJson";
        String STOCK_USER_JSON = "stockUserJson";
        String PUSH_CLIENT_ID = "PUSH_CLIENT_ID";
        String SERVER_IP_PORT = "server_ip_port";
        String SERVER_TIME = "server_time";
        String AUTHORIZATION_LOGIN_TIME = "authorization_login_time";
        String IS_FIRST_WITH_DRAW = "is_first_with_draw";
        String USER_HAS_SafePass = "user_has_safe_pass";
        String MISS_TALK_ANSWERS = "miss_talk_answers";
        String USER_LOOK_DETAIL = "user_look_detail";
        String IS_FIRST_TRAIN = "IS_FIRST_TRAIN";
        String IS_GUIDE_UPDATE = "IS_GUIDE_UPDATE";
        String STUDY_OPTION = "study_option";
        String MY_STUDY = "my_study";
        String TRAINING_SUBMITS = "training_submits";
        String SERVICE_CONNECT_WAY = "service_connect_way";
        String FIRST_OPEN_WALLET_PAGE = "first_open_wallet_page";
        String SHOW_REGISTER_INVITE = "show_register_invite";
        String SHOW_BIND_WECHAT = "show_bind_wechat";
        String FIRST_OPEN_APP = "first_open_app";
        String UPDATE_OPEN_APP_TIME = "update_open_app_time";
        String FIRST_LOGIN = "first_login";
    }

    private static Preference sInstance;

    private SharedPreferences mPrefs;

    public static Preference get() {
        if (sInstance == null) {
            sInstance = new Preference();
        }
        return sInstance;
    }

    private Preference() {
        mPrefs = App.getAppContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        return mPrefs.edit();
    }

    private void apply(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    private void apply(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    private void apply(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    private void apply(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    private void apply(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public void setForeground(boolean foreground) {
        apply(Key.FOREGROUND, foreground);
    }

    public boolean isForeground() {
        return mPrefs.getBoolean(Key.FOREGROUND, false);
    }

    public void setMarketServerIpPort(String ipPortStr) {
        apply(Key.SERVER_IP_PORT, ipPortStr);
    }

    public String getMarketServerIpPort() {
        return mPrefs.getString(Key.SERVER_IP_PORT, null);
    }

    public void setTimestamp(String key, long timestamp) {
        apply(key, timestamp);
    }

    public void setUserJson(String userJson) {
        apply(Key.USER_JSON, userJson);
    }

    public String getUserJson() {
        return mPrefs.getString(Key.USER_JSON, null);
    }

    public void setStockUserJson(String stockUserJson) {
        apply(Key.STOCK_USER_JSON, stockUserJson);
    }

    public String getStockUserJson() {
        return mPrefs.getString(Key.STOCK_USER_JSON, null);
    }

    public void setPushClientId(String clientId) {
        apply(Key.PUSH_CLIENT_ID, clientId);
    }

    public String getPushClientId() {
        return mPrefs.getString(Key.PUSH_CLIENT_ID, "");
    }

    public String getServicePhone() {
        return mPrefs.getString(Key.SERVICE_PHONE, null);
    }

    public void setServiceQQ(String serviceQQ) {
        apply(Key.SERVICE_QQ, serviceQQ);
    }

    public String getServiceQQ() {
        return mPrefs.getString(Key.SERVICE_QQ, null);
    }

    public long getTimestamp(String key) {
        return mPrefs.getLong(key, 0);
    }

    public void setServerTime(long serverTime) {
        apply(Key.SERVER_TIME, serverTime);
    }

    public long getServerTime() {
        return mPrefs.getLong(Key.SERVER_TIME, 0);
    }

    public void setAuthorizationTime(long serverTime) {
        apply(Key.AUTHORIZATION_LOGIN_TIME, serverTime);
    }

    public long getAuthorizationTime() {
        return mPrefs.getLong(Key.AUTHORIZATION_LOGIN_TIME, 0);
    }


    public boolean isFirstWithDraw(String key) {
        return mPrefs.getBoolean(key + Key.IS_FIRST_WITH_DRAW, true);
    }

    public void setIsFirstWithDraw(String key, boolean isFirstWithDraw) {
        apply(key + Key.IS_FIRST_WITH_DRAW, isFirstWithDraw);
    }

    public boolean hasUserSetSafePass(String key) {
        return mPrefs.getBoolean(key + Key.USER_HAS_SafePass, false);
    }

    public void setUserSetSafePass(String key, boolean userHasSetPass) {
        apply(key + Key.USER_HAS_SafePass, userHasSetPass);
    }


    public void setNeedUpdateOpenAppTime(long systemTime) {
        apply(Key.UPDATE_OPEN_APP_TIME, systemTime);
    }


    public boolean isFirstLogin() {
        return mPrefs.getBoolean(Key.FIRST_LOGIN, true);
    }

    public void setFirstLogin(boolean isFirstLogin) {
        apply(Key.FIRST_LOGIN, isFirstLogin);
    }

}
