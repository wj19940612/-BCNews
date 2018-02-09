package com.sbai.bcnews.model;

/**
 * 个推
 */

public class PushMessage {
    private int classify;      // 以前用的classify 和type 双重判断  后面用type 作为唯一表示
    private long createTime;
    private String dataId;
    private String iconUrl;
    private String msg;
    private String title;
    private int type;
    private String url;

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
