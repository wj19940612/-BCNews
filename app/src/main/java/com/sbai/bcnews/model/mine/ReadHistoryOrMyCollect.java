package com.sbai.bcnews.model.mine;

import java.util.List;

/**
 * Created by ${wangJie} on 2018/2/11.
 * /api/news-user/operate/list/{type}
 */

public class ReadHistoryOrMyCollect {

    public static final int MESSAGE_TYPE_COLLECT = 0;
    public static final int MESSAGE_TYPE_READ_HISTORY = 1;

    public static final int CANCEL_COLLECT = 1;

    /**
     * collectTime : 1518317883000
     * collected : 1
     * createTime : 1518165538000
     * dataId : 960799631036612600
     * id : 1206
     * isRead : 0
     * original : 0
     * picture : https://esongtest.oss-cn-shanghai.aliyuncs.com/news/20180206/lm1517907457348.jpg
     * source : 比特币资讯网
     * title : 中国证券报：虚拟数字货币新一轮整顿措施已在酝酿
     * type : 0
     * userId : 1664
     */

    //
//    private String content;  //h5内容
//    private long createTime;
//    private String detail;  //一级类型描述
//    private String id;
//    private int praiseCount; //点赞数量
//    private int readerCount; //阅读数量
//    private String secondDetail; //二级类型描述
//    private int secondType;     //二级类型
//    private String source;      //来源
//    private String summary;     //摘要
//    private String title;       //标题
//    private int type;           //一级类型
//    private List<String> imgs;    //图片集合
//    private List<String> tags;    //标签
//    private int readerTime;       //需要阅读时间
//    private boolean isRead;       //阅读状态已读未读
//    private int original;         //是否原创
//    private int praise;           //是否点赞 0-未点赞 1-已点赞
//    private int collect;          //是否收藏 0-未收藏 1-已收藏
//

    private long collectTime;      //收藏时间
    private long readTime;       //需要阅读时间
    private int collected;
    private long createTime;
    private String dataId;
    private String id;
    private int isRead;
    private int original;  //是否原创
    private String picture;
    private String source;
    private String title;
    private int type;
    private int userId;
    private List<String> imgs;    //图片集合
    private List<String> channel;
    private String author;       //作者

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private int readHeight; //阅读高度,用于用户记忆浏览

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getOriginal() {
        return original;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public int getReadHeight() {
        return readHeight;
    }

    public void setReadHeight(int readHeight) {
        this.readHeight = readHeight;
    }

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
    }
}
