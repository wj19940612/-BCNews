package com.sbai.bcnews.activity.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
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
    @BindView(R.id.askPrice)
    TextView mAskPrice;
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
    @BindView(R.id.bidPrice)
    TextView mBidPrice;
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
        requestTrendData();
    }

    private void requestTrendData() {
        Apic.reqTrendData(mCode, mExchangeCode, null).tag(TAG)
                .callback(new Callback2D<Resp<List<TrendData>>, List<TrendData>>() {
                    @Override
                    protected void onRespSuccessData(List<TrendData> data) {
                        Collections.sort(data);
                        mTrendChart.initWithData(data);
                    }
                }).fireFreely();
    }

    @Override
    protected void onStart() {
        super.onStart();
        stopScheduleJob();
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
        mAskPrice.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getAskPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getAskPrice() * mMarketData.getRate()));

        mMarketValue.setText(MarketDataUtils.formatMarketValue(mMarketData.getMarketValue()));
        mLowestPrice.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getLowestPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getLowestPrice() * mMarketData.getRate()));
        mBidPrice.setText(MarketDataUtils.formatDollarWithSign(mMarketData.getBidPrice())
                + " " + MarketDataUtils.formatRmbWithSign(mMarketData.getBidPrice() * mMarketData.getRate()));
    }

    private void requestSingleMarket() {
        Apic.reqSingleMarket(mMarketData.getCode(), mMarketData.getExchangeCode()).tag(TAG)
                .callback(new Callback2D<Resp<MarketData>, MarketData>() {
                    @Override
                    protected void onRespSuccessData(MarketData data) {
                        mMarketData = data;
                        updateMarketView();
                    }
                }).fireFreely();
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
    }

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    showTrendView();
                    requestTrendData();
                    break;
                case 1:
                    showKlineView();
                    requestKlineMarket("5");
                    break;
                case 2:
                    showKlineView();
                    requestKlineMarket("15");
                    break;
                case 3:
                    showKlineView();
                    requestKlineMarket("30");
                    break;
                case 4:
                    showKlineView();
                    requestKlineMarket("60");
                    break;
                case 5:
                    showKlineView();
                    requestKlineMarket("day");
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

    private void showKlineView() {
        mKlineChart.setVisibility(View.VISIBLE);
        mTrendChart.setVisibility(View.GONE);
    }

    private void showTrendView() {
        mKlineChart.setVisibility(View.GONE);
        mTrendChart.setVisibility(View.VISIBLE);
    }

    private void requestKlineMarket(String klineType) {
        mKlineChart.clearData();
        Apic.reqKlineMarket(mCode, mExchangeCode, klineType, null).tag(TAG)
                .callback(new Callback2D<Resp<List<KlineViewData>>, List<KlineViewData>>() {
                    @Override
                    protected void onRespSuccessData(List<KlineViewData> data) {
                        Collections.sort(data);
                        mKlineChart.setDataList(data);
                    }
                }).fire();
    }

    @OnClick(R.id.checkRelatedNews)
    public void onViewClicked() {
        umengEventCount(UmengCountEventId.MARKET_RELEATE_NEWS);
        Launcher.with(getActivity(), RelatedNewsActivity.class)
                .putExtra(ExtraKeys.DIGITAL_CURRENCY, mMarketData)
                .execute();
    }
}
