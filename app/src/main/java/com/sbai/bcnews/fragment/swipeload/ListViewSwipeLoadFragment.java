package com.sbai.bcnews.fragment.swipeload;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;

/**
 * Created by ${wangJie} on 2018/1/26.
 * 使用listView作为控件 可以上拉加载，下拉刷新
 * 提供了默认的布局  如果要使用自定义布局 可以重写onCreateView
 */

public abstract class ListViewSwipeLoadFragment extends BaseSwipeLoadFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_base_listview_swipe_load, container, false);
    }

    @NonNull
    @Override
    protected SwipeToLoadLayout getSwipeToLoadLayout() {
        return getView().findViewById(R.id.swipeToLoadLayout);
    }

    @NonNull
    @Override
    protected View getSwipeTargetView() {
        return getView().findViewById(R.id.swipe_target);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView listView = getListView();
        listView.setOnScrollListener(mOnScrollChangeListener);
    }

    protected ListView getListView() {
        return (ListView) mSwipeTargetView;
    }

    protected AbsListView.OnScrollListener mOnScrollChangeListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                if (view.getLastVisiblePosition() == view.getCount() - 1 && !view.canScrollVertically(1)) {
                    onScrollToBottom();
                }
            }
            onListViewScrollStateChanged(view, scrollState);
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    };

    protected void onListViewScrollStateChanged(AbsListView view, int scrollState) {

    }

    protected void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
