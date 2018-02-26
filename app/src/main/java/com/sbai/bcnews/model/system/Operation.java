package com.sbai.bcnews.model.system;

import com.sbai.bcnews.http.Apic;

/**
 * Created by ${wangJie} on 2018/2/26.
 * {@link Apic# /api/news-user/dictionary/json.do}
 */

public class Operation {

    public static final String OPERATION_REQ_TYPE_WECHAT = "SYS_OPERATE_WX";

    private String SYS_OPERATE_WX;

    public String getSYS_OPERATE_WX() {
        return SYS_OPERATE_WX;
    }

    public void setSYS_OPERATE_WX(String SYS_OPERATE_WX) {
        this.SYS_OPERATE_WX = SYS_OPERATE_WX;
    }
}
