package com.sbai.bcnews.view.market;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

/**
 * Created by ${wangJie} on 2018/2/6.
 */

public class DigitalCashTouchView extends DigitalCashTrendChart {

    private DigitalCashTrendChart mDigitalCashTrendChart;

    public DigitalCashTouchView(Context context, DigitalCashTrendChart digitalCashTrendChart) {
        super(context);
        mDigitalCashTrendChart = digitalCashTrendChart;
        setVisibleList(digitalCashTrendChart.getVisibleList());
    }

    @Override
    protected void calculateBaseLines(float[] baselines) {
    }

    @Override
    protected void calculateIndexesBaseLines(long[] indexesBaseLines) {
    }

    @Override
    protected void drawBaseLines(boolean indexesEnable,
                                 float[] baselines, int left, int top, int width, int height,
                                 long[] indexesBaseLines, int left2, int top2, int width2, int height2,
                                 Canvas canvas) {
        setPriceAreaWidth(mDigitalCashTrendChart.getPriceAreaWidth());
    }

    @Override
    protected void drawRealTimeData(boolean indexesEnable,
                                    int left, int top, int width, int height,
                                    int left2, int top2, int width2, int height2,
                                    Canvas canvas) {
    }

    @Override
    protected void drawUnstableData(boolean indexesEnable,
                                    int left, int top, int width, int topPartHeight,
                                    int left2, int top2, int width1, int bottomPartHeight,
                                    Canvas canvas) {
    }

    @Override
    protected void drawTimeLine(int left, int top, int width, Canvas canvas) {
    }

    @Override
    protected int calculateTouchIndex(MotionEvent e) {
        int touchIndex = super.calculateTouchIndex(e);
        if (getVisibleList() != null && getVisibleList().size() > 0) {
            touchIndex = Math.max(touchIndex, mDigitalCashTrendChart.getFirstVisibleIndex());
            touchIndex = Math.min(touchIndex, mDigitalCashTrendChart.getLastVisibleIndex());
        }
        return touchIndex;
    }

    @Override
    protected boolean hasThisTouchIndex(int touchIndex) {
        if (touchIndex != -1 && getVisibleList() != null && getVisibleList().get(touchIndex) != null) {
            return true;
        }
        return super.hasThisTouchIndex(touchIndex);
    }

    @Override
    protected boolean enableDrawTouchLines() {
        return true;
    }
}
