package com.sbai.bcnews.http;

import com.sbai.bcnews.App;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.model.news.WriteComment;
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
     * /api/news-user/author/page/bitcoin.do
     * GET
     * 用户查询作者文章--薛松
     *
     * @param page
     * @param authorId
     * @return
     */
    public static Api requestAuthorArticle(int page, int authorId) {
        return Api.get("/api/news-user/author/page/bitcoin.do",
                new ReqParams()
                        .put("page", page)
                        .put("authorId", authorId)
                        .put("size", DEFAULT_PAGE_SIZE));
    }

    /**
     * /api/news-user/author/workbench.do
     * GET
     * 作者工作台--薛松
     *
     * @return
     */
    public static Api requestWorkbenchAuthorInfo() {
        return Api.get("/api/news-user/author/workbench.do");
    }

    /**
     * /api/news-user/author/detail.do
     * GET
     * 作者详情--薛松
     *
     * @return
     */
    public static Api requestAuthorInfo(int authorId) {
        return Api.get("/api/news-user/author/detail.do", new ReqParams().put("authorId", authorId));
    }

    /**
     * HTTP
     * /api/news-user/author/bitcoin.do
     * GET
     * 作者的文章--薛松
     *
     * @return
     */
    public static Api requestAuthorWorkbenchArticle(int page) {
        return Api.get("/api/news-user/author/bitcoin.do", new ReqParams().put("page", page).put("size", DEFAULT_PAGE_SIZE));
    }


    /**
     * /api/news-user/author/concern.do
     * POST
     * 关注、取消关注作者--薛松
     *
     * @param authorId
     * @param type     1关注,0取消关注
     * @return
     */
    public static Api attentionAuthor(int authorId, int type) {
        return Api.post("/api/news-user/author/concern.do",
                new ReqParams()
                        .put("authorId", authorId)
                        .put("type", type));
    }


    public interface url {
        String SHARE_NEWS = Api.getFixedHost() + "/news/share/index.html?id=%s";

        //作者详情页
        String SHARE_AUTHOR = Api.getFixedHost() + "/news/author/authordetail.html?id=%s";

        String QR_CODE = Api.getFixedHost() + "/qc.png";

        //关于我们界面链接
        String WEB_URI_ABOUT_PAGE = Api.getFixedHost() + "/news/banner/about.html?version=%s";
        //用户协议
        String WEB_URI_AGREEMENT = Api.getFixedHost() + "/news/banner/agreement.html?code=1";

        //邀请有礼
        String MINE_INVITE_HAS_GIFT = Api.getFixedHost() + "/news/appinvite/inviteindex.html";
        //qkc介绍页
        String QKC_INTRODUCE_PAGE_URL = Api.getFixedHost() + "/news/appinvite/mentoring.html";

    }

    /**
     * 首页--banner点击
     */
    public static Api requesBannerUpdate(String bannerId) {
        return Api.post("/api/news-user/banner/click", new ReqParams().put("id", bannerId));
    }

    /**
     * 用户--同步阅读记录--薛松
     */
    public static Api uploadReadHistory(String readText, String deviceId) {
        return Api.post("/api/news-user/operate/read/device", new ReqParams().put("read", readText).put("deviceId", deviceId));
    }

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
        return Api.get("/api/news-info/news/detail/{id}",
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
        return Api.get("/api/news-user/user/time.do");
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
     * /api/news-quota/variety/list
     * GET
     * 行情品种列表
     *
     * @return
     */
    public static Api requestMarketVarietyList(int page, int size, String exchangeCode) {
        return Api.get("/api/news-quota/variety/list", new ReqParams()
                .put("page", page)
                .put("size", size)
                .put("exchangeCode", exchangeCode));
    }


    /**
     * /api/news-info/news/PRAISE/{id}
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
    public static String getImageAuthCode(String phone) {
        return Api.getFixedHost() + "/api/news-user/login/download/image/" + phone;
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
    public static Api requestBindWeChat(String openId, String name, String iconUrl) {
        return Api.post("/api/news-user/user/bound/{openId}", new ReqParams()
                .put("openId", openId)
                .put("name", name)
                .put("iconUrl", iconUrl));
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
     * 查询用户反馈数据
     */
    public static Api requestFeedbackList(int page) {
        return Api.get("/api/news-user/feedback/page", new ReqParams()
                .put("page", page)
                .put("size", Apic.DEFAULT_PAGE_SIZE)
                .put("deviceId", Preference.get().getPushClientId()));
    }

    /**
     * 提交用户反馈数据
     */
    public static Api requestSendFeedback(String content, int contentType) {
        return Api.post("/api/news-user/feedback/add", new ReqParams()
                .put("content", content)
                .put("contentType", contentType)
                .put("deviceId", Preference.get().getPushClientId()));
    }

    /**
     * 用户--资讯收藏--薛松
     * /api/news-user/operate/collect/{id}
     *
     * @param id         资讯id
     * @param collectNum 0-收藏 1-取消收藏
     * @return
     */
    public static Api collectOrCancelCollect(String id, int collectNum, int type) {
        return Api.post("/api/news-user/operate/collect/{id}", new ReqParams()
                .put("id", id)
                .put("type", type)
                .put("cancel", collectNum));
    }

    /**
     * /api/news-user/banner/findBannerList.do
     * <p>首页--获取banner--薛松</p>
     *
     * @return
     */
    public static Api requestBanners() {
        return Api.get("/api/news-user/banner/findBannerList.do");
    }

    public static Api requestOperationSetting(String type) {
        return Api.get("/api/news-user/dictionary/json.do", new ReqParams().put("type", type));
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

    /**
     * POST
     * 用户--取消微信绑定--薛松
     *
     * @return
     */
    public static Api unbindWeChatAccount() {
        return Api.post("/api/news-user/user/bound/cancel");
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
     * 请求阅读历史数据 或者 收藏
     *
     * @param type
     * @param page
     */
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
    public static Api reqKlineMarket(String code, String exchangeCode, String klineType, String endTime) {
        return Api.get("/api/news-quota/quota/{code}/k",
                new ReqParams()
                        .put("code", code)
                        .put("exchangeCode", exchangeCode)
                        .put("type", klineType)
                        .put("endTime", endTime)
                        .put("limit", 100));
    }

    /**
     * /api/news-quota/quota/{code}/trend
     * <p>获取分时图数据</p>
     *
     * @param code
     * @param exchangeCode
     * @param endTime
     */
    public static Api reqTrendData(String code, String exchangeCode, String endTime) {
        return Api.get("/api/news-quota/quota/{code}/trend",
                new ReqParams()
                        .put("code", code)
                        .put("exchangeCode", exchangeCode)
                        .put("endTime", endTime));
    }

    /**
     * /api/news-user/operate/read/coolect/count
     * GET
     * 用户--收藏阅读数量--薛松
     *
     * @return
     */
    public static Api requestUserReadOrCollectNumber() {
        return Api.get("/api/news-user/operate/read/coolect/count");
    }

    public static Api share(Object id, int type) {
        return Api.post("/api/news-info/news/share/{id}",
                new ReqParams()
                        .put("id", id)
                        .put("type", type));
    }

    public static Api reqUseWxInfo() {
        return Api.post("/api/news-user/user/use/wx");
    }

    /**
     * 每日首次激活APP获取积分
     *
     * @return
     */
    public static Api requestFirstIntegral() {
        return Api.post("/api/news-user/integral/online.do");
    }

    /**
     * 获取兑换商品列表
     *
     * @param exchangeType 0-数字货币 1-支付宝 2-话费
     * @return
     */
    public static Api requestConversionGoods(int exchangeType) {
        return Api.get("/api/news-user/product/list.do", new ReqParams().put("exchangeType", exchangeType));
    }

    /**
     * 获取我的算力和积分
     *
     * @return
     */
    public static Api requestMyIntegral() {
        return Api.get("/api/news-user/integral/my.do");
    }

    /**
     * 获取用户算力绑定信息
     *
     * @return
     */
    public static Api requestConversionAddress() {
        return Api.get("/api/news-user/integral/account.do");
    }

    /**
     * 绑定、修改账户(提币地址)--薛松
     *
     * @param extractCoinAddress
     * @return
     */
    public static Api submitCoinAddress(String extractCoinAddress) {
        return Api.post("/api/news-user/integral/update.do", new ReqParams().put("extractCoinAddress", extractCoinAddress));
    }

    /**
     * 绑定、修改账户(支付宝地址)--薛松
     *
     * @param aliPay
     * @param aliPayName
     * @return
     */
    public static Api submitAliPayAddress(String aliPay, String aliPayName) {
        return Api.post("/api/news-user/integral/update.do", new ReqParams().put("aliPay", aliPay).put("aliPayName", aliPayName));
    }

    /**
     * 绑定、修改账户(手机地址)--薛松
     *
     * @param phone
     * @param phoneName
     * @param msgCode
     * @return
     */
    public static Api submitTelephoneAddress(String phone, String phoneName, String msgCode) {
        return Api.post("/api/news-user/integral/update.do", new ReqParams().put("phone", phone).put("phoneName", phoneName).put("msgCode", msgCode));
    }

    /**
     * 兑换物品
     *
     * @param pId
     * @return
     */
    public static Api exchangeGood(String pId) {
        return Api.post("/api/news-user/integral/exchange.do", new ReqParams().put("pId", pId));
    }

    /**
     * 获取兑换历史
     *
     * @param page
     * @return
     */
    public static Api requestExchangeHistory(int page) {
        return Api.get("/api/news-user/product/exchange/list.do", new ReqParams().put("page", page).put("size", DEFAULT_PAGE_SIZE));
    }

    /**
     * 获取算力
     *
     * @param type 通过类型
     * @return
     */
    public static Api requestHashRate(int type) {
        return Api.post("/api/news-user/rate/add.do", new ReqParams().put("rateType", type));
    }

    /**
     * /api/news-msg/msg/history
     * GET
     * <p>用户--消息列表--薛松</p>
     *
     * @param startTime
     * @return
     */
    public static Api requestMessage(Long startTime) {
        return Api.get("/api/news-msg/msg/history",
                new ReqParams()
                        .put("size", DEFAULT_PAGE_SIZE)
                        .put("createTime", startTime));
    }


    /**
     * /api/news-msg/msg/new
     * GET
     * 用户--是否有新的消息(包括新的反馈)--薛松
     */
    public static Api requestWhetherHasAllNotReadMessage() {
        return Api.get("/api/news-msg/msg/new", new ReqParams().put("deviceId", Preference.get().getPushClientId()));
    }

    /**
     * /api/news-user/user/use/push
     * POST
     * 推送设置----齐慕伟
     *
     * @param notificationType
     * @param status
     * @return
     */
    public static Api switchNotificationStatus(int notificationType, int status) {
        return Api.post("/api/news-user/user/use/push", new ReqParams().put("type", notificationType).put("status", status));
    }

    /**
     * /api/news-msg/msg/read/all
     * POST
     * 用户--全部阅读--薛松
     *
     * @return
     */
    public static Api postAllMessageRead() {
        return Api.post("/api/news-msg/msg/read/all");
    }

    /**
     * /api/news-msg/msg/read
     * POST
     * 用户--阅读消息(读一条)--薛松
     *
     * @param id
     */
    public static Api readMessage(String id) {
        return Api.post("/api/news-msg/msg/read", new ReqParams().put("msgId", id));
    }

    /**
     * /api/news-info/discuss/opinion/{id}
     * GET
     * 各方观点----齐慕伟
     *
     * @param id
     * @return
     */
    public static Api requestNewsViewpoint(String id) {
        return Api.get("/api/news-info/discuss/opinion/{id}", new ReqParams().put("id", id));
    }

    /**
     * /api/news-info/discuss/comment
     * POST
     * 发表评论/回复----齐慕伟
     *
     * @param writeComment
     */
    public static Api submitComment(WriteComment writeComment) {
        return Api.post("/api/news-info/discuss/comment", new ReqParams(WriteComment.class, writeComment));
    }

    /**
     * /api/news-info/discuss/first/{id}
     * GET
     * 全部评论----齐慕伟
     *
     * @param id
     * @param page
     * @param pageSize
     */
    public static Api requestNewsViewpointList(String id, int page, int pageSize) {
        return Api.get("/api/news-info/discuss/first/{id}", new ReqParams().put("id", id).put("page", page).put("size", pageSize));
    }

    /**
     * /api/news-user/report/add.do
     * POST
     * 用户--举报--薛松
     *
     * @return
     */
    public static Api submitWhistleBlowing(String dataId, int type, String reson) {
        return Api.post("/api/news-user/report/add.do",
                new ReqParams()
                        .put("dataId", dataId)
                        .put("type", type)
                        .put("reason", reson));
    }

    /**
     * /api/news-info/discuss/PRAISE
     * POST
     * 点赞----齐慕伟
     *
     * @param id
     * @param dataId
     * @param userId
     * @param type
     */
    public static Api praiseComment(String id, String dataId, Integer userId, int type) {
        return Api.post("/api/news-info/discuss/praise",
                new ReqParams()
                        .put("id", id)
                        .put("dataId", dataId)
                        .put("userId", userId)
                        .put("type", type));
    }

    /**
     * /api/news-info/discuss/second/{id}
     * GET
     * 评论详情----齐慕伟
     *
     * @param newsId
     * @param page
     * @param pageSize
     * @param viewpointId
     */
    public static Api requestCommentList(String newsId, int page, int pageSize, String viewpointId) {
        return Api.get("/api/news-info/discuss/second/{id}",
                new ReqParams()
                        .put("dataId", newsId)
                        .put("page", page)
                        .put("size", pageSize)
                        .put("id", viewpointId));
    }

    /**
     * /api/news-info/discuss/mine/comment
     * GET
     * 我评论的----齐慕伟
     */
    public static Api requestMineReplyOrCommentViewpointList(int page, int type, int size) {
        return Api.get("/api/news-info/discuss/mine/comment",
                new ReqParams()
                        .put("page", page)
                        .put("size", size)
                        .put("type", type));
    }

    /**
     * /api/news-user/report/reason/{type}
     * GET
     * 用户--举报理由--薛松
     *
     * @param whistleBlowingType
     */
    public static Api requestWhistleBlowingList(int whistleBlowingType) {
        return Api.get("/api/news-user/report/reason/{type}", new ReqParams().put("type", whistleBlowingType));
    }

    /**
     * /api/news-info/discuss/delete/{id}
     * POST
     * 删除评论--薛松
     *
     * @param type
     * @param dataId
     * @param id
     * @return
     */
    public static Api deleteReview(int type, String dataId, String id) {
        return Api.get("/api/news-info/discuss/delete/{id}", new ReqParams().put("type", type).put("dataId", dataId).put("id", id));
    }

    /**
     * /api/news-info/discuss/praise/status.do
     * POST
     * 是否点赞--薛松
     *
     * @param id
     * @return
     */
    public static Api requestViewpointPraiseStatus(String id) {
        return Api.post("/api/news-info/discuss/praise/status.do", new ReqParams().put("id", id));
    }

    /**
     * /api/news-user/redPacket/status.do
     * POST
     * 获取红包的状态--薛松
     */
    public static Api requestRedPacketStatus() {
        return Api.post("/api/news-user/redPacket/status.do");
    }

    /**
     * api/news-user/redPacket/user/status.do
     * GET
     * 用户抢红包的状态--薛松
     */
    public static Api requestUserRedPacketStatus() {
        return Api.get("/api/news-user/redPacket/user/status.do");
    }

    /**
     * /api/new-user/redPacket/rob.do
     * POST
     * 抢红包--薛松
     */
    public static Api robRedPacket() {
        return Api.post("/api/news-user/redPacket/rob.do");
    }

    /**
     * /api/news-user/redPacket/list.do
     * GET
     * 红包列表--薛松
     */
    public static Api requestHourWelfareList(int page, int pageSize) {
        return Api.get("/api/news-user/redPacket/list.do"
                , new ReqParams()
                        .put("page", page)
                        .put("size", pageSize));
    }

    /**
     * /api/news-user/integral/my.do
     * GET
     * QKC明细--我的qkc和算力--薛松
     */
    public static Api requestIntegral() {
        return Api.get("/api/news-user/integral/my.do");
    }

    /**
     * /api/news-user/log/not/get.do
     * GET
     * 未获取矿产--薛松
     */
    public static Api requestQKC() {
        return Api.get("/api/news-user/log/not/get.do");
    }

    /**
     * /api/news-user/log/get/integral.do
     * POST
     * 获取矿产--薛松
     */
    public static Api getQKC(String id) {
        return Api.post("/api/news-user/log/get/integral.do", new ReqParams().put("id", id));
    }

    /**
     * /api/news-user/log/page.do
     * GET
     * QKC明细--薛松
     */
    public static Api requestQKCDetailsList(int page) {
        return Api.get("/api/news-user/log/page.do",
                new ReqParams()
                        .put("page", page)
                        .put("size", DEFAULT_PAGE_SIZE));
    }

    /**
     * /api/news-user/user/show.do
     * POST
     * 邀请有礼、积分开关--薛松
     */
    public static Api requestQKCAndInviteHasGiftTabVisible() {
        return Api.get("/api/news-user/user/show.do");
    }

    /**
     * 注册
     *
     * @return
     */
    public static Api register(String phone, String code, String password) {
        return Api.post("/api/news-user/login/password/register.do", new ReqParams()
                .put("phone", phone)
                .put("code", code)
                .put("password", password)
                .put("deviceId", Preference.get().getPushClientId())
                .put("platform", 0)
                .put("source", AppInfo.getMetaData(App.getAppContext(), "UMENG_CHANNEL")));
    }

    /**
     * 密码登陆
     *
     * @param phone
     * @param password
     * @return
     */
    public static Api passLogin(String phone, String password) {
        return Api.post("/api/news-user/login/password/login.do", new ReqParams().put("phone", phone).put("password", password));
    }

    /**
     * 忘记密码-设置密码
     *
     * @return
     */
    public static Api forgetPass(String phone, String code, String password) {
        return Api.post("/api/news-user/user/forget.do", new ReqParams().put("phone", phone).put("code", code).put("password", password));
    }

    /**
     * 检测手机号状态 - 是否注册过等 type 0-注册 1-登陆
     *
     * @return
     */
    public static Api checkPhone(String phone, int type) {
        return Api.post("/api/news-user/login/check.do", new ReqParams().put("phone", phone).put("type", type));
    }

    /**
     * 设置密码
     *
     * @return
     */
    public static Api setPassword(String password) {
        return Api.post("/api/news-user/user/set/password.do", new ReqParams().put("password", password));
    }

    /**
     * 修改密码
     *
     * @return
     */
    public static Api modifyPassword(String oldPassword, String newPassword) {
        return Api.post("/api/news-user/user/modify/password.do", new ReqParams().put("oldPassword", oldPassword).put("newPassword", newPassword));
    }

    /**
     * 获取糖果列表
     */
    public static Api requestCandyList(int page) {
        return Api.get("/api/news-user/sweet/page.do", new ReqParams().put("page", page).put("size", DEFAULT_PAGE_SIZE));
    }

    /**
     * 点击糖果
     *
     * @param id 糖果id
     * @return
     */
    public static Api requestClickCandy(String id) {
        return Api.post("/api/news-user/sweet/click.do", new ReqParams().put("id", id));
    }

    /**
     * 领取糖果
     *
     * @return
     */
    public static Api receiveCandy(String id) {
        return Api.post("/api/news-user/sweet/receive.do", new ReqParams().put("id", id));
    }

    /**
     * 关注的作者列表
     * @return
     */
    public static Api requestAttentionAuthor(){
        return Api.get("/api/news-user/author/list/concern.do");
    }

    /**
     * 关注的文章列表
     * @return
     */
    public static Api requestAttentionArticle(int page){
        return Api.get("/api/news-user/author/list/concern/bitcoin.do",new ReqParams().put("page",page).put("size",DEFAULT_PAGE_SIZE));
    }

    /**
     * 获取推荐作者列表
     * @return
     */
    public static Api requestRecommendAuthorList(){
        return Api.get("/api/news-user/author/list/recommend.do");
    }

    /**
     * 关注/取消关注 作者
     * @param authorId 作者Id
     * @param type     1关注 0-取消关注
     * @return
     */
    public static Api requestConcernAuthor(int authorId,int type){
        return Api.post("/api/news-user/author/concern.do",new ReqParams().put("authorId",authorId).put("type",type));
    }

    /**
     * 启动弹窗
     * @return
     */
    public static Api requestStartWindow(){
        return Api.get("/api/news-user/window/list.do");
    }
}
