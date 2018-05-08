package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.slidingtab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description:
 * <p>
 * APIs:
 * 首页行情列表
 */
public class MarketFragment extends BaseFragment {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    Unbinder unbinder;
    private marketListFragmentAdapter mMarketListFragmentAdapter;


    public enum BourseName {
        GATE_IO("gate.io"), HUO_BI("huobi"), BI_AN("bian");
        private String name;

        BourseName(String name) {
            this.name = name;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mMarketListFragmentAdapter = new marketListFragmentAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setCurrentItem(0, false);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mMarketListFragmentAdapter);

        initSlidingTabLayout();
    }

    private void initSlidingTabLayout() {
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//        mSlidingTabLayout.setCustomTabView(R.layout.view_tab_news, R.id.tab);
//        mSlidingTabLayout.setPadding((int) Display.dp2Px(60, getResources()), 0, (int) Display.dp2Px(60, getResources()), 0);
        mSlidingTabLayout.setSelectedIndicatorPadding(Display.dp2Px(40, getResources()));
        mSlidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(getActivity(), R.color.home_sliding_text));
        mSlidingTabLayout.setSelectedIndicatorThickness(4);
        mSlidingTabLayout.setHasBottomBorder(false);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class marketListFragmentAdapter extends FragmentPagerAdapter {
        private Context mContext;

        public marketListFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return MarketListFragment.newInstance(BourseName.GATE_IO.name);
                case 1:
                    return MarketListFragment.newInstance(BourseName.HUO_BI.name);
                case 2:
                    return MarketListFragment.newInstance(BourseName.BI_AN.name);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.gate);
                case 1:
                    return mContext.getString(R.string.huo_bi);
                case 2:
                    return mContext.getString(R.string.bi_an);
            }
            return super.getPageTitle(position);
        }
    }
}
