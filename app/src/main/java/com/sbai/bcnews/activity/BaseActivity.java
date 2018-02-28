package com.sbai.bcnews.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.sbai.bcnews.Preference;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ScreenShotListenManager;
import com.sbai.bcnews.utils.SecurityUtil;
import com.sbai.bcnews.utils.TimerHandler;
import com.sbai.bcnews.view.RequestProgress;
import com.sbai.bcnews.view.ScreenShotView;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.httplib.ReqIndeterminate;
import com.umeng.analytics.MobclickAgent;

import java.security.NoSuchAlgorithmException;

/**
 * Modified by john on 18/01/2018
 * <p>
 * Description:
 */
public class BaseActivity extends StatusBarActivity implements
        ReqIndeterminate, TimerHandler.TimerCallback {

    public static final String ACTION_TOKEN_EXPIRED = "com.sbai.fin.token_expired";
    public static final String ACTION_LOGIN_SUCCESS = "com.sbai.fin.login_success";
    public static final String ACTION_LOGOUT_SUCCESS = "com.sbai.fin.logout_success";
    public static final String ACTION_REWARD_SUCCESS = "com.sbai.fin.reward_success";

    public static final String EX_TOKEN_EXPIRED_MESSAGE = "token_expired_msg";

    public static final int REQ_QUESTION_DETAIL = 802;
    public static final int REQ_CODE_LOGIN = 803;
    public static final int REQ_SUBMIT_QUESTION_LOGIN = 1002;
    public static final int REQ_CODE_COMMENT = 1001;

    protected String TAG;

    private TimerHandler mTimerHandler;
    private RequestProgress mRequestProgress;
    private ScreenShotListenManager mScreenShotListenManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_TOKEN_EXPIRED.equalsIgnoreCase(intent.getAction())) {
                LocalUser.getUser().logout();
                Launcher.with(getActivity(), MainActivity.class).execute();

                //TODO 跳转登陆页
//                Launcher.with(getActivity(), LoginActivity.class).execute();
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mRequestProgress = new RequestProgress(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Api.cancel(TAG);
            }
        });
        //TODO 服务器还没这个接口，经常打印toast
//        SysTime.getSysTime().sync();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        initScreenShotListener();
    }

    private void initScreenShotListener() {
        mScreenShotListenManager = ScreenShotListenManager.newInstance(this);
        mScreenShotListenManager.setListener(new ScreenShotListenManager.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
                ScreenShotView.show(getActivity(), imagePath, 5 * 1000);
            }
        });
    }

    private void scrollToTop(View view) {
        if (view instanceof AbsListView) {
            ((AbsListView) view).smoothScrollToPositionFromTop(0, 0);
        } else if (view instanceof RecyclerView) {
            ((RecyclerView) view).smoothScrollToPosition(0);
        } else if (view instanceof ScrollView) {
            ((ScrollView) view).smoothScrollTo(0, 0);
        }
    }

    protected void scrollToTop(View anchor, final View view) {
        anchor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTop(view);
            }
        });
    }

    /**
     * 友盟统计埋点
     *
     * @param eventKey
     */
    protected void umengEventCount(String eventKey) {
        MobclickAgent.onEvent(getActivity(), eventKey);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Preference.get().setForeground(true);

        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Preference.get().setForeground(false);

        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mScreenShotListenManager.startListen();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(ACTION_TOKEN_EXPIRED));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScreenShotListenManager.stopListen();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Api.cancel(TAG);

        SmartDialog.dismiss(this);
        mRequestProgress.dismissAll();

        stopScheduleJob();
    }

    protected FragmentActivity getActivity() {
        return this;
    }

    @Override
    public void onHttpUiShow(String tag) {
        if (mRequestProgress != null) {
            mRequestProgress.show(this);
        }
    }

    @Override
    public void onHttpUiDismiss(String tag) {
        if (mRequestProgress != null) {
            mRequestProgress.dismiss();
        }
    }

    protected void startScheduleJob(int millisecond, long delayMillis) {
        stopScheduleJob();

        if (mTimerHandler == null) {
            mTimerHandler = new TimerHandler(this);
        }
        mTimerHandler.sendEmptyMessageDelayed(millisecond, delayMillis);
    }

    protected void startScheduleJob(int millisecond) {
        startScheduleJob(millisecond, 0);
    }

    protected void stopScheduleJob() {
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler.resetCount();
        }
    }

    @Override
    public void onTimeUp(int count) {
    }

    /**
     * md5 加密
     *
     * @param value
     * @return
     */
    protected String md5Encrypt(String value) {
        try {
            return SecurityUtil.md5Encrypt(value);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return value;
        }
    }
}
