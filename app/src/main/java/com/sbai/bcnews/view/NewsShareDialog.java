package com.sbai.bcnews.view;

import android.app.Activity;
import android.view.View;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.ToastUtil;

/**
 * Modified by $nishuideyu$ on 2018/4/26
 * <p>
 * Description: 资讯的带分享的弹窗
 * </p>
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public class NewsShareDialog extends ShareDialog implements View.OnClickListener {

    public static ShareDialog with(Activity activity) {
        return ShareDialog.with(activity, R.layout.dialog_news_share);
    }

    @Override
    protected void init() {
        super.init();
        mView.findViewById(R.id.whistleBlowing).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.whistleBlowing:
                ToastUtil.show("哈哈哈");
                break;
        }
    }
}
