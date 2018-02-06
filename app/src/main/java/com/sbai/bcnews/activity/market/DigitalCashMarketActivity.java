package com.sbai.bcnews.activity.market;

import android.os.Bundle;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.DigitalCashTrendData;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.view.market.DigitalCashTrendView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DigitalCashMarketActivity extends BaseActivity {

    @BindView(R.id.trendView)
    DigitalCashTrendView mTrendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);

        MarketData marketData = getIntent().getParcelableExtra(ExtraKeys.DigitalCash);

        requestDigitalCashDetail(marketData);

        initChartView();
    }

    private void requestDigitalCashDetail(MarketData marketData) {
        Apic.requestDigitalCashDetail(marketData.getCode(), marketData.getExchangeCode())
                .callback(new Callback2D<Resp<MarketData>, MarketData>() {
                    @Override
                    protected void onRespSuccessData(MarketData data) {
                        requestTimeSharingPlanData(data);
                    }
                })
                .tag(TAG)
                .fireFreely();
    }

    private void requestTimeSharingPlanData(MarketData data) {
        Apic.requestTimeSharingPlanData(data.getCode())
                .tag(TAG)
                .callback(new Callback2D<Resp<List<DigitalCashTrendData>>, List<DigitalCashTrendData>>() {
                    @Override
                    protected void onRespSuccessData(List<DigitalCashTrendData> data) {

                    }
                })
                .fireFreely();
    }

    private void initChartView() {
        DigitalCashTrendView.Settings settings = new DigitalCashTrendView.Settings();
        settings.setBaseLines(5);
        settings.setNumberScale(2);
        settings.setIndexesEnable(true);
        settings.setIndexesBaseLines(2);
        settings.setOpenMarketTimes(DigitalCashTrendView.TIME_LINE_DIGITAL_CASH);
        settings.setCalculateXAxisFromOpenMarketTime(true);
        mTrendView.setSettings(settings);
    }
}
