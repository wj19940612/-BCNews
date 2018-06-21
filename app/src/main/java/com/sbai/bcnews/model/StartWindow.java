package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StartWindow implements Parcelable {

    public static final int START_TYPE_MODULE = 0;
    public static final int START_TYPE_ARTICLE = 1;
    public static final int START_TYPE_H5 = 2;


    private int clicks;
    private long createTime;
    private int id;
    private String link;
    private int linkType;
    private long updateTime;
    private String windowUrl;

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLinkType() {
        return linkType;
    }

    public void setLinkType(int linkType) {
        this.linkType = linkType;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getWindowUrl() {
        return windowUrl;
    }

    public void setWindowUrl(String windowUrl) {
        this.windowUrl = windowUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clicks);
        dest.writeLong(this.createTime);
        dest.writeInt(this.id);
        dest.writeString(this.link);
        dest.writeInt(this.linkType);
        dest.writeLong(this.updateTime);
        dest.writeString(this.windowUrl);
    }

    public StartWindow() {
    }

    protected StartWindow(Parcel in) {
        this.clicks = in.readInt();
        this.createTime = in.readLong();
        this.id = in.readInt();
        this.link = in.readString();
        this.linkType = in.readInt();
        this.updateTime = in.readLong();
        this.windowUrl = in.readString();
    }

    public static final Parcelable.Creator<StartWindow> CREATOR = new Parcelable.Creator<StartWindow>() {
        @Override
        public StartWindow createFromParcel(Parcel source) {
            return new StartWindow(source);
        }

        @Override
        public StartWindow[] newArray(int size) {
            return new StartWindow[size];
        }
    };
}
