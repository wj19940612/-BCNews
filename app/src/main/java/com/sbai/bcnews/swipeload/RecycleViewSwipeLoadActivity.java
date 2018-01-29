package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 如果使用包含一个recycleView 的activity 可以继承该类
 * 该类封装了 recycleview的上拉加载和滑动事件
 * <p>
 * 该类不可直接使用
 */

public abstract class RecycleViewSwipeLoadActivity extends BaseSwipeLoadActivity<RecyclerView> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
}
