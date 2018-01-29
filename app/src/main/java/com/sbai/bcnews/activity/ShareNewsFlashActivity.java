package com.sbai.bcnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.bcnews.view.TitleBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.File;

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
        overridePendingTransition(R.anim.slide_in_from_bottom, 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }

    private void initData(Intent intent) {
        NewsFlash newsFlash = intent.getParcelableExtra(ExtraKeys.NEWS_FLASH);
        if (newsFlash != null) {
            mWeek.setText(getString(R.string.week_, DateUtil.getDayOfWeek(newsFlash.getReleaseTime())));
            mTime.setText(DateUtil.getFormatTime(newsFlash.getReleaseTime()).concat(" ").concat(getString(R.string.news_flash)));
            if (newsFlash.isImportant()) {
                mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                        Color.parseColor("#476E92"), Color.parseColor("#476E92")));
            } else {
                mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                        Color.parseColor("#494949"), Color.parseColor("#494949")));
            }
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
                ImageUtils.saveImageToGallery(getActivity(), screenShot(mShareArea));
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
