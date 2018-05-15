package com.sbai.bcnews.utils.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/4/25
 * <p>
 * Description:
 * </p>
 */
public abstract class BaseRecycleViewAdapter<T, K> extends RecyclerView.Adapter {

    private final Object mLock = new Object();

    private List<T> mDataList;


    public abstract K onDefaultCreateViewHolder(ViewGroup parent, int viewType);


    public abstract void onBindDefaultViewHolder(K holder, T data, int position);

    public BaseRecycleViewAdapter() {
        this(null);
    }

    public BaseRecycleViewAdapter(@Nullable List<T> dataList) {
        mDataList = dataList == null ? new ArrayList<T>() : dataList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (RecyclerView.ViewHolder) onDefaultCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindDefaultViewHolder((K) holder, mDataList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataList != null ? mDataList.size() : 0;
    }


    public void add(@Nullable T object) {
        synchronized (mLock) {
            mDataList.add(object);
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
            return getItemCount() == 0;
        }
    }

}
