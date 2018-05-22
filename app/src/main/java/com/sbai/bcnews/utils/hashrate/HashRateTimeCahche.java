package com.sbai.bcnews.utils.hashrate;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.OnlineTime;
import com.sbai.bcnews.model.SysTime;

import java.lang.reflect.Type;
import java.util.LinkedList;

public class HashRateTimeCahche {
    private static Gson sGson = new Gson();

    private static OnlineTime mOnlineTimeCache;

    public static void updateOnlineTime(OnlineTime onlineTime) {
        mOnlineTimeCache = onlineTime;
        String onlineTimeString = sGson.toJson(mOnlineTimeCache);
        Preference.get().setOnlineTime(onlineTimeString);
    }

    public static OnlineTime getOnlineTime(int userId) {
        if (mOnlineTimeCache == null) {
            readFromPreference();
        }
        if (mOnlineTimeCache.getUserId() == userId) {
            return mOnlineTimeCache;
        }
        mOnlineTimeCache.setUserId(userId);
        mOnlineTimeCache.setDay(SysTime.getSysTime().getSystemTimestamp());
        return mOnlineTimeCache;
    }

    private static void readFromPreference() {
        String onlineTime = Preference.get().getOnlineTime();
        if (!TextUtils.isEmpty(onlineTime)) {
            Type type = new TypeToken<OnlineTime>() {
            }.getType();
            try {
                mOnlineTimeCache = sGson.fromJson(onlineTime, type);
            } catch (JsonSyntaxException exception) {
                mOnlineTimeCache = new OnlineTime();
            }
        } else {
            mOnlineTimeCache = new OnlineTime();
        }
    }
}
