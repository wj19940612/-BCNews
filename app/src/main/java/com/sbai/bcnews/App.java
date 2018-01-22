package com.sbai.bcnews;

import android.app.Application;
import android.content.Intent;

import com.sbai.bcnews.activity.CrashInfoActivity;
import com.sbai.bcnews.utils.BuildConfigUtils;

/**
 * Created by ${wangJie} on 2018/1/22.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        processCaughtException();
    }

    private void processCaughtException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                if (BuildConfigUtils.isProductFlavor()) {
                    submitErrorInfoToServers(e);
                } else {
                    openCrashInfoPage(e);
                }
                System.exit(1);
            }
        });
    }

    private void openCrashInfoPage(Throwable e) {
        Intent intent = new Intent();
        intent.setClass(this.getApplicationContext(), CrashInfoActivity.class);
        intent.putExtra(ExtraKeys.CRASH_INFO, e);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void submitErrorInfoToServers(Throwable e) {
        // TODO: 2018/1/22 上传至服务器

        Intent intent = new Intent();
        intent.setClass(this.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
