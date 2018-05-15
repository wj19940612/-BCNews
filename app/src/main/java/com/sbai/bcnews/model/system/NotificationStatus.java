package com.sbai.bcnews.model.system;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/4/24
 * <p>
 * Description:
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NotificationStatus {

    public static final int USER_RECEIVE_COMMENT_ON_REPLY_NOTIFICATION = 1;
    public static final int USER_RECEIVE_PRAISE_NOTIFICATION = 1;

    public static final int NOTIFICATION_TYPE_RECEIVE_COMMENT_ON_REPLY = 0;
    public static final int NOTIFICATION_TYPE_RECEIVE_PRAISE = 1;

    /**
     * PRAISE : 1
     * discuss : 0
     */

    private int praise;
    private int discuss;


    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getDiscuss() {
        return discuss;
    }

    public void setDiscuss(int discuss) {
        this.discuss = discuss;
    }


}
