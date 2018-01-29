package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.ListRecycleViewItemDecoration;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.math.RoundingMode;
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

    private static final int REFRESH_MARKET_DATE_TIME_INTERVAL = 5000;

    @BindView(R.id.titleBar)
    TextView mTitleBar;
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
        startScheduleJob(REFRESH_MARKET_DATE_TIME_INTERVAL);
    }

    private void requestMarketListData() {
        Apic.requestMarkListData(MarketData.DEFAULT_MARKET_BOURSE_CODE)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<MarketData>>, List<MarketData>>() {
                    @Override
                    protected void onRespSuccessData(List<MarketData> data) {
                        mMarkListAdapter.addAll(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    private void initRecycleView() {
        mMarkListAdapter = new MarkListAdapter(new ArrayList<MarketData>(), getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));

        int dividerHeight = (int) Display.dp2Px(1, getResources());
        ListRecycleViewItemDecoration listRecycleViewItemDecoration = new ListRecycleViewItemDecoration(getActivity(),
                ListRecycleViewItemDecoration.VERTICAL_LIST,
                dividerHeight,
                ContextCompat.getColor(getActivity(), R.color.split));
        mSwipeTarget.addItemDecoration(listRecycleViewItemDecoration);
        mSwipeTarget.setAdapter(mMarkListAdapter);

        mMarkListAdapter.setOnItemClickListener(new OnItemClickListener<MarketData>() {
            @Override
            public void onItemClick(MarketData marketData, int position) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
//        mSwipeToLoadLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeToLoadLayout.setLoadingMore(false);
//            }
//        }, 500);

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

    static class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.ViewHolder> {

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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_market_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(mMarketDataList.get(position), position, mContext, mOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mMarketDataList.size();
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

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final MarketData marketData, final int position, Context context, final OnItemClickListener<MarketData> onItemClickListener) {

                mBourseName.setText(marketData.getExchangeCode());
                mMarketName.setText(marketData.getName().toUpperCase());
                mNumberCurrency.setText(marketData.getCurrencyMoney());
                mDealNumber.setText(formatExchangeVolume(context, marketData.getVolume()));

                boolean isRise = marketData.getUpDropSpeed() >= 0;
                mUsPrice.setSelected(isRise);
                mYuanPrice.setSelected(isRise);
                mPriceChangeRatio.setSelected(isRise);

                mUsPrice.setText(formatMoneyCurrencyUsPrice(marketData.getLastPrice(), context));

                mYuanPrice.setText(formatMoneyCurrencyCNPrice(marketData, context));

                mPriceChangeRatio.setText(formatPriceChangeRadio(marketData.getUpDropSpeed(), context));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(marketData, position);
                        }
                    }
                });
            }

            private String formatPriceChangeRadio(double priceChangeRadio, Context context) {
                if (priceChangeRadio < 0) {
                    return FinanceUtil.downToPercentage(priceChangeRadio, 2);
                }
                return context.getString(R.string.up_range, FinanceUtil.downToPercentage(priceChangeRadio, 2));
            }

            private String formatMoneyCurrencyCNPrice(MarketData marketData, Context context) {
                double cnPrice = FinanceUtil.multiply(marketData.getLastPrice(), marketData.getRate()).doubleValue();
                return context.getString(R.string.product_price_cn, FinanceUtil.formatWithScale(cnPrice, 2, RoundingMode.DOWN));
            }

            private String formatMoneyCurrencyUsPrice(double lastPrice, Context context) {
                String price = "";
                if (lastPrice >= 10) {
                    price = FinanceUtil.formatWithScale(lastPrice, 2, RoundingMode.DOWN);
                } else {
                    price = FinanceUtil.formatWithScale(lastPrice, 4, RoundingMode.DOWN);
                }
                return context.getString(R.string.product_price_us, price);
            }

            private String formatExchangeVolume(Context context, double volume) {
                String exchangeVolume = "";
                if (volume >= 10000) {
                    float v = FinanceUtil.divide(volume, 10000, 1, RoundingMode.DOWN).floatValue();
                    exchangeVolume = context.getString(R.string.ten_thousand_number, " " + String.valueOf(v));
                } else {
                    exchangeVolume = String.valueOf(volume);
                }
                return context.getString(R.string.market_volume, " " + exchangeVolume);
            }
        }
    }
}
