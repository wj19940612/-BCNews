package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.search.SearchSynthesizeFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.search.SearchEditText;
import com.sbai.bcnews.view.slidingtab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchEditText.OnSearchContentListener {


    @BindView(R.id.searchEditText)
    SearchEditText mSearchEditText;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private SearchContentFragmentAdapter mSearchContentFragmentAdapter;

    private SearchSynthesizeFragment mSearchSynthesizeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        translucentStatusBar();

        init();
    }

    private void init() {
        mSearchEditText.setOnSearchContentListener(this);
        mSearchEditText.setHint(R.string.please_input_antistop);

        mSearchContentFragmentAdapter = new SearchContentFragmentAdapter(getSupportFragmentManager(), getActivity());
        mViewPager.setAdapter(mSearchContentFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//        mSlidingTabLayout.setCustomTabView(R.layout.view_tab_news, R.id.tab);
        mSlidingTabLayout.setPadding((int) Display.dp2Px(60, getResources()), 0, (int) Display.dp2Px(60, getResources()), 0);
        mSlidingTabLayout.setSelectedIndicatorPadding(Display.dp2Px(30, getResources()));
        mSlidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.home_sliding_text));
        mSlidingTabLayout.setSelectedIndicatorThickness(4);
        mSlidingTabLayout.setHasBottomBorder(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mViewPager.post(() -> {
            SearchSynthesizeFragment searchSynthesizeFragment = getSearchSynthesizeFragment();
            if (searchSynthesizeFragment != null) {

            }
        });
    }

    private SearchSynthesizeFragment getSearchSynthesizeFragment() {
        if (mSearchSynthesizeFragment != null) return mSearchSynthesizeFragment;
        SearchSynthesizeFragment fragment = (SearchSynthesizeFragment) mSearchContentFragmentAdapter.getFragment(0);
        mSearchSynthesizeFragment = fragment;
        return fragment;
    }


    @OnClick(R.id.cancel)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSearchContent(String values) {

    }

    private static class SearchContentFragmentAdapter extends FragmentPagerAdapter {

        private Context mContext;
        private FragmentManager mFragmentManager;

        public SearchContentFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SearchSynthesizeFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.synthesize);
                case 1:
                    return mContext.getString(R.string.author);
                case 2:
                    return mContext.getString(R.string.article);
                case 3:
                    return mContext.getString(R.string.news_flash);
            }
            return super.getPageTitle(position);
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
