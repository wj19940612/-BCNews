package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.os.PersistableBundle;
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

    public void triggerRefresh() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(true);
        }
    }

    protected void triggerLoadMore() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setLoadingMore(true);
        }
    }

    public void refreshFail() {
        refreshFail(getString(R.string.refresh_fail));
    }

    public void refreshFail(@StringRes int resId) {
        refreshFail(getString(resId));
    }

    @Override
    public void refreshFail(String failMsg) {
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.refreshFail();
        }
        stopFreshOrLoadAnimation();
    }

    public void refreshSuccess() {
        refreshSuccess(getString(R.string.refresh_complete));
    }

    public void refreshSuccess(@StringRes int resId) {
        refreshSuccess(getString(resId));
    }

    @Override
    public void refreshSuccess(String successMsg) {
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.refreshSuccess(successMsg);
        }
        stopFreshOrLoadAnimation();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mSwipeTargetView = getSwipeTargetView();
        mSwipeToLoadLayout = getSwipeToLoadLayout();
        mRefreshHeaderView = getRefreshHeaderView();
        mLoadMoreFooterView = getLoadMoreFooterView();


        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
        }
    }

}
