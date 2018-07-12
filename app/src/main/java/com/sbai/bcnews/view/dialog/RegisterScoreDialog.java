package com.sbai.bcnews.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.TimerHandler;
import com.sbai.bcnews.view.SmartDialog;

public class RegisterScoreDialog implements TimerHandler.TimerCallback {

    public static final int DISMISS_TIME_COUNT = 2;
    public static final int HANDLER_TIME = 1000;

    private TimerHandler mTimerHandler;

    private Activity mActivity;
    private SmartDialog mSmartDialog;
    private View mView;

    private ImageView mSrcView;
    private TextView mName;
    private TextView mScore;
    private OnClickListener mOnClickListener;
    private SmartDialog.OnDismissListener mOnDismissListener;

    private Style mStyle;
    private int mScoreValue;

    @Override
    public void onTimeUp(int count) {
        if (count >= DISMISS_TIME_COUNT) {
            mSmartDialog.dismiss();
            stopScheduleJob();
        }
    }

    private void startTimeHandler(int millisecond) {
        stopScheduleJob();

        if (mTimerHandler == null) {
            mTimerHandler = new TimerHandler(this);
        }
        mTimerHandler.sendEmptyMessageDelayed(millisecond, millisecond);
    }

    private void stopScheduleJob() {
        if (mTimerHandler != null) {
            mTimerHandler.removeCallbacksAndMessages(null);
            mTimerHandler.resetCount();
        }
    }

    public interface OnClickListener {

        void onClick();
    }

    public enum Style {
        LOGIN,
        REGISTER
    }

    public static RegisterScoreDialog with(Activity activity, Style style) {
        RegisterScoreDialog registerScoreDialog = new RegisterScoreDialog();
        registerScoreDialog.mStyle = style;
        registerScoreDialog.mActivity = activity;
        registerScoreDialog.mSmartDialog = SmartDialog.with(activity);
        registerScoreDialog.mView = LayoutInflater.from(activity).inflate(R.layout.dialog_register_score, null);
        registerScoreDialog.mSmartDialog.setCustomView(registerScoreDialog.mView);
        registerScoreDialog.init();
        return registerScoreDialog;
    }

    public RegisterScoreDialog setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        return this;
    }

    public RegisterScoreDialog setScore(int score) {
        this.mScoreValue = score;
        return this;
    }

    public RegisterScoreDialog setDismissListener(SmartDialog.OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    private void init() {
        mSrcView = mView.findViewById(R.id.lookDetailBtn);
        mName = mView.findViewById(R.id.name);
        mScore = mView.findViewById(R.id.score);
        mSrcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick();
                }
            }
        });
        mSmartDialog.setOnDismissListener(new SmartDialog.OnDismissListener() {
            @Override
            public void onDismiss(Dialog dialog) {
                if (mOnDismissListener != null) {
                    mOnDismissListener.onDismiss(dialog);
                }
            }
        });
    }

    public void show() {
        if (mStyle == Style.LOGIN) {
            mName.setText(mActivity.getString(R.string.congratulations_login_success));
            mScore.setText(mActivity.getString(R.string.login_award_x, mScoreValue));
        } else {
            mName.setText(mActivity.getString(R.string.congratulations_register_success));
            mScore.setText(mActivity.getString(R.string.register_award_x, mScoreValue));
        }
        mSmartDialog.setOnDismissListener(new SmartDialog.OnDismissListener() {
            @Override
            public void onDismiss(Dialog dialog) {
                stopScheduleJob();
            }
        });
        mSmartDialog.setWidthScale(1).show();
        mSmartDialog.setGravity(Gravity.CENTER);
        startTimeHandler(HANDLER_TIME);
    }
}
