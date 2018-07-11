package com.sbai.bcnews;

import android.content.Context;
import android.content.SharedPreferences;

import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.TextSizeModel;


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
        String FIRST_OPEN_APP = "first_open";
        String UPDATE_OPEN_APP_TIME = "update_open_app_time";
        String FIRST_LOGIN = "first_login";
        String NEWS_DETAIL = "news_detail";
        String NEWS_SUMMARY = "news_summary";
        String NEWS_READ = "news_read";
        String NEWS_BIG_IMAGE = "news_big_image";
        String NEWS_CHANNEL = "news_channel";
        String NEWS_PRAISE = "news_praise";
        String FIRST_PRAISE = "first_praise";
        String WEB_TEXT_SIZE = "text_size";
        String FRONT_TIME = "front_time";
        String READ_TIME = "read_time";
        String TODAY_FIRST_OPEN_APP = "first_open_app";
        String SEARCH_HISTORY = "search_history";
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

    public void setNewsDetail(String newsRead) {
        String key = Key.NEWS_DETAIL;
        apply(key, newsRead);
    }

    public String getNewsDetail() {
        String key = Key.NEWS_DETAIL;
        return mPrefs.getString(key, null);
    }

    public void setNewsSummary(String newsSummary) {
        String key = Key.NEWS_SUMMARY;
        apply(key, newsSummary);
    }

    public String getNewsSummary() {
        String key = Key.NEWS_SUMMARY;
        return mPrefs.getString(key, null);
    }


    public void setNewsRead(String newsRead) {
        String key = Key.NEWS_READ;
        apply(key, newsRead);
    }

    public String getNewsRead() {
        String key = Key.NEWS_READ;
        return mPrefs.getString(key, null);
    }

    public void setBigImage(String img) {
        String key = Key.NEWS_BIG_IMAGE;
        apply(key, img);
    }

    public String getBigImage() {
        String key = Key.NEWS_BIG_IMAGE;
        return mPrefs.getString(key, null);
    }

    public void setChannelCache(String channelCache) {
        String key = Key.NEWS_CHANNEL;
        apply(key, channelCache);
    }

    public String getChannelCache() {
        String key = Key.NEWS_CHANNEL;
        return mPrefs.getString(key, null);
    }

    public void setPraiseCache(String praiseCache) {
        String key = Key.NEWS_PRAISE;
        apply(key, praiseCache);
    }

    public String getPraiseCahce() {
        String key = Key.NEWS_PRAISE;
        return mPrefs.getString(key, null);
    }

    public boolean isFirstPraise() {
        String key = Key.FIRST_PRAISE;
        return mPrefs.getBoolean(key, true);
    }

    public void setFirstPraise(boolean firstPraise) {
        String key = Key.FIRST_PRAISE;
        apply(key, firstPraise);
    }

    public int getLocalWebTextSize() {
        String key = Key.WEB_TEXT_SIZE;
        return mPrefs.getInt(key, TextSizeModel.NORMAL);
    }

    public void setLocalWebTextSize(int textSize) {
        String key = Key.WEB_TEXT_SIZE;
        apply(key, textSize);
    }

    public String getOnlineTime() {
        String key = Key.FRONT_TIME;
        return mPrefs.getString(key, null);
    }

    public void setOnlineTime(String onlineTime) {
        String key = Key.FRONT_TIME;
        apply(key, onlineTime);
    }

    public int getLastReadTime() {
        String key = Key.READ_TIME;
        return mPrefs.getInt(key, TextSizeModel.NORMAL);
    }

    public void setReadTime(int readTime) {
        String key = Key.READ_TIME;
        apply(key, readTime);
    }

    public boolean canShowStartPage() {
        long firstOpenTime = mPrefs.getLong(Key.TODAY_FIRST_OPEN_APP, 0);
        long systemTime = System.currentTimeMillis();
        return firstOpenTime == 0 || !DateUtil.isToday(firstOpenTime, systemTime);
    }

    public void setTodayFirstOpenAppTime(long time) {
        apply(Key.TODAY_FIRST_OPEN_APP, time);
    }

    public boolean isFirstOpenApp() {
        String key = Key.FIRST_OPEN_APP;
        return mPrefs.getBoolean(key, true);
    }

    public void setNoFirstOpenApp() {
        apply(Key.FIRST_OPEN_APP, false);
    }

    public void setHistorySearch(String values) {
        apply(Key.SEARCH_HISTORY, values);
    }

    public String getHistorySearch() {
        return mPrefs.getString(Key.SEARCH_HISTORY, null);
    }
}
