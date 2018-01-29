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
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class NewsCache {
    private static final int TOTAL_NEWS = 200;

    private static Queue<NewsDetail> sNewsCache;
    private static Gson sGson = new Gson();

    public static void markNews(NewsDetail newsDetail) {
        readFromPreference();

        if (sNewsCache.size() >= TOTAL_NEWS) {
            sNewsCache.poll();
        }
        sNewsCache.add(newsDetail);
        String news = sGson.toJson(sNewsCache);
        Preference.get().setNewsRead(news);
    }

    private static void readFromPreference() {
        String news = Preference.get().getNewsRead();
        if (!TextUtils.isEmpty(news) && isJsonArray(news)) {
            Type type = new TypeToken<LinkedList<NewsDetail>>() {
            }.getType();
            sNewsCache = sGson.fromJson(news, type);
        } else {
            sNewsCache = new LinkedList<>();
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

    //插入或替换缓存(替换指的是阅读记忆的阅读高度)
    public static void insertOrReplaceNews(NewsDetail newsDetail) {
        if (sNewsCache == null) {
            readFromPreference();
        }
        boolean isContains = false;
        for (NewsDetail detail : sNewsCache) {
            if (detail.getId().equals(newsDetail.getId())) {
                detail.setReadHeight(newsDetail.getReadHeight());
                isContains = true;
                break;
            }
        }
        //插入
        if (!isContains) {
            if (sNewsCache.size() >= TOTAL_NEWS) {
                sNewsCache.poll();
            }
            sNewsCache.add(newsDetail);
        }

        String news = sGson.toJson(sNewsCache);
        Preference.get().setNewsRead(news);
    }

    //读取缓存
    public static NewsDetail getCahceNewsForId(String id) {
        if (sNewsCache == null) {
            readFromPreference();
        }
        for (NewsDetail detail : sNewsCache) {
            if (detail.getId().equals(id)) {
                return detail;
            }
        }
        return null;
    }

    /**
     * @param location     从第几个cache开始获取
     * @param requireCount 获取多少个news
     * @return
     */
    public static List<NewsDetail> getCahceNews(int location, int requireCount) {
        if (sNewsCache == null) {
            readFromPreference();
        }
        int i = 0;
        List<NewsDetail> newsDetails = new ArrayList<>();
        for (NewsDetail detail : sNewsCache) {
            if (i > location && i <= requireCount + location) {
                newsDetails.add(detail);
            }
            if (i > location + requireCount) {
                break;
            }
            i++;
        }
        return newsDetails;
    }
}
