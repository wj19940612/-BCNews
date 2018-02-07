package com.sbai.bcnews.fragment;

import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ChannelActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ChannelCacheModel;
import com.sbai.bcnews.model.ChannelEntity;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.ChannelCache;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.slidingTab.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.sbai.bcnews.fragment.NewsFragment.HAS_BANNER;
import static com.sbai.bcnews.fragment.NewsFragment.NO_BANNER;

/**
 * Created by Administrator on 2018\2\5 0005.
 */

public class HomeNewsFragment extends BaseFragment {

    public static final int REQUEST_CODE_CHANNEL = 886;

    Unbinder unbinder;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.reLa)
    RelativeLayout mReLa;
    @BindView(R.id.layout)
    RelativeLayout mLayout;

    private PagerAdapter mPagerAdapter;
    private ChannelCacheModel mChannelCacheModel;
    private List<String> mMyChannels;
    private int mCurrentItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMyChannels = new ArrayList<>();
        initTabView();
        initViewPager();
        getChannels();
        mTabLayout.setViewPager(mViewPager);
    }

    private void initViewPager() {
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity(), mMyChannels);
        mViewPager.setCurrentItem(0, false);
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
    }

    private void getChannels() {
        final ChannelCacheModel channelCacheModel = ChannelCache.getChannel();
        Apic.getChannels().tag(TAG).callback(new Callback2D<Resp<List<String>>, List<String>>() {

            @Override
            protected void onRespSuccessData(List<String> data) {
                ChannelCacheModel contrastedChannelCacheModel = ChannelCache.contrastChannel(channelCacheModel, data);
                updateChannel(contrastedChannelCacheModel);
            }
        }).fireFreely();
    }

    private void updateChannel(ChannelCacheModel channelCacheModel) {
        if (channelCacheModel == null) {
            return;
        }
        mChannelCacheModel = channelCacheModel;
        if (mChannelCacheModel.isModified())
            updateViewPager(channelCacheModel.getMyChannelEntities());

    }

    private void updateViewPager(List<String> myChannelEntities) {
        mViewPager.setOffscreenPageLimit(myChannelEntities.size() - 1);
        mMyChannels.clear();
        mMyChannels.addAll(myChannelEntities);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(mCurrentItem, false);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text:
                mCurrentItem = mViewPager.getCurrentItem();
                Launcher.with(this, ChannelActivity.class).putExtra(ExtraKeys.CHANNEL, mChannelCacheModel).excuteForResultFragment(REQUEST_CODE_CHANNEL);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANNEL && resultCode == RESULT_OK) {
            mChannelCacheModel = data.getParcelableExtra(ExtraKeys.CHANNEL);
            updateChannel(mChannelCacheModel);
        }
    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;
        private List<String> mMyChannelEntities;

        public PagerAdapter(FragmentManager fm, Context context, List<String> myChannelEntities) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
            mMyChannelEntities = myChannelEntities;
        }

        public void setMyChannelEntities(List<String> myChannelEntities) {
            mMyChannelEntities = myChannelEntities;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (mMyChannelEntities != null && mMyChannelEntities.get(position) != null)
                return mMyChannelEntities.get(position);
            return super.getPageTitle(position);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return NewsFragment.newsInstance(HAS_BANNER);
            }
            return NewsFragment.newsInstance(NO_BANNER);
        }

        @Override
        public int getCount() {
            return mMyChannelEntities.size();
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
