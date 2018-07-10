package com.sbai.bcnews.view.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.search.HistorySearch;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by $nishuideyu$ on 2018/7/5
 * <p>
 * Description: 搜索的时候的热门 历史搜索标签
 * </p>
 */
public class SearchLabelLayout extends FrameLayout {

    public static final int HOT_SEARCH_LABEL_MAX_SIZE = 6;

    @BindView(R.id.hotSearchLabel)
    TextView mHotSearchLabel;
    @BindView(R.id.firstHotSearch)
    TextView mFirstHotSearch;
    @BindView(R.id.secondHotSearch)
    TextView mSecondHotSearch;
    @BindView(R.id.ThirdHotSearch)
    TextView mThirdHotSearch;
    @BindView(R.id.ForthHotSearch)
    TextView mForthHotSearch;
    @BindView(R.id.FifthHotSearch)
    TextView mFifthHotSearch;
    @BindView(R.id.sixthHotSearch)
    TextView mSixthHotSearch;
    @BindView(R.id.hotFlexboxLayout)
    FlexboxLayout mHotFlexboxLayout;
    @BindView(R.id.split)
    View mSplit;
    @BindView(R.id.historySearchLabel)
    TextView mHistorySearchLabel;
    @BindView(R.id.firstHistorySearch)
    TextView mFirstHistorySearch;
    @BindView(R.id.secondHistorySearch)
    TextView mSecondHistorySearch;
    @BindView(R.id.ThirdHistorySearch)
    TextView mThirdHistorySearch;
    @BindView(R.id.ForthHistorySearch)
    TextView mForthHistorySearch;
    @BindView(R.id.FifthHistorySearch)
    TextView mFifthHistorySearch;
    @BindView(R.id.sixthHistorySearch)
    TextView mSixthHistorySearch;
    @BindView(R.id.seventhHistorySearch)
    TextView mSeventhHistorySearch;
    @BindView(R.id.eighthHistorySearch)
    TextView mEighthHistorySearch;
    @BindView(R.id.ninthHistorySearch)
    TextView mNinthHistorySearch;
    @BindView(R.id.historyFlexboxLayout)
    FlexboxLayout mHistoryFlexboxLayout;
    @BindView(R.id.clearHistory)
    TextView mClearHistory;
    @BindView(R.id.emptyView)
    TextView mEmptyView;

    private Unbinder mBind;

    private List<String> mHotSearchList;
    private List<String> mHistorySearchList;
    private int mOldHistorySearchSize = 0;

    public SearchLabelLayout(@NonNull Context context) {
        this(context, null);
    }

    public SearchLabelLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchLabelLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_search_label, null);
        addView(view);
        mBind = ButterKnife.bind(this, view);
    }

    @OnClick(R.id.clearHistory)
    public void onViewClicked() {
        Preference.get().setHistoryRearch(null);
        setHistorySearchLabel(null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBind.unbind();
    }

    public void setHotSearchLabel(List<String> data) {
        mHotSearchList = data;
        checkDataIsEmpty();
        updateHotSearch();
    }

    public void setHistorySearchLabel(List<String> data) {
        mHistorySearchList = data;
        checkDataIsEmpty();
        if (data == null || data.isEmpty()) {
            mHistoryFlexboxLayout.setVisibility(GONE);
            mClearHistory.setVisibility(GONE);
            mHistorySearchLabel.setVisibility(GONE);
            return;
        } else {
            mHistoryFlexboxLayout.setVisibility(VISIBLE);
            mClearHistory.setVisibility(VISIBLE);
            mHistorySearchLabel.setVisibility(VISIBLE);
        }
        updateHistorySearch();
    }

    private void updateHotSearch() {
        if (mHotSearchList == null) return;

        int viewSize = 0;
        if (mHotSearchList.size() > HOT_SEARCH_LABEL_MAX_SIZE) {
            viewSize = HOT_SEARCH_LABEL_MAX_SIZE;
        } else {
            viewSize = mHotSearchList.size();
        }

        updateSearchText(viewSize, true, mHotFlexboxLayout, mHotSearchList);
    }

    private void updateHistorySearch() {
        if (mHistorySearchList == null) return;

        int viewSize = 0;
        if (mHistorySearchList.size() > HistorySearch.HISTORY_SEARCH_LABEL_MAX_SIZE) {
            viewSize = HistorySearch.HISTORY_SEARCH_LABEL_MAX_SIZE;
        } else {
            viewSize = mHistorySearchList.size();
        }

        boolean ifNeedUpdateView = false;
        if (mOldHistorySearchSize == mHistorySearchList.size()) {
            ifNeedUpdateView = true;
        }
        mOldHistorySearchSize = mHistorySearchList.size();

        updateSearchText(viewSize, ifNeedUpdateView, mHistoryFlexboxLayout, mHistorySearchList);
    }


    private void updateSearchText(int viewSize, boolean ifNeedUpdateView, FlexboxLayout flexboxLayout, List<String> searchList) {
        if (ifNeedUpdateView) {
            for (int i = 0; i < flexboxLayout.getChildCount(); i++) {
                flexboxLayout.getChildAt(i).setVisibility(GONE);
            }
        }
        for (int i = 0; i < viewSize; i++) {
            TextView textView = (TextView) flexboxLayout.getChildAt(i);
            textView.setText(searchList.get(i));
            if (ifNeedUpdateView) {
                textView.setVisibility(VISIBLE);
            }
        }
    }


    private void checkDataIsEmpty() {
        boolean historySearchIsEmpty = mHistorySearchList == null || mHistorySearchList.isEmpty();
        boolean hotSearchIsEmpty = mHotSearchList == null || mHotSearchList.isEmpty();
        if (historySearchIsEmpty && hotSearchIsEmpty) {
            mEmptyView.setVisibility(VISIBLE);
        } else {
            mEmptyView.setVisibility(GONE);
        }
    }
}
