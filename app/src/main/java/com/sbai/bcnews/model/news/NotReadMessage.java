package com.sbai.bcnews.model.news;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/10
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NotReadMessage {

    /**
     * msg : 8
     * feedBack : 9
     */

    private int msg;
    private int feedBack;

    public boolean hasNewMessage() {
        return getFeedBack() + getMsg() > 0;
    }

    public int getMsg() {
        return msg;
    }

    public void setMsg(int msg) {
        this.msg = msg;
    }

    public int getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(int feedBack) {
        this.feedBack = feedBack;
    }

    @Override
    public String toString() {
        return "NotReadMessage{" +
                "msg=" + msg +
                ", feedBack=" + feedBack +
                '}';
    }
}
