package com.sbai.bcnews.model.market;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.http.Apic;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wangJie} on 2018/1/26.
 * {@link Apic#  /api/news-quota/quota/list}
 */

public class MarketData {

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

    private double askPrice;     //卖价
    private double bidPrice;     //买价
    private String code;         //代码
    private String currencyMoney;//计价货币
    private String exchangeCode; //交易所code
    private double highestPrice; //最高价
    private double lastPrice;    // 最新 成交价
    private double lastVolume;   // 最新量
    private double lowestPrice;
    private String name;
    private double rate;         //兑换rmb 汇率
    private int status;
    private String tradeDay;
    private double upDropSpeed;  //涨跌幅
    private long upTime;
    private String upTimeFormat;
    private double volume;

    // TODO: 2018/1/26 先写死数据
    public static List<MarketData> arrayMarketListDataFromData(String str) {

        Type listType = new TypeToken<ArrayList<MarketData>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

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
}
