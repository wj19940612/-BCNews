package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.fragment.BaseFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.slidingtab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.arrow)
    ImageView mArrow;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;
    private ReviewFragmentAdapter mReviewFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mReviewFragmentAdapter = new ReviewFragmentAdapter(getSupportFragmentManager(), getActivity());
        mViewPager.setAdapter(mReviewFragmentAdapter);

        initSlidingTabLayout();
        mAppBarLayout.addOnOffsetChangedListener(sOnOffsetChangedListener);

    }

    static AppBarLayout.OnOffsetChangedListener sOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        }
    };


    private void initSlidingTabLayout() {
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//        mSlidingTabLayout.setCustomTabView(R.layout.view_tab_news, R.id.tab);
        mSlidingTabLayout.setPadding((int) Display.dp2Px(60, getResources()), 0, (int) Display.dp2Px(60, getResources()), 0);
        mSlidingTabLayout.setSelectedIndicatorPadding(Display.dp2Px(30, getResources()));
        mSlidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.home_sliding_text));
        mSlidingTabLayout.setSelectedIndicatorThickness(4);
        mSlidingTabLayout.setHasBottomBorder(false);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    static class ReviewFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext;

        public ReviewFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BaseFragment();
                case 1:
                    return new BaseFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.reply_mine);
                case 1:
                    return mContext.getString(R.string.mine_review);
            }
            return super.getPageTitle(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppBarLayout.removeOnOffsetChangedListener(sOnOffsetChangedListener);
    }
}
