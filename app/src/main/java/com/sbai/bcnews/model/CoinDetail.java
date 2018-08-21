package com.sbai.bcnews.model;

public class CoinDetail {
    private long createTime;

    private int id;

    private String minExtractCoin;

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

    public String getMinExtractCoin() {
        return minExtractCoin;
    }

    public void setMinExtractCoin(String minExtractCoin) {
        this.minExtractCoin = minExtractCoin;
    }
}
