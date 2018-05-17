package com.sbai.bcnews.activity.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.activity.RelatedNewsActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.MarketDataUtils;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.autofit.AutofitTextView;
import com.sbai.bcnews.view.market.KlineDataPlane;
import com.sbai.bcnews.view.tabLayout.TabLayout;
import com.sbai.chart.domain.KlineViewData;
import com.sbai.chart.domain.TrendData;
import com.sbai.chart.kline.KlineChart;
import com.sbai.chart.trend.InfiniteTrendChart;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarketDetailActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.lastPrice)
    AutofitTextView mLastPrice;
    @BindView(R.id.priceChange)
    TextView mPriceChange;
    @BindView(R.id.priceArea)
    LinearLayout mPriceArea;
    @BindView(R.id.volume)
    TextView mVolume;
    @BindView(R.id.highest)
    TextView mHighest;
    @BindView(R.id.bidPrice1)
    TextView mBidPrice1;
    @BindView(R.id.marketValue)
    TextView mMarketValue;
    @BindView(R.id.marketDataArea)
    LinearLayout mMarketDataArea;
    @BindView(R.id.checkRelatedNews)
    TextView mCheckRelatedNews;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.lowestPrice)
    TextView mLowestPrice;
    @BindView(R.id.askPrice1)
    TextView mAskPrice1;
    @BindView(R.id.klinePlane)
    KlineDataPlane mKlinePlane;
    @BindView(R.id.klineChart)
    KlineChart mKlineChart;
    @BindView(R.id.trendChart)
    InfiniteTrendChart mTrendChart;
    @BindView(R.id.screenShotArea)
    RelativeLayout mScreenShotArea;

    private MarketData mMarketData;
    private String mCode;
    private String mExchangeCode;

    private boolean mRefreshTrendWhenArriveRight;
    private boolean mRefreshKlineWhenArriveRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);

        initData(getIntent());

        initTitleBar();
        initTabLayout();
        initCharts();

        updateMarketView();

        requestSingleMarket();

        showTrendView();
        requestTrendData(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startScheduleJob(5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScheduleJob();
    }

    private void initCharts() {
        InfiniteTrendChart.Settings settings = new InfiniteTrendChart.Settings();
        settings.setBaseLines(5);
        settings.setNumberScale(mMarketData.getHighestPrice() >= 10 ? 2 : 4);
        settings.setXAxis(45);
        settings.setIndexesType(KlineChart.Settings.INDEXES_VOL);
        settings.setIndexesEnable(true);
        settings.setIndexesBaseLines(2);
        mTrendChart.setSettings(settings);
        mTrendChart.setOnDragListener(new InfiniteTrendChart.OnDragListener() {
            @Override
            public void onArriveLeft(TrendData theLeft) {
                requestTrendData(theLeft.getTime());
            }

            @Override
            public void onArriveRight(TrendData theRight) {
                if (mRefreshTrendWhenArriveRight) {
                    requestTrendData(null);
                }
            }
        });

        KlineChart.Settings klineSettings = new KlineChart.Settings();
        klineSettings.setBaseLines(5);
        klineSettings.setNumberScale(mMarketData.getHighestPrice() >= 10 ? 2 : 4);
        klineSettings.setXAxis(45);
        klineSettings.setIndexesType(KlineChart.Settings.INDEXES_VOL);
        klineSettings.setIndexesEnable(true);
        klineSettings.setIndexesBaseLines(2);
        mKlineChart.setSettings(klineSettings);
        mKlineChart.setOnTouchLinesAppearListener(new KlineChart.OnTouchLinesAppearListener() {
            @Override
            public void onAppear(KlineViewData data, KlineViewData previousData, boolean isLeftArea) {
                mTabLayout.setVisibility(View.INVISIBLE);
                mKlinePlane.setVisibility(View.VISIBLE);
                mKlinePlane.setPrices(data.getOpenPrice(), data.getMaxPrice(), data.getMinPrice(), data.getClosePrice());
                mKlinePlane.setDate(data.getTimeStamp());
            }

            @Override
            public void onDisappear() {
                mTabLayout.setVisibility(View.VISIBLE);
                mKlinePlane.setVisibility(View.INVISIBLE);
            }
        });
        mKlineChart.setOnDragListener(new KlineChart.OnDragListener() {
            @Override
            public void onArriveLeft(KlineViewData theLeft) {
                String klineType = (String) mKlineChart.getTag();
                requestKlineMarket(klineType, theLeft.getTime());
            }

            @Override
            public void onArriveRight(KlineViewData theRight) {
                if (mRefreshKlineWhenArriveRight) {
                    String klineType = (String) mKlineChart.getTag();
                    requestKlineMarket(klineType, null);
                }
            }
        });
    }

    /*设置最新价，人民币换算，涨跌幅以及颜色，以及一些基本的数据*/
    private void updateMarketView() {
        double lastPrice = mMarketData.getLastPrice();
        mLastPrice.setText(MarketDataUtils.formatDollar(lastPrice));
        // mPriceChange show last price in RMB, and price change

        mPriceChange.setText(MarketDataUtils.formatRmbWithSign(lastPrice * mMarketData.getRate()) + '\n'
                + MarketDataUtils.formatDollarWithPrefix(mMarketData.getUpDropPrice())
                + "  " + MarketDataUtils.percentWithPrefix(mMarketData.getUpDropSpeed()));

        int color = mMarketData.getUpDropSpeed() < 0 ? R.color.redPrimary : R.color.greenPrimary;
        mLastPrice.setTextColor(ContextCompat.getColor(getActivity(), color));
        mPriceChange.setTextColor(ContextCompat.getColor(getActivity(), color));

        mVolume.setText(MarketDataUtils.formatVolume(mMarketData.getLastVolume()));
        mHighest.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getHighestPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getHighestPrice() * mMarketData.getRate()));
        mBidPrice1.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getBidPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getBidPrice() * mMarketData.getRate()));

        mMarketValue.setText(MarketDataUtils.formatMarketValue(mMarketData.getMarketValue()));
        mLowestPrice.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getLowestPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getLowestPrice() * mMarketData.getRate()));
        mAskPrice1.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getAskPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getAskPrice() * mMarketData.getRate()));
    }

    private void initData(Intent intent) {
        mMarketData = intent.getParcelableExtra(ExtraKeys.DIGITAL_CURRENCY);
        mCode = mMarketData.getCode();
        mExchangeCode = mMarketData.getExchangeCode();
    }

    private void initTitleBar() {
        String baseCurrency = mMarketData.getName();
        String counterCurrency = mMarketData.getCurrencyMoney();
        String exchangeCode = mMarketData.getExchangeCode();

        View view = mTitleBar.getCustomView();
        TextView currencyPair = view.findViewById(R.id.currencyPair);
        TextView exchange = view.findViewById(R.id.exchange);
        currencyPair.setText(baseCurrency.toUpperCase() + "/" + counterCurrency.toUpperCase());
        exchange.setText(exchangeCode);

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                umengEventCount(UmengCountEventId.MARKET_SHARE);
                Bitmap bitmap = getScreenShot(mScreenShotArea);
                File file = ImageUtils.getUtil().saveBitmap(bitmap, createFilename());
                Launcher.with(getActivity(), ShareMarketActivity.class)
                        .putExtra(ExtraKeys.BITMAP_PATH, file.getAbsolutePath())
                        .putExtra(ExtraKeys.DIGITAL_CURRENCY, mMarketData)
                        .execute();
            }
        });
    }

    private String createFilename() {
        return "market_" + SystemClock.elapsedRealtime() + ".png";
    }

    private Bitmap getScreenShot(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    private void initTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.trend_chart));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.five_min_k));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.fifteen_min_k));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.thirty_min_k));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.sixty_min_k));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.day_k_line));
        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);
    }

    @Override
    public void onTimeUp(int count) {
        requestSingleMarket();

        int seconds = count * 5;
        if (mTrendChart.getVisibility() == View.VISIBLE && seconds % 60 == 0) {
            if (mTrendChart.getTransactionX() != 0) {
                mRefreshTrendWhenArriveRight = true;
            } else {
                requestTrendData(null);
            }
        }

        if (mKlineChart.getVisibility() == View.VISIBLE) {
            String klineType = (String) mKlineChart.getTag();
            if (klineType == null || klineType.equals("day")) return;

            int klineSeconds = Integer.valueOf(klineType) * 60;
            if (seconds % klineSeconds == 0) {
                if (mKlineChart.getTransactionX() != 0) {
                    mRefreshKlineWhenArriveRight = true;
                } else {
                    requestKlineMarket(klineType, null);
                }
            }
        }
    }

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    showTrendView();
                    requestTrendData(null);
                    break;
                case 1:
                    showKlineView(false);
                    requestKlineMarket("5", null);
                    break;
                case 2:
                    showKlineView(false);
                    requestKlineMarket("15", null);
                    break;
                case 3:
                    showKlineView(false);
                    requestKlineMarket("30", null);
                    break;
                case 4:
                    showKlineView(false);
                    requestKlineMarket("60", null);
                    break;
                case 5:
                    showKlineView(true);
                    requestKlineMarket("day", null);
                    break;

            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    private void showKlineView(boolean dayLine) {
        mKlineChart.setVisibility(View.VISIBLE);
        mKlineChart.setDayLine(dayLine);
        mTrendChart.setVisibility(View.GONE);
    }

    private void showTrendView() {
        mKlineChart.setVisibility(View.GONE);
        mTrendChart.setVisibility(View.VISIBLE);
    }

    private void requestSingleMarket() {
        Apic.reqSingleMarket(mMarketData.getCode(), mMarketData.getExchangeCode()).tag(TAG)
                .callback(new Callback2D<Resp<MarketData>, MarketData>() {
                    @Override
                    protected void onRespSuccessData(MarketData data) {
                        if (mMarketData != null && !TextUtils.isEmpty(mMarketData.getName())) {
                            data.setName(mMarketData.getName());
                        }
                        mMarketData = data;
                        updateMarketView();
                    }
                }).fireFreely();
    }

    private void requestKlineMarket(String klineType, final String endTime) {
        mKlineChart.setTag(klineType);
        Apic.reqKlineMarket(mCode, mExchangeCode, klineType, Uri.encode(endTime)).tag(TAG)
                .callback(new Callback2D<Resp<List<KlineViewData>>, List<KlineViewData>>() {
                    @Override
                    protected void onRespSuccessData(List<KlineViewData> data) {
                        Collections.sort(data);
                        if (TextUtils.isEmpty(endTime)) {
                            mKlineChart.initWithData(data);
                            mRefreshKlineWhenArriveRight = false;
                        } else {
                            if (data.isEmpty()) {
                                ToastUtil.show(R.string.there_is_no_more_data);
                                return;
                            }
                            mKlineChart.addHistoryData(data);
                        }
                    }
                }).fireFreely();
    }

    private void requestTrendData(final String endTime) {
        Apic.reqTrendData(mCode, mExchangeCode, Uri.encode(endTime)).tag(TAG)
                .callback(new Callback2D<Resp<List<TrendData>>, List<TrendData>>() {
                    @Override
                    protected void onRespSuccessData(List<TrendData> data) {
                        Collections.sort(data);
                        if (TextUtils.isEmpty(endTime)) {
                            mTrendChart.initWithData(data);
                            mRefreshTrendWhenArriveRight = false;
                        } else {
                            if (data.isEmpty()) {
                                ToastUtil.show(R.string.there_is_no_more_data);
                                return;
                            }
                            mTrendChart.addHistoryData(data);
                        }
                    }
                }).fireFreely();
    }

    @OnClick(R.id.checkRelatedNews)
    public void onViewClicked() {
        umengEventCount(UmengCountEventId.MARKET_RELEATE_NEWS);
        Launcher.with(getActivity(), RelatedNewsActivity.class)
                .putExtra(ExtraKeys.DIGITAL_CURRENCY, mMarketData)
                .execute();
    }
}
