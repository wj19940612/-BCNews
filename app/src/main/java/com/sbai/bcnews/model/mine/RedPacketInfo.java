package com.sbai.bcnews.model.mine;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author yangguangda
 * @date 2018/5/24
 */
public class RedPacketInfo implements Parcelable {

    /**
     * createTime : 1527148864000
     * id : 356
     * integral : 20.0
     * updateTime : 1527148864000
     * userId : 1673
     * userName : 用户m
     * userPortrait : https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180305151441544/1673i1520234081545.png
     */

    private long createTime;
    private String id;
    private double integral;
    private long updateTime;
    private int userId;
    private String userName;
    private String userPortrait;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPortrait() {
        return userPortrait;
    }

    public void setUserPortrait(String userPortrait) {
        this.userPortrait = userPortrait;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.createTime);
        dest.writeString(this.id);
        dest.writeDouble(this.integral);
        dest.writeLong(this.updateTime);
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeString(this.userPortrait);
    }

    public RedPacketInfo() {
    }

    protected RedPacketInfo(Parcel in) {
        this.createTime = in.readLong();
        this.id = in.readString();
        this.integral = in.readDouble();
        this.updateTime = in.readLong();
        this.userId = in.readInt();
        this.userName = in.readString();
        this.userPortrait = in.readString();
    }

    public static final Parcelable.Creator<RedPacketInfo> CREATOR = new Parcelable.Creator<RedPacketInfo>() {
        @Override
        public RedPacketInfo createFromParcel(Parcel source) {
            return new RedPacketInfo(source);
        }

        @Override
        public RedPacketInfo[] newArray(int size) {
            return new RedPacketInfo[size];
        }
    };
}
