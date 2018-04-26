package com.sbai.bcnews.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.BuildConfig;
import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.ToastUtil;

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

    private View.OnClickListener mWhistleBlowingClickListener;


    public NewsShareDialog setWhistleBlowingClickListener(View.OnClickListener whistleBlowingClickListener) {
        mWhistleBlowingClickListener = whistleBlowingClickListener;
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whistleBlowing:
                if (mWhistleBlowingClickListener != null) {
                    mWhistleBlowingClickListener.onClick(v);
                }
                mSmartDialog.dismiss();
                break;
            case R.id.subTextSize:
                ToastUtil.show("坚守");
                break;
            case R.id.enlargeTextSize:
                ToastUtil.show("增大");
                break;
            case R.id.nightModelSwitch:
                // TODO: 2018/4/26 夜晚模式
                if (BuildConfig.DEBUG) {
                    mWhistleBlowing.setSelected(true);
                }
                break;
        }
    }
}
