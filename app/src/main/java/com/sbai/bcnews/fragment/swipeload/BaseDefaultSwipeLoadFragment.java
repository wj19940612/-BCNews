package com.sbai.bcnews.fragment.swipeload;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;

/**
 * Created by ${wangJie} on 2018/1/26.
 * 提供两种基础 刷新和加载的布局
 * mSwipeTargetType 为1 使用recycleView
 * mSwipeTargetType 为2 使用ListView
 */

public abstract class BaseDefaultSwipeLoadFragment extends BaseSwipeLoadFragment {


    public static final int SWIPE_TARGET_RECYCLE_VIEW = 1;
    public static final int SWIPE_TARGET_LIST_VIEW = 2;

    private static final int DEFAULT_SWIPE_TARGET = SWIPE_TARGET_RECYCLE_VIEW;

    protected int mSwipeTargetType = DEFAULT_SWIPE_TARGET;

    public BaseDefaultSwipeLoadFragment(int swipeTargetType) {
        mSwipeTargetType = swipeTargetType;
    }

    /**
     * 提供两个默认的布局
     * mSwipeTargetType 为1  使用默认的recycleView布局
     * mSwipeTargetType 为2  使用默认的listView布局
     * mSwipeTargetType 为3  使用自定义的布局  也可以直接重写该布局
     *
     * @return onCreateView
     */

    public View getCreateView(Context context) {
        return getCreateView(context, null, null, null);
    }

    public View getCreateView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return getCreateView(null, inflater, container, null);
    }

    protected View getCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getCreateView(null, inflater, container, savedInstanceState);
    }

    private View getCreateView(Context context, LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (context != null) {
            switch (mSwipeTargetType) {
                case SWIPE_TARGET_RECYCLE_VIEW:
                    View rootView = LayoutInflater.from(context).inflate(R.layout.view_base_recycleview_swipe_load, null);
                    mSwipeToLoadLayout = rootView.findViewById(R.id.swipeToLoadLayout);
                    mSwipeTargetView = rootView.findViewById(R.id.swipe_target);
                    return mSwipeTargetView;
                case SWIPE_TARGET_LIST_VIEW:
                    View view = inflater.inflate(R.layout.view_base_listview_swipe_load, null);
                    mSwipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
                    mSwipeTargetView = view.findViewById(R.id.swipe_target);
                default:
                    return null;
            }
        } else {
            switch (mSwipeTargetType) {
                case SWIPE_TARGET_RECYCLE_VIEW:
                    View rootView = inflater.inflate(R.layout.view_base_recycleview_swipe_load, container, false);
                    mSwipeToLoadLayout = rootView.findViewById(R.id.swipeToLoadLayout);
                    mSwipeTargetView = rootView.findViewById(R.id.swipe_target);
                    return rootView;
                case SWIPE_TARGET_LIST_VIEW:
                    View view = inflater.inflate(R.layout.view_base_listview_swipe_load, container, false);
                    mSwipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout);
                    mSwipeTargetView = view.findViewById(R.id.swipe_target);
                    return view;
                default:
                    return null;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @NonNull
    @Override
    protected View getSwipeTargetView() {
        return mSwipeTargetView;
    }

    @NonNull
    @Override
    protected SwipeToLoadLayout getSwipeToLoadLayout() {
        return mSwipeToLoadLayout;
    }
}
