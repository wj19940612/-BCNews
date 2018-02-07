package com.sbai.bcnews.http;

import com.sbai.bcnews.App;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.utils.AppInfo;
import com.sbai.httplib.ReqParams;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description: 统一管理 api 请求
 * <p>
 */
public class Apic {

    public static final int NORMAL_PAGESIZE = 20;

    public static final String SHARE_NEWS_URL = Api.getHost() + "/news/share/index.html?id=%s";

    public static Api getNewsDetail(String id) {
        return Api.get("/api/news-info/info/details/" + id);
    }

    public static Api getNewsList(int page) {
        return Api.get("/api/news-info/info/list.do", new ReqParams().put("page", page).put("size", NORMAL_PAGESIZE));
    }

    public static Api syncSystemTime() {
        return Api.get("/user/user/getSystemTime.do");
    }

    /**
     * 0请求小于时间戳的数据
     * 1请求大于时间戳的数据
     *
     * @param time
     * @param status
     * @return
     */
    public static Api getNewsFlash(long time, int status) {
        return Api.get("/api/news-info/info/information.do", new ReqParams().put("time", time).put("status", status));
    }

    /**
     * /api/news-quota/quota/list
     * GET
     * 行情列表 (ws)
     *
     * @param exchangeCode
     * @return
     */
    public static Api requestMarkListData(String exchangeCode) {
        return Api.get("/api/news-quota/quota/list", new ReqParams().put("exchangeCode", exchangeCode));
    }

    public static Api getNewsFlash(int page) {
        return Api.get("/api/news-info/info/information.do", new ReqParams().put("page", page));
    }

    public static Api praiseNews(String newsId) {
        return Api.put("/api/news-info/info/like/" + newsId);
    }

    /**
     * 接口名称 获取验证码
     *
     * @param phone
     * @return
     */
    public static Api getAuthCode(String phone) {
        return getAuthCode(phone, null);
    }

    /**
     * 接口名称 获取验证码
     *
     * @param phone
     * @param imgCode
     * @return
     */
    public static Api getAuthCode(String phone, String imgCode) {
        return Api.post("/api/news-user/login/msg/{phone}" + phone, new ReqParams()
                .put("phone", phone)
                .put("imgCode", imgCode));
    }

    /**
     * 图片验证码地址
     *
     * @param phone
     * @return
     */
    public static String getImageAuthCode(String phone) {
        return Api.getHost() + "/user/registerLogin/getRegImage.do?userPhone=" + phone;
    }

    /**
     * 接口名称 验证码快捷登入
     *
     * @param authCode 短信验证码
     * @param phone    手机
     *                 deviceId 设备id
     *                 platform 平台 0-安卓 1-ios
     * @return
     */
    public static Api requestAuthCodeLogin(String phone, String authCode) {
        return Api.post("/api/news-user/login/quick/{phone}/{msgCode}", new ReqParams()
                .put("phone", phone)
                .put("msgCode", authCode)
                .put("deviceId", Preference.get().getPushClientId())
                .put("platform", 0)
                .put("source", AppInfo.getMetaData(App.getAppContext(), "UMENG_CHANNEL")));
    }

    /**
     * 接口名称 绑定微信
     *
     * @return
     */
    public static Api requestBindWeChat(String openId, String name, String iconUrl, int sex) {
        return Api.post("/api/news-user/user/bound/{openId}", new ReqParams()
                .put("openId", openId)
                .put("name", name)
                .put("iconUrl", iconUrl)
                .put("sex", sex));
    }

    /**
     * 微信登录
     *
     * @param openId
     * @return
     */
    public static Api requestWeChatLogin(String openId) {
        return Api.post("/api/news-user/login/wechat/{openId}", new ReqParams()
                .put("openId", openId)
                .put("deviceId", Preference.get().getPushClientId())
                .put("platform", 0)
                .put("source", AppInfo.getMetaData(App.getAppContext(), "UMENG_CHANNEL")));
    }

}
