package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

/**
 * Created by ${wangJie} on 2018/1/26.
 * 使用recycleView作为控件 可以上拉加载，下拉刷新
 * 提供了默认的布局  如果要使用自定义布局 可以重写onCreateView
 */

public abstract class RecycleViewSwipeLoadFragment extends BaseSwipeLoadFragment<RecyclerView> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_base_recycleview_swipe_load, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView recycleView = getSwipeTargetView();
        if (recycleView != null) {
            recycleView.addOnScrollListener(mOnScrollListener);
        }
    }


    @NonNull
    @Override
    public SwipeToLoadLayout getSwipeToLoadLayout() {
        if (getView() != null) {
            return getView().findViewById(R.id.swipeToLoadLayout);
        }
        return null;
    }


    @NonNull
    @Override
    public RecyclerView getSwipeTargetView() {
        if (getView() != null) {
            return getView().findViewById(R.id.swipe_target);
        }
        return null;
    }

    @NonNull
    public RefreshHeaderView getRefreshHeaderView() {
        if (getView() != null) {
            return getView().findViewById(R.id.swipe_refresh_header);
        }
        return null;
    }

    protected void refreshSuccess() {
        RefreshHeaderView refreshHeaderView = getRefreshHeaderView();
        if (refreshHeaderView != null) {
            refreshHeaderView.refreshSuccess();
        }
        stopFreshOrLoadAnimation();
    }

    protected void refreshFail() {
        RefreshHeaderView refreshHeaderView = getRefreshHeaderView();
        if (refreshHeaderView != null) {
            refreshHeaderView.refreshFail();
        }
        stopFreshOrLoadAnimation();
    }
    public void smoothScrollToTop(){
        RecyclerView swipeTargetView = getSwipeTargetView();
        if(swipeTargetView!=null){
            swipeTargetView.smoothScrollToPosition(0);
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
    public void onDestroy() {
        super.onDestroy();
        mOnScrollListener = null;
    }
}
