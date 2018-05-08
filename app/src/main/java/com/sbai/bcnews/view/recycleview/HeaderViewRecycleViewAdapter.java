package com.sbai.bcnews.view.recycleview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/4/19
 * <p>
 * Description: 添加头部和尾部的adapter
 * 暂时只支持添加一个头部和一个尾部
 * </p>
 */
public abstract class HeaderViewRecycleViewAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter implements HeaderViewController {

    private List<T> mDataList;

    private View mHeaderView;

    private View mFooterView;

    private View mEmptyView;

    private final Object mLock = new Object();


    @NonNull
    public abstract K onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void onBindContentViewHolder(@NonNull K holder, int position);


    public HeaderViewRecycleViewAdapter() {
        this(null);
    }

    public HeaderViewRecycleViewAdapter(@Nullable List<T> dataList) {
        mDataList = dataList == null ? new ArrayList<T>() : dataList;
    }



    public boolean isEmpty() {
        synchronized (mLock) {
            return mDataList.isEmpty();
        }
    }

    @NonNull
    public T getItemData(int position) {
        return mDataList.get(position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                if (mHeaderView != null) {
                    return new HeaderViewViewHolder(mHeaderView);
                }
            case FOOTER_VIEW_TYPE:
                if (mFooterView != null) {
                    return new FooterViewViewHolder(mFooterView);
                }
            default:
                return onContentCreateViewHolder(parent, viewType);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                break;
            case FOOTER_VIEW_TYPE:
                break;
            default:
                onBindContentViewHolder((K) holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return mDataList.size() + getFooterViewsCount() + getHeaderViewsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (getHeaderViewsCount() != 0 && position == 0) {
            return HEADER_VIEW_TYPE;
        }
        if (getFooterViewsCount() != 0 && position == mDataList.size()) {
            return FOOTER_VIEW_TYPE;
        }
        return super.getItemViewType(position);
    }


    @Override
    public void addHeaderView(View view) {
        synchronized (mLock) {
            mHeaderView = view;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getHeaderViewsCount() {
        if (mHeaderView != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean removeHeaderView() {
        mFooterView = null;
        notifyDataSetChanged();
        return false;
    }

    @Override
    public void addFooterView(View view) {
        mFooterView = view;
        notifyDataSetChanged();
    }

    @Override
    public int getFooterViewsCount() {
        if (mFooterView != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean removeFooterView() {
        mFooterView = null;
        notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean hasFooterView() {
        return mFooterView != null;
    }

    public static class HeaderViewViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterViewViewHolder extends RecyclerView.ViewHolder {

        public FooterViewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
