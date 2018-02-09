package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 如果使用包含一个recycleView 的activity 可以继承该类
 * 该类封装了 recycleview的上拉加载和滑动事件
 * <p>
 * 该类不可直接使用
 */

public  class RecycleViewSwipeLoadActivity extends BaseSwipeLoadActivity<RecyclerView> {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.view_base_recycleview_swipe_load);
        ButterKnife.bind(this);
        if (getSwipeTargetView() != null) {
            getSwipeTargetView().addOnScrollListener(mOnScrollListener);
        }
    }

    protected void onRecycleViewScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    protected void onRecycleViewScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    protected RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                    triggerLoadMore();
                }
            }
            onRecycleViewScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            onRecycleViewScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getSwipeTargetView() != null) {
            getSwipeTargetView().removeOnScrollListener(mOnScrollListener);
        }
    }

    /**
     * @return 根布局view
     */
    public View getContentView() {
        return mRootView;
    }

    @NonNull
    @Override
    public SwipeToLoadLayout getSwipeToLoadLayout() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.findViewById(R.id.swipeToLoadLayout);
        }
        return null;
    }

    @NonNull
    @Override
    public RecyclerView getSwipeTargetView() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.findViewById(R.id.swipe_target);
        }
        return null;
    }


    @NonNull
    @Override
    public RefreshHeaderView getRefreshHeaderView() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.findViewById(R.id.swipe_refresh_header);
        }
        return null;
    }

    @NonNull
    @Override
    public LoadMoreFooterView getLoadMoreFooterView() {
        View contentView = getContentView();
        if (contentView != null) {
            return contentView.findViewById(R.id.swipe_load_more_footer);
        }
        return null;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
