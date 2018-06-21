package com.sbai.bcnews.utils.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Zhang on 2018\2\23 0023.
 */

public class NewsWithHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NORMAL_HEADER = 0;
    public static final int EMPTY_HEADER = -1;

    private View mHeaderView;

    private int mHeaderType = NORMAL_HEADER;

    private RecyclerView.Adapter mInnerAdapter;

    public NewsWithHeaderAdapter(RecyclerView.Adapter adapter) {
        mInnerAdapter = adapter;
    }

    public void setHasFoot(boolean mHasFoot) {
        if (mInnerAdapter != null && mInnerAdapter instanceof NewsAdapter) {
            ((NewsAdapter) mInnerAdapter).setHasFoot(mHasFoot);
        }
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
            return mHeaderType;
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

    private boolean isHeaderViewPos(int viewType) {
        return mHeaderView != null && (viewType == EMPTY_HEADER || viewType == NORMAL_HEADER);
    }


    public void setHeaderView(View view) {
        setHeaderView(view, false);
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View view, boolean isEmptyHeader) {
        mHeaderView = view;
        if (isEmptyHeader) {
            mHeaderType = EMPTY_HEADER;
        } else {
            mHeaderType = NORMAL_HEADER;
        }
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
