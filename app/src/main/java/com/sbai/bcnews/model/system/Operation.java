package com.sbai.bcnews.model.system;

import com.sbai.bcnews.http.Apic;

/**
 * Created by ${wangJie} on 2018/2/26.
 * {@link Apic# /api/news-user/dictionary/json.do}
 */

public class Operation {

    public static final String OPERATION_TYPE_MARKET_PAGE_SWITCH = "quota";
    public static final String OPERATION_TYPE_WE_CHAT = "SYS_OPERATE_WX";

    public static final String OPERATION_SETTING_OPEN_MARKET_PAGE = "1";

    private String SYS_OPERATE_WX;

    private String quota;

    public String getSYS_OPERATE_WX() {
        return SYS_OPERATE_WX;
    }

    public void setSYS_OPERATE_WX(String SYS_OPERATE_WX) {
        this.SYS_OPERATE_WX = SYS_OPERATE_WX;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }
}
