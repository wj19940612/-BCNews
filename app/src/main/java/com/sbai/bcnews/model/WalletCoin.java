package com.sbai.bcnews.model;

public class WalletCoin {
   private double ableCoin;//可用数量

    private String coinSymbol;//币种

    public double getAbleCoin() {
        return ableCoin;
    }

    public void setAbleCoin(double ableCoin) {
        this.ableCoin = ableCoin;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }
}
