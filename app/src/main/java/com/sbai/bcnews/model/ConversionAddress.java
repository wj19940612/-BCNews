package com.sbai.bcnews.model;

public class ConversionAddress {
    private long createTime;
    private String id;
    private String phone; //手机号
    private long updateTime;
    private String phoneName;//机主姓名
    private String aliPay;//支付宝账号
    private String aliPayName;//支付宝账号姓名
    private String extractCoinAddress;//提币地址

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getAliPay() {
        return aliPay;
    }

    public void setAliPay(String aliPay) {
        this.aliPay = aliPay;
    }

    public String getAliPayName() {
        return aliPayName;
    }

    public void setAliPayName(String aliPayName) {
        this.aliPayName = aliPayName;
    }

    public String getExtractCoinAddress() {
        return extractCoinAddress;
    }

    public void setExtractCoinAddress(String extractCoinAddress) {
        this.extractCoinAddress = extractCoinAddress;
    }
}
