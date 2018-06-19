package com.sbai.bcnews.model;

import android.os.Parcel;

/**
 * 活动弹窗
 */

public class Pop extends Banner {
    private String buttonMsg;
    private String buttonPicUrl;
    private String linkUrl;
    private String popImg;
    private int deviceType;//0安卓 2H5
    private int sceneType;

    public void setSceneType(int sceneType) {
        this.sceneType = sceneType;
    }

    public int getSceneType() {
        return sceneType;
    }

    public String getButtonPicUrl() {
        return buttonPicUrl;
    }

    public String getButtonMsg() {
        return buttonMsg;
    }

    public void setButtonMsg(String buttonMsg) {
        this.buttonMsg = buttonMsg;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getPopImg() {
        return popImg;
    }

    public void setPopImg(String popImg) {
        this.popImg = popImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.buttonMsg);
        dest.writeString(this.linkUrl);
        dest.writeString(this.popImg);
    }

    public Pop() {
    }

    protected Pop(Parcel in) {
        this.buttonMsg = in.readString();
        this.linkUrl = in.readString();
        this.popImg = in.readString();
    }

    public static final Creator<Pop> CREATOR = new Creator<Pop>() {
        @Override
        public Pop createFromParcel(Parcel source) {
            return new Pop(source);
        }

        @Override
        public Pop[] newArray(int size) {
            return new Pop[size];
        }
    };
}
