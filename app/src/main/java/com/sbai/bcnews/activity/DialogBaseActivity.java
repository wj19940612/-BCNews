package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.sbai.bcnews.utils.TimerHandler;
import com.umeng.analytics.MobclickAgent;

public class DialogBaseActivity extends AppCompatActivity implements TimerHandler.TimerCallback {

    protected String TAG;

    private TimerHandler mTimerHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (dm.widthPixels * getWidthRatio());

        Window window = getWindow();
        if (getWindowGravity() != -1) {
            window.setGravity(getWindowGravity());
        }
    }

    protected void umengEventCount(String eventKey) {
        MobclickAgent.onEvent(getActivity(), eventKey);
    }

    protected float getWidthRatio() {
        return 0.75f;
    }

    protected int getWindowGravity() {
        return -1;
    }

    protected FragmentActivity getActivity() {
        return this;
    }

    @Override
    public void onTimeUp(int count) {}

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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScheduleJob();
    }
}

