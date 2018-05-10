package com.sbai.bcnews.view.share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.SmartDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 分享弹框，使用看下方代码：
 * <p>
 * ShareDialog.with(getActivity())
 * .setShareTitle("TEST")
 * .setShareDescription("TEST DES")
 * .setShareUrl(String.format(Client.SHARE_URL_QUESTION, 1234))
 * .setListener(new ShareDialog.OnShareDialogCallback() {
 *
 * @Override public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
 * }
 * @Override public void onFeedbackClick(View view) {
 * }
 * }).show();
 */
public  class ShareDialog {


    public interface OnShareDialogCallback {
        void onSharePlatformClick(SHARE_PLATFORM platform);
    }

    public enum SHARE_PLATFORM {
        WECHAT_FRIEND,
        WECHAT_CIRCLE,
        SINA_WEIBO,
        QQ
    }

    protected Activity mActivity;
    protected SmartDialog mSmartDialog;
    protected View mView;

    private CharSequence mTitle;
    private String mShareTitle;
    private String mShareDescription;
    private String mShareUrl;
    private String mShareThumbUrl;
    private boolean mHasWeiBo = true;
    private boolean mHasTitle = true;
    private boolean mShareImageOnly;
    private Bitmap mBitmap;

    private OnShareDialogCallback mListener;

    private class ShareButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.weChatFriend:
                    if (UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.WEIXIN)) {
                        if (mShareImageOnly) {
                            shareImageToPlatform(SHARE_MEDIA.WEIXIN);
                        } else {
                            if (mShareUrl.contains("?")) {
                                mShareUrl += "&userFrom=friend";
                            } else {
                                mShareUrl += "?userFrom=friend";
                            }
                            shareToPlatform(SHARE_MEDIA.WEIXIN);
                        }

                        onSharePlatformClicked(SHARE_PLATFORM.WECHAT_FRIEND);
                    } else {
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.you_not_install_weixin);
                                }
                            });
                        }
                    }
                    mSmartDialog.dismiss();
                    break;
                case R.id.weChatFriendCircle:
                    if (UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.WEIXIN_CIRCLE)) {
                        if (mShareImageOnly) {
                            shareImageToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                        } else {
                            if (mShareUrl.contains("?")) {
                                mShareUrl += "&userFrom=friend";
                            } else {
                                mShareUrl += "?userFrom=friend";
                            }
                            shareToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                        }

                        onSharePlatformClicked(SHARE_PLATFORM.WECHAT_CIRCLE);
                    } else {
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.you_not_install_weixin);
                                }
                            });
                        }
                    }
                    mSmartDialog.dismiss();
                    break;
                case R.id.qq:
                    if (UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.QQ)) {
                        if (mShareImageOnly) {
                            shareImageToPlatform(SHARE_MEDIA.QQ);
                        } else {
                            if (mShareUrl.contains("?")) {
                                mShareUrl += "&userFrom=friend";
                            } else {
                                mShareUrl += "?userFrom=friend";
                            }
                            shareToPlatform(SHARE_MEDIA.QQ);
                        }

                        onSharePlatformClicked(SHARE_PLATFORM.QQ);
                    } else {
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.you_not_install_qq);
                                }
                            });
                        }
                    }
                    mSmartDialog.dismiss();
                    break;
                case R.id.sinaWeibo:
                    if (UMShareAPI.get(mActivity).isInstall(mActivity, SHARE_MEDIA.SINA)) {
                        if (mShareUrl.contains("?")) {
                            mShareUrl += "&userFrom=weibo";
                        } else {
                            mShareUrl += "?userFrom=weibo";
                        }
                        shareToPlatform(SHARE_MEDIA.SINA);
                        onSharePlatformClicked(SHARE_PLATFORM.SINA_WEIBO);
                    } else {
                        if (mActivity != null) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.show(R.string.you_not_install_weibo);
                                }
                            });

                        }
                    }
                    mSmartDialog.dismiss();
                    break;
            }
        }
    }

    private void shareToPlatform(SHARE_MEDIA platform) {
        if (platform != SHARE_MEDIA.SINA) {
            UMWeb mWeb = new UMWeb(mShareUrl);
            mWeb.setTitle(mShareTitle);
            mWeb.setDescription(mShareDescription);
            UMImage thumb;
            if (TextUtils.isEmpty(mShareThumbUrl)) {
                thumb = new UMImage(mActivity, R.mipmap.ic_launcher);
            } else {
                thumb = new UMImage(mActivity, mShareThumbUrl);
            }
            mWeb.setThumb(thumb);
            if (mActivity != null && !mActivity.isFinishing()) {
                new ShareAction(mActivity)
                        .withMedia(mWeb)
                        .setPlatform(platform)
                        .setCallback(mUMShareListener)
                        .share();
            }
        } else {
            String text = mShareTitle + mShareUrl;
            UMImage image;
            if (TextUtils.isEmpty(mShareThumbUrl)) {
                image = new UMImage(mActivity, R.mipmap.ic_launcher);
            } else {
                image = new UMImage(mActivity, mShareThumbUrl);
            }
            if (mActivity != null && !mActivity.isFinishing()) {
                new ShareAction(mActivity)
                        .withText(text)
                        .withMedia(image)
                        .setPlatform(platform)
                        .setCallback(mUMShareListener)
                        .share();
            }
        }
    }

    private void shareImageToPlatform(SHARE_MEDIA platform) {
        UMImage image = new UMImage(mActivity, mBitmap);
        if (mActivity != null && !mActivity.isFinishing()) {
            new ShareAction(mActivity)
                    .withMedia(image)
                    .setPlatform(platform)
                    .setCallback(mUMShareListener)
                    .share();
        }
    }

    private void onSharePlatformClicked(SHARE_PLATFORM platform) {
        if (mListener != null) {
            mListener.onSharePlatformClick(platform);
        }
    }

    private UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_succeed);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            //   ToastUtil.show(R.string.share_failed);
            ToastUtil.show(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_cancel);
        }
    };

    public static ShareDialog with(Activity activity) {
        return with(activity, R.layout.dialog_share);
    }

    public static ShareDialog with(Activity activity, @LayoutRes int resource) {
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.mActivity = activity;
        shareDialog.mSmartDialog = SmartDialog.single(activity);
        shareDialog.mView = LayoutInflater.from(activity).inflate(resource, null);
        shareDialog.mSmartDialog.setCustomView(shareDialog.mView);
        shareDialog.init();
        return shareDialog;
    }

    protected void init() {
        mView.findViewById(R.id.weChatFriend).setOnClickListener(new ShareButtonClickListener());
        mView.findViewById(R.id.weChatFriendCircle).setOnClickListener(new ShareButtonClickListener());
        mView.findViewById(R.id.qq).setOnClickListener(new ShareButtonClickListener());
        mView.findViewById(R.id.sinaWeibo).setOnClickListener(new ShareButtonClickListener());
        mView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSmartDialog.dismiss();
            }
        });
    }

    public ShareDialog setListener(OnShareDialogCallback listener) {
        mListener = listener;
        return this;
    }

    public ShareDialog hasWeiBo(boolean hasWeiBo) {
        mHasWeiBo = hasWeiBo;
        return this;
    }

    public ShareDialog setTitle(int titleRes) {
        mTitle = mActivity.getText(titleRes);
        return this;
    }

    public ShareDialog setTitleVisible(boolean visible) {
        mHasTitle = visible;
        return this;
    }

    public ShareDialog setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        return this;
    }

    public ShareDialog setShareImageOnly(boolean shareImageOnly) {
        mShareImageOnly = shareImageOnly;
        return this;
    }

    public ShareDialog setTitle(CharSequence title) {
        mTitle = title;
        return this;
    }

    public ShareDialog setShareUrl(String url) {
        mShareUrl = url;
        return this;
    }

    public ShareDialog setShareTitle(String shareTitle) {
        mShareTitle = shareTitle;
        return this;
    }

    public ShareDialog setShareDescription(String shareDescription) {
        mShareDescription = shareDescription;
        return this;
    }

    public ShareDialog setShareThumbUrl(String shareThumbUrl) {
        mShareThumbUrl = shareThumbUrl;
        return this;
    }


    public void show() {
        if (!mHasWeiBo) {
            mView.findViewById(R.id.sinaWeibo).setVisibility(View.GONE);
        }
        TextView title = (TextView) mView.findViewById(R.id.title);
        if (!TextUtils.isEmpty(mTitle)) {
            title.setText(mTitle);
        }
        if (mHasTitle) {
            title.setVisibility(View.VISIBLE);
        } else {
            title.setVisibility(View.GONE);
        }

        mSmartDialog.setWidthScale(1)
                .setGravity(Gravity.BOTTOM)
                .setWindowAnim(R.style.BottomDialogAnimation)
                .show();
    }
}
