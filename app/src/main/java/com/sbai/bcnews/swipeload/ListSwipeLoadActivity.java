package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 提供的基础的包含一个ListView 的activity ，不可直接使用，里面封装了ListView的滚动事件监听和刷新 加载方法
 * 如果该activity包含一个ListView
 *
 * 
 * 该类不可直接使用
 */

public abstract class ListSwipeLoadActivity extends BaseSwipeLoadActivity<ListView> {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


        getSwipeTargetView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (view.getLastVisiblePosition() == view.getCount() - 1
                        && !view.canScrollVertically(1)) {
                    triggerLoadMore();
                }
                onListViewScrollStateChanged(view, scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });
    }

    protected void onListViewScrollStateChanged(AbsListView view, int scrollState) {

    }

    protected void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
