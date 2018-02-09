package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.ScrollableViewPager;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.autofit.AutofitTextView;
import com.sbai.bcnews.view.slidingTab.SlidingTabLayout;

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
    @BindView(R.id.slidingTab)
    SlidingTabLayout mSlidingTab;
    @BindView(R.id.viewPager)
    ScrollableViewPager mViewPager;

    private ChartViewAdapter mViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        ButterKnife.bind(this);

        initTitleBar();
        initSlidingTab();
    }

    private void initSlidingTab() {
        mSlidingTab.setDistributeEvenly(true);
        mSlidingTab.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        mSlidingTab.setTabViewTextSize(12);
        mSlidingTab.setTabViewPadding(Display.dp2Px(10, getResources()));
        mSlidingTab.setSelectedIndicatorPadding(Display.dp2Px(20, getResources()));

        mViewPager.setScrollable(false);
        mViewAdapter = new ChartViewAdapter(getActivity());
        mViewPager.setAdapter(mViewAdapter);

        mSlidingTab.setViewPager(mViewPager);
    }

    private void initTitleBar() {

    }

    static class ChartViewAdapter extends PagerAdapter {

        private Context mContext;

        public ChartViewAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("Temp", "instantiateItem: " + position);
            return new TextView(mContext);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("Temp", "destroyItem: " + position + " " + object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.trend_chart);
                case 1:
                    return mContext.getString(R.string.five_min_k);
                case 2:
                    return mContext.getString(R.string.fifteen_min_k);
                case 3:
                    return mContext.getString(R.string.thirty_min_k);
                case 4:
                    return mContext.getString(R.string.sixty_min_k);
                case 5:
                    return mContext.getString(R.string.day_k_line);
            }
            return super.getPageTitle(position);
        }
    }

//    private void initTabLayout() {
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.trend_chart));
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.five_min_k));
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.fifteen_min_k));
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.thirty_min_k));
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.sixty_min_k));
//        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.day_k_line));
//        mTabLayout.addOnTabSelectedListener(mOnTabSelectedListener);
//    }

    private TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String tabText = tab.getText().toString();
            if (tabText.equals(getString(R.string.day_k_line))) {
            } else if (tabText.equals(getString(R.string.sixty_min_k))) {
            } else if (tabText.equals(getString(R.string.thirty_min_k))) {
            } else if (tabText.equals(getString(R.string.fifteen_min_k))) {
            } else {
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
