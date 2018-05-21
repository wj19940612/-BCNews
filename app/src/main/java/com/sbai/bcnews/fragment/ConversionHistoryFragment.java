package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.ConversionHistory;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ConversionHistoryFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.emptyView)
    TextView mEmptyView;

    private Unbinder mBind;

    private HistoryAdapter mHistoryAdapter;
    private List<ConversionHistory> mConversionHistoryList;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversion_history, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData(true);
    }

    private void initView() {
        mConversionHistoryList = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(getContext(), mConversionHistoryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mHistoryAdapter);
    }

    private void loadData(final boolean refresh) {

    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData(true);
    }

    private void updateData(List<ConversionHistory> data, boolean refresh) {
        if (refresh) {
            mConversionHistoryList.clear();
        }
        if (data == null || data.size() == 0) {
            if (refresh) {
                mEmptyView.setVisibility(View.VISIBLE);
            }
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            mHistoryAdapter.notifyDataSetChanged();
            return;
        }
        mEmptyView.setVisibility(View.VISIBLE);
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mConversionHistoryList.addAll(data);
        mHistoryAdapter.notifyDataSetChanged();
    }

    public static class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ConversionHistory> mHistoryList;
        private Context mContext;

        public HistoryAdapter(Context context, List<ConversionHistory> historyList) {
            mHistoryList = historyList;
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_conversion_history, parent, false);
            return new HistoryHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((HistoryHolder) holder).bindingData(mContext, mHistoryList.get(position), position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return mHistoryList.size();
        }

        static class HistoryHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.detail)
            TextView mDetail;
            @BindView(R.id.status)
            TextView mStatus;
            @BindView(R.id.split)
            View mSplit;

            HistoryHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, ConversionHistory conversionHistory, int position, int count) {
                if (position == count - 1) {
                    mSplit.setVisibility(View.GONE);
                } else {
                    mSplit.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
