package com.sbai.bcnews.utils.news;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.ChannelCacheModel;
import com.sbai.bcnews.model.ChannelEntity;
import com.sbai.bcnews.model.NewsDetail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Administrator on 2018\2\7 0007.
 */

public class ChannelCache {
    private static Gson sGson = new Gson();

    public static void modifyChannel(List<String> myChannels, List<String> otherChannels) {
        if ((myChannels == null || myChannels.size() == 0) && (otherChannels == null || otherChannels.size() == 0))
            return;
        ChannelCacheModel channelCacheModel = new ChannelCacheModel();
        if (myChannels != null && myChannels.size() != 0)
            channelCacheModel.setMyChannelEntities(myChannels);
        if (otherChannels != null && otherChannels.size() != 0)
            channelCacheModel.setOtherChannelEntities(otherChannels);
        String channles = sGson.toJson(channelCacheModel);
        Preference.get().setChannelCache(channles);
    }

    public static ChannelCacheModel getChannel() {
        String channels = Preference.get().getChannelCache();
        if (!TextUtils.isEmpty(channels)) {
            Type type = new TypeToken<ChannelCacheModel>() {
            }.getType();
            return (sGson.fromJson(channels, type));
        }
        return null;
    }

    public static ChannelCacheModel contrastChannel(ChannelCacheModel channelCacheModel, List<String> netChannels) {
        boolean modified = false;
        if (netChannels == null) {
            channelCacheModel.setModified(false);
            return channelCacheModel;
        }
        if (channelCacheModel == null) {
            //没缓存用netChannels
            ChannelCacheModel netChannelCacheModel = new ChannelCacheModel();
            netChannelCacheModel.setMyChannelEntities(netChannels);
            netChannelCacheModel.setModified(true);
            return netChannelCacheModel;
        }
        //删除在远端没有的频道
        List<String> myChannels = channelCacheModel.getMyChannelEntities();
        if (myChannels != null && myChannels.size() != 0) {
            for (String channel : myChannels) {
                //这个频道在远端已经没了，剔除
                if (!netChannels.contains(channel)) {
                    myChannels.remove(channel);
                    modified = true;
                }
            }
        }

        List<String> otherChannels = channelCacheModel.getOtherChannelEntities();
        if (otherChannels != null && otherChannels.size() != 0) {
            for (String channel : otherChannels) {
                //这个频道在远端已经没了，剔除
                if (!netChannels.contains(channel)) {
                    otherChannels.remove(channel);
                }
            }
        }

        //增加远端有，本地没有的频道
        for (String channel : netChannels) {
            if (myChannels != null && otherChannels != null) {
                if (!myChannels.contains(channel) && !otherChannels.contains(channel)) {
                    otherChannels.add(channel);
                }
            } else if(myChannels != null && otherChannels == null) {
                if (!myChannels.contains(channel)) {
                    otherChannels = new ArrayList<String>();
                    otherChannels.add(channel);
                }
            }
        }
        ChannelCacheModel newChannelCacheModel = new ChannelCacheModel();
        newChannelCacheModel.setMyChannelEntities(myChannels);
        newChannelCacheModel.setOtherChannelEntities(otherChannels);
        newChannelCacheModel.setModified(modified);
        return newChannelCacheModel;
    }
}
