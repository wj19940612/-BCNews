package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018\1\26 0026.
 */
//资讯详情
public class NewsDetail implements Parcelable {

    private List<String> channel; //渠道
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

    private String advertCopyWriter;//广告文案
    private String advertName;//广告名
    private String androidUrl; //广告链接
    private int isAdvert;//是否广告 0不是 1是
    private int urlType; //0-h5 1-下载

    private String author;       //作者
    private int rankType;
    private int rankTypeStr;
    private int isConcern;      //是否关注:0未关注,1关注
    private int userPortrait;
    private int authorId;


    public int getRankType() {
        return rankType;
    }

    public void setRankType(int rankType) {
        this.rankType = rankType;
    }

    public int getRankTypeStr() {
        return rankTypeStr;
    }

    public void setRankTypeStr(int rankTypeStr) {
        this.rankTypeStr = rankTypeStr;
    }

    public int getIsConcern() {
        return isConcern;
    }

    public void setIsConcern(int isConcern) {
        this.isConcern = isConcern;
    }

    public int getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(int userPortrait) {
        this.userPortrait = userPortrait;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

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

    public List<String> getChannel() {
        return channel;
    }

    public void setChannel(List<String> channel) {
        this.channel = channel;
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

    public NewsDetail() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.channel);
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
        dest.writeString(this.detail);
        dest.writeString(this.id);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.readerCount);
        dest.writeLong(this.releaseTime);
        dest.writeString(this.secondDetail);
        dest.writeInt(this.secondType);
        dest.writeString(this.source);
        dest.writeString(this.summary);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeStringList(this.imgs);
        dest.writeStringList(this.tags);
        dest.writeInt(this.readerTime);
        dest.writeInt(this.original);
        dest.writeInt(this.praise);
        dest.writeInt(this.collect);
        dest.writeLong(this.updateTime);
        dest.writeLong(this.readTime);
        dest.writeString(this.author);
        dest.writeString(this.advertCopyWriter);
        dest.writeString(this.advertName);
        dest.writeString(this.androidUrl);
        dest.writeInt(this.isAdvert);
        dest.writeInt(this.urlType);
        dest.writeInt(this.readHeight);
    }

    protected NewsDetail(Parcel in) {
        this.channel = in.createStringArrayList();
        this.content = in.readString();
        this.createTime = in.readLong();
        this.detail = in.readString();
        this.id = in.readString();
        this.praiseCount = in.readInt();
        this.readerCount = in.readInt();
        this.releaseTime = in.readLong();
        this.secondDetail = in.readString();
        this.secondType = in.readInt();
        this.source = in.readString();
        this.summary = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.imgs = in.createStringArrayList();
        this.tags = in.createStringArrayList();
        this.readerTime = in.readInt();
        this.original = in.readInt();
        this.praise = in.readInt();
        this.collect = in.readInt();
        this.updateTime = in.readLong();
        this.readTime = in.readLong();
        this.author = in.readString();
        this.advertCopyWriter = in.readString();
        this.advertName = in.readString();
        this.androidUrl = in.readString();
        this.isAdvert = in.readInt();
        this.urlType = in.readInt();
        this.readHeight = in.readInt();
    }

    public static final Creator<NewsDetail> CREATOR = new Creator<NewsDetail>() {
        @Override
        public NewsDetail createFromParcel(Parcel source) {
            return new NewsDetail(source);
        }

        @Override
        public NewsDetail[] newArray(int size) {
            return new NewsDetail[size];
        }
    };
}
