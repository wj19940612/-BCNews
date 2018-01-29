package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.activity.BaseActivity;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 提供的基础刷新 加载基类
 */

public abstract class BaseSwipeLoadActivity<T extends View> extends BaseActivity implements SwipeLoadTarget<T>, OnLoadMoreListener, OnRefreshListener {

    private SwipeToLoadLayout mSwipeToLoadLayout;

    /**
     *  该view 的id 必须为 swipe_target
     */
    private T mSwipeTargetView;

    protected void triggerLoadMore() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setLoadingMore(true);
        }
    }

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
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        mSwipeTargetView = getSwipeTargetView();
        mSwipeToLoadLayout = getSwipeToLoadLayout();

        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
        }
    }
}
