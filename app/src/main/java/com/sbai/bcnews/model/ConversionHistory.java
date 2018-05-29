package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ConversionHistory implements Parcelable{

    /**
     * accountAliPay : 18928939
     * accountAliPayName : 松仔
     * createTime : 1527063635000
     * id : 5
     * pId : 1
     * pName : 1元
     * price : 1
     * status : 0
     * updateTime : 1527063635000
     * userId : 1665
     */

    public static final int STATUS_REVIEWING = 0;
    public static final int STATUS_HAS_CONVERSION = 1;
    public static final int STATUS_FAILED = 2;

    private String accountAliPay;
    private String accountAliPayName;
    private long createTime;
    private String id;
    private String pId;
    private String pName;
    private double price;
    private int status;
    private long updateTime;
    private int userId;
    private int type;  //0-数字货币,1-支付宝,2-话费
    private String remark;
    private String accountPhone;//手机号
    private String accountPhoneName;//机主姓名
    private String extractCoinAddress;//提币地址

    public String getAccountAliPay() {
        return accountAliPay;
    }

    public void setAccountAliPay(String accountAliPay) {
        this.accountAliPay = accountAliPay;
    }

    public String getAccountAliPayName() {
        return accountAliPayName;
    }

    public void setAccountAliPayName(String accountAliPayName) {
        this.accountAliPayName = accountAliPayName;
    }

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

    public String getPId() {
        return pId;
    }

    public void setPId(String pId) {
        this.pId = pId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public String getAccountPhoneName() {
        return accountPhoneName;
    }

    public void setAccountPhoneName(String accountPhoneName) {
        this.accountPhoneName = accountPhoneName;
    }

    public String getExtractCoinAddress() {
        return extractCoinAddress;
    }

    public void setExtractCoinAddress(String extractCoinAddress) {
        this.extractCoinAddress = extractCoinAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountAliPay);
        dest.writeString(this.accountAliPayName);
        dest.writeLong(this.createTime);
        dest.writeString(this.id);
        dest.writeString(this.pId);
        dest.writeString(this.pName);
        dest.writeDouble(this.price);
        dest.writeInt(this.status);
        dest.writeLong(this.updateTime);
        dest.writeInt(this.userId);
        dest.writeInt(this.type);
        dest.writeString(this.remark);
        dest.writeString(this.accountPhone);
        dest.writeString(this.accountPhoneName);
        dest.writeString(this.extractCoinAddress);
    }

    public ConversionHistory() {
    }

    protected ConversionHistory(Parcel in) {
        this.accountAliPay = in.readString();
        this.accountAliPayName = in.readString();
        this.createTime = in.readLong();
        this.id = in.readString();
        this.pId = in.readString();
        this.pName = in.readString();
        this.price = in.readDouble();
        this.status = in.readInt();
        this.updateTime = in.readLong();
        this.userId = in.readInt();
        this.type = in.readInt();
        this.remark = in.readString();
        this.accountPhone = in.readString();
        this.accountPhoneName = in.readString();
        this.extractCoinAddress = in.readString();
    }

    public static final Creator<ConversionHistory> CREATOR = new Creator<ConversionHistory>() {
        @Override
        public ConversionHistory createFromParcel(Parcel source) {
            return new ConversionHistory(source);
        }

        @Override
        public ConversionHistory[] newArray(int size) {
            return new ConversionHistory[size];
        }
    };
}
