package com.sbai.bcnews.utils.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zhang on 2018\2\23 0023.
 */

public class NewsWithHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View mHeaderView;

    private RecyclerView.Adapter mInnerAdapter;

    public NewsWithHeaderAdapter(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //第0项是header,header的viewType=0 并且headerCount != 0
        if (isHeaderViewPos(viewType)) {
            return new BannerHolder(mHeaderView);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderViewPos(position)) {
            return position;
        }
        return mInnerAdapter.getItemViewType(position - getHeadersCount());
    }

    private int getRealItemCount() {
        return mInnerAdapter.getItemCount();
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position)) {
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getRealItemCount();
    }

    private boolean isHeaderViewPos(int position) {
        return position < getHeadersCount();
    }


    public void addHeaderView(View view) {
        mHeaderView = view;
    }


    public int getHeadersCount() {
        return mHeaderView == null ? 0 : 1;
    }


    static class BannerHolder extends RecyclerView.ViewHolder {
        public BannerHolder(View itemView) {
            super(itemView);
        }
    }
}
