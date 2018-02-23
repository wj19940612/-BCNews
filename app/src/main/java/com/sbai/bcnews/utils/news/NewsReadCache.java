package com.sbai.bcnews.utils.news;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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

    private static Queue<String> sNewsReadMapCache;

    //记录已读
    public static void markNewsRead(NewsDetail newsDetail) {
        if (sNewsReadMapCache == null) {
            readFromPreference();
        }

        if (sNewsReadMapCache.size() > TOTAL_NEWS) {
            sNewsReadMapCache.poll();
        }
        sNewsReadMapCache.add(newsDetail.getId());
        String news = sGson.toJson(sNewsReadMapCache);
        Preference.get().setNewsRead(news);
    }

    public static boolean isRead(String id){
        if (sNewsReadMapCache == null) {
            readFromPreference();
        }
        return sNewsReadMapCache.contains(id);
    }

    private static void readFromPreference() {
        String news = Preference.get().getNewsRead();
        if (!TextUtils.isEmpty(news) && isJsonArray(news)) {
            Type type = new TypeToken<LinkedList<String>>() {
            }.getType();
            try {
                sNewsReadMapCache = sGson.fromJson(news, type);
            }catch (JsonSyntaxException exception){
                sNewsReadMapCache = new LinkedList<>();
            }
        } else {
            sNewsReadMapCache = new LinkedList<>();
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
}
