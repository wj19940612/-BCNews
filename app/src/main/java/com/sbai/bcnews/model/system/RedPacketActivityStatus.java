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
public class RedPacketActivityStatus implements Parcelable{

    private static final int RED_PACKET_ACTIVITY_IS_RUNNING = 1;
    private static final int RED_PACKET_ACTIVITY_IS_CLOSED = 0;

    private boolean activityIsRunning;

    private boolean isRobRedPacketTime;

    private long time;

    private long nexChangeTime;

    public boolean isRobRedPacketTime() {
        return isRobRedPacketTime;
    }

    public void setRobRedPacketTime(boolean robRedPacketTime) {
        isRobRedPacketTime = robRedPacketTime;
    }

    public boolean isActivityIsRunning() {
        return activityIsRunning;
    }

    public void setActivityIsRunning(boolean activityIsRunning) {
        this.activityIsRunning = activityIsRunning;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getNexChangeTime() {
        return nexChangeTime;
    }

    public void setNexChangeTime(long nexChangeTime) {
        this.nexChangeTime = nexChangeTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.activityIsRunning ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRobRedPacketTime ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time);
        dest.writeLong(this.nexChangeTime);
    }

    public RedPacketActivityStatus() {
    }

    protected RedPacketActivityStatus(Parcel in) {
        this.activityIsRunning = in.readByte() != 0;
        this.isRobRedPacketTime = in.readByte() != 0;
        this.time = in.readLong();
        this.nexChangeTime = in.readLong();
    }

    public static final Creator<RedPacketActivityStatus> CREATOR = new Creator<RedPacketActivityStatus>() {
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
