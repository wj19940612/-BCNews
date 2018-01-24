package com.sbai.bcnews.model;

import com.sbai.bcnews.Preference;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.utils.TimeRecorder;

import java.util.Date;

public class SysTime {

    private static final String RECORD_KEY = "SysTime";

    private static SysTime sSysTime;

    public static SysTime getSysTime() {
        if (sSysTime == null) {
            sSysTime = new SysTime();
        }
        return sSysTime;
    }

    private long mSystemTime;

    public void sync() {
        if (Math.abs(TimeRecorder.getElapsedTimeInMinute(RECORD_KEY)) < 10) return;

        //TODO 获取时间
        Apic.syncSystemTime()
                .timeout(5 * 1000)
                .callback(new Callback<Resp<Long>>() {
                    @Override
                    protected void onRespSuccess(Resp<Long> resp) {

                    }
                }).fire();
    }

    public long getSystemTimestamp() {
        if (mSystemTime == 0) {
            mSystemTime = Preference.get().getServerTime();
            if (mSystemTime == 0) {
                return new Date().getTime();
            }
        }
        return mSystemTime + TimeRecorder.getElapsedTimeInMillis(RECORD_KEY);
    }
}
