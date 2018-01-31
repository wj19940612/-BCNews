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
import java.util.Map;
import java.util.Queue;

/**
 * Created by Administrator on 2018\1\30 0030.
 */

public class NewsSummaryCache {
    private static final int TOTAL_NEWSUMMARY = 100;
    private static final int NEED_NUM = 20;
    private static LinkedList<NewsDetail> sNewsSummaryCache;

    private static Gson sGson = new Gson();

    public static void markNewsSummarys(List<NewsDetail> newsDetails) {
        if (sNewsSummaryCache == null) {
            readFromPreference();
        }

        if (sNewsSummaryCache.size() >= TOTAL_NEWSUMMARY) {
            sNewsSummaryCache.clear();
        }
        sNewsSummaryCache.addAll(newsDetails);
        String news = sGson.toJson(sNewsSummaryCache);
        Preference.get().setNewsSummary(news);
    }

    public static List<NewsDetail> getNewsSummaryCache() {
        if (sNewsSummaryCache == null) {
            readFromPreference();
        }
        if (sNewsSummaryCache.size() == 0) {
            return sNewsSummaryCache;
        }
        if (NEED_NUM > sNewsSummaryCache.size()) {
            return sNewsSummaryCache.subList(0, sNewsSummaryCache.size() - 1);
        }
        return sNewsSummaryCache.subList(sNewsSummaryCache.size() - NEED_NUM, sNewsSummaryCache.size() - 1);
    }

    private static void readFromPreference() {
        String news = Preference.get().getNewsSummary();
        if (!TextUtils.isEmpty(news) && isJsonArray(news)) {
            Type type = new TypeToken<LinkedList<NewsDetail>>() {
            }.getType();
            sNewsSummaryCache = sGson.fromJson(news, type);
        } else {
            sNewsSummaryCache = new LinkedList<>();
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
