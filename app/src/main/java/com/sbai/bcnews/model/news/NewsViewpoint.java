package com.sbai.bcnews.model.news;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modified by $nishuideyu$ on 2018/4/27
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#requestNewsViewpoint()}
 */
public class NewsViewpoint implements ViewpointType,Parcelable {

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
    private String id;
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

    private String dataId;
    private int isPraise;
    private int type;



    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public NewsViewpoint() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeInt(this.praiseCount);
        dest.writeInt(this.replayCount);
        dest.writeLong(this.replayTime);
        dest.writeInt(this.userId);
        dest.writeString(this.userPortrait);
        dest.writeString(this.username);
        dest.writeString(this.dataId);
        dest.writeInt(this.isPraise);
        dest.writeInt(this.type);
    }

    protected NewsViewpoint(Parcel in) {
        this.content = in.readString();
        this.id = in.readString();
        this.praiseCount = in.readInt();
        this.replayCount = in.readInt();
        this.replayTime = in.readLong();
        this.userId = in.readInt();
        this.userPortrait = in.readString();
        this.username = in.readString();
        this.dataId = in.readString();
        this.isPraise = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<NewsViewpoint> CREATOR = new Creator<NewsViewpoint>() {
        @Override
        public NewsViewpoint createFromParcel(Parcel source) {
            return new NewsViewpoint(source);
        }

        @Override
        public NewsViewpoint[] newArray(int size) {
            return new NewsViewpoint[size];
        }
    };
}
