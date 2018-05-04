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
 * APIS:{@link com.songbai.coinpro.http.Api#requestNewsViewpoint()}
 */
public class NewsViewpoint implements ViewpointType{

    public static final int ALREADY_PRAISE = 1;

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
    /**
     * dataId : 960799530167795700
     * isPraise : 0
     * type : 0
     */

    private long dataId;
    private int isPraise;
    private int type;


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

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
