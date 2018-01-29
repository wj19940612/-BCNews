package com.sbai.bcnews.http;

import com.sbai.httplib.ReqParams;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description: 统一管理 api 请求
 * <p>
 */
public class Apic {

    public static Api getNewsDetail(String id) {
        return Api.get("/api/news-info/info/details/" + id);
    }

    public static Api syncSystemTime() {
        return Api.get("/user/user/getSystemTime.do");
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

}
