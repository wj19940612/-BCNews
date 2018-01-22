package com.sbai.bcnews;

import android.app.Application;
import android.content.Context;

import com.igexin.sdk.PushManager;
import com.sbai.bcnews.service.PushIntentService;
import com.sbai.bcnews.service.PushService;
import com.umeng.socialize.UMShareAPI;

public class App extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        UMShareAPI.get(this);
        //init getui sdk
        PushManager.getInstance().initialize(this, PushService.class);
        //init getui service
        PushManager.getInstance().registerPushIntentService(this, PushIntentService.class);
        handleUncaughtException();
    }

    private void handleUncaughtException() {
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                System.exit(1);
            }
        });
    }

    public static Context getAppContext() {
        return sContext;
    }

}
