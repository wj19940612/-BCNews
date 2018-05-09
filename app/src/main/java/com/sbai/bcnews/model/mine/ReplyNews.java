package com.sbai.bcnews.model.mine;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/8
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class ReplyNews  {


    /**
     * content : 1444
     * dataId : 971290271235096577
     * firstId : 992372910226403329
     * id : 993758468076982274
     * img : https://esongtest.oss-cn-shanghai.aliyuncs.com/news/20180307/lm1520408563987.png
     * isPraise : 0
     * praiseCount : 0
     * relayContent : 所以他们入手了，但是却没有注意到这个Twitter账户名多出了一个字母“L”所以他们入手了，但是却没有注意到这个Twitter账户名多出了一个字母“L”所以他们入手了，但是却没有注意到这个Twitter账户名多出了一个字母“L”
     * relayUsername : 空军建军节空军建
     * replayId : 993748415592779778
     * replayTime : 1525765456586
     * replayUserId : 1657
     * replayUserPortrait : https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180228103054792/1657i1519785054795.png
     * secondId : 993748415592779778
     * title : 挖矿从入门到精通：想要成为矿工，你需要了解的一些事
     * type : 2
     * userId : 1657
     * userPortrait : https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180228103054792/1657i1519785054795.png
     * username : 空军建军节空军建
     */

    private String content;
    private String dataId;
    private String firstId;
    private String id;
    private String img;
    private int isPraise;
    private int praiseCount;
    private String relayContent;
    private String relayUsername;
    private String replayId;
    private long replayTime;
    private int replayUserId;
    private String replayUserPortrait;
    private long secondId;
    private String title;
    private int type;
    private int userId;
    private String userPortrait;
    private String username;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(int isPraise) {
        this.isPraise = isPraise;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getRelayContent() {
        return relayContent;
    }

    public void setRelayContent(String relayContent) {
        this.relayContent = relayContent;
    }

    public String getRelayUsername() {
        return relayUsername;
    }

    public void setRelayUsername(String relayUsername) {
        this.relayUsername = relayUsername;
    }

    public String getReplayId() {
        return replayId;
    }

    public void setReplayId(String replayId) {
        this.replayId = replayId;
    }

    public long getReplayTime() {
        return replayTime;
    }

    public void setReplayTime(long replayTime) {
        this.replayTime = replayTime;
    }

    public int getReplayUserId() {
        return replayUserId;
    }

    public void setReplayUserId(int replayUserId) {
        this.replayUserId = replayUserId;
    }

    public String getReplayUserPortrait() {
        return replayUserPortrait;
    }

    public void setReplayUserPortrait(String replayUserPortrait) {
        this.replayUserPortrait = replayUserPortrait;
    }

    public long getSecondId() {
        return secondId;
    }

    public void setSecondId(long secondId) {
        this.secondId = secondId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
