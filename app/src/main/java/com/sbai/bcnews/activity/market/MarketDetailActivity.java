package com.sbai.bcnews.activity.market;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.view.HackTabLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.autofit.AutofitTextView;

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
    HackTabLayout mTabLayout;
    private MarketData mMarketData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);

        initData(getIntent());

        initTitleBar();
        initTabLayout();
        initMarketView();

        requestSingleMarket();
    }

    private void initMarketView() {
        
    }

    private void requestSingleMarket() {
        Apic.reqSingleMarket(mMarketData.getCode(), mMarketData.getExchangeCode()).tag(TAG)
                .callback(new Callback2D<Resp<MarketData>, MarketData>() {
                    @Override
                    protected void onRespSuccessData(MarketData data) {

                    }
                }).fireFreely();
    }

    private void initData(Intent intent) {
        mMarketData = intent.getParcelableExtra(ExtraKeys.DIGITAL_CURRENCY);
    }

    private void initTitleBar() {
        String baseCurrency = mMarketData.getName();
        String counterCurrency = mMarketData.getCurrencyMoney();
        String exchange = mMarketData.getExchangeCode();
        SpannableString title = StrUtil.mergeTextWithRatioColor(baseCurrency + "/" + counterCurrency, exchange,
                0.5f, ContextCompat.getColor(getActivity(), R.color.luckyText));
        mTitleBar.setTitle(title);
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 09/02/2018 分享
            }
        });
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

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:break;
                case 1:
                    break;
                case 2:break;
                case 3:break;
                case 4:break;
                case 5:break;

            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @OnClick(R.id.checkRelatedNews)
    public void onViewClicked() {
    }
}
