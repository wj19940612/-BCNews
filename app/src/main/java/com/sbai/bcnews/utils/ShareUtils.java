package com.sbai.bcnews.utils;

import android.app.Activity;

import com.sbai.bcnews.R;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

public class ShareUtils {
    public static boolean canShare(Activity activity, SHARE_MEDIA platform) {
        switch (platform) {
            case WEIXIN:
            case WEIXIN_CIRCLE:
                if (UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.WEIXIN)) {
                    return true;
                } else {
                    ToastUtil.show(R.string.you_not_install_weixin);
                }
                break;
            case QQ:
            case QZONE:
                if (UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.QQ)) {
                    return true;
                } else {
                    ToastUtil.show(R.string.you_not_install_qq);
                }
                break;
            case SINA:
                if (UMShareAPI.get(activity).isInstall(activity, SHARE_MEDIA.SINA)) {
                    return true;
                } else {
                    ToastUtil.show(R.string.you_not_install_weibo);
                }
                break;
        }
        return false;
    }
}
