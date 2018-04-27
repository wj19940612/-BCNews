package com.sbai.bcnews.model.news;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/4/27
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NewsViewpoint {

    /**
     * totalComment : 7
     * data : [{"content":"发表第二个一级评论-----","id":977023029349253121,"praiseCount":0,"replayCount":7,"replayTime":1521775417064,"userId":1664,"userPortrait":"https://esongtest.oss-cn-shanghai.aliyuncs.com/news/20180206/lm1517907436609.jpg","username":"逗号帝"}]
     */

    private int totalComment;
    private List<DataBean> data;

    public static NewsViewpoint objectFromData(String str) {

        return new Gson().fromJson(str, NewsViewpoint.class);
    }

    public static NewsViewpoint objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), NewsViewpoint.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<NewsViewpoint> arrayNewsViewpointFromData(String str) {

        Type listType = new TypeToken<ArrayList<NewsViewpoint>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<NewsViewpoint> arrayNewsViewpointFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<NewsViewpoint>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * content : 发表第二个一级评论-----
         * id : 977023029349253121
         * praiseCount : 0
         * replayCount : 7
         * replayTime : 1521775417064
         * userId : 1664
         * userPortrait : https://esongtest.oss-cn-shanghai.aliyuncs.com/news/20180206/lm1517907436609.jpg
         * username : 逗号帝
         */

        private String content;
        private long id;
        private int praiseCount;
        private int replayCount;
        private long replayTime;
        private int userId;
        private String userPortrait;
        private String username;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public static DataBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<DataBean> arrayDataBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DataBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<DataBean> arrayDataBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<DataBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getPraiseCount() {
            return praiseCount;
        }

        public void setPraiseCount(int praiseCount) {
            this.praiseCount = praiseCount;
        }

        public int getReplayCount() {
            return replayCount;
        }

        public void setReplayCount(int replayCount) {
            this.replayCount = replayCount;
        }

        public long getReplayTime() {
            return replayTime;
        }

        public void setReplayTime(long replayTime) {
            this.replayTime = replayTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserPortrait() {
            return userPortrait;
        }

        public void setUserPortrait(String userPortrait) {
            this.userPortrait = userPortrait;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
