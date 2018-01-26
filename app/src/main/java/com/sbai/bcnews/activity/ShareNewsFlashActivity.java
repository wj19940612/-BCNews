package com.sbai.bcnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.TitleBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 快讯分享页
 */

public class ShareNewsFlashActivity extends BaseActivity {
    @BindView(R.id.title)
    TitleBar mTitle;
    @BindView(R.id.week)
    TextView mWeek;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.wechat)
    ImageView mWechat;
    @BindView(R.id.circle)
    ImageView mCircle;
    @BindView(R.id.qq)
    ImageView mQq;
    @BindView(R.id.weibo)
    ImageView mWeibo;
    @BindView(R.id.download)
    ImageView mDownload;
    @BindView(R.id.share)
    LinearLayout mShare;
    @BindView(R.id.shareArea)
    LinearLayout mShareArea;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_flash_share);
        ButterKnife.bind(this);
        initData(getIntent());
    }

    private void initData(Intent intent) {
        NewsFlash newsFlash = intent.getParcelableExtra(ExtraKeys.NEWS_FLASH);
        if (newsFlash != null) {

        }
    }

    @OnClick({R.id.back, R.id.wechat, R.id.circle, R.id.qq, R.id.weibo, R.id.download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().onBackPressed();
                break;
            case R.id.wechat:
                if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {
                    shareImageToPlatform(SHARE_MEDIA.WEIXIN, screenShot(mShareArea));
                } else {
                    ToastUtil.show(R.string.you_not_install_weixin);
                }
                break;
            case R.id.circle:
                if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {
                    shareImageToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE, screenShot(mShareArea));
                } else {
                    ToastUtil.show(R.string.you_not_install_weixin);
                }
                break;
            case R.id.qq:
                if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.QQ)) {
                    shareImageToPlatform(SHARE_MEDIA.QQ, screenShot(mShareArea));
                } else {
                    ToastUtil.show(R.string.you_not_install_qq);
                }
                break;
            case R.id.weibo:
                if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.SINA)) {
                    shareImageToPlatform(SHARE_MEDIA.SINA, screenShot(mShareArea));
                } else {
                    ToastUtil.show(R.string.you_not_install_weibo);
                }
                break;
            case R.id.download:
                // TODO: 2018-01-26
                break;
        }
    }

    private Bitmap screenShot(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void shareImageToPlatform(SHARE_MEDIA platform, Bitmap bitmap) {
        UMImage image = new UMImage(getActivity(), bitmap);
        new ShareAction(getActivity())
                .withMedia(image)
                .setPlatform(platform)
                .share();
    }
}
