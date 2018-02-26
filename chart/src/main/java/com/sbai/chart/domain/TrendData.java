package com.sbai.chart.domain;

import android.support.annotation.NonNull;

/**
 * Modified by john on 24/02/2018
 * <p>
 * Description:
 * <p>
 * APIs:
 */
public class TrendData implements Comparable<TrendData> {

    private float closePrice;
    private String time;
    private double nowVolume;
    private long timeStamp;

    public float getClosePrice() {
        return closePrice;
    }

    public String getTime() {
        return time;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getNowVolume() {
        return nowVolume;
    }

    @Override
    public int compareTo(@NonNull TrendData o) {
        return (int) (this.timeStamp - o.getTimeStamp());
    }
}
