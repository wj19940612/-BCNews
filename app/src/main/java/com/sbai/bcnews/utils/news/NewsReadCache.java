package com.sbai.bcnews.utils.news;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.NewsDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Administrator on 2018\1\30 0030.
 */

public class NewsReadCache {
    private static final int TOTAL_NEWS = 200;
    private static Gson sGson = new Gson();

    private static LinkedHashMap<String, Boolean> sNewsReadMapCache;

    //记录已读
    public static void markNewsRead(NewsDetail newsDetail) {
        if (sNewsReadMapCache == null) {
            readFromPreference();
        }

        sNewsReadMapCache.put(newsDetail.getId(), true);
        String news = sGson.toJson(sNewsReadMapCache);
        Preference.get().setNewsRead(news);
    }

    //过滤列表已读状态
    public static List<NewsDetail> filterReadCache(List<NewsDetail> newsDetails) {
        if (newsDetails == null || newsDetails.size() == 0) {
            return newsDetails;
        }
        if (sNewsReadMapCache == null) {
            readFromPreference();
        }
        for (NewsDetail newsDetail : newsDetails) {
            Boolean read = sNewsReadMapCache.get(newsDetail.getId());
            newsDetail.setRead(read == null ? false : read);
        }
        return newsDetails;
    }

    private static void readFromPreference() {
        String news = Preference.get().getNewsRead();
        if (!TextUtils.isEmpty(news)) {
            Type type = new TypeToken<MaxLinkedHashMap<String, Boolean>>() {
            }.getType();
            sNewsReadMapCache = sGson.fromJson(news, type);
        } else {
            sNewsReadMapCache = new MaxLinkedHashMap<String, Boolean>();
        }
    }

    private static boolean isJsonArray(String news) {
        boolean result = false;
        try {
            Object json = new JSONTokener(news).nextValue();
            if (json instanceof JSONArray) {
                result = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public static class MaxLinkedHashMap<T, E> extends LinkedHashMap<T, E> {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<T, E> eldest) {
            //每当调用myMap.put()的时候，就会自动判断是否个数已经超过maximumSize，如果超过就删掉最旧的那条
            return size() > TOTAL_NEWS;
        }
    }
}
