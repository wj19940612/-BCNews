package com.sbai.bcnews.view.market;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.sbai.bcnews.model.market.DigitalCashTrendData;
import com.zcmrr.chart.ChartSettings;
import com.zcmrr.chart.TrendView;

import java.util.Iterator;
import java.util.List;

/**
 * Created by ${wangJie} on 2018/2/6.
 */

public class DigitalCashTrendView extends FrameLayout {

    public static final String[] TIME_LINE_DIGITAL_CASH_ARRAY = new String[]{"09:30", "11:30", "13:30", "15:30", "17:30"};
    public static final String TIME_LINE_DIGITAL_CASH= "09:30;11:30;13:30;15:30;17:30";

    private DigitalCashTrendChart mTrendView;
    private DigitalCashTouchView mTouchView;

    public DigitalCashTrendView(@NonNull Context context) {
        this(context, null);
    }

    public DigitalCashTrendView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigitalCashTrendView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mTrendView = new DigitalCashTrendChart(getContext());
        int padding = (int) mTrendView.dp2Px(14);
        mTrendView.setPadding(padding, 0, padding, 0);

        mTouchView = new DigitalCashTouchView(getContext(), mTrendView);
        mTouchView.setPadding(padding, 0, padding, 0);

        addView(mTrendView);
        addView(mTouchView);
    }

    public void setSettings(ChartSettings settings) {
        mTrendView.setSettings(settings);
        mTouchView.setSettings(settings);
    }

    public Settings getSettings() {
        return mTrendView.getSettings();
    }

    public void setDataList(List<DigitalCashTrendData> dataList) {
        filterInvalidData(dataList);
        mTrendView.setDataList(dataList);
    }

    private void filterInvalidData(List<DigitalCashTrendData> dataList) {
        if (dataList != null) {
            String[] openMarketTimes = getSettings().getOpenMarketTimes();
            Iterator<DigitalCashTrendData> iterator = dataList.iterator();
            while (iterator.hasNext()) {
                DigitalCashTrendData data = iterator.next();
                if (!TrendView.Util.isValidDate(data.getHHmm(), openMarketTimes)) {
                    iterator.remove();
                }
            }
        }
    }

    public static class Settings extends ChartSettings {

        private String mOpenMarketTimes;
        private String mDisplayMarketTimes;
        private boolean mCalculateXAxisFromOpenMarketTime;
        private boolean mXAxisRefresh;

        public void setOpenMarketTimes(String openMarketTimes) {
            mOpenMarketTimes = openMarketTimes;
        }

        public void setDisplayMarketTimes(String displayMarketTimes) {
            mDisplayMarketTimes = displayMarketTimes;
        }

        public String[] getOpenMarketTimes() {
            String[] result = new String[0];
            if (!TextUtils.isEmpty(mOpenMarketTimes)) {
                return mOpenMarketTimes.split(";");
            }
            return result;
        }

        public String[] getDisplayMarketTimes() {
            String[] result = new String[0];
            if (TextUtils.isEmpty(mDisplayMarketTimes)) {
                return mDisplayMarketTimes.split(";");
            }
            return result;
        }

        public void setCalculateXAxisFromOpenMarketTime(boolean value) {
            mCalculateXAxisFromOpenMarketTime = value;
            mXAxisRefresh = true;
        }

        @Override
        public int getXAxis() {
            if (mCalculateXAxisFromOpenMarketTime) {
                if (mXAxisRefresh) {
                    String[] openMarketTime = getOpenMarketTimes();
                    int size = openMarketTime.length % 2 == 0 ?
                            openMarketTime.length : openMarketTime.length - 1;
                    int xAxis = 0;
                    for (int i = 0; i < size; i += 2) {
                        xAxis += TrendView.Util.getDiffMinutes(openMarketTime[i], openMarketTime[i + 1]);
                    }
                    setXAxis(xAxis - 1);
                    mXAxisRefresh = false;

                }
                return super.getXAxis();
            }
            return super.getXAxis();
        }
    }
}
