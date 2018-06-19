package com.sbai.bcnews.model;

public class Candy {

    /**
     * createTime : 1528872653394
     * id : 1006791017661411330
     * intro : 很好吃喔
     * introduce : qeqweqweq
     * name : 棒棒糖
     * photo : https://esongtest.oss-cn-shanghai.aliyuncs.com/upload/20180613145019559.png
     * sort : 1
     * status : 1
     * sweetIntroduce : eqweqwe
     * updateTime : 1528872790340
     * url : http://www.baidu.com
     * welfare : 2018-06-14T16:00:00.000Z
     */

    private long createTime;
    private String id;
    private String intro; //简介
    private String introduce;//活动介绍
    private String name;//糖果名
    private String photo;
    private int sort;
    private int status;
    private String sweetIntroduce; //糖果介绍
    private long updateTime;
    private String url;
    private String welfare; //福利时间
    private int clicks;//领取数

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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public String getSweetIntroduce() {
        return sweetIntroduce;
    }

    public void setSweetIntroduce(String sweetIntroduce) {
        this.sweetIntroduce = sweetIntroduce;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }
}
