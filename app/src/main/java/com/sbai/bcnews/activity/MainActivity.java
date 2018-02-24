package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.HomeNewsFragment;
import com.sbai.bcnews.fragment.MarketFragment;
import com.sbai.bcnews.fragment.MineFragment;
import com.sbai.bcnews.fragment.NewsFlashFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketPageSwitch;
import com.sbai.bcnews.swipeload.BaseSwipeLoadFragment;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.BottomTabs;
import com.sbai.bcnews.view.ScrollableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    public static final int PAGE_POSITION_NEWS = 0;
    public static final int PAGE_POSITION_NEWS_FLASH = 1;
    private static final int PAGE_POSITION_MARKET = 2;
    private static final int PAGE_POSITION_MINE = 3;

    @BindView(R.id.viewPager)
    ScrollableViewPager mViewPager;
    @BindView(R.id.bottomTabs)
    BottomTabs mBottomTabs;

    private MainFragmentsAdapter mMainFragmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initViews();
        requestShowMarketPageSwitch();
    }

    private void requestShowMarketPageSwitch() {
        mBottomTabs.setTabVisibility(PAGE_POSITION_MARKET, View.VISIBLE);
        Apic.requestShowMarketPageSwitch()
                .tag(TAG)
                .callback(new Callback2D<Resp<MarketPageSwitch>, MarketPageSwitch>() {
                    @Override
                    protected void onRespSuccessData(MarketPageSwitch data) {
                        if (data.getQuota() == MarketPageSwitch.SHOW_MARKET_PAGE) {
                            mBottomTabs.setTabVisibility(PAGE_POSITION_MARKET, View.VISIBLE);
                        }
                    }
                })
                .fire();
    }

    private void initViews() {
        mMainFragmentsAdapter = new MainFragmentsAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainFragmentsAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setScrollable(false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mBottomTabs.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0);

        mBottomTabs.setOnTabClickListener(new BottomTabs.OnTabClickListener() {
            @Override
            public void onTabClick(int position) {
                if (position == mViewPager.getCurrentItem() && position != PAGE_POSITION_MARKET) {
                    refreshPageData(position);
                }
                mBottomTabs.selectTab(position);
                mViewPager.setCurrentItem(position, false);
                umengClickStatistics(position);
            }
        });
    }

    private void refreshPageData(int position) {
        Fragment fragment = mMainFragmentsAdapter.getFragment(position);
        if (fragment instanceof BaseSwipeLoadFragment) {
            ((BaseSwipeLoadFragment) fragment).triggerRefresh();
        } else if (fragment instanceof MineFragment) {
            ((MineFragment) fragment).refreshUserData();
        }
    }

    private void umengClickStatistics(int position) {
        switch (position) {
            case PAGE_POSITION_NEWS:
                umengEventCount(UmengCountEventId.NEWS01);
                break;
            case PAGE_POSITION_NEWS_FLASH:
                umengEventCount(UmengCountEventId.NEWS_FLASH_TAB);
                break;
            case PAGE_POSITION_MARKET:
                umengEventCount(UmengCountEventId.MARKET_LIST_TAB);
                break;
            case PAGE_POSITION_MINE:
                umengEventCount(UmengCountEventId.TAB_MINE);
                break;
        }
    }

    private static class MainFragmentsAdapter extends FragmentPagerAdapter {

        FragmentManager mFragmentManager;
        private boolean mShowMarketPage;

        public MainFragmentsAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeNewsFragment();
                case 1:
                    return new NewsFlashFragment();
                case 2:
                    return new MarketFragment();
                case 3:
                    return new MineFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
