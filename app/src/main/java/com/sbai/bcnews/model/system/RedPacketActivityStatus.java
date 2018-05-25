package com.sbai.bcnews.model.system;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modified by $nishuideyu$ on 2018/5/17
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class RedPacketActivityStatus implements Parcelable {
    /**
     * currentEndTime : 1527139200939
     * currentStartTime : 1527138000939
     * nextEndTime : 1527096000939
     * redPacketStatus : 1
     * robStatus : 0
     * nextStartTime : 1527094800939
     * startTime : 1527141600939
     * time : 1527140023939
     * endTime : 1527142800939
     */
    public static final int REDPACKET_STATE_ON = 1;
    public static final int REDPACKET_CANROB = 1;
    private long currentEndTime;
    private long currentStartTime;
    private long nextEndTime;
    /**
     * 是否开启红包活动：0关闭,1开启
     */
    private int redPacketStatus;
    /**
     * 红包是否可抢:0不可抢,1可抢
     */
    private int robStatus;
    private long nextStartTime;
    private long startTime;
    private long time;
    private long endTime;

    public long getCurrentEndTime() {
        return currentEndTime;
    }

    public void setCurrentEndTime(long currentEndTime) {
        this.currentEndTime = currentEndTime;
    }

    public long getCurrentStartTime() {
        return currentStartTime;
    }

    public void setCurrentStartTime(long currentStartTime) {
        this.currentStartTime = currentStartTime;
    }

    public long getNextEndTime() {
        return nextEndTime;
    }

    public void setNextEndTime(long nextEndTime) {
        this.nextEndTime = nextEndTime;
    }

    public int getRedPacketStatus() {
        return redPacketStatus;
    }

    public void setRedPacketStatus(int redPacketStatus) {
        this.redPacketStatus = redPacketStatus;
    }

    public int getRobStatus() {
        return robStatus;
    }

    public void setRobStatus(int robStatus) {
        this.robStatus = robStatus;
    }

    public long getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(long nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.currentEndTime);
        dest.writeLong(this.currentStartTime);
        dest.writeLong(this.nextEndTime);
        dest.writeInt(this.redPacketStatus);
        dest.writeInt(this.robStatus);
        dest.writeLong(this.nextStartTime);
        dest.writeLong(this.startTime);
        dest.writeLong(this.time);
        dest.writeLong(this.endTime);
    }

    public RedPacketActivityStatus() {
    }

    protected RedPacketActivityStatus(Parcel in) {
        this.currentEndTime = in.readLong();
        this.currentStartTime = in.readLong();
        this.nextEndTime = in.readLong();
        this.redPacketStatus = in.readInt();
        this.robStatus = in.readInt();
        this.nextStartTime = in.readLong();
        this.startTime = in.readLong();
        this.time = in.readLong();
        this.endTime = in.readLong();
    }

    public static final Parcelable.Creator<RedPacketActivityStatus> CREATOR = new Parcelable.Creator<RedPacketActivityStatus>() {
        @Override
        public RedPacketActivityStatus createFromParcel(Parcel source) {
            return new RedPacketActivityStatus(source);
        }

        @Override
        public RedPacketActivityStatus[] newArray(int size) {
            return new RedPacketActivityStatus[size];
        }
    };
}
