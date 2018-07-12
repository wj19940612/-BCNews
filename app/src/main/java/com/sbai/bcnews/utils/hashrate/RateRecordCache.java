package com.sbai.bcnews.utils.hashrate;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.OnlineTime;
import com.sbai.bcnews.model.RateLimit;
import com.sbai.bcnews.utils.DateUtil;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class RateRecordCache {

    private static final int TOTAL = 200;
    private static Gson sGson = new Gson();
    private static MaxLinkedHashMap<Integer, RateLimit> sRateLimitMap;

    public static void setRateLimit() {
        if (!LocalUser.getUser().isLogin()) {
            return;
        }
        if (sRateLimitMap == null) {
            readFromPreference();
        }
        int userId = LocalUser.getUser().getUserInfo().getId();
        RateLimit cacheLimit = sRateLimitMap.get(userId);
        if (cacheLimit == null) {
            cacheLimit = new RateLimit(userId);
        }
        cacheLimit.setDay(System.currentTimeMillis());
        cacheLimit.setLimit(true);
        sRateLimitMap.put(userId, cacheLimit);
        String rateLimitStr = sGson.toJson(sRateLimitMap);
        Preference.get().setRateLimit(rateLimitStr);
    }

    public static boolean isRateLimit() {
        if (!LocalUser.getUser().isLogin()) {
            return false;
        }
        if (sRateLimitMap == null) {
            readFromPreference();
        }

        int userId = LocalUser.getUser().getUserInfo().getId();
        RateLimit cacheLimit = sRateLimitMap.get(userId);
        if (cacheLimit == null) {
            return false;
        } else {
            return (DateUtil.isInThisDay(System.currentTimeMillis(), cacheLimit.getDay()) && cacheLimit.isLimit());
        }
    }

    private static void readFromPreference() {
        String rateLimit = Preference.get().getRateLimit();
        if (!TextUtils.isEmpty(rateLimit)) {
            Type type = new TypeToken<MaxLinkedHashMap<Integer, RateLimit>>() {
            }.getType();
            try {
                sRateLimitMap = sGson.fromJson(rateLimit, type);
            } catch (JsonSyntaxException exception) {
                sRateLimitMap = new MaxLinkedHashMap<Integer, RateLimit>();
            }
        } else {
            sRateLimitMap = new MaxLinkedHashMap<Integer, RateLimit>();
        }
    }


    public static class MaxLinkedHashMap<T, E> extends LinkedHashMap<T, E> {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<T, E> eldest) {
            //每当调用myMap.put()的时候，就会自动判断是否个数已经超过maximumSize，如果超过就删掉最旧的那条
            return size() > TOTAL;
        }
    }


}
