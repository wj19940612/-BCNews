package com.sbai.bcnews.utils.news;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect.MESSAGE_TYPE_READ_HISTORY;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class NewsCache {
    private static final int TOTAL_NEWS = 200;
    private static MaxLinkedHashMap<String, NewsDetail> sNewsCache;
    private static Gson sGson = new Gson();


    private static void readFromPreference() {
        String news = Preference.get().getNewsDetail();
        if (!TextUtils.isEmpty(news)) {
            Type type = new TypeToken<MaxLinkedHashMap<String, NewsDetail>>() {
            }.getType();
            sNewsCache = sGson.fromJson(news, type);
        } else {
            sNewsCache = new MaxLinkedHashMap<String, NewsDetail>();
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

    //插入或替换缓存(替换指的是更新阅读记忆的阅读高度)
    public static void insertOrReplaceNews(NewsDetail newsDetail) {
        if (sNewsCache == null) {
            readFromPreference();
        }
        sNewsCache.put(newsDetail.getId(), newsDetail);
        String news = sGson.toJson(sNewsCache);
        Preference.get().setNewsDetail(news);
    }

    //读取缓存
    public static NewsDetail getCacheForId(String id) {
        if (sNewsCache == null) {
            readFromPreference();
        }
        NewsDetail newsDetail = sNewsCache.get(id);
        return newsDetail;
    }

    //遍历缓存,并转换为ReadHistoryOrMyCollect数据
    public static List<ReadHistoryOrMyCollect> getReadHistory() {
        if (sNewsCache == null) {
            readFromPreference();
        }
        List<ReadHistoryOrMyCollect> readHistoryOrMyCollects = new ArrayList<>();
        for (NewsDetail newsDetail : sNewsCache.values()) {
            ReadHistoryOrMyCollect readHistoryOrMyCollect = copyToReadHistory(newsDetail);
            readHistoryOrMyCollects.add(readHistoryOrMyCollect);
        }
        return readHistoryOrMyCollects;
    }

    private static ReadHistoryOrMyCollect copyToReadHistory(NewsDetail newsDetail) {
        ReadHistoryOrMyCollect readHistoryOrMyCollect = new ReadHistoryOrMyCollect();
        readHistoryOrMyCollect.setReadTime(newsDetail.getReadTime());
        readHistoryOrMyCollect.setCreateTime(newsDetail.getCreateTime());
        readHistoryOrMyCollect.setDataId(newsDetail.getId());
        readHistoryOrMyCollect.setIsRead(1);
        readHistoryOrMyCollect.setOriginal(newsDetail.getOriginal());
        readHistoryOrMyCollect.setSource(newsDetail.getSource());
        readHistoryOrMyCollect.setTitle(newsDetail.getTitle());
        readHistoryOrMyCollect.setType(MESSAGE_TYPE_READ_HISTORY);
        readHistoryOrMyCollect.setImgs(newsDetail.getImgs());
        readHistoryOrMyCollect.setChannel(newsDetail.getChannel());
        return readHistoryOrMyCollect;
    }

    public static String getUploadJson() {
        if (sNewsCache == null) {
            readFromPreference();
        }
        List<UploadNews> uploadNewsList = new ArrayList<>();
        for (NewsDetail newsDetail : sNewsCache.values()) {
            UploadNews uploadNews = new UploadNews();
            uploadNews.setDataId(newsDetail.getId());
            uploadNews.setReadTime(newsDetail.getReadTime());
            uploadNewsList.add(uploadNews);
        }
        return sGson.toJson(uploadNewsList);
    }

    public static class MaxLinkedHashMap<T, E> extends LinkedHashMap<T, E> {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<T, E> eldest) {
            //每当调用myMap.put()的时候，就会自动判断是否个数已经超过maximumSize，如果超过就删掉最旧的那条
            return size() > TOTAL_NEWS;
        }
    }

    public static class UploadNews {
        private long readTime;
        private String dataId;

        public long getReadTime() {
            return readTime;
        }

        public void setReadTime(long readTime) {
            this.readTime = readTime;
        }

        public String getDataId() {
            return dataId;
        }

        public void setDataId(String dataId) {
            this.dataId = dataId;
        }
    }
}
