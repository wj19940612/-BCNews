package com.sbai.bcnews.model.push;

/**
 * 个推
 */

public class PushMessage implements PushType{
    private int classify;
    private long createTime;
    private String dataId;
    private String iconUrl;
    private String msg;
    private String title;
    private int type;
    private String url;

    public boolean isNews() {
        return type == NEWS;
    }

    public boolean isLoginReward(){
        return type == LOGIN_REWARD;
    }

    public boolean isTimeReward(){
        return type == TIME_REWARD;
    }

    public boolean isNewsFlash() {
        return type == NEWS_FLASH;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PushMessageModel{" +
                "classify=" + classify +
                ", createTime=" + createTime +
                ", dataId='" + dataId + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", msg='" + msg + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
