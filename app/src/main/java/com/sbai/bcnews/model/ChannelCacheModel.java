package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2018\2\7 0007.
 */

public class ChannelCacheModel implements Parcelable{
    private List<String> mMyChannelEntities;
    private List<String> mOtherChannelEntities;

    public ChannelCacheModel(){

    }

    protected ChannelCacheModel(Parcel in) {
        this.mMyChannelEntities = in.createStringArrayList();
        this.mOtherChannelEntities = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.mMyChannelEntities);
        dest.writeStringList(this.mOtherChannelEntities);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelCacheModel> CREATOR = new Creator<ChannelCacheModel>() {
        @Override
        public ChannelCacheModel createFromParcel(Parcel in) {
            return new ChannelCacheModel(in);
        }

        @Override
        public ChannelCacheModel[] newArray(int size) {
            return new ChannelCacheModel[size];
        }
    };

    public List<String> getMyChannelEntities() {
        return mMyChannelEntities;
    }

    public void setMyChannelEntities(List<String> myChannelEntities) {
        mMyChannelEntities = myChannelEntities;
    }

    public List<String> getOtherChannelEntities() {
        return mOtherChannelEntities;
    }

    public void setOtherChannelEntities(List<String> otherChannelEntities) {
        mOtherChannelEntities = otherChannelEntities;
    }
}
