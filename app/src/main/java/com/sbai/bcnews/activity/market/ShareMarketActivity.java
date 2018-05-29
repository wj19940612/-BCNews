package com.sbai.bcnews.activity.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.IntegralUtils;
import com.sbai.bcnews.utils.ShareUtils;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.glide.GlideApp;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareMarketActivity extends BaseActivity {

    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.currencyPair)
    TextView mCurrencyPair;
    @BindView(R.id.marketScreenShot)
    ImageView mMarketScreenShot;
    @BindView(R.id.qrCode)
    ImageView mQrCode;
    @BindView(R.id.shareArea)
    LinearLayout mShareArea;
    @BindView(R.id.exchange)
    TextView mExchange;

    private MarketData mMarketData;
    private String mBitmapPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_market);
        ButterKnife.bind(this);

        initData(getIntent());

        mDate.setText(DateUtil.format(mMarketData.getUpTime(), DateUtil.FORMAT_SPECIAL_SLASH));
        String baseCurrency = mMarketData.getName();
        String counterCurrency = mMarketData.getCurrencyMoney();
        String exchangeCode = mMarketData.getExchangeCode();
        mCurrencyPair.setText(baseCurrency.toUpperCase() + "/" + counterCurrency.toUpperCase());
        mExchange.setText(exchangeCode);

        GlideApp.with(getActivity())
                .load(Apic.url.QR_CODE)
                .into(mQrCode);

        GlideApp.with(getActivity())
                .load(mBitmapPath)
                .into(mMarketScreenShot);
    }

    private void initData(Intent intent) {
        mBitmapPath = intent.getStringExtra(ExtraKeys.BITMAP_PATH);
        mMarketData = intent.getParcelableExtra(ExtraKeys.DIGITAL_CURRENCY);
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
    }

    private UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            IntegralUtils.shareIntegral(ShareMarketActivity.this);
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
