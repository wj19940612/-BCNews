package com.sbai.bcnews.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 频道实体类
 * Created by YoKeyword on 15/12/29.
 */
public class ChannelEntity implements Parcelable{

    private long id;
    private String name;

    protected ChannelEntity(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChannelEntity> CREATOR = new Creator<ChannelEntity>() {
        @Override
        public ChannelEntity createFromParcel(Parcel in) {
            return new ChannelEntity(in);
        }

        @Override
        public ChannelEntity[] newArray(int size) {
            return new ChannelEntity[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
