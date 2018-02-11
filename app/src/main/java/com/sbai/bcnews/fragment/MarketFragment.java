package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.market.MarketDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.MarketDataUtils;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Modified by john on 24/01/2018
 * <p>
 * Description:
 * <p>
 * APIs:
 * 首页行情列表
 */
public class MarketFragment extends RecycleViewSwipeLoadFragment {

    private static final int REFRESH_MARKET_DATE_TIME_INTERVAL = 6000;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    Unbinder unbinder;

    private MarkListAdapter mMarkListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycleView();
        requestMarketListData();
    }

    @Override
    public void onResume() {
        super.onResume();
        startScheduleJob(REFRESH_MARKET_DATE_TIME_INTERVAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScheduleJob();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startScheduleJob(REFRESH_MARKET_DATE_TIME_INTERVAL);
        } else {
            stopScheduleJob();
        }
    }

    private void requestMarketListData() {
        Apic.requestMarkListData(MarketData.DEFAULT_MARKET_BOURSE_CODE)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<MarketData>>, List<MarketData>>() {
                    @Override
                    protected void onRespSuccessData(List<MarketData> data) {
                        mSwipeRefreshHeader.refreshSuccess();
                        mMarkListAdapter.clear();
                        mMarkListAdapter.addAll(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                        mSwipeRefreshHeader.refreshFail();
                    }
                })
                .fire();
    }

    private void initRecycleView() {
        mMarkListAdapter = new MarkListAdapter(new ArrayList<MarketData>(), getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mMarkListAdapter);
        mMarkListAdapter.setOnItemClickListener(new OnItemClickListener<MarketData>() {
            @Override
            public void onItemClick(MarketData marketData, int position) {
                Launcher.with(getActivity(), MarketDetailActivity.class)
                        .putExtra(ExtraKeys.DIGITAL_CURRENCY, marketData)
                        .execute();

                umengEventCount(UmengCountEventId.MARKET_LIST_TAB);
            }
        });

        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mSwipeToLoadLayout.setLoadingMore(false);
                stopFreshOrLoadAnimation();
            }
        }, 500);

    }

    @Override
    public void onRefresh() {
        requestMarketListData();
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        requestMarketListData();
    }

    static class MarkListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM_VIEW_TYPE_FOOTER = 1;

        private ArrayList<MarketData> mMarketDataList;
        private Context mContext;
        private OnItemClickListener<MarketData> mOnItemClickListener;

        public MarkListAdapter(ArrayList<MarketData> marketListDataArray, Context context) {
            mMarketDataList = marketListDataArray;
            mContext = context;
        }

        public void clear() {
            mMarketDataList.clear();
            notifyDataSetChanged();
        }

        public void add(MarketData marketData) {
            mMarketDataList.add(marketData);
            notifyDataSetChanged();
        }

        public void addAll(List<MarketData> data) {
            mMarketDataList.addAll(data);
            notifyDataSetChanged();
        }

        public void setOnItemClickListener(OnItemClickListener<MarketData> onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_VIEW_TYPE_FOOTER:
                    View footerView = LayoutInflater.from(mContext).inflate(R.layout.view_footer_view, parent, false);
                    return new FooterViewHolder(footerView);
                default:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_market_list, parent, false);
                    return new ViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MarkListAdapter.ViewHolder) {
                ((MarkListAdapter.ViewHolder) holder).bindDataWithView(mMarketDataList.get(position), position, mContext, mOnItemClickListener);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (!mMarketDataList.isEmpty() && position == mMarketDataList.size()) {
                return ITEM_VIEW_TYPE_FOOTER;
            }
            return super.getItemViewType(position);
        }


        @Override
        public int getItemCount() {
            return mMarketDataList.isEmpty() ? 0 : mMarketDataList.size() + 1;
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.bourseName)
            TextView mBourseName;
            @BindView(R.id.marketName)
            TextView mMarketName;
            @BindView(R.id.dealNumber)
            TextView mDealNumber;
            @BindView(R.id.numberCurrency)
            TextView mNumberCurrency;
            @BindView(R.id.usPrice)
            TextView mUsPrice;
            @BindView(R.id.yuanPrice)
            TextView mYuanPrice;
            @BindView(R.id.priceChangeRatio)
            TextView mPriceChangeRatio;
            @BindView(R.id.rootView)
            ConstraintLayout mRootView;
            @BindView(R.id.split)
            View mSplit;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final MarketData marketData, final int position, Context context, final OnItemClickListener<MarketData> onItemClickListener) {

                if (position == 0) {
                    mSplit.setVisibility(View.VISIBLE);
                } else {
                    mSplit.setVisibility(View.GONE);
                }

                mBourseName.setText(marketData.getExchangeCode());
                mMarketName.setText(marketData.getName().toUpperCase());
                mNumberCurrency.setText(marketData.getCurrencyMoney());
                mDealNumber.setText(context.getString(R.string.market_volume,
                        MarketDataUtils.formatVolume(marketData.getLastVolume())));

                boolean isRise = marketData.getUpDropSpeed() >= 0;
                mUsPrice.setSelected(isRise);
                mYuanPrice.setSelected(isRise);
                mPriceChangeRatio.setSelected(isRise);

                mUsPrice.setText(MarketDataUtils.formatDollarWithSign(marketData.getLastPrice()));

                mYuanPrice.setText(MarketDataUtils.formatRmbWithSign(
                        FinanceUtil.multiply(marketData.getLastPrice(), marketData.getRate()).doubleValue()));

                mPriceChangeRatio.setText(MarketDataUtils.percentWithPrefix(marketData.getUpDropSpeed()));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(marketData, position);
                        }
                    }
                });
            }
        }

        static class FooterViewHolder extends RecyclerView.ViewHolder {

            public FooterViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
