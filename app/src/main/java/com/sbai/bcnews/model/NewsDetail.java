package com.sbai.bcnews.model;

import java.util.List;

/**
 * Created by Administrator on 2018\1\26 0026.
 */
//资讯详情
public class NewsDetail {

    private String content;  //h5内容
    private long createTime;
    private String detail;  //一级类型描述
    private String id;
    private int praiseCount; //点赞数量
    private int readerCount; //阅读数量
    private long releaseTime; //发布时间
    private String secondDetail; //二级类型描述
    private int secondType;     //二级类型
    private String source;      //来源
    private String summary;     //摘要
    private String title;       //标题
    private int type;           //一级类型
    private List<String> imgs;    //图片集合
    private List<String> tags;    //标签
    private int readerTime;       //需要阅读时间
    private int original;         //是否原创
    private int praise;           //是否点赞 0-未点赞 1-已点赞
    private int collect;          //是否收藏 0-未收藏 1-已收藏
    private long updateTime;      //更新时间
    private long readTime;        //阅读时机

    private int readHeight; //阅读高度,用于用户记忆浏览

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSecondDetail() {
        return secondDetail;
    }

    public void setSecondDetail(String secondDetail) {
        this.secondDetail = secondDetail;
    }

    public int getSecondType() {
        return secondType;
    }

    public void setSecondType(int secondType) {
        this.secondType = secondType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getReaderTime() {
        return readerTime;
    }

    public void setReaderTime(int readerTime) {
        this.readerTime = readerTime;
    }

    public int getReadHeight() {
        return readHeight;
    }

    public void setReadHeight(int readHeight) {
        this.readHeight = readHeight;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
