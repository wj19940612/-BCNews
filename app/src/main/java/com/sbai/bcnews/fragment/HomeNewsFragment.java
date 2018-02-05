package com.sbai.bcnews.fragment;

import android.content.Context;
import android.graphics.Color;
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
import com.sbai.bcnews.view.slidingTab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018\2\5 0005.
 */

public class HomeNewsFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViewPager();
        initTabView();
        return view;
    }

    private void initViewPager() {
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setCurrentItem(0, false);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initTabView() {
        mTabLayout.setDistributeEvenly(true);
        mTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        mTabLayout.setPadding((int) Display.dp2Px(10, getResources()), 0, (int) Display.dp2Px(10, getResources()), 0);
        mTabLayout.setWeightEqual(false);
        mTabLayout.setPadding(Display.dp2Px(5, getResources()));
        mTabLayout.setSelectedIndicatorColors(Color.BLACK);
        mTabLayout.setTabViewTextColor(ContextCompat.getColorStateList(getActivity(), R.color.sliding_tab_text));
        mTabLayout.setTabViewTextSize(15);
        mTabLayout.setSelectedIndicatorHeight(2);
        mTabLayout.setHasBottomBorder(false);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;

        public PagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "推荐";
                case 1:
                    return "比特币";
                case 2:
                    return "以太坊";
                case 3:
                    return "比特现金";
                case 4:
                    return "ICO";
                case 5:
                    return "比特现金";
                case 6:
                    return "比特现金";
                case 7:
                    return "比特现金";
                case 8:
                    return "比特现金";
            }
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            return new NewsFragment();
        }

        @Override
        public int getCount() {
            return 1;
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
