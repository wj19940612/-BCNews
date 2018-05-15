package com.sbai.bcnews.view.share;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.sbai.bcnews.R;
import com.sbai.bcnews.view.SmartDialog;

/**
 * Modified by $nishuideyu$ on 2018/5/9
 * <p>
 * Description:
 * </p>
 */
public class NewsShareDialog extends ShareDialog implements View.OnClickListener {

    private NewsShareAndSetDialog.OnNewsLinkCopyListener mOnNewsLinkCopyListener;

    public NewsShareDialog setOnNewsLinkCopyListener(NewsShareAndSetDialog.OnNewsLinkCopyListener onNewsLinkCopyListener) {
        mOnNewsLinkCopyListener = onNewsLinkCopyListener;
        return this;
    }

    public static NewsShareDialog with(Activity activity) {
        NewsShareDialog newsShareDialog = new NewsShareDialog();
        newsShareDialog.mActivity = activity;
        newsShareDialog.mSmartDialog = SmartDialog.single(activity);
        newsShareDialog.mView = LayoutInflater.from(activity).inflate(R.layout.dialog_news_share, null);
        newsShareDialog.mSmartDialog.setCustomView(newsShareDialog.mView);
        newsShareDialog.init();
        return newsShareDialog;
    }

    @Override
    protected void init() {
        super.init();
        mView.findViewById(R.id.copyLink).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyLink:
                if (mSmartDialog != null) mSmartDialog.dismiss();
                if (mOnNewsLinkCopyListener != null) {
                    mOnNewsLinkCopyListener.onCopyLink();
                }
                break;
        }
    }
}
