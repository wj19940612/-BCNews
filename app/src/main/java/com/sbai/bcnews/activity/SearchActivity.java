package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.ArticleSearchFragment;
import com.sbai.bcnews.fragment.AuthorSearchFragment;
import com.sbai.bcnews.fragment.FlashNewsSearchFragment;
import com.sbai.bcnews.fragment.search.SearchSynthesizeFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.search.HistorySearch;
import com.sbai.bcnews.model.system.Operation;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.view.search.SearchEditText;
import com.sbai.bcnews.view.slidingtab.SlidingTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends BaseActivity implements SearchEditText.OnSearchContentListener, SearchSynthesizeFragment.OnSearchLabelSelectListener, SearchEditText.OnSearchContentResultListener {

    public static final int FRAGMENT_SYNTHESIZE_POSITION = 0;
    public static final int FRAGMENT_AUTHOR_POSITION = 1;
    public static final int FRAGMENT_ARTICLE_POSITION = 2;
    public static final int FRAGMENT_NEWS_FLASH_POSITION = 3;


    @BindView(R.id.searchEditText)
    SearchEditText mSearchEditText;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private SearchContentFragmentAdapter mSearchContentFragmentAdapter;

    SearchSynthesizeFragment mSearchSynthesizeFragment;

    private int mFragmentPosition;

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mSearchEditText.setOnSearchContentListener(this);
        mSearchContentFragmentAdapter = new SearchContentFragmentAdapter(getSupportFragmentManager(), getActivity());
        mViewPager.setAdapter(mSearchContentFragmentAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setDividerColors(ContextCompat.getColor(getActivity(), android.R.color.transparent));
//        mSlidingTabLayout.setCustomTabView(R.layout.view_tab_news, R.id.tab);
//        mSlidingTabLayout.setPadding((int) Display.dp2Px(60, getResources()), 0, (int) Display.dp2Px(60, getResources()), 0);
        mSlidingTabLayout.setSelectedIndicatorPadding(Display.dp2Px(30, getResources()));
        mSlidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorThickness(4);
        mSlidingTabLayout.setHasBottomBorder(true);
        mSlidingTabLayout.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mFragmentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        requestRecommendContent();
    }

    private void requestRecommendContent() {
        Apic.requestOperationSetting(Operation.RECOMMEND_HOT_SEARCH_CONTENT)
                .tag(TAG)
                .callback(new Callback2D<Resp<Operation>, Operation>() {
                    @Override
                    protected void onRespSuccessData(Operation data) {
                        if (TextUtils.isEmpty(data.getSYS_SEARCH())) return;
                        mSearchEditText.setHint(data.getSYS_SEARCH());
                    }
                })
                .fire();
    }

    private SearchSynthesizeFragment getSearchSynthesizeFragment() {
        return (SearchSynthesizeFragment) mSearchContentFragmentAdapter.getFragment(FRAGMENT_SYNTHESIZE_POSITION);
    }

    private AuthorSearchFragment getSearchAuthorFragment() {
        return (AuthorSearchFragment) mSearchContentFragmentAdapter.getFragment(FRAGMENT_AUTHOR_POSITION);
    }

    private ArticleSearchFragment getSearchArticleFragment() {
        return (ArticleSearchFragment) mSearchContentFragmentAdapter.getFragment(FRAGMENT_ARTICLE_POSITION);
    }

    private FlashNewsSearchFragment getSearchFlashNewsFragment() {
        return (FlashNewsSearchFragment) mSearchContentFragmentAdapter.getFragment(FRAGMENT_NEWS_FLASH_POSITION);
    }

    @OnClick(R.id.cancel)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSearchContent(String values) {
        if (mSearchSynthesizeFragment == null) {
            mSearchSynthesizeFragment = getSearchSynthesizeFragment();
        }

        if (mSearchSynthesizeFragment != null) {
            mSearchSynthesizeFragment.updateSearchContent(values);
        }
    }

    @Override
    public void onKeyBoardSearch(String values) {
        mSearchEditText.setText(values);
        updateSearchValues(values);
    }

    @Override
    public void onSearchLabelSelect(String values) {
        mSearchEditText.setText(values);
        updateSearchValues(values);
    }

    private void updateSearchValues(String values) {
        HistorySearch.updateHistorySearch(values);
        SearchSynthesizeFragment searchSynthesizeFragment = getSearchSynthesizeFragment();
        if (searchSynthesizeFragment != null) {
            searchSynthesizeFragment.setSearchContent(values);
        }

        AuthorSearchFragment authorSearchFragment = getSearchAuthorFragment();
        if (authorSearchFragment != null) {
            authorSearchFragment.setSearchContent(values);
        }

        ArticleSearchFragment articleSearchFragment = getSearchArticleFragment();
        if (articleSearchFragment != null) {
            articleSearchFragment.setSearchContent(values);
        }

        FlashNewsSearchFragment flashNewsSearchFragment = getSearchFlashNewsFragment();
        if (flashNewsSearchFragment != null) {
            flashNewsSearchFragment.setSearchContent(values);
        }
    }

    @Override
    public void onSearchFinish(String searchKeyBoard, Object result) {
        KeyBoardUtils.closeKeyboard(mSearchEditText);
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
                case 1:
                    return new AuthorSearchFragment();
                case 2:
                    return new ArticleSearchFragment();
                case 3:
                    return new FlashNewsSearchFragment();

            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
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
