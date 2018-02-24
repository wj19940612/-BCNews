package com.sbai.bcnews.model.market;

import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.utils.DateUtil;

/**
 * Created by ${wangJie} on 2018/2/6.
 * {@link Apic# /api/news-quota/quota/{code}/trend}
 */

public class DigitalCashTrendData {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String nowVolume;
    private String closePrice; // 最新价
    private long time;
    private long timeStamp;
    private String code;
    private String day;

    public long getNowVolume() {
        return Long.valueOf(nowVolume).longValue();
    }

    public float getClosePrice() {
        return Float.valueOf(closePrice).floatValue();
    }

    public String getRawClosePrice() {
        return closePrice;
    }

    public boolean isSameData(DigitalCashTrendData unstableData) {
        return nowVolume.equals(unstableData.getNowVolume())
                && closePrice.equals(unstableData.getRawClosePrice());
    }

    public String getHHmm() {
        return DateUtil.format(time, "HH:mm");
    }

}
