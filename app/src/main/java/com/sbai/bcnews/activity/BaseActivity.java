package com.sbai.bcnews.activity;

import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.SysTime;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ScreenShotListenManager;
import com.sbai.bcnews.utils.SecurityUtil;
import com.sbai.bcnews.utils.TimerHandler;
import com.sbai.bcnews.view.RequestProgress;
import com.sbai.bcnews.view.ScreenShotView;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.dialog.RegisterScoreDialog;
import com.sbai.httplib.ReqIndeterminate;
import com.umeng.analytics.MobclickAgent;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.sbai.bcnews.view.dialog.RegisterScoreDialog.Style.LOGIN;
import static com.sbai.bcnews.view.dialog.RegisterScoreDialog.Style.REGISTER;

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

    public String TAG;

    private TimerHandler mTimerHandler;
    private RequestProgress mRequestProgress;
    private ScreenShotListenManager mScreenShotListenManager;
    private List<Dialog> mDialogList;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_TOKEN_EXPIRED.equalsIgnoreCase(intent.getAction())) {
                LocalUser.getUser().logout();
                Launcher.with(getActivity(), MainActivity.class).execute();
                Launcher.with(getActivity(), LoginActivity.class).execute();
            } else if (ACTION_LOGIN_SUCCESS.equalsIgnoreCase(intent.getAction())) {
                showUpdateSetLoginDialog();
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
        SysTime.getSysTime().sync();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        initScreenShotListener();
        initBroadcastListener();
    }

    private void initScreenShotListener() {
        mScreenShotListenManager = ScreenShotListenManager.newInstance(this);
        mScreenShotListenManager.setListener(new ScreenShotListenManager.OnScreenShotListener() {
            @Override
            public void onShot(String imagePath) {
                if (mDialogList == null) {
                    mDialogList = new ArrayList<>();
                }
                Dialog dialog = ScreenShotView.show(getActivity(), imagePath, 5 * 1000);
                mDialogList.add(dialog);
            }
        });
    }

    private void initBroadcastListener(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginActivity.ACTION_LOGIN_SUCCESS);
        intentFilter.addAction(LoginActivity.ACTION_TOKEN_EXPIRED);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
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
        showLoginRewardDialog();
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScreenShotListenManager.stopListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Api.cancel(TAG);

        SmartDialog.dismiss(this);

        dismissScreenDialog();

        mRequestProgress.dismissAll();

        stopScheduleJob();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private void showLoginRewardDialog() {
        if (LocalUser.getUser().isLogin() && LocalUser.getUser().getUserInfo().getAddRate() > 0) {
            RegisterScoreDialog.with(this, REGISTER).setScore(LocalUser.getUser().getUserInfo().getAddRate()).show();
            LocalUser.getUser().getUserInfo().setAddRate(0);
            LocalUser.getUser().getUserInfo().setAddIntegral(0);
            LocalUser.getUser().saveToPreference();
        } else if (LocalUser.getUser().isLogin() && LocalUser.getUser().getUserInfo().getAddIntegral() > 0) {
            RegisterScoreDialog.with(this, LOGIN).setScore(LocalUser.getUser().getUserInfo().getAddIntegral()).show();
            LocalUser.getUser().getUserInfo().setAddRate(0);
            LocalUser.getUser().getUserInfo().setAddIntegral(0);
            LocalUser.getUser().saveToPreference();
        }
    }

    public void showUpdateSetLoginDialog() {
        if (LocalUser.getUser().getUserInfo().getIsPassword() == 0) {
            SmartDialog.with(getActivity(), getString(R.string.please_set_login_password)).setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog) {
                    dialog.dismiss();
                    Launcher.with(BaseActivity.this, ModifyPassActivity.class).putExtra(ExtraKeys.HAS_LOGIN_PSD, LocalUser.getUser().getUserInfo().getIsPassword() > 0).execute();
                }
            }).show();
        }
    }

    private void dismissScreenDialog() {
        if (mDialogList != null && mDialogList.size() > 0) {
            for (Dialog dialog : mDialogList) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }
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
