package com.sbai.bcnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.IntegralUtils;
import com.sbai.bcnews.utils.ShareUtils;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
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
    LinearLayout mWechat;
    @BindView(R.id.circle)
    LinearLayout mCircle;
    @BindView(R.id.qq)
    LinearLayout mQq;
    @BindView(R.id.weibo)
    LinearLayout mWeibo;
    @BindView(R.id.download)
    LinearLayout mDownload;
    @BindView(R.id.share)
    LinearLayout mShare;
    @BindView(R.id.shareArea)
    LinearLayout mShareArea;
    @BindView(R.id.downloadImg)
    ImageView mDownloadImg;

    private long mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in_from_bottom, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_flash_share);
        ButterKnife.bind(this);
        initData(getIntent());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private void initData(Intent intent) {
        NewsFlash newsFlash = intent.getParcelableExtra(ExtraKeys.NEWS_FLASH);
        if (newsFlash != null) {
            mId = newsFlash.getId();
            mWeek.setText(getString(R.string.week_, DateUtil.getDayOfWeek(newsFlash.getReleaseTime())));
            mTime.setText(DateUtil.getFormatTime(newsFlash.getReleaseTime()).concat(" ").concat(getString(R.string.news_flash)));
            if (newsFlash.isImportant()) {
                if (TextUtils.isEmpty(newsFlash.getTitle())) {
                    mContent.setText(newsFlash.getContent());
                    mContent.setTextColor(Color.parseColor("#476E92"));
                } else {
                    mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                            Color.parseColor("#476E92"), Color.parseColor("#476E92")));
                }
            } else {
                if (TextUtils.isEmpty(newsFlash.getTitle())) {
                    mContent.setText(newsFlash.getContent());
                    mContent.setTextColor(Color.parseColor("#476E92"));
                } else {
                    mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                            Color.parseColor("#494949"), Color.parseColor("#494949")));
                }
            }
        }
        GlideApp.with(getActivity())
                .load(Apic.url.QR_CODE)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(mDownloadImg);
    }

    @OnClick({R.id.back, R.id.wechat, R.id.circle, R.id.qq, R.id.weibo, R.id.download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                getActivity().onBackPressed();
                break;
            case R.id.wechat:
                if (ShareUtils.canShare(getActivity(), SHARE_MEDIA.WEIXIN)) {
                    shareImageToPlatform(SHARE_MEDIA.WEIXIN, screenShot(mShareArea));
                }
                break;
            case R.id.circle:
                if (ShareUtils.canShare(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE)) {
                    shareImageToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE, screenShot(mShareArea));
                }
                break;
            case R.id.qq:
                if (ShareUtils.canShare(getActivity(), SHARE_MEDIA.QQ)) {
                    shareImageToPlatform(SHARE_MEDIA.QQ, screenShot(mShareArea));
                }
                break;
            case R.id.weibo:
                if (ShareUtils.canShare(getActivity(), SHARE_MEDIA.SINA)) {
                    shareImageToPlatform(SHARE_MEDIA.SINA, screenShot(mShareArea));
                }
                break;
            case R.id.download:
                ImageUtils.saveImageToGallery(getActivity(), screenShot(mShareArea));
                ToastUtil.show(R.string.save_success);
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
                .setCallback(mUMShareListener)
                .setPlatform(platform)
                .share();
        if (mId > 0) {
            Apic.share(mId, 1).tag(TAG).fireFreely();
        }
    }

    private UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            IntegralUtils.shareIntegral(ShareNewsFlashActivity.this);
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
}
