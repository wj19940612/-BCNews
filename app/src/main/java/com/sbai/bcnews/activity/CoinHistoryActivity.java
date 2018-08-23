package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.sbai.bcnews.model.CoinCount;
import com.sbai.bcnews.model.CoinHistory;
import com.sbai.bcnews.swipeload.BaseSwipeLoadActivity;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinHistoryActivity extends RecycleViewSwipeLoadActivity {

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
    RelativeLayout mRootView;

    private String mType;
    private int mPage;

    private HistoryAdapter mHistoryAdapter;
    private List<CoinHistory> mCoinHistories;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_history);
        ButterKnife.bind(this);
        initData();
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mPage = 0;
        loadData(true);
    }

    private void initData() {
        mType = getIntent().getStringExtra(ExtraKeys.TYPE);
    }

    private void initView() {
        mTitleBar.setTitle(mType);
        mCoinHistories = new ArrayList<>();
        mHistoryAdapter = new HistoryAdapter(this, mCoinHistories, new HistoryAdapter.OnGetCoinClickListener() {
            @Override
            public void onGetCoinClick(String usableCoin, String type) {
                Launcher.with(getActivity(), WithDrawCoinActivity.class).putExtra(ExtraKeys.TYPE, type).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mHistoryAdapter);
    }

    private void loadData(boolean refresh) {

        Apic.requestCoinCount(mType).tag(TAG).callback(new Callback2D<Resp<CoinCount>, CoinCount>() {
            @Override
            protected void onRespSuccessData(CoinCount data) {
                if (data != null) {
                    mHistoryAdapter.setAllCount(data.getAbleCoin(), mType);
                    mHistoryAdapter.notifyDataSetChanged();
                }
            }
        }).fireFreely();

        Apic.requestCoinHistory(mType, mPage).tag(TAG).callback(new Callback<ListResp<CoinHistory>>() {
            @Override
            protected void onRespSuccess(ListResp<CoinHistory> resp) {
                updateData(resp.getListData(), refresh);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }
        }).fireFreely();
    }

    private void updateData(ArrayList<CoinHistory> data, boolean refresh) {
        if (refresh) {
            mCoinHistories.clear();
        }
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            if (refresh) {
                mHistoryAdapter.setShowEmptyView(true);
            } else {
                mHistoryAdapter.setShowEmptyView(false);
            }
            refreshFoot(0);
            mHistoryAdapter.notifyDataSetChanged();
            return;
        }
        mHistoryAdapter.setShowEmptyView(false);
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mCoinHistories.addAll(data);
        refreshFoot(data.size());
        if (!TextUtils.isEmpty(mHistoryAdapter.getType())) {
            mHistoryAdapter.notifyDataSetChanged();
        }
    }

    private void refreshFoot(int size) {
        if (mCoinHistories.size() >= Apic.DEFAULT_PAGE_SIZE && size < Apic.DEFAULT_PAGE_SIZE) {
            mHistoryAdapter.setShowFoot(true);
        } else {
            mHistoryAdapter.setShowFoot(false);
        }
    }

    public static class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public static final int HEADER = 0;
        public static final int NORMAL = 1;
        public static final int EMPTY = 2;

        interface OnGetCoinClickListener {
            public void onGetCoinClick(String usableCoin, String type);
        }

        private Context mContext;
        private List<CoinHistory> mCoinHistoryList;
        private String mAllCount;
        private String mType;
        private OnGetCoinClickListener mOnGetCoinClickListener;
        private boolean mShowEmptyView;
        private boolean mShowFoot;

        public HistoryAdapter(Context context, List<CoinHistory> coinHistories, OnGetCoinClickListener onGetCoinClickListener) {
            mContext = context;
            mCoinHistoryList = coinHistories;
            mOnGetCoinClickListener = onGetCoinClickListener;
        }

        public void setAllCount(String allCount, String type) {
            mAllCount = allCount;
            mType = type;
        }

        public String getType() {
            return mType;
        }

        public void setShowEmptyView(boolean showEmptyView) {
            mShowEmptyView = showEmptyView;
        }

        public void setShowFoot(boolean isShowFoot) {
            mShowFoot = isShowFoot;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == HEADER) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_coin_history_header, parent, false);
                return new HeaderHolder(view);
            } else if (viewType == EMPTY) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.layout_empty_wallet, parent, false);
                return new EmptyHolder(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_coin_history, parent, false);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeaderHolder) {
                ((HeaderHolder) holder).bindingData(mContext, mAllCount, mType, mOnGetCoinClickListener);
            } else if (holder instanceof EmptyHolder) {

            } else {
                ((ViewHolder) holder).bindingData(mShowFoot, position - 1, getItemCount() - 1, mContext, mCoinHistoryList.get(position - 1), mType);
            }
        }

        @Override
        public int getItemCount() {
            return mCoinHistoryList.size() + 1 + (mShowEmptyView ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER;
            } else if (position == 1 && mShowEmptyView) {
                return EMPTY;
            } else {
                return NORMAL;
            }
        }

        static class HeaderHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.allCount)
            TextView mAllCount;
            @BindView(R.id.getCoin)
            TextView mGetCoin;
            @BindView(R.id.type)
            TextView mType;

            HeaderHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(Context context, String allCount, String type, OnGetCoinClickListener onGetCoinClickListener) {

                mAllCount.setText(allCount);

                mType.setText(type);

                mGetCoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onGetCoinClickListener != null) {
                            onGetCoinClickListener.onGetCoinClick(allCount, type);
                        }
                    }
                });

            }
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.line)
            View mLine;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.count)
            TextView mCount;
            @BindView(R.id.footer)
            LinearLayout mFooter;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindingData(boolean showFoot, int position, int count, Context context, CoinHistory coinHistory, String type) {
                mName.setText(coinHistory.getTypeStr());

                mTime.setText(DateUtil.formatSlash(coinHistory.getUpdateTime()));

                if (coinHistory.getChangeType() == CoinHistory.ADD) {
                    mCount.setTextColor(ContextCompat.getColor(context, R.color.green));
                    mCount.setText("+" + coinHistory.getExtractNum() + "   " + type);
                } else {
                    mCount.setTextColor(ContextCompat.getColor(context, R.color.red));
                    mCount.setText("-" + coinHistory.getExtractNum() + "   " + type);
                }

                if (showFoot && count - 1 == position) {
                    mFooter.setVisibility(View.VISIBLE);
                } else {
                    mFooter.setVisibility(View.GONE);
                }
            }
        }

        static class EmptyHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.emptyView)
            LinearLayout mEmptyView;

            EmptyHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
