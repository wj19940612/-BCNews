package com.sbai.bcnews.model.mine;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modified by $nishuideyu$ on 2018/5/17
 * <p>
 * Description:
 * </p>
 */
public class RobRedPacketInfo implements Parcelable {

    private double money;

    private int totalPeople;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.money);
        dest.writeInt(this.totalPeople);
    }

    public RobRedPacketInfo() {
    }

    protected RobRedPacketInfo(Parcel in) {
        this.money = in.readDouble();
        this.totalPeople = in.readInt();
    }

    public static final Creator<RobRedPacketInfo> CREATOR = new Creator<RobRedPacketInfo>() {
        @Override
        public RobRedPacketInfo createFromParcel(Parcel source) {
            return new RobRedPacketInfo(source);
        }

        @Override
        public RobRedPacketInfo[] newArray(int size) {
            return new RobRedPacketInfo[size];
        }
    };
}
