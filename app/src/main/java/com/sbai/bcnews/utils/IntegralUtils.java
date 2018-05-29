package com.sbai.bcnews.utils;

import android.app.Activity;
import android.view.Gravity;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.QKC;

/**
 * 积分相关
 */
public class IntegralUtils {
    /**
     * 传入activity，需要确保BaseActivity在退出后取消回调，例如Api.cancel断开回调，防止内存泄露
     * @param activity
     */
    public static void shareIntegral(final Activity activity) {
        if (activity != null) {
            String tag = null;
            if (activity instanceof BaseActivity) {
                tag = ((BaseActivity) activity).TAG;
            }
            final String finalTag = tag;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Apic.requestHashRate(QKC.TYPE_SHARE).tag(finalTag).callback(new Callback2D<Resp<Integer>, Integer>() {

                        @Override
                        protected void onRespSuccessData(Integer data) {
                            if (data != null && data > 0 && activity != null) {
                                ToastUtil.show(activity.getString(R.string.share_success_add_hash_rate_x, data));
                            } else {
                                ToastUtil.show(R.string.share_succeed);
                            }
                        }
                    }).fireFreely();
                }
            });
        } else {
            ToastUtil.show(R.string.share_succeed);
        }
    }
}
