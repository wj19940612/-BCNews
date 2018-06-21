package com.sbai.bcnews;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.sbai.bcnews.activity.CrashInfoActivity;
import com.sbai.bcnews.activity.MainActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.OnlineTime;
import com.sbai.bcnews.model.SysTime;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.service.PushIntentService;
import com.sbai.bcnews.service.PushService;
import com.sbai.bcnews.utils.BuildConfigUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.TimerHandler;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.hashrate.HashRateTimeCahche;
import com.sbai.httplib.ReqLogger;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * Modified by john on 23/01/2018
 * <p>
 * Description:
 */
public class App extends Application implements TimerHandler.TimerCallback {

    private static Context sContext;

    public static final String TAG = "Application";

    public static final int DEFAULT_TIME = 1000;
    public static final int DEFAULT_COUNT_MAX = 30 * 60;
//    public static final int DEFAULT_COUNT_MAX = 20;

    //app连续停留前台的时间
    private TimerHandler mBaseHandler;
    //    private int mBaseTimingCount;
    private OnlineTime mOnlineTime;
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
        registerLoginBroadcast();
        registerFrontBackCallback();
    }

    private BroadcastReceiver mLoginBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(LoginActivity.ACTION_LOGIN_SUCCESS)) {
                startTiming();
            }
        }
    };


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

    private void registerLoginBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginActivity.ACTION_LOGIN_SUCCESS);
        LocalBroadcastManager.getInstance(getAppContext())
                .registerReceiver(mLoginBroadcastReceiver, intentFilter);

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

    /**
     * 监听app前后台切换
     */
    private void registerFrontBackCallback() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (mFinalCount == 0) {
                    startTiming();
                }
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
                mFinalCount--;
                if (mFinalCount == 0) {
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
        if (LocalUser.getUser().isLogin()) {
            mOnlineTime = HashRateTimeCahche.getOnlineTime(LocalUser.getUser().getUserInfo().getId());
            if (mOnlineTime != null) {
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
                if (mOnlineTime.getOnlineTime() < DEFAULT_COUNT_MAX) {
                    startFrontTiming();
                }
            }
        }
    }

    private void startFrontTiming() {
        stopFrontTiming();

        if (mBaseHandler == null) {
            mBaseHandler = new TimerHandler(this);
        }
        mBaseHandler.sendEmptyMessage(DEFAULT_TIME);
    }

    @Override
    public void onTimeUp(int count) {
        if (LocalUser.getUser().isLogin()) {
            mOnlineTime.setOnlineTime(mOnlineTime.getOnlineTime() + DEFAULT_TIME / 1000);
            if (!DateUtil.isInThisDay(mOnlineTime.getDay(), SysTime.getSysTime().getSystemTimestamp())) {
                mOnlineTime.setDay(SysTime.getSysTime().getSystemTimestamp());
            }
            if (mOnlineTime.getOnlineTime() == DEFAULT_COUNT_MAX) {
                stopFrontTiming();
                onlineAddQKC();
            }
        }
    }

    private void onlineAddQKC() {
        Apic.requestHashRate(QKC.TYPE_ONLINE).tag(TAG).callback(new Callback2D<Resp<Integer>, Integer>() {

            @Override
            protected void onRespSuccessData(Integer data) {
                if (data != null && data > 0) {
                    ToastUtil.show(getAppContext().getString(R.string.get_online_hash_data_x, data));
                }
            }
        }).fireFreely();
    }

    private void endTiming() {
        stopFrontTiming();
        recordFrontTiming();
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
