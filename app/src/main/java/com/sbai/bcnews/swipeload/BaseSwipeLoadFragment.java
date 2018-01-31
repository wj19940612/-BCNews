package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.fragment.BaseFragment;

/**
 * Created by ${wangJie} on 2018/1/25.
 * 基础的刷新和加载总父类fragment  提供了基础的刷新加载方法
 * 提供了两个直接子类 BaseListViewSwipeLoadFragment  和 BaseRecycleViewSwipeLoadFragment
 * 可以直接继承这两个子类
 * <p>
 */

public abstract class BaseSwipeLoadFragment<T extends View> extends BaseFragment implements OnLoadMoreListener, OnRefreshListener, SwipeLoadTarget<T> {

    private SwipeToLoadLayout mSwipeToLoadLayout;

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

    public void triggerRefresh() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(true);
            if (mSwipeTargetView instanceof RecyclerView) {
                ((RecyclerView) mSwipeTargetView).scrollToPosition(0);
            }
            if (mSwipeTargetView instanceof ListView) {
                ((ListView) mSwipeTargetView).smoothScrollToPosition(0);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeTargetView = getSwipeTargetView();
        mSwipeToLoadLayout = getSwipeToLoadLayout();

        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setOnLoadMoreListener(this);
            mSwipeToLoadLayout.setOnRefreshListener(this);
        }
    }

}
