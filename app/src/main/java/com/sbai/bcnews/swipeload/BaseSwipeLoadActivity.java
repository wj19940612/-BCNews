package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 提供的基础刷新 加载基类
 * 有两个子类 可以直接使用 {@link ListSwipeLoadActivity }和{@link RecycleViewSwipeLoadActivity}
 */

public abstract class BaseSwipeLoadActivity<T extends View> extends BaseActivity implements
        SwipeLoader<T>, OnLoadMoreListener, OnRefreshListener {

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private T mSwipeTargetView;

    private RefreshHeaderView mRefreshHeaderView;

    private LoadMoreFooterView mLoadMoreFooterView;

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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setup(getSwipeTargetView(), getSwipeToLoadLayout(), getRefreshHeaderView(), getLoadMoreFooterView());
    }

    @Override
    public void triggerRefresh() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(true);
        }
    }

    @Override
    public void triggerLoadMore() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setLoadingMore(true);
        }
    }

    public void refreshFailure() {
        refreshFailure(getString(R.string.refresh_fail));
    }

    @Override
    public void refreshFailure(@StringRes int resId) {
        refreshFailure(getString(resId));
    }

    @Override
    public void refreshFailure(String msg) {
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.refreshFail();
        }
        stopFreshOrLoadAnimation();
    }

    public void refreshSuccess() {
        refreshSuccess(getString(R.string.refresh_complete));
    }

    @Override
    public void refreshSuccess(@StringRes int resId) {
        refreshSuccess(getString(resId));
    }

    @Override
    public void refreshSuccess(String msg) {
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.refreshSuccess(msg);
        }
        stopFreshOrLoadAnimation();
    }

    private void setup(T swipeTargetView, SwipeToLoadLayout swipeToLoadLayout,
                         RefreshHeaderView refreshHeaderView, LoadMoreFooterView loadMoreFooterView) {
        mSwipeTargetView = swipeTargetView;
        mSwipeToLoadLayout = swipeToLoadLayout;
        mRefreshHeaderView = refreshHeaderView;
        mLoadMoreFooterView = loadMoreFooterView;

        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
        }
    }
}
