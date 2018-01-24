package com.sbai.bcnews.utils;


import com.sbai.bcnews.Preference;

import java.util.Date;

public class TimeRecorder {

    public static void record(String key) {
        Preference.get().setTimestamp(key, new Date().getTime());
    }

    private static long getElapsedTime(String key) {
        long recordedTime = Preference.get().getTimestamp(key);
        return new Date().getTime() - recordedTime;
    }

    public static long getElapsedTimeInMinute(String key) {
        return getElapsedTimeInSecond(key) / 60;
    }

    public static long getElapsedTimeInSecond(String key) {
        return getElapsedTime(key) / 1000;
    }

    public static long getElapsedTimeInMillis(String key) {
        return getElapsedTime(key);
    }

}
