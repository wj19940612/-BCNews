package com.sbai.bcnews.utils.news;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.NewsPraise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhang on 2018/2/8.
 */

//点赞缓存
public class NewsPraiseCache {
    private static final int TOTAL_PRAISE = 200;
    private static Gson sGson = new Gson();

    private static MaxLinkedHashMap<String, MaxLinkedHashMap<String, Boolean>> sPraiseCache;

    public static void markNewsPraise(String userId, String newsId, boolean praise) {
        if (sPraiseCache == null) {
            readFromPreference();
        }

        MaxLinkedHashMap<String, Boolean> userPraiseMap = sPraiseCache.get(userId);
        if (userPraiseMap == null) {
            userPraiseMap = new MaxLinkedHashMap<String, Boolean>();
        }
        userPraiseMap.put(newsId, praise);
        sPraiseCache.put(userId, userPraiseMap);
        String praises = sGson.toJson(sPraiseCache);
        Preference.get().setPraiseCache(praises);
    }

    private static void readFromPreference() {
        String praises = Preference.get().getPraiseCahce();
        if (!TextUtils.isEmpty(praises)) {
            Type type = new TypeToken<MaxLinkedHashMap<String, MaxLinkedHashMap<String, Boolean>>>() {
            }.getType();
            sPraiseCache = sGson.fromJson(praises, type);
        } else {
            sPraiseCache = new MaxLinkedHashMap<String, MaxLinkedHashMap<String, Boolean>>();
        }
    }

    //所有点赞状态转换成jsonString
    public static String getPraiseJsonString(String userId) {
        if (sPraiseCache == null) {
            readFromPreference();
        }
        MaxLinkedHashMap<String, Boolean> userPraiseMap = sPraiseCache.get(userId);
        if (userPraiseMap == null)
            return null;
        List<NewsPraise> newsPraiseList = new ArrayList<>();
        Iterator<Map.Entry<String, Boolean>> entries = userPraiseMap.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Boolean> entry = entries.next();
            NewsPraise newsPraise = new NewsPraise();
            newsPraise.setNewsId(entry.getKey());
            newsPraise.setPraise(entry.getValue() ? 1 : 0);
            newsPraiseList.add(newsPraise);
        }
        return sGson.toJson(newsPraiseList);
    }

    //获取本地的点赞状态
    public static boolean getPraise(String userId, String newsId) {
        if (sPraiseCache == null) {
            readFromPreference();
        }
        MaxLinkedHashMap<String, Boolean> userPraiseMap = sPraiseCache.get(userId);
        if (userPraiseMap == null)
            return false;
        Boolean praise = userPraiseMap.get(newsId);
        return praise == null ? false : praise;
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
            return size() > TOTAL_PRAISE;
        }
    }
}
