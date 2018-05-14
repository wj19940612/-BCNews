package com.sbai.bcnews.view.recycleview;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/7
 * <p>
 * Description:
 * </p>
 */
public abstract class BaseRecycleViewAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> implements RecycleViewType {

    private List<T> mDataList;

    private final Object mLock = new Object();

    protected int getHeaderViewsCount() {
        return 0;
    }

    protected int getFooterViewsCount() {
        return 0;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public BaseRecycleViewAdapter() {
        this(null);
    }

    public BaseRecycleViewAdapter(@Nullable List<T> dataList) {
        mDataList = dataList == null ? new ArrayList<T>() : dataList;
    }

    public void add(@Nullable T object) {
        synchronized (mLock) {
            mDataList.add(object);
            notifyItemInserted(mDataList.size() + getHeaderViewsCount());
        }
    }

    public void add(@IntRange(from = 0) int position, @Nullable T object) {
        synchronized (mLock) {
            mDataList.add(position, object);
            notifyItemInserted(position + getHeaderViewsCount());
        }
    }


    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            mDataList.addAll(collection);
            notifyItemRangeInserted(mDataList.size() - collection.size() + getHeaderViewsCount(), collection.size());
        }
    }

    public void remove(@Nullable T object) {
        synchronized (mLock) {
            mDataList.remove(object);
            notifyDataSetChanged();
        }
    }

    public void remove(@IntRange(from = 0) int position) {
        synchronized (mLock) {
            mDataList.remove(position);
            int internalPosition = position + getHeaderViewsCount();
            notifyItemRemoved(internalPosition);
            notifyItemRangeChanged(internalPosition, mDataList.size() - internalPosition);
        }
    }

    public void clear() {
        synchronized (mLock) {
            mDataList.clear();
            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        synchronized (mLock) {
            return mDataList == null || mDataList.isEmpty();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @NonNull
    public T getItemData(int position) {
        if (position >= 0 && position < mDataList.size())
            return mDataList.get(position);
        else
            return null;
    }
}
