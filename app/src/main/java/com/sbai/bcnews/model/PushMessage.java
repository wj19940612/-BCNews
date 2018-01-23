package com.sbai.bcnews.model;

/**
 * 个推
 */

public class PushMessage {

    /**
     * createTime : 2017-11-03 10:20:42
     * pushTopic : dasda
     * pushContent : dasdasda
     * url : ccsdasdasd
     * pushType : 2
     */

    private String createTime;
    private String pushTopic;
    private String pushContent;
    private String url;
    private int pushType;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPushTopic() {
        return pushTopic;
    }

    public void setPushTopic(String pushTopic) {
        this.pushTopic = pushTopic;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPushType() {
        return pushType;
    }

    public void setPushType(int pushType) {
        this.pushType = pushType;
    }

    @Override
    public String toString() {
        return "PushMessage{" +
                "createTime='" + createTime + '\'' +
                ", pushTopic='" + pushTopic + '\'' +
                ", pushContent='" + pushContent + '\'' +
                ", url='" + url + '\'' +
                ", pushType=" + pushType +
                '}';
    }
}
