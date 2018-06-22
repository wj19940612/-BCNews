package com.sbai.bcnews.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.view.SmartDialog;

public class AttentionDialog {

    private Activity mActivity;
    private SmartDialog mSmartDialog;
    private View mView;

    private TextView mTitle;
    private TextView mFirstButton;
    private TextView mSecondButton;
    private OnClickListener mOnClickListener;

    private String mTitleString;

    public interface OnClickListener {
        void onClick();
    }


    public static AttentionDialog with(Activity activity) {
        AttentionDialog attentionDialog = new AttentionDialog();
        attentionDialog.mActivity = activity;
        attentionDialog.mSmartDialog = SmartDialog.with(activity, "");
        attentionDialog.mView = LayoutInflater.from(activity).inflate(R.layout.dialog_common_bottom, null);
        attentionDialog.mSmartDialog.setStyle(R.style.BaseDialog_Bottom);
        attentionDialog.mSmartDialog.setGravity(Gravity.BOTTOM);
        attentionDialog.mSmartDialog.setCustomView(attentionDialog.mView);
        attentionDialog.init();
        return attentionDialog;
    }

    private void init() {
        mTitle = mView.findViewById(R.id.title);
        mFirstButton = mView.findViewById(R.id.firstBtn);
        mSecondButton = mView.findViewById(R.id.secondBtn);
        mFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmartDialog.dismiss();
                if (mOnClickListener != null) {
                    mOnClickListener.onClick();
                }
            }
        });

        mSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmartDialog.dismiss();
            }
        });
    }

    public AttentionDialog setOnSureClickListener(OnClickListener onSureClickListener) {
        this.mOnClickListener = onSureClickListener;
        return this;
    }

    public AttentionDialog setTitle(String title) {
        this.mTitleString = title;
        return this;
    }

    public AttentionDialog setTitle(int title) {
        this.mTitleString = mActivity.getString(title);
        return this;
    }

    public void show() {
        mTitle.setText(mTitleString);
        mSmartDialog.setWidthScale(1).show();
    }
}
