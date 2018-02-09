package com.sbai.bcnews.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Sampler;
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
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
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
import static android.view.animation.Animation.ABSOLUTE;
import static android.view.animation.Animation.RELATIVE_TO_PARENT;
import static com.sbai.bcnews.fragment.NewsFragment.HAS_BANNER;
import static com.sbai.bcnews.fragment.NewsFragment.NO_BANNER;

/**
 * Created by Administrator on 2018\2\5 0005.
 */

public class HomeNewsFragment extends BaseFragment implements NewsFragment.OnScrollListener {

    public static final int REQUEST_CODE_CHANNEL = 886;

    public static final int SCROLL_STATE_NORMAL = 0;
    public static final int SCROLL_STATE_GONE = 1;

    public static final int SCROLL_GLIDING = 20;

    Unbinder unbinder;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.toChannel)
    ImageView mToChannel;
    @BindView(R.id.reLa)
    RelativeLayout mReLa;
    @BindView(R.id.layout)
    RelativeLayout mLayout;

    private PagerAdapter mPagerAdapter;
    private ChannelCacheModel mChannelCacheModel;
    private List<String> mMyChannels;
    private int mCurrentItem;
    private boolean mAnimating;
    private int mTitleScrollState;  //0-默认 1-已经滚上去了

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
        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity(), mMyChannels, this);
        mViewPager.setCurrentItem(0, false);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initTabView() {
        mTabLayout.setDistributeEvenly(false);
        mTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        mTabLayout.setCustomTabView(R.layout.view_home_tab, R.id.tab);
        mTabLayout.setSelectedIndicatorPadding(Display.dp2Px(15, getResources()));
        mTabLayout.setSelectedIndicatorColors(Color.BLACK);
        mTabLayout.setSelectedIndicatorHeight(2);
        mTabLayout.setHasBottomBorder(false);
    }

    private void getChannels() {
        final ChannelCacheModel channelCacheModel = ChannelCache.getChannel();
        Apic.getChannels().tag(TAG).callback(new Callback2D<Resp<List<String>>, List<String>>() {

            @Override
            protected void onRespSuccessData(List<String> data) {
                ChannelCacheModel contrastedChannelCacheModel = ChannelCache.contrastChannel(channelCacheModel, data);
                updateChannel(contrastedChannelCacheModel, true);
            }
        }).fireFreely();
    }

    private void updateChannel(ChannelCacheModel channelCacheModel, boolean mustRefresh) {
        if (channelCacheModel == null) {
            return;
        }
        mChannelCacheModel = channelCacheModel;
        if (mChannelCacheModel.isModified() || mustRefresh)
            updateViewPager(channelCacheModel.getMyChannelEntities());

    }

    private void updateViewPager(List<String> myChannelEntities) {
        mViewPager.setOffscreenPageLimit(myChannelEntities.size() - 1);
        mMyChannels.clear();
        mMyChannels.addAll(myChannelEntities);
        mPagerAdapter.notifyDataSetChanged();
        if (mCurrentItem < mChannelCacheModel.getMyChannelEntities().size() - 1)
            mViewPager.setCurrentItem(mCurrentItem, false);
        else
            mViewPager.setCurrentItem(mChannelCacheModel.getMyChannelEntities().size() - 1, false);
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.toChannel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toChannel:
                mCurrentItem = mViewPager.getCurrentItem();
                Launcher.with(this, ChannelActivity.class).putExtra(ExtraKeys.CHANNEL, mChannelCacheModel).excuteForResultFragment(REQUEST_CODE_CHANNEL);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHANNEL && resultCode == RESULT_OK) {
            mChannelCacheModel = data.getParcelableExtra(ExtraKeys.CHANNEL);
            updateChannel(mChannelCacheModel, false);
        }
    }

    @Override
    public void onScroll(int dy) {
        if (Math.abs(dy) < SCROLL_GLIDING) {
            return;
        }
        scrollTitleBar(dy > 0 ? true : false);
    }

    private void scrollTitleBar(final boolean down) {
        if (mAnimating || (mTitleScrollState == SCROLL_STATE_GONE && down) || (mTitleScrollState == SCROLL_STATE_NORMAL && !down)) {
            return;
        }
        int titleHeight = mTitleBar.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(down ? 0 : -titleHeight, down ? -titleHeight : 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mTitleBar.getLayoutParams();
                lp.setMargins(0, y, 0, 0);
                mTitleBar.setLayoutParams(lp);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mTitleScrollState = down ? SCROLL_STATE_GONE : SCROLL_STATE_NORMAL;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(300);
        mAnimating = true;
        valueAnimator.start();
    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;
        private List<String> mMyChannelEntities;
        private NewsFragment.OnScrollListener mOnScrollListener;

        public PagerAdapter(FragmentManager fm, Context context, List<String> myChannelEntities, NewsFragment.OnScrollListener onScrollListener) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
            mMyChannelEntities = myChannelEntities;
            mOnScrollListener = onScrollListener;
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
            String title = mMyChannelEntities.get(position);
            if (position == 0) {
                NewsFragment newsFragment = NewsFragment.newsInstance(HAS_BANNER,title);
                newsFragment.setOnScrollListener(mOnScrollListener);
                return newsFragment;
            }
            NewsFragment newsFragment = NewsFragment.newsInstance(NO_BANNER,title);
            newsFragment.setOnScrollListener(mOnScrollListener);
            return newsFragment;
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
