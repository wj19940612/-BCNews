package com.sbai.bcnews.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.view.SmartDialog;

public class RegisterScoreDialog {

    private Activity mActivity;
    private SmartDialog mSmartDialog;
    private View mView;

    private ImageView mSrcView;
    private ImageView mClearView;
    private OnClickListener mOnClickListener;

    private Style mStyle;

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

    private void init() {
        mSrcView = mView.findViewById(R.id.lookDetailBtn);
        mClearView = mView.findViewById(R.id.clearBtn);
        if (mStyle == Style.LOGIN) {
            mSrcView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_login_score));
        } else {
            mSrcView.setImageDrawable(ContextCompat.getDrawable(mActivity, R.drawable.ic_register_score));
        }
        mSrcView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick();
                }
            }
        });
        mClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClearClick();
                }
            }
        });
    }

    public void show() {
        mSmartDialog.setWidthScale(1).show();
    }
}
