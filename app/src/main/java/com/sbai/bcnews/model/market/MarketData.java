package com.sbai.bcnews.model.market;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Modified by john on 06/02/2018
 * <p>
 * Description:
 *
 */
public class MarketData implements Parcelable{

    public static final String DEFAULT_MARKET_BOURSE_CODE  = "gate.io";

    /**
     * askPrice : 0.1752
     * bidPrice : 0.165
     * code : data_usdt
     * currencyMoney : usdt
     * exchangeCode : gate.io
     * highestPrice : 0.1907
     * lastPrice : 0.165
     * lastVolume : 518044.2545
     * lowestPrice : 0.159
     * name : data
     * rate : 6.35
     * status : 0
     * tradeDay : 2018-01-26
     * upDropSpeed : -0.13157894736842
     * upTime : 1516950438965
     * upTimeFormat : 2018-01-26 15:07:18
     * volume : 91431.3021
     */

    private double askPrice;     //卖出价（买价）
    private double bidPrice;     //买入价（卖价）
    private String code;         //代码
    private String currencyMoney;//计价货币
    private String exchangeCode; //交易所code
    private double highestPrice; //最高价
    private double lastPrice;    // 最新 成交价
    private double lastVolume;   // 最新量（最新成交总量）
    private double lowestPrice;
    private String name;         // 基准货币
    private double rate;         //兑换rmb 汇率
    private int status;
    private String tradeDay;
    private double upDropSpeed;  //涨跌幅
    private long upTime;
    private String upTimeFormat;
    private double volume;

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrencyMoney() {
        return currencyMoney;
    }

    public void setCurrencyMoney(String currencyMoney) {
        this.currencyMoney = currencyMoney;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public void setExchangeCode(String exchangeCode) {
        this.exchangeCode = exchangeCode;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getLastVolume() {
        return lastVolume;
    }

    public void setLastVolume(double lastVolume) {
        this.lastVolume = lastVolume;
    }

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTradeDay() {
        return tradeDay;
    }

    public void setTradeDay(String tradeDay) {
        this.tradeDay = tradeDay;
    }

    public double getUpDropSpeed() {
        return upDropSpeed;
    }

    public void setUpDropSpeed(double upDropSpeed) {
        this.upDropSpeed = upDropSpeed;
    }

    public long getUpTime() {
        return upTime;
    }

    public void setUpTime(long upTime) {
        this.upTime = upTime;
    }

    public String getUpTimeFormat() {
        return upTimeFormat;
    }

    public void setUpTimeFormat(String upTimeFormat) {
        this.upTimeFormat = upTimeFormat;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "MarketListData{" +
                "askPrice=" + askPrice +
                ", bidPrice=" + bidPrice +
                ", code='" + code + '\'' +
                ", currencyMoney='" + currencyMoney + '\'' +
                ", exchangeCode='" + exchangeCode + '\'' +
                ", highestPrice=" + highestPrice +
                ", lastPrice=" + lastPrice +
                ", lastVolume=" + lastVolume +
                ", lowestPrice=" + lowestPrice +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", status=" + status +
                ", tradeDay='" + tradeDay + '\'' +
                ", upDropSpeed=" + upDropSpeed +
                ", upTime=" + upTime +
                ", upTimeFormat='" + upTimeFormat + '\'' +
                ", volume=" + volume +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.askPrice);
        dest.writeDouble(this.bidPrice);
        dest.writeString(this.code);
        dest.writeString(this.currencyMoney);
        dest.writeString(this.exchangeCode);
        dest.writeDouble(this.highestPrice);
        dest.writeDouble(this.lastPrice);
        dest.writeDouble(this.lastVolume);
        dest.writeDouble(this.lowestPrice);
        dest.writeString(this.name);
        dest.writeDouble(this.rate);
        dest.writeInt(this.status);
        dest.writeString(this.tradeDay);
        dest.writeDouble(this.upDropSpeed);
        dest.writeLong(this.upTime);
        dest.writeString(this.upTimeFormat);
        dest.writeDouble(this.volume);
    }

    public MarketData() {
    }

    protected MarketData(Parcel in) {
        this.askPrice = in.readDouble();
        this.bidPrice = in.readDouble();
        this.code = in.readString();
        this.currencyMoney = in.readString();
        this.exchangeCode = in.readString();
        this.highestPrice = in.readDouble();
        this.lastPrice = in.readDouble();
        this.lastVolume = in.readDouble();
        this.lowestPrice = in.readDouble();
        this.name = in.readString();
        this.rate = in.readDouble();
        this.status = in.readInt();
        this.tradeDay = in.readString();
        this.upDropSpeed = in.readDouble();
        this.upTime = in.readLong();
        this.upTimeFormat = in.readString();
        this.volume = in.readDouble();
    }

    public static final Creator<MarketData> CREATOR = new Creator<MarketData>() {
        @Override
        public MarketData createFromParcel(Parcel source) {
            return new MarketData(source);
        }

        @Override
        public MarketData[] newArray(int size) {
            return new MarketData[size];
        }
    };
}
