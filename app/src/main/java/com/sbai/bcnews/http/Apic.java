package com.sbai.bcnews.http;

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

    public static final String DOWNLOAD_URL = Api.getHost() + "/qc.png";

    /**
     * 资讯详情 - 齐慕伟
     *
     * @param id
     * @return
     */
    public static Api getNewsDetail(String id) {
        return Api.get("/api/news-info/info/details/{id}", new ReqParams().put("id", id));
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

    /**
     * 资讯点赞 - 齐慕伟
     *
     * @param newsId
     * @return
     */
    public static Api praiseNews(String newsId) {
        return Api.put("/api/news-info/info/like/{id}", new ReqParams().put("id", newsId));
    }

    /**
     * /api/news-quota/quota/{code}/trend
     * POST
     * 分时图列表
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
     * 查询行情界面是否显示接口
     */
    public static Api requestShowMarketPageSwitch() {
        return Api.get("/dic.html");
    }

    /**
     * 请求当个数字货币的行情
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
     * 获取 k 线数据
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
}
