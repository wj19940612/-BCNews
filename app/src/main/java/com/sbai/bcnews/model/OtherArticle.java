package com.sbai.bcnews.model;

import java.util.List;

/**
 * Created by Administrator on 2018\2\8 0008.
 */

public class OtherArticle {

    /**
     * channel : ["推荐"]
     * edit : 1
     * id : 961449589276262401
     * imgs : ["https://esongb.oss-cn-shanghai.aliyuncs.com/news/20180208/lm1518062419085.jpg"]
     * original : 0
     * praiseCount : 0
     * readerCount : 39
     * releaseTime : 1518019200000
     * shareCount : 0
     * source : 比特币资讯网
     * status : 1
     * tags : []
     * title : 文克莱沃斯兄弟：比特币的价值有朝一日将升至今天的40倍
     */

    private int edit;
    private String id;
    private int original;
    private int praiseCount;
    private int readerCount;
    private long releaseTime;
    private int shareCount;
    private String source;
    private int status;
    private String title;
    private List<String> channel;
    private List<String> imgs;
    private List<?> tags;

    public int getEdit() {
        return edit;
    }

    public void setEdit(int edit) {
        this.edit = edit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public int getReaderCount() {
        return readerCount;
    }

    public void setReaderCount(int readerCount) {
        this.readerCount = readerCount;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getShareCount() {
        return shareCount;
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<?> getTags() {
        return tags;
    }

    public void setTags(List<?> tags) {
        this.tags = tags;
    }
}
