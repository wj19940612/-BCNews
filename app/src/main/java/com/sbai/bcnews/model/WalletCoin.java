package com.sbai.bcnews.model;

public class WalletCoin {
   private String ableCoin;//可用数量

    private String coinSymbol;//币种

    private int numPoint;

    public String getAbleCoin() {
        return ableCoin;
    }

    public void setAbleCoin(String ableCoin) {
        this.ableCoin = ableCoin;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public int getNumPoint() {
        return numPoint;
    }

    public void setNumPoint(int numPoint) {
        this.numPoint = numPoint;
    }
}
