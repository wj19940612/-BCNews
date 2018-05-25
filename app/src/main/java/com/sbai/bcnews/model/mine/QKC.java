package com.sbai.bcnews.model.mine;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 */
public class QKC {

    public static final int TYPE_LOGIN = 0;                //登陆
    public static final int TYPE_READ_ARTICLE = 1;         //阅读文章
    public static final int TYPE_SHARE = 2;                     //分享
    public static final int TYPE_COMMENT = 3;                   //评论
    public static final int TYPE_INFORM = 4;                    //举报
    public static final int TYPE_ONLINE = 5;                    //在线

    /**
     * id : 204
     * integral : 6.3212
     */

    private String id;
    private double integral;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }
}
