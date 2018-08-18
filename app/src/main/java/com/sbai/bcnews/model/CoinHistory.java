package com.sbai.bcnews.model;

public class CoinHistory {

    public static final int ADD = 0;
    public static final int REDUCE = 1;

    private String typeStr;

    private long updateTime;

    private double extractNum;

    private int changeType;  //0加 1减

    public String getTypeStr() {
        return typeStr;
    }

    public void setTypeStr(String typeStr) {
        this.typeStr = typeStr;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public double getExtractNum() {
        return extractNum;
    }

    public void setExtractNum(double extractNum) {
        this.extractNum = extractNum;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }
}
