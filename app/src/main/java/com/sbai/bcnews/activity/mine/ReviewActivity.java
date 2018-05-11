package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.activity.dialog.WriteCommentActivity;
import com.sbai.bcnews.fragment.mine.ReplyMineFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.slidingtab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReviewActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

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
        mViewPager.setOffscreenPageLimit(2);
        initSlidingTabLayout();
        mAppBarLayout.addOnOffsetChangedListener(sOnOffsetChangedListener);
    }

    AppBarLayout.OnOffsetChangedListener sOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset < -20) {
                if (mBack.getVisibility() == View.GONE) {
                    mBack.setVisibility(View.VISIBLE);
                }
            } else {
                if (mBack.getVisibility() == View.VISIBLE) {
                    mBack.setVisibility(View.GONE);
                }
            }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == BaseActivity.RESULT_OK) {
            switch (requestCode) {
                case WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_MINE_REPLY:
                    //海天说不刷
//                    if (data != null) {
//                        WriteCommentResponse writeCommentResponse = data.getParcelableExtra(ExtraKeys.DATA);
//                        if (writeCommentResponse != null) {
//                            ReplyNews replyComment = writeCommentResponse.getReplyComment();
//                            ReplyMineFragment item = (ReplyMineFragment) mReviewFragmentAdapter.getFragment(1);
//                        }
//                    }
                    break;
            }
        }
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    static class ReviewFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;

        public ReviewFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragmentManager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ReplyMineFragment.newInstance(ReplyMineFragment.NEWS_TYPE_REPLY_TO_MINE);
                case 1:
                    return ReplyMineFragment.newInstance(ReplyMineFragment.NEWS_TYPE_MINE_COMMENT);
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

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppBarLayout.removeOnOffsetChangedListener(sOnOffsetChangedListener);
    }
}
