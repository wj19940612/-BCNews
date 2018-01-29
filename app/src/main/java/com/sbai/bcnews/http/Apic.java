package com.sbai.bcnews.http;

import com.sbai.httplib.ReqParams;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description: 统一管理 api 请求
 * <p>
 */
public class Apic {

    public static Api syncSystemTime() {
        return Api.get("/user/user/getSystemTime.do");
    }

    public static Api getNewsFlash(long time) {
        return Api.get("/api/news-info/info/information.do", new ReqParams().put("time", time));
    }

}
