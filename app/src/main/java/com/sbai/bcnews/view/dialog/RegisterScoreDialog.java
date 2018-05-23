package com.sbai.bcnews.view.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.view.SmartDialog;

public class RegisterScoreDialog {

    private Activity mActivity;
    private SmartDialog mSmartDialog;
    private View mView;

    private ImageView mSrcView;
    private TextView mName;
    private TextView mScore;
    private OnClickListener mOnClickListener;

    private Style mStyle;
    private int mScoreValue;

    public interface OnClickListener {
        void onClearClick();

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
        registerScoreDialog.mSmartDialog = SmartDialog.single(activity);
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
    }

    public void show() {
        if (mStyle == Style.LOGIN) {
            mName.setText(mActivity.getString(R.string.congratulations_login_success));
            mScore.setText(mActivity.getString(R.string.login_award_x, mScoreValue));
        } else {
            mName.setText(mActivity.getString(R.string.congratulations_register_success));
            mScore.setText(mActivity.getString(R.string.register_award_x, mScoreValue));
        }
        mSmartDialog.setWidthScale(1).show();
    }
}
