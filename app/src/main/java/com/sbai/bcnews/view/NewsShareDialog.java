package com.sbai.bcnews.view;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.BuildConfig;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.TextSizeModel;

/**
 * Modified by $nishuideyu$ on 2018/4/26
 * <p>
 * Description: 资讯的带分享的弹窗
 * </p>
 */
public class NewsShareDialog extends ShareDialog implements View.OnClickListener {
    private static final String TAG = "NewsShareDialog";

    private TextView mWhistleBlowing;
    private TextView mTextSize;

    private int mLocalTextSize;
    private String[] mStringArray;

    public interface OnNewsSettingClickListener {

        void onWhistleBlowing();

        void subTextSize(int textSize);

        void addTextSize(int textSize);

        void copyLink();
    }


    private OnNewsSettingClickListener mOnNewsSettingClickListener;

    public NewsShareDialog setOnNewsSettingClickListener(OnNewsSettingClickListener onNewsSettingClickListener) {
        mOnNewsSettingClickListener = onNewsSettingClickListener;
        return this;
    }

    @Override
    protected void init() {
        super.init();
        mWhistleBlowing = mView.findViewById(R.id.whistleBlowing);
        mTextSize = mView.findViewById(R.id.textSize);

        mWhistleBlowing.setOnClickListener(this);
        mView.findViewById(R.id.subTextSize).setOnClickListener(this);
        mView.findViewById(R.id.enlargeTextSize).setOnClickListener(this);
        mView.findViewById(R.id.nightModelSwitch).setOnClickListener(this);
        mView.findViewById(R.id.copyLink).setOnClickListener(this);

        mStringArray = mActivity.getResources().getStringArray(R.array.webTextSize);
        updateTextSizeHint();
    }

    private void updateTextSizeHint() {
        String textModel = mActivity.getString(R.string.web_text_size_model, mStringArray[1]);

        switch (mLocalTextSize) {
            case TextSizeModel.LITTLE:
                textModel = mActivity.getString(R.string.web_text_size_model, mStringArray[0]);
                break;
            case TextSizeModel.NORMAL:
                textModel = mActivity.getString(R.string.web_text_size_model, mStringArray[1]);
                break;
            case TextSizeModel.BIG:
                textModel = mActivity.getString(R.string.web_text_size_model, mStringArray[2]);
                break;
            case TextSizeModel.HUGE:
                textModel = mActivity.getString(R.string.web_text_size_model, mStringArray[3]);
                break;
        }

        SpannableString spannableString = StrUtil.mergeTextWithColor(mActivity.getString(R.string.web_text_size), textModel, ContextCompat.getColor(mActivity, R.color.text_10));

        mTextSize.setText(spannableString);
    }

    public static NewsShareDialog with(Activity activity) {
        NewsShareDialog shareDialog = new NewsShareDialog();
        shareDialog.mActivity = activity;
        shareDialog.mSmartDialog = SmartDialog.single(activity);
        shareDialog.mView = LayoutInflater.from(activity).inflate(R.layout.dialog_news_share, null);
        shareDialog.mSmartDialog.setCustomView(shareDialog.mView);
        shareDialog.init();
        return shareDialog;
    }

    public NewsShareDialog setTextSize(int textSize) {
        mLocalTextSize = textSize;
        return this;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whistleBlowing:
                if (mOnNewsSettingClickListener != null) {
                    mOnNewsSettingClickListener.onWhistleBlowing();
                }
                mSmartDialog.dismiss();
                break;
            case R.id.subTextSize:
                if (mLocalTextSize > TextSizeModel.LITTLE) {
                    mLocalTextSize -= TextSizeModel.CHANGE_POINT;
                    Preference.get().setLocalWebTextSize(mLocalTextSize);
                    if (mOnNewsSettingClickListener != null) {
                        mOnNewsSettingClickListener.subTextSize(mLocalTextSize);
                    }
                    updateTextSizeHint();
                }
                break;
            case R.id.enlargeTextSize:
                if (mLocalTextSize < TextSizeModel.HUGE) {
                    mLocalTextSize += TextSizeModel.CHANGE_POINT;
                    Preference.get().setLocalWebTextSize(mLocalTextSize);
                    if (mOnNewsSettingClickListener != null) {
                        mOnNewsSettingClickListener.addTextSize(mLocalTextSize);
                    }
                    updateTextSizeHint();
                }
                break;
            case R.id.nightModelSwitch:
                // TODO: 2018/4/26 夜晚模式
                if (BuildConfig.DEBUG) {
                    mWhistleBlowing.setSelected(true);
                }
                break;
            case R.id.copyLink:
                if (mOnNewsSettingClickListener != null) {
                    mOnNewsSettingClickListener.copyLink();
                }
                break;

        }
    }

}
