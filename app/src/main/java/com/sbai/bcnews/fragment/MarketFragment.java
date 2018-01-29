package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.view.ListRecycleViewItemDecoration;

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
public class MarketFragment extends BaseFragment {

    @BindView(R.id.titleBar)
    TextView mTitleBar;
    @BindView(R.id.recycleView)
    RecyclerView mRecycleView;
    Unbinder unbinder;

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
        Apic.requestMarkListData(MarketData.DEFAULT_MARKET_BOURSE_CODE)
                .tag(TAG)
                .fire();

        String a = "[ { \"askPrice\":0.1752, \"bidPrice\":0.165, \"code\":\"data_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.1907, \"lastPrice\":0.165, \"lastVolume\":518044.2545, \"lowestPrice\":0.159, \"name\":\"data\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.13157894736842, \"upTime\":1516950414422, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":91431.3021 }, { \"askPrice\":7.31E-4, \"bidPrice\":7.07E-4, \"code\":\"link_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":7.4E-4, \"lastPrice\":7.11E-4, \"lastVolume\":3936.99, \"lowestPrice\":6.5E-4, \"name\":\"link\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.046916947208941, \"upTime\":1516950414417, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":2.861 }, { \"askPrice\":0.1329, \"bidPrice\":0.1308, \"code\":\"cdt_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.1399, \"lastPrice\":0.133, \"lastVolume\":1129579.1174, \"lowestPrice\":0.1268, \"name\":\"cdt\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.078309078317741, \"upTime\":1516950414416, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":149838.0863 }, { \"askPrice\":0.4827, \"bidPrice\":0.4711, \"code\":\"req_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.5, \"lastPrice\":0.4686, \"lastVolume\":19710.2352, \"lowestPrice\":0.4607, \"name\":\"req\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.038769230769231, \"upTime\":1516950414411, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":9418.7628 }, { \"askPrice\":0.2456, \"bidPrice\":0.2435, \"code\":\"mds_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.2621, \"lastPrice\":0.2436, \"lastVolume\":3385077.71, \"lowestPrice\":0.1997, \"name\":\"mds\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.21375186846039, \"upTime\":1516950414437, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":793553.87 }, { \"askPrice\":1.56E-4, \"bidPrice\":1.54E-4, \"code\":\"gtc_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":1.63E-4, \"lastPrice\":1.55E-4, \"lastVolume\":3641034.71, \"lowestPrice\":1.29E-4, \"name\":\"gtc\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.11510791222151, \"upTime\":1516950414432, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":505.779 }, { \"askPrice\":1661.73, \"bidPrice\":1638.55, \"code\":\"bch_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":1700.39, \"lastPrice\":1661.68, \"lastVolume\":256.3216, \"lowestPrice\":1611.49, \"name\":\"bch\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.016798120787896, \"upTime\":1516950414409, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":424374.26 }, { \"askPrice\":0.0044, \"bidPrice\":0.0042, \"code\":\"btf_btc\", \"currencyMoney\":\"btc\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.0044, \"lastPrice\":0.0043, \"lastVolume\":26.83, \"lowestPrice\":0.0042, \"name\":\"btf\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.0, \"upTime\":1516950414435, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":0.1145 }, { \"askPrice\":0.0272, \"bidPrice\":0.027182, \"code\":\"etc_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.028131, \"lastPrice\":0.02718, \"lastVolume\":2472.386, \"lowestPrice\":0.027154, \"name\":\"etc\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.017353579175705, \"upTime\":1516950414449, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":67.611 }, { \"askPrice\":2.2287E-4, \"bidPrice\":2.1595E-4, \"code\":\"iota_btc\", \"currencyMoney\":\"btc\", \"exchangeCode\":\"gate.io\", \"highestPrice\":2.2884E-4, \"lastPrice\":2.2036E-4, \"lastVolume\":18096.54, \"lowestPrice\":2.15E-4, \"name\":\"iota\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.015678861631795, \"upTime\":1516950414448, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":4.02726 }, { \"askPrice\":0.1028, \"bidPrice\":0.0983, \"code\":\"fun_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.1071, \"lastPrice\":0.0981, \"lastVolume\":356348.1906, \"lowestPrice\":0.096, \"name\":\"fun\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.06749049430024699, \"upTime\":1516950414422, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":36027.1834 }, { \"askPrice\":14.598, \"bidPrice\":14.5583, \"code\":\"eos_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":14.7143, \"lastPrice\":14.6, \"lastVolume\":448217.9654, \"lowestPrice\":13.76, \"name\":\"eos\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.0027322404371585003, \"upTime\":1516950414411, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":6403629.9948 }, { \"askPrice\":0.3296, \"bidPrice\":0.3164, \"code\":\"rcn_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.3631, \"lastPrice\":0.3256, \"lastVolume\":191299.2394, \"lowestPrice\":0.3097, \"name\":\"rcn\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.074474135431182, \"upTime\":1516950414421, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":63461.9875 }, { \"askPrice\":0.142, \"bidPrice\":0.1407, \"code\":\"snet_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.1472, \"lastPrice\":0.1407, \"lastVolume\":973449.4101, \"lowestPrice\":0.1281, \"name\":\"snet\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.022270114942529, \"upTime\":1516950414456, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":132004.0605 }, { \"askPrice\":40.72, \"bidPrice\":40.6, \"code\":\"qtum_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":41.91, \"lastPrice\":40.61, \"lastVolume\":20345.5609, \"lowestPrice\":39.36, \"name\":\"qtum\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.024969987995198003, \"upTime\":1516950414410, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":831253.02 }, { \"askPrice\":229.73, \"bidPrice\":228.7404, \"code\":\"dgd_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":238.5, \"lastPrice\":229.6567, \"lastVolume\":33.4218, \"lowestPrice\":187.5454, \"name\":\"dgd\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.20551913761572, \"upTime\":1516950414438, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":7285.9031 }, { \"askPrice\":8.42E-4, \"bidPrice\":8.2E-4, \"code\":\"stx_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":9.09E-4, \"lastPrice\":8.11E-4, \"lastVolume\":11017.074, \"lowestPrice\":7.82E-4, \"name\":\"stx\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.010975869578549, \"upTime\":1516950414417, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":9.184 }, { \"askPrice\":5.06E-4, \"bidPrice\":4.63E-4, \"code\":\"gnx_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":5.27E-4, \"lastPrice\":5.0E-4, \"lastVolume\":49358.009, \"lowestPrice\":4.84E-4, \"name\":\"gnx\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.042145593869732, \"upTime\":1516950414446, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":24.915 }, { \"askPrice\":0.4857, \"bidPrice\":0.4836, \"code\":\"ddd_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.5332, \"lastPrice\":0.4847, \"lastVolume\":1.44628941928E7, \"lowestPrice\":0.4059, \"name\":\"ddd\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.061871447310888, \"upTime\":1516950414426, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":6659878.0248 }, { \"askPrice\":2.82E-5, \"bidPrice\":2.699E-5, \"code\":\"mdt_btc\", \"currencyMoney\":\"btc\", \"exchangeCode\":\"gate.io\", \"highestPrice\":2.943E-5, \"lastPrice\":2.943E-5, \"lastVolume\":13112.787, \"lowestPrice\":2.61E-5, \"name\":\"mdt\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.041032659394234, \"upTime\":1516950414428, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":0.35948 }, { \"askPrice\":6.48E-4, \"bidPrice\":5.98E-4, \"code\":\"ost_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":7.13E-4, \"lastPrice\":6.22E-4, \"lastVolume\":11147.511, \"lowestPrice\":5.28E-4, \"name\":\"ost\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.15828677839851, \"upTime\":1516950414424, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":6.817 }, { \"askPrice\":6.87E-4, \"bidPrice\":6.8E-4, \"code\":\"pst_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":7.3E-4, \"lastPrice\":6.87E-4, \"lastVolume\":211340.16, \"lowestPrice\":6.2E-4, \"name\":\"pst\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":0.0992, \"upTime\":1516950414453, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":143.3895 }, { \"askPrice\":4.87E-5, \"bidPrice\":4.74E-5, \"code\":\"bat_btc\", \"currencyMoney\":\"btc\", \"exchangeCode\":\"gate.io\", \"highestPrice\":5.18E-5, \"lastPrice\":4.78E-5, \"lastVolume\":26329.84, \"lowestPrice\":4.57E-5, \"name\":\"bat\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.0062373735572763996, \"upTime\":1516950414457, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":1.2513 }, { \"askPrice\":2.57E-4, \"bidPrice\":2.52E-4, \"code\":\"bto_eth\", \"currencyMoney\":\"eth\", \"exchangeCode\":\"gate.io\", \"highestPrice\":2.64E-4, \"lastPrice\":2.52E-4, \"lastVolume\":2181739.254, \"lowestPrice\":2.36E-4, \"name\":\"bto\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.0078741420127953, \"upTime\":1516950414426, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":545.47 }, { \"askPrice\":1.3824, \"bidPrice\":1.3601, \"code\":\"xrp_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":1.47, \"lastPrice\":1.36, \"lastVolume\":995008.5987, \"lowestPrice\":1.338, \"name\":\"xrp\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.057519057519057004, \"upTime\":1516950414413, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":1394791.8282 }, { \"askPrice\":0.9928, \"bidPrice\":0.9826, \"code\":\"dai_usdt\", \"currencyMoney\":\"usdt\", \"exchangeCode\":\"gate.io\", \"highestPrice\":0.999, \"lastPrice\":0.9826, \"lastVolume\":4385.308, \"lowestPrice\":0.9706, \"name\":\"dai\", \"rate\":6.35, \"status\":0, \"tradeDay\":\"2018-01-26\", \"upDropSpeed\":-0.019263399540872, \"upTime\":1516950414427, \"upTimeFormat\":\"2018-01-26 15:06:54\", \"volume\":4333.221 } ]";
        List<MarketData> marketData = MarketData.arrayMarketListDataFromData(a);


        MarkListAdapter markListAdapter = new MarkListAdapter(new ArrayList<MarketData>(), getActivity());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        int dividerHeight = (int) Display.dp2Px(1, getResources());
        ListRecycleViewItemDecoration listRecycleViewItemDecoration = new ListRecycleViewItemDecoration(getActivity(), ListRecycleViewItemDecoration.VERTICAL_LIST, dividerHeight, ContextCompat.getColor(getActivity(), R.color.split));
        mRecycleView.addItemDecoration(listRecycleViewItemDecoration);
        mRecycleView.setAdapter(markListAdapter);
        for (MarketData result : marketData) {
//            Log.d(TAG, "onActivityCreated: " + result.toString());
            markListAdapter.add(result);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.ViewHolder> {


        private ArrayList<MarketData> mMarketDataList;
        private Context mContext;

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
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_market_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(mMarketDataList.get(position), position, mContext);
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

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(MarketData marketData, int position, Context context) {

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

                mPriceChangeRatio.setText(FinanceUtil.downToPercentage(marketData.getUpDropSpeed(), 2));
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
                    exchangeVolume = context.getString(R.string.ten_thousand_number," "+ String.valueOf(v));
                } else {
                    exchangeVolume = String.valueOf(volume);
                }
                return context.getString(R.string.market_volume, " "+exchangeVolume);
            }
        }
    }
}
