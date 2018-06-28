package com.sbai.bcnews.swipeload;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 下拉刷新  上拉加载的接口
 * <p>
 * 有两个实现类  {@link BaseSwipeLoadActivity
 * {@link BaseSwipeLoadFragment 这两个实现子类
 */

public interface SwipeLoader<T> {

    /**
     * 设置需要刷新和加载的的view 如ListView RecycleView
     * <p>
     * 该view 的id 必须为 swipe_target
     *
     * @return
     */
    @NonNull
    T getSwipeTargetView();

    @NonNull
    SwipeToLoadLayout getSwipeToLoadLayout();

    @NonNull
    RefreshHeaderView getRefreshHeaderView();

    @NonNull
    LoadMoreFooterView getLoadMoreFooterView();

    void triggerRefresh();

    void triggerLoadMore();

    @NonNull
    void refreshComplete(CharSequence msg);

    void refreshComplete(@StringRes int msgRes);

    void loadMoreComplete(CharSequence msg);

    void loadMoreComplete(@StringRes int msgRes);

    /**
     * <p>
     * 是否使用默认的自动加载条件
     * 默认为true
     * 如果改为false  需要自己实现listview或者recycleview的滑动事件判断 来处理加载更多事件
     * </p>
     *
     * @return
     */
    default boolean isUseDefaultLoadMoreConditions() {
        return true;
    }

}
