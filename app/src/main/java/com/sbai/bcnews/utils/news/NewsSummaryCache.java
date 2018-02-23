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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by Administrator on 2018\1\30 0030.
 */

public class NewsSummaryCache {
    private static final int TOTAL_SUMMARY = 100;
    private static final int NEED_NUM = 20;

    private static HashMap<String, ArrayList<NewsDetail>> sNewsSummaryCache;

    private static Gson sGson = new Gson();

    public static void markNewsSummarys(String channel, List<NewsDetail> newsDetails) {
        if (sNewsSummaryCache == null) {
            readFromPreference();
        }

        ArrayList<NewsDetail> newsDetailList = sNewsSummaryCache.get(channel);
        if (newsDetailList != null) {
            if (newsDetailList.size() >= TOTAL_SUMMARY) {
                newsDetailList.clear();
            }
            newsDetailList.addAll(newsDetails);
        } else {
            newsDetailList = new ArrayList<NewsDetail>();
            newsDetailList.addAll(newsDetailList);
        }
        sNewsSummaryCache.put(channel, newsDetailList);
        String news = sGson.toJson(sNewsSummaryCache);
        Preference.get().setNewsSummary(news);
    }

    public static List<NewsDetail> getNewsSummaryCache(String channel) {
        if (sNewsSummaryCache == null) {
            readFromPreference();
        }
        ArrayList<NewsDetail> newsDetails = sNewsSummaryCache.get(channel);
        if (newsDetails == null || newsDetails.size() == 0) {
            return null;
        }
        if (NEED_NUM > newsDetails.size()) {
            return newsDetails.subList(0, newsDetails.size() - 1);
        }
        return newsDetails.subList(newsDetails.size() - NEED_NUM, newsDetails.size() - 1);
    }

    private static void readFromPreference() {
        String news = Preference.get().getNewsSummary();
        if (!TextUtils.isEmpty(news)) {
            Type type = new TypeToken<HashMap<String, ArrayList<NewsDetail>>>() {
            }.getType();
            try {
                sNewsSummaryCache = sGson.fromJson(news, type);
            } catch (JsonSyntaxException exception) {
                //这里数据类型和老的发生更改了
                sNewsSummaryCache = new HashMap<String, ArrayList<NewsDetail>>();
            }
        } else {
            sNewsSummaryCache = new HashMap<String, ArrayList<NewsDetail>>();
        }
    }
}
