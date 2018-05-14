package com.sbai.bcnews.view.recycleview;

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
 * APIS:{@link com.songbai.coinpro.http.Api#$methodName$}
 */
public abstract class BaseRecycleViewAdapter<T, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> implements RecycleViewType {

    private List<T> mDataList;

    private final Object mLock = new Object();

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
            notifyDataSetChanged();
        }
    }

    public void add(int position, @Nullable T object) {
        synchronized (mLock) {
            mDataList.add(position, object);
            notifyDataSetChanged();
        }
    }

    public void addAll(@NonNull Collection<? extends T> collection) {
        synchronized (mLock) {
            mDataList.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void remove(@Nullable T object) {
        synchronized (mLock) {
            mDataList.remove(object);
            notifyDataSetChanged();
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

    public T getItemData(int position) {
        return mDataList.get(position);
    }
}
