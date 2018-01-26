package com.sbai.bcnews.fragment.swipeload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.fragment.BaseFragment;

/**
 * Created by ${wangJie} on 2018/1/25.
 * 基础的刷新和加载fragment  提供了基础的刷新加载方法
 * <p>
 */

public abstract class BaseSwipeLoadFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    protected SwipeToLoadLayout mSwipeToLoadLayout;

    protected View mSwipeTargetView;

    protected OnListViewScrollListener mOnListViewScrollListener;

    protected OnRecycleViewScrollListener mOnRecycleViewScrollListener;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeTargetView = getSwipeTargetView();

        mSwipeToLoadLayout = getSwipeToLoadLayout();

        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
        }

        if (mSwipeTargetView != null) {
            if (mSwipeTargetView instanceof ListView) {
                initListViewScrollListener((ListView) mSwipeTargetView);
            } else if (mSwipeTargetView instanceof RecyclerView) {
                initRecycleViewOnScrollListener((RecyclerView) mSwipeTargetView);
            }
        }

    }


    private void initRecycleViewOnScrollListener(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                        mSwipeToLoadLayout.setLoadingMore(true);
                    }
                }
                if (mOnRecycleViewScrollListener != null) {
                    mOnRecycleViewScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnRecycleViewScrollListener != null) {
                    mOnRecycleViewScrollListener.onScrolled(recyclerView, dx, dy);
                }
            }
        });
    }

    private void initListViewScrollListener(ListView view) {
        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1 && !view.canScrollVertically(1)) {
                        if (mSwipeToLoadLayout != null) {
                            mSwipeToLoadLayout.setLoadingMore(true);
                        }
                    }
                }
                if (mOnListViewScrollListener != null) {
                    mOnListViewScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mOnListViewScrollListener != null) {
                    mOnListViewScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
    }

    /**
     * 设置需要刷新和加载的的view 如ListView RecycleView
     *
     * @return
     */
    @NonNull
    protected abstract View getSwipeTargetView();

    @NonNull
    protected abstract SwipeToLoadLayout getSwipeToLoadLayout();


    protected void stopFreshOrLoadAnimation() {
        if (mSwipeToLoadLayout != null) {
            if (mSwipeToLoadLayout.isRefreshing()) {
                mSwipeToLoadLayout.setRefreshing(false);
            }
            if (mSwipeToLoadLayout.isLoadingMore()) {
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        }
    }


    protected interface OnListViewScrollListener {
        void onScrollStateChanged(AbsListView view, int scrollState);

        void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                      int totalItemCount);
    }

    protected interface OnRecycleViewScrollListener {

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }

    public void setOnListViewScrollListener(OnListViewScrollListener onListViewScrollListener) {
        this.mOnListViewScrollListener = onListViewScrollListener;
    }

    public void setOnRecycleViewScrollListener(OnRecycleViewScrollListener onRecycleViewScrollListener) {
        this.mOnRecycleViewScrollListener = onRecycleViewScrollListener;
    }

}
