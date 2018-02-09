package com.sbai.bcnews.swipeload;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 提供的基础的包含一个ListView 的activity ，可直接使用，里面封装了ListView的滚动事件监听和刷新 加载方法
 * 如果该activity包含一个ListView
 * <p>
 * <p>
 * 该类不可直接使用
 */

public class ListSwipeLoadActivity extends BaseSwipeLoadActivity<ListView> {

    @BindView(R.id.titleBar)
    protected TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
     RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
     ListView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);

        ListView swipeTargetView = getSwipeTargetView();

        if (swipeTargetView != null) {
            swipeTargetView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
    }

    protected void onListViewScrollStateChanged(AbsListView view, int scrollState) {

    }

    protected void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    /**
     * @return 根布局view
     */
    public View getContentView() {
        return mRootView;
    }

    /**
     * setContentView 中的布局文件id
     *
     * @return
     */
    @LayoutRes
    public int getContentViewId() {
        return R.layout.view_base_listview_swipe_load;
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
    public ListView getSwipeTargetView() {
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
