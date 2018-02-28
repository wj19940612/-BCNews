package com.sbai.bcnews;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.webkit.JavascriptInterface;

import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.view.ShareDialog;


public class AppJs {
    public final static int NET_NONE = 0;
    public final static int NET_WIFI = 1;
    public final static int NET_2G = 2;
    public final static int NET_3G = 3;
    public final static int NET_4G = 4;

    private Context mContext;

    public AppJs(Context context) {
        mContext = context;
    }

    @JavascriptInterface
    public void openShareDialogWithoutReward(String title, String description, String shareUrl, String shareThumbnailUrl) {
        if (mContext instanceof Activity) {
            final Activity activity = (Activity) mContext;
            ShareDialog.with(activity)
                    .setTitle(mContext.getString(R.string.share_to))
                    .setTitleVisible(true)
                    .setShareTitle(title)
                    .setShareDescription(description)
                    .setShareUrl(shareUrl)
                    .setShareThumbUrl(shareThumbnailUrl)
                    .show();
        }
    }

    /**
     * @param title
     * @param description
     * @param shareUrl
     * @param shareThumbnailUrl
     * @param shareChannel      分享渠道配置 onlyywx 只有微信分享
     */
    @JavascriptInterface
    public void openShareDialog(String title, String description, String shareUrl, String shareThumbnailUrl, String shareChannel) {
        openShareDialog(title, description, shareUrl, shareThumbnailUrl, shareChannel, "");
    }

    @JavascriptInterface
    public void openShareDialog(String title, String description, String shareUrl, String shareThumbnailUrl, String shareChannel, String shareTitle) {
        if (mContext instanceof Activity) {
            final Activity activity = (Activity) mContext;
            ShareDialog.with(activity)
                    .setTitle(shareTitle)
                    .setTitleVisible(true)
                    .setShareTitle(title)
                    .setShareDescription(description)
                    .setShareUrl(shareUrl)
                    .setShareThumbUrl(shareThumbnailUrl)
                    .setListener(new ShareDialog.OnShareDialogCallback() {
                        @Override
                        public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
                        }
                    }).show();
        }
    }

    /**
     * 打开分享弹窗
     */
    @JavascriptInterface
    public void openShareDialog(final String title, final String description, final String shareUrl, final String shareThumbnailUrl) {
        if (mContext instanceof Activity) {
            final Activity activity = (Activity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ShareDialog.with(activity)
                            .hasWeiBo(false)
                            .setTitle(title)
                            .setTitleVisible(false)
                            .setShareTitle(title)
                            .setShareDescription(description)
                            .setShareUrl(shareUrl)
                            .setShareThumbUrl(shareThumbnailUrl)
                            .show();
                }
            });
        }
    }

    /**
     * 截图
     */
    @JavascriptInterface
    public void screenShot() {
        if (mContext instanceof WebActivity) {
            final WebActivity activity = (WebActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.screenShot();
                }
            });
        }
    }

    /**
     * 获取当前可用网络的类型
     *
     * @return <li>0 无可用网络</li>
     * <li>1 wifi</li>
     * <li>2 2g</li>
     * <li>3 3g</li>
     * <li>4 4g</li>
     */
    @JavascriptInterface
    public int getAvailableNetwork() {
        int availableNetwork = NET_NONE;

        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return availableNetwork;
        }

        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            availableNetwork = NET_WIFI;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = networkInfo.getSubtype();

            if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                    || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                availableNetwork = NET_2G;

            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                availableNetwork = NET_3G;
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {
                availableNetwork = NET_4G;
            }
        }

        return availableNetwork;
    }

    @JavascriptInterface
    public void openImage(final String img){
//        Log.e("zzz","openImage");
        if (mContext instanceof NewsDetailActivity) {
            final NewsDetailActivity activity = (NewsDetailActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.openBigImage(img);
                }
            });
        }
    }

    @JavascriptInterface
    public void updateTitleText(final String titleContent) {
        if (mContext instanceof WebActivity) {
            final WebActivity activity = (WebActivity) mContext;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.updateTitleText(titleContent);
                }
            });
        }
    }
}
