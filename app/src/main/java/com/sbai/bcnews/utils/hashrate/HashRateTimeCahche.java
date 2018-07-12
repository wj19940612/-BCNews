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

    private static OnlineTime sOnlineTimeCache;

    public static void updateOnlineTime(OnlineTime onlineTime) {
        sOnlineTimeCache = onlineTime;
        String onlineTimeString = sGson.toJson(sOnlineTimeCache);
        Preference.get().setOnlineTime(onlineTimeString);
    }

    public static OnlineTime getOnlineTime(int userId) {
        if (sOnlineTimeCache == null) {
            readFromPreference();
        }
        if (sOnlineTimeCache.getUserId() == userId) {
            return sOnlineTimeCache;
        }
        sOnlineTimeCache.setUserId(userId);
        sOnlineTimeCache.setDay(SysTime.getSysTime().getSystemTimestamp());
        sOnlineTimeCache.setOnlineTime(0);
        return sOnlineTimeCache;
    }

    private static void readFromPreference() {
        String onlineTime = Preference.get().getOnlineTime();
        if (!TextUtils.isEmpty(onlineTime)) {
            Type type = new TypeToken<OnlineTime>() {
            }.getType();
            try {
                sOnlineTimeCache = sGson.fromJson(onlineTime, type);
            } catch (JsonSyntaxException exception) {
                sOnlineTimeCache = new OnlineTime();
            }
        } else {
            sOnlineTimeCache = new OnlineTime();
        }
    }
}
