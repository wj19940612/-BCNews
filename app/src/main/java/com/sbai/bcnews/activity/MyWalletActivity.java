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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.CoinHistory;
import com.sbai.bcnews.model.WalletCoin;
import com.sbai.bcnews.swipeload.BaseSwipeLoadActivity;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletActivity extends BaseSwipeLoadActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;
    @BindView(R.id.emptyView)
    LinearLayout mEmptyView;
    @BindView(R.id.swipe_target)
    RelativeLayout mSwipeTarget;

    private List<WalletCoin> mWalletCoins;
    private WalletAdapter mWalletAdapter;
    private int mPage;

    protected RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                    triggerLoadMore();
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isTop = layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                if(!isTop){
                    mSwipeToLoadLayout.setRefreshEnabled(false);
                }else{
                    mSwipeToLoadLayout.setRefreshEnabled(true);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @NonNull
    @Override
    public Object getSwipeTargetView() {
        return mSwipeTarget;
    }

    @NonNull
    @Override
    public SwipeToLoadLayout getSwipeToLoadLayout() {
        return mSwipeToLoadLayout;
    }

    @NonNull
    @Override
    public RefreshHeaderView getRefreshHeaderView() {
        return mSwipeRefreshHeader;
    }

    @NonNull
    @Override
    public LoadMoreFooterView getLoadMoreFooterView() {
        return mSwipeLoadMoreFooter;
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
        mWalletAdapter = new WalletAdapter(this, mWalletCoins, new OnItemClickListener<WalletCoin>() {
            @Override
            public void onItemClick(WalletCoin walletCoin, int position) {
                Launcher.with(getActivity(), CoinHistoryActivity.class).putExtra(ExtraKeys.TYPE,walletCoin.getCoinSymbol()).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mWalletAdapter);
    }

    private void loadData(boolean refresh) {
        Apic.requestMyWallet(mPage).tag(TAG).callback(new Callback<ListResp<WalletCoin>>() {
            @Override
            protected void onRespSuccess(ListResp<WalletCoin> resp) {
                updateData(resp.getListData(), refresh);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }
        }).fireFreely();
    }

    private void updateData(List<WalletCoin> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            if (refresh) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
            mWalletAdapter.notifyDataSetChanged();
            return;
        }
        if (refresh) {
            mWalletCoins.clear();
        }
        mEmptyView.setVisibility(View.GONE);
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
        private OnItemClickListener<WalletCoin> mOnItemClickListener;

        public WalletAdapter(Context context, List<WalletCoin> walletCoins, OnItemClickListener<WalletCoin> onItemClickListener) {
            mContext = context;
            mWalletCoins = walletCoins;
            mOnItemClickListener = onItemClickListener;
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
                ((ViewHolder) holder).bindingData(mWalletCoins.get(position - 1), position-1, getItemCount()-1, mOnItemClickListener);
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
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(WalletCoin walletCoin, int position, int count, OnItemClickListener<WalletCoin> onItemClickListener) {

                mBitType.setText(walletCoin.getCoinSymbol());

                mBitCount.setText(String.valueOf(walletCoin.getAbleCoin()));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(walletCoin, position);
                        }
                    }
                });
                if (count - 1 == position) {
                    mLine.setVisibility(View.GONE);
                } else {
                    mLine.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
