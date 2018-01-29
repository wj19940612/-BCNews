package com.sbai.bcnews.swipeload;

import android.support.annotation.NonNull;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Created by ${wangJie} on 2018/1/29.
 * 下拉刷新  上拉加载的接口
 * <p>
 * 有两个实现类  {@link BaseSwipeLoadActivity
 *              {@link BaseSwipeLoadFragment 这两个实现子类
 */

public interface SwipeLoadTarget<T> {

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
}
