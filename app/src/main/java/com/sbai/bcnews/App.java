package com.sbai.bcnews;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.sbai.bcnews.activity.CrashInfoActivity;
import com.sbai.bcnews.activity.MainActivity;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.OnlineTime;
import com.sbai.bcnews.model.SysTime;
import com.sbai.bcnews.service.PushIntentService;
import com.sbai.bcnews.service.PushService;
import com.sbai.bcnews.utils.BuildConfigUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.ScreenShotListenManager;
import com.sbai.bcnews.utils.TimerHandler;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.hashrate.HashRateTimeCahche;
import com.sbai.bcnews.view.ScreenShotView;
import com.sbai.httplib.ReqLogger;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description:
 */
public class App extends Application {

    private static Context sContext;

    public static final String TAG = "Application";

    public static final int DEFAULT_TIME = 1000;
    //    public static final int DEFAULT_COUNT_MAX = 30 * 60;
    public static final int DEFAULT_COUNT_MAX = 30;

    //app连续停留前台的时间
    private TimerHandler mBaseHandler;
    //    private int mBaseTimingCount;
    private OnlineTime mOnlineTime;
    private boolean TimingStarted;
    private int mFinalCount;  //activity在前台的数量


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Api.init(sContext.getCacheDir(), sContext.getFilesDir());
        Api.setLogger(new ReqLogger() {
            @Override
            public void onTag(String log) {
                Log.d("VolleyHttp", log);
            }
        });

        UMShareAPI.get(this);
        //init getui sdk
        PushManager.getInstance().initialize(this, PushService.class);
//        //init getui service
        PushManager.getInstance().registerPushIntentService(this, PushIntentService.class);
        processCaughtException();
        registerFrontBackCallback();
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

    private void registerFrontBackCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                Log.d(TAG, " onStart");
//                if (mFinalCount == 0) {
//                    Log.d(TAG, "onStart result");
                startTiming();
//                }
                mFinalCount++;

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                Log.d(TAG, " onStop");
                mFinalCount--;
                if (mFinalCount == 0) {
                    Log.d(TAG, "onStop result");
                    endTiming();
                }

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void startTiming() {
        if (LocalUser.getUser().isLogin() && !TimingStarted) {
            Log.d(TAG, "startTiming");
            mOnlineTime = HashRateTimeCahche.getOnlineTime(LocalUser.getUser().getUserInfo().getId());
            if (mOnlineTime != null && mOnlineTime.getOnlineTime() < DEFAULT_COUNT_MAX) {
                //跨天了,重置
                if (!DateUtil.isInThisDay(mOnlineTime.getDay(), SysTime.getSysTime().getSystemTimestamp())) {
                    mOnlineTime.setOnlineTime(0);
                    mOnlineTime.setDay(SysTime.getSysTime().getSystemTimestamp());
                }
                //换用户了
                if (mOnlineTime.getUserId() != LocalUser.getUser().getUserInfo().getId()) {
                    mOnlineTime.setOnlineTime(0);
                    mOnlineTime.setUserId(LocalUser.getUser().getUserInfo().getId());
                }
                startFrontTiming();
                TimingStarted = true;
            }
        }
    }

    private void startFrontTiming() {
        stopFrontTiming();

        if (mBaseHandler == null) {
            mBaseHandler = new TimerHandler(new TimerHandler.TimerCallback() {
                @Override
                public void onTimeUp(int count) {
                    if (LocalUser.getUser().isLogin()) {
                        mOnlineTime.setOnlineTime(mOnlineTime.getOnlineTime() + DEFAULT_TIME / 1000);
                        if (!DateUtil.isInThisDay(mOnlineTime.getDay(), SysTime.getSysTime().getSystemTimestamp())) {
                            mOnlineTime.setDay(SysTime.getSysTime().getSystemTimestamp());
                        }
                        Log.d(TAG, getClass().getName() + " time:" + mOnlineTime.getOnlineTime());
                        if (mOnlineTime.getOnlineTime() == DEFAULT_COUNT_MAX) {
                            ToastUtil.show("结束了");
                            stopFrontTiming();
                        }
                    }
                }
            });
        }
        mBaseHandler.sendEmptyMessage(DEFAULT_TIME);
    }

    private void endTiming() {
        if (TimingStarted) {
            Log.d(TAG, "endTiming");
            stopFrontTiming();
            recordFrontTiming();
            TimingStarted = false;
        }
    }

    private void stopFrontTiming() {
        if (mBaseHandler != null) {
            mBaseHandler.removeCallbacksAndMessages(null);
            mBaseHandler.resetCount();
        }
    }

    private void recordFrontTiming() {
        if (LocalUser.getUser().isLogin() && mOnlineTime != null) {
//            new CacheThread(mOnlineTime).run();
            HashRateTimeCahche.updateOnlineTime(mOnlineTime);
        }
    }

    public static Context getAppContext() {
        return sContext;
    }

    static {
        // 注意：测试用 appId & secret
        PlatformConfig.setWeixin("wx7576ec9bb65aea1a", "d640a05c70ec272f56557bd7e9c15dc4");
        PlatformConfig.setQQZone("1106465763", "qYrMZDFn2dn5KQhP");
    }
}
