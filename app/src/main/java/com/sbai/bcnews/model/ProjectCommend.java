package com.sbai.bcnews.model;

public class ProjectCommend {
    private String projectName;
    private long createTime;//
    private String id;
    private double circulateCount;  //流通量激励机制
    private String incentiveSystem;//激励机制
    private String info;//详情
    private String intro;//简介
    private long publishTime;//发行时间
    private double publishPrice; //发行价格
    private double publishTotal;//发行总量
    private int status;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public double getCirculateCount() {
        return circulateCount;
    }

    public void setCirculateCount(double circulateCount) {
        this.circulateCount = circulateCount;
    }

    public String getIncentiveSystem() {
        return incentiveSystem;
    }

    public void setIncentiveSystem(String incentiveSystem) {
        this.incentiveSystem = incentiveSystem;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public double getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(double publishPrice) {
        this.publishPrice = publishPrice;
    }

    public double getPublishTotal() {
        return publishTotal;
    }

    public void setPublishTotal(double publishTotal) {
        this.publishTotal = publishTotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
