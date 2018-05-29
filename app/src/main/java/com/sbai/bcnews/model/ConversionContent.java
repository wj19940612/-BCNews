package com.sbai.bcnews.model;

public class ConversionContent {

    /**
     * createTime : 1527037877000
     * id : 1
     * margin : 100
     * name : 1元
     * price : 1
     * priceType : 0
     * sort : 1
     * status : 1
     * type : 1
     * updateTime : 1527037877000
     */

    private long createTime;
    private String id;
    private int margin; //余量
    private String name;//名字
    private double price;  //价格
    private int priceType;//价格类型--现在不需要用
    private int sort;//排序
    private int status;//状态:0停用,1启用
    private int type;//类型:0数字货币,1支付宝,2话费
    private long updateTime;

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

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriceType() {
        return priceType;
    }

    public void setPriceType(int priceType) {
        this.priceType = priceType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
