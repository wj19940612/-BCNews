package com.sbai.bcnews.http;

import com.sbai.bcnews.App;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.utils.AppInfo;
import com.sbai.httplib.ReqParams;

import java.io.File;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description: 统一管理 api 请求
 * <p>
 */
public class Apic {

    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * /api/news-info/news/tag/{tag}
     * <p>(标签)相关资讯-----齐慕伟</p>
     *
     * @param tag
     * @param page
     * @return
     */
    public static Api getRelatedNews(String tag, int page) {
        return Api.get("/api/news-info/news/tag/{tag}",
                new ReqParams()
                        .put("tag", tag)
                        .put("size", DEFAULT_PAGE_SIZE)
                        .put("page", page));
    }

    /**
     * /api/news-info/news/tag/{tag}/{id}
     * <p>(标签)资讯相关推荐-----齐慕伟</p>
     *
     * @param tag
     * @param id
     * @return
     */
    public static Api getRelatedNewsRecommend(String tag, String id) {
        return Api.get("/api/news-info/news/tag/{tag}/{id}",
                new ReqParams()
                        .put("tag", tag)
                        .put("id", id));
    }

    /**
     * /api/news-info/news/detail/{id}
     * <p>资讯详情 - 齐慕伟</p>
     *
     * @param id
     * @return
     */
    public static Api getNewsDetail(String id) {
        return Api.get("/api/news-info/info/details/{id}",
                new ReqParams()
                        .put("id", id));
    }

    /**
     * /api/news-info/news/{channel}/list
     * <p>获取资讯列表 - 齐慕伟</p>
     *
     * @param channel 频道名称
     * @return
     */
    public static Api requestNewsListWithChannel(String channel, int page) {
        return Api.get("/api/news-info/news/{channel}/list",
                new ReqParams()
                        .put("channel", channel)
                        .put("page", page).put("size", DEFAULT_PAGE_SIZE));
    }

    public static Api syncSystemTime() {
        return Api.get("/user/user/getSystemTime.do");
    }

    /**
     * /api/news-info/info/information.do
     * <p>快讯列表-----齐慕伟</p>
     *
     * @param time
     * @param status 0 请求小于时间戳的数据, 1 请求大于时间戳的数据
     * @return
     */
    public static Api getNewsFlash(long time, int status) {
        return Api.get("/api/news-info/info/information.do",
                new ReqParams()
                        .put("time", time)
                        .put("status", status));
    }

    /**
     * /api/news-quota/quota/list
     * <p>行情列表 (ws)</p>
     *
     * @param exchangeCode
     * @return
     */
    public static Api requestMarkListData(String exchangeCode) {
        return Api.get("/api/news-quota/quota/list",
                new ReqParams()
                        .put("exchangeCode", exchangeCode));
    }

    /**
     * /api/news-info/news/praise/{id}
     * <p>资讯点赞 - 齐慕伟</p>
     *
     * @param newsId 资讯id
     * @param type   0-取消点赞 1-点赞
     * @return
     */
    public static Api praiseNews(String newsId, int type) {
        return Api.post("/api/news-info/news/praise/{id}",
                new ReqParams()
                        .put("id", newsId)
                        .put("type", type));
    }

    /**
     * /api/news-info/news/channels
     * <p>频道列表-----齐慕伟</p>
     */
    public static Api getChannels() {
        return Api.get("/api/news-info/news/channels");
    }

    /**
     * /api/news-info/news/channel/{channel}/{id}
     * <p> (频道)资讯相关推荐-----齐慕伟</p>
     *
     * @param channel
     * @param id
     * @return
     */
    public static Api getOtherArticles(String channel, String id) {
        return Api.get("/api/news-info/news/channel/{channel}/{id}",
                new ReqParams()
                        .put("channel", channel)
                        .put("id", id));
    }

    /**
     * /api/news-user/login/msg/{phone}
     * <p>用户--发送短信验证码--薛松</p>
     *
     * @param phone
     * @return
     */
    public static Api getAuthCode(String phone) {
        return getAuthCode(phone, null);
    }

    /**
     * /api/news-user/login/msg/{phone}
     * <p>用户--发送短信验证码--薛松</p>
     *
     * @param phone
     * @param imgCode
     * @return
     */
    public static Api getAuthCode(String phone, String imgCode) {
        return Api.post("/api/news-user/login/msg/{phone}", new ReqParams()
                .put("phone", phone)
                .put("imgCode", imgCode));
    }

    /**
     * /api/news-user/login/get/image/{phone}
     * <p>用户--获取图片验证码--薛松</p>
     *
     * @param phone
     * @return
     */
    public static Api getImageAuthCode(String phone) {
        return Api.post("/api/news-user/login/get/image/{phone}", new ReqParams()
                .put("phone", phone));
    }

    /**
     * /api/news-user/login/quick/{phone}/{msgCode}
     * <p>用户--手机验证码快速登录--薛松</p>
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
     * /api/news-user/login/quick/{phone}/{msgCode}
     * <p>用户--手机验证码快速登录--薛松</p>
     * <p>快捷登入(for 微信)</p>
     *
     * @param authCode 短信验证码
     * @param phone    手机
     *                 deviceId 设备id
     *                 platform 平台 0-安卓 1-ios
     * @return
     */
    public static Api requestAuthCodeLogin(String phone, String authCode, String openId, String name, String iconUrl, int sex) {
        return Api.post("/api/news-user/login/quick/{phone}/{msgCode}", new ReqParams()
                .put("phone", phone)
                .put("msgCode", authCode)
                .put("deviceId", Preference.get().getPushClientId())
                .put("platform", 0)
                .put("channel", AppInfo.getMetaData(App.getAppContext(), "UMENG_CHANNEL"))
                .put("openId", openId)
                .put("name", name)
                .put("iconUrl", iconUrl)
                .put("sex", sex));
    }

    /**
     * /api/news-user/user/bound/{openId}
     * <p>用户--绑定微信--薛松</p>
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
     * /api/news-user/user/bound/{openId}
     * <p>用户--微信快速登录--薛松</p>
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

    /**
     * <<<<<<< HEAD
     * <<<<<<< HEAD
     * 查询用户反馈数据
     */
    public static Api requestFeedbackList(int page) {
        return Api.get("/api/news-user/feedback/page", new ReqParams()
                .put("page", page)
                .put("size", Apic.DEFAULT_PAGE_SIZE));
    }

    /**
     * 提交用户反馈数据
     */
    public static Api requestSendFeedback(String content, int contentType) {
        return Api.post("/api/news-user/feedback/add", new ReqParams()
                .put("content", content)
                .put("contentType", contentType));
    }

    /**
     * 用户--资讯收藏--薛松
     * =======
     * /api/news-user/operate/collect/{id}
     * <p>用户--收藏--薛松</p>
     * >>>>>>> dev
     *
     * @param id         资讯id
     * @param collectNum 0-收藏 1-取消收藏
     * @return
     */
    public static Api requestCollect(String id, int collectNum) {
        return Api.post("/api/news-user/operate/collect/{id}", new ReqParams()
                .put("id", id).put("type", 0)
                .put("cancel", collectNum));
    }

    /**
     * /api/news-user/banner/findBannerList.do
     * <p>首页--获取banner--薛松</p>
     *
     * @return
     */
    public static Api requestBanners() {
        return Api.get("/api/news-user/banner/findBannerList.do",
                new ReqParams()
                        .put("showType", 0));
    }

    // TODO: 2018/2/8 请求运营微信账户
    public static Api requestOperationWetchatAccount() {
        return Api.get("");
    }

    /**
     * /api/news-user/login/logout
     * <p>用户--退出登录--薛松</p>
     *
     * @return
     */
    public static Api logout() {
        return Api.post("/api/news-user/login/logout");
    }

    // TODO: 2018/2/9 解绑微信号
    public static Api unbindWeChatAccount() {
        return Api.post("");
    }

    /**
     * /api/news-quota/quota/{code}/trend
     * <p>分时图列表</p>
     *
     * @param exchangeCode
     * @param code
     */
    // TODO: 2018/2/6 没有完善
    public static Api requestTimeSharingPlanData(String code, String exchangeCode) {
        return Api.get("/api/news-quota/quota/{code}/trend",
                new ReqParams()
                        .put("code", code)
                        .put("exchangeCode", exchangeCode));

    }

    /**
     * /dic.html
     * <p>查询行情界面是否显示接口</p>
     */
    public static Api requestShowMarketPageSwitch() {
        return Api.get("/dic.html");
    }

    /**
     * 请求阅读历史数据 或者 收藏
     *
     * @param type
     * @param page
     */
    // TODO: 2018/2/11
    public static Api requestReadHistoryOrMyCollectData(int type, int page) {
        return Api.get("/api/news-user/operate/list/{type}",
                new ReqParams()
                        .put("type", type)
                        .put("page", page)
                        .put("size", DEFAULT_PAGE_SIZE));
    }

    /**
     * GET
     * 用户--用户详情--薛松
     */
    public static Api requestUserInfo() {
        return Api.get("/api/news-user/user/info");
    }

    /**
     * PUT
     * 用户--修改用户信息--薛松
     *
     * @return
     */
    public static Api submitUserIntroduce(String introduction) {
        return Api.post("/api/news-user/user/update",
                new ReqParams()
                        .put("introduction", introduction));
    }

    public static Api submitNickName(String nickName) {
        return Api.post("/api/news-user/user/update", new ReqParams().put("userName", nickName));
    }

    public static Api updateUserInfo(String province, String city, String birthday, Integer userSex) {
        return Api.post("/api/news-user/user/update",
                new ReqParams()
                        .put("userProvince", province)
                        .put("userCity", city)
                        .put("birthday", birthday)
                        .put("userSex", userSex));
    }

    /**
     * @param file
     */
    public static Api submitFile(File file, String fileName) {
        return Api.post("/api/zuul/news-user/upload/file.do",
                new ReqParams()
                        .put("file", file),
                fileName,
                file);
    }

    public static Api submitPortraitPath(String data) {
        return Api.post("/api/news-user/user/update",
                new ReqParams().put("userPortrait", data));
    }

    public static Api uploadImage(String picture) {
        return Api.post("/api/news-user/upload/image.do",
                new ReqParams().put("picture", picture));
    }

    /**
     * /api/news-quota/quota/{code}
     * <p>请求单个数字货币的行情</p>
     *
     * @param code
     * @param exchangeCode
     * @return
     */
    public static Api reqSingleMarket(String code, String exchangeCode) {
        return Api.get("/api/news-quota/quota/{code}",
                new ReqParams()
                        .put("code", code)
                        .put("exchangeCode", exchangeCode));
    }

    /**
     * /api/news-quota/quota/{code}/k
     * <p>获取 k 线数据</p>
     *
     * @param code
     * @param exchangeCode
     * @param klineType
     * @return
     */
    public static Api reqKlineMarket(String code, String exchangeCode, String klineType, String startTime) {
        return Api.get("/api/news-quota/quota/{code}/k",
                new ReqParams()
                        .put("code", code)
                        .put("exchangeCode", exchangeCode)
                        .put("type", klineType)
                        .put("startTime", startTime)
                        .put("limit", 100));
    }

    public interface url {
        String SHARE_NEWS = Api.getFixedHost() + "/news/share/index.html?id=%s";
        String QR_CODE = Api.getFixedHost() + "/qc.png";
    }
}
