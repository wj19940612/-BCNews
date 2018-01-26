package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 快讯
 */

public class NewsFlash implements Parcelable {

    public static final int IMPORTANT = 1;

    /**
     * content : 链圈微信CoinMeet(MEE)今日下午3点整即将全球首发火币PRO全球交易站、CoinEgg、BJEX、NB.top、龙币网。
     * id : 5a6840f48940b041d862567a
     * releaseTime : 1516776600000
     * title : 【CoinMeet(MEE)下午3点即将首发火币PRO等五家交易所】
     */

    private String content;
    private String id;
    private long releaseTime;
    private String title;
    private int rank;

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public String getTitle() {
        return title;
    }

    public int getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return "NewsFlash{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", releaseTime=" + releaseTime +
                ", title='" + title + '\'' +
                '}';
    }

    public NewsFlash() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeLong(this.releaseTime);
        dest.writeString(this.title);
        dest.writeInt(this.rank);
    }

    protected NewsFlash(Parcel in) {
        this.content = in.readString();
        this.id = in.readString();
        this.releaseTime = in.readLong();
        this.title = in.readString();
        this.rank = in.readInt();
    }

    public static final Creator<NewsFlash> CREATOR = new Creator<NewsFlash>() {
        @Override
        public NewsFlash createFromParcel(Parcel source) {
            return new NewsFlash(source);
        }

        @Override
        public NewsFlash[] newArray(int size) {
            return new NewsFlash[size];
        }
    };
}
