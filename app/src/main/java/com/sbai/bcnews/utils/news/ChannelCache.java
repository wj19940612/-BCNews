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
import java.util.Iterator;
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
            Iterator<String> myChannelIt = myChannels.iterator();
            while (myChannelIt.hasNext()) {
                String channel = myChannelIt.next();
                if (!netChannels.contains(channel)) {
                    myChannelIt.remove();
                    modified = true;
                }
            }
        }

        List<String> otherChannels = channelCacheModel.getOtherChannelEntities();
        if (otherChannels != null && otherChannels.size() != 0) {
            Iterator<String> otherIt = otherChannels.iterator();
            while(otherIt.hasNext()){
                String channel = otherIt.next();
                if(!netChannels.contains(channel)){
                    //这个频道在远端已经没了，剔除
                    otherIt.remove();
                }
            }
        }

        //增加远端有，本地没有的频道
        for (String channel : netChannels) {
            if (myChannels != null && otherChannels != null) {
                if (!myChannels.contains(channel) && !otherChannels.contains(channel)) {
                    otherChannels.add(channel);
                }
            } else if (myChannels != null && otherChannels == null) {
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

    public static boolean isSameChannel(ChannelCacheModel newChannelCacheModel, ChannelCacheModel oldChannelCacheModel) {
        //size不同 说明变化了
        if (newChannelCacheModel.getMyChannelEntities() != null && oldChannelCacheModel.getMyChannelEntities() != null) {
            if (newChannelCacheModel.getMyChannelEntities().size() != oldChannelCacheModel.getMyChannelEntities().size()) {
                return false;
            }

            for (int i = 0; i < newChannelCacheModel.getMyChannelEntities().size(); i++) {
                String newChannel = newChannelCacheModel.getMyChannelEntities().get(i);
                String oldChannel = oldChannelCacheModel.getMyChannelEntities().get(i);
                //第i项内容不同，变化了
                if (oldChannel == null || !newChannel.equals(oldChannel)) {
                    return false;
                }
            }
        }
        //size 想通 每一项都相同
        return true;
    }
}
