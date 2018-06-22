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
    private String author;       //作者
    private int showReadCount;

    private String advertCopyWriter;//广告文案
    private String advertName;//广告名
    private String androidUrl; //广告链接
    private int isAdvert;//是否广告 0不是 1是
    private int urlType; //0-h5 1-下载

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

    public String getAdvertCopyWriter() {
        return advertCopyWriter;
    }

    public void setAdvertCopyWriter(String advertCopyWriter) {
        this.advertCopyWriter = advertCopyWriter;
    }

    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public int getIsAdvert() {
        return isAdvert;
    }

    public void setIsAdvert(int isAdvert) {
        this.isAdvert = isAdvert;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getShowReadCount() {
        return showReadCount;
    }

    public void setShowReadCount(int showReadCount) {
        this.showReadCount = showReadCount;
    }
}
