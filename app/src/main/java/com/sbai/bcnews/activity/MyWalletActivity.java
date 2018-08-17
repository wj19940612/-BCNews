package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.WalletCoin;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    private List<WalletCoin> mWalletCoins;
    private WalletAdapter mWalletAdapter;
    private int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public View getContentView() {
        return mRootView;
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

    private void initView() {
        mWalletCoins = new ArrayList<>();
        mWalletAdapter = new WalletAdapter(this, mWalletCoins);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mWalletAdapter);
    }

    private void loadData(boolean refresh) {

    }

    private void updateData(List<WalletCoin> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }
        if (refresh) {
            mWalletCoins.clear();
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mWalletCoins.addAll(data);
        mWalletAdapter.notifyDataSetChanged();
    }

    public static class WalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int HEAD = 0;
        public static final int NORMAL = 1;

        private Context mContext;
        private List<WalletCoin> mWalletCoins;

        public WalletAdapter(Context context, List<WalletCoin> walletCoins) {
            mContext = context;
            mWalletCoins = walletCoins;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == HEAD) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_wallet_header, parent, false);
                return new HeaderHolder(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_wallet, parent, false);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                ((HeaderHolder) holder).bindingData(getItemCount() - 1);
            } else {
                ((ViewHolder) holder).bindingData(mWalletCoins.get(position - 1), position, getItemCount() - 1);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEAD;
            } else {
                return NORMAL;
            }
        }

        @Override
        public int getItemCount() {
            return mWalletCoins.size() + 1;
        }

        static class HeaderHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.line)
            View mLine;

            HeaderHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(int count) {
                if (count == 0) {
                    mLine.setVisibility(View.GONE);
                } else {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.bitType)
            TextView mBitType;
            @BindView(R.id.bitCount)
            TextView mBitCount;
            @BindView(R.id.line)
            View mLine;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(WalletCoin walletCoin, int position, int count) {

                mBitType.setText(walletCoin.getName());

                mBitCount.setText(String.valueOf(walletCoin.getCoin()));

                if (count - 1 == position) {
                    mLine.setVisibility(View.GONE);
                } else {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
