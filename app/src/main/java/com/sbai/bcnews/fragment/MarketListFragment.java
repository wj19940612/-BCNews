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

import com.sbai.bcnews.BuildConfig;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.market.MarketDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.model.market.Variety;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.MarketDataUtils;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.httplib.ReqError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MarketListFragment extends RecycleViewSwipeLoadFragment {

    private static final String EXCHANGE_CODE = "exchange_code";

    private String mExchangeCode;

    public MarketListFragment() {
    }

    public static MarketListFragment newInstance(String exchangeCode) {
        MarketListFragment fragment = new MarketListFragment();
        Bundle args = new Bundle();
        args.putString(EXCHANGE_CODE, exchangeCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mExchangeCode = getArguments().getString(EXCHANGE_CODE);
        }
    }


    private static final int REFRESH_MARKET_DATE_TIME_INTERVAL = 5000;

    private MarkListAdapter mMarkListAdapter;

    private HashSet<String> mVarietySet;
    private List<Variety> mVarietyList;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mVarietySet = new HashSet<>();
        initRecycleView();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScheduleJob();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean userVisibleHint = getUserVisibleHint();
        if (userVisibleHint) {
            startScheduleJob(REFRESH_MARKET_DATE_TIME_INTERVAL);
        }
        requestMarketVarietyData();
    }

    private void requestMarketVarietyData() {
        Apic.requestMarketVarietyList(0, 1000, mExchangeCode)
                .tag(TAG)
                .callback(new Callback<ListResp<Variety>>() {

                    @Override
                    protected void onRespSuccess(ListResp<Variety> resp) {
                        if (resp != null && resp.getListData() != null) {
                            mVarietyList = resp.getListData();
                            for (Variety variety : resp.getListData()) {
                                mVarietySet.add(variety.getCode());
                            }

                        }
                        requestMarketListData();
                    }
                })
                .fire();
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
        Apic.requestMarkListData(mExchangeCode)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<MarketData>>, List<MarketData>>() {
                    @Override
                    protected void onRespSuccessData(List<MarketData> data) {
                        refreshSuccess();
                        Iterator<MarketData> iterator = data.iterator();

                        if (!mVarietySet.isEmpty()) {
                            while (iterator.hasNext()) {
                                MarketData next = iterator.next();
                                if (!mVarietySet.contains(next.getCode())) {
                                    iterator.remove();
                                }
                            }
                            for (MarketData marketData : data) {
                                for (Variety variety : mVarietyList) {
                                    if (marketData.getCode().equalsIgnoreCase(variety.getCode())) {
                                        marketData.setSeq(variety.getSeq());
                                        marketData.setName(variety.getName());
                                        break;
                                    }
                                }
                            }

                            Collections.sort(data, new Comparator<MarketData>() {
                                @Override
                                public int compare(MarketData o1, MarketData o2) {
                                    return o1.getSeq() - o2.getSeq();
                                }
                            });
                        }

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
                        refreshFailure();
                    }

                })
                .fire();
    }

    private void initRecycleView() {
        mMarkListAdapter = new MarkListAdapter(new ArrayList<MarketData>(), getActivity());
        mSwipeTargetView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTargetView.setAdapter(mMarkListAdapter);
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
        mVarietySet.clear();
        if (mVarietyList != null) {
            mVarietyList.clear();
        }
        requestMarketVarietyData();
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        if (!BuildConfig.DEBUG) {
            requestMarketListData();
        }
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
                        " " + MarketDataUtils.formatVolume(marketData.getLastVolume())));

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
