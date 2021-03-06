//package com.sbai.bcnews.view.market;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.CornerPathEffect;
//import android.graphics.DashPathEffect;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.RectF;
//import android.support.v4.content.ContextCompat;
//import android.util.AttributeSet;
//import android.util.SparseArray;
//import android.view.MotionEvent;
//import android.view.ViewGroup;
//
//import com.sbai.bcnews.R;
//import com.sbai.bcnews.model.market.DigitalCashTrendData;
//import com.zcmrr.chart.ChartSettings;
//import com.zcmrr.chart.ChartView;
//import com.zcmrr.chart.TrendView;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.util.List;
//
///**
// * Created by ${wangJie} on 2018/2/6.
// */
//
//public class DigitalCashTrendChart extends ChartView {
//
//    private static final String TAG = "DigitalCashTrendChart";
//
//    private static final float VOLUME_WIDTH_DP = 1.0f; //dp
//
//    private List<DigitalCashTrendData> mDataList;
//    private DigitalCashTrendData mUnstableData;
//    private SparseArray<DigitalCashTrendData> mVisibleList;
//    private int mFirstVisibleIndex;
//    private int mLastVisibleIndex;
//
//    private String[] mTimeLine;
//    private float mVolumeWidth;
//    private DigitalCashTrendView.Settings mSettings;
//
//    public DigitalCashTrendChart(Context context) {
//        super(context);
//        init();
//    }
//
//    public DigitalCashTrendChart(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        mTimeLine = DigitalCashTrendView.TIME_LINE_DIGITAL_CASH_ARRAY;
//        mVolumeWidth = dp2Px(VOLUME_WIDTH_DP);
//
//        mVisibleList = new SparseArray<>();
//        mFirstVisibleIndex = Integer.MAX_VALUE;
//        mLastVisibleIndex = Integer.MIN_VALUE;
//    }
//
//    private void setRealTimeFillPaint(Paint paint) {
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.digitalCashRealTimeFullColor));
//        paint.setPathEffect(null);
//    }
//
//    protected void setRealTimeLinePaint(Paint paint) {
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(ContextCompat.getColor(getContext(), R.color.digitalCashRealTimeLineColor));
//        paint.setStrokeWidth(dp2Px(4));
//        paint.setPathEffect(new CornerPathEffect(20));
//    }
//
//    private void setCandleBodyPaint(Paint paint, String color) {
//        paint.setColor(Color.parseColor(color));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setPathEffect(null);
//    }
//
//    protected void setDashLinePaint(Paint paint) {
//
//        paint.setColor(Color.parseColor(ChartColor.RED.get()));
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setPathEffect(new DashPathEffect(new float[]{8, 3}, 1));
//    }
//
//    protected void setTouchLineTextPaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartView.ChartColor.WHITE.get()));
//        paint.setTextSize(mBigFontSize);
//        paint.setPathEffect(null);
//    }
//
//    protected void setRectBgPaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartColor.BLACK.get()));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setPathEffect(null);
//    }
//
//    protected void setTouchLinePaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartColor.BLACK.get()));
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setPathEffect(null);
//    }
//
//    protected void setUnstablePricePaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartView.ChartColor.WHITE.get()));
//        paint.setTextSize(mBigFontSize);
//        paint.setPathEffect(null);
//    }
//
//    protected void setUnstablePriceBgPaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartView.ChartColor.BLACK.get()));
//        paint.setStyle(Paint.Style.FILL);
//        paint.setPathEffect(null);
//    }
//
//    private void setTouchCirclePointPaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartColor.BLACK.get()));
//        paint.setPathEffect(null);
//        paint.setStyle(Paint.Style.FILL);
//    }
//
//    private void setTouchAnnulusPointPaint(Paint paint) {
//        paint.setColor(Color.parseColor(ChartColor.WHITE.get()));
//        paint.setPathEffect(null);
//        paint.setStyle(Paint.Style.FILL);
//    }
//
//    public List<DigitalCashTrendData> getDataList() {
//        return mDataList;
//    }
//
//    public void setDataList(List<DigitalCashTrendData> dataList) {
//        mDataList = dataList;
//        redraw();
//    }
//
//    public void setUnstableData(DigitalCashTrendData unstableData) {
//        if (mUnstableData != null && unstableData != null
//                && mUnstableData.isSameData(unstableData)) {
//            return;
//        }
//
//        mUnstableData = unstableData;
//
//        ViewGroup viewGroup = (ViewGroup) getParent();
//        if (viewGroup.getVisibility() != VISIBLE || getVisibility() != VISIBLE) {
//            return;
//        }
//
//        if (enableDrawUnstableData()) {
//            redraw();
//        } else if (mSettings != null && mUnstableData != null) { // When unstable data > top || < bottom, still redraw
//            float[] baseLines = mSettings.getBaseLines();
//            if (mUnstableData.getClosePrice() > baseLines[0]
//                    || mUnstableData.getClosePrice() < baseLines[baseLines.length - 1]) {
//                redraw();
//            }
//        }
//    }
//
//    @Override
//    public void setSettings(ChartSettings settings) {
//        mSettings = (DigitalCashTrendView.Settings) settings;
//        super.setSettings(settings);
//        redraw();
//    }
//
//    @Override
//    public DigitalCashTrendView.Settings getSettings() {
//        return mSettings;
//    }
//
//    public void setVisibleList(SparseArray<DigitalCashTrendData> visibleList) {
//        mVisibleList = visibleList;
//    }
//
//    public SparseArray<DigitalCashTrendData> getVisibleList() {
//        return mVisibleList;
//    }
//
//    public void setPriceAreaWidth(float priceAreaWidth) {
//        mPriceAreaWidth = priceAreaWidth;
//    }
//
//    public float getPriceAreaWidth() {
//        return mPriceAreaWidth;
//    }
//
//    public int getFirstVisibleIndex() {
//        return mFirstVisibleIndex;
//    }
//
//    public int getLastVisibleIndex() {
//        return mLastVisibleIndex;
//    }
//
//    @Override
//    protected void calculateBaseLines(float[] baselines) {
//        float preClosePrice = mSettings.getPreClosePrice();
//        float max = Float.MIN_VALUE;
//        float min = Float.MAX_VALUE;
//
//        if (mDataList != null) {
//            for (DigitalCashTrendData stockTrendData : mDataList) {
//                if (max < stockTrendData.getClosePrice()) {
//                    max = stockTrendData.getClosePrice();
//                }
//                if (min > stockTrendData.getClosePrice()) {
//                    min = stockTrendData.getClosePrice();
//                }
//            }
//        }
//
//        if (mUnstableData != null) {
//            if (max < mUnstableData.getClosePrice()) {
//                max = mUnstableData.getClosePrice();
//            }
//            if (min > mUnstableData.getClosePrice()) {
//                min = mUnstableData.getClosePrice();
//            }
//        }
//
//        if (max == Float.MIN_VALUE || min == Float.MAX_VALUE) {
//            max = preClosePrice * (1 + 0.1f);
//            min = preClosePrice * (1 - 0.1f);
//        } else if (max == min) {
//            // limit up/down when market open
//            if (preClosePrice != 0) {
//                if (max > preClosePrice) {
//                    min = preClosePrice - Math.abs(preClosePrice - max);
//                } else {
//                    max = preClosePrice + Math.abs(preClosePrice - min);
//                }
//            }
//        } else {
//            if (preClosePrice != 0) {
//                if (Math.abs(preClosePrice - max) > Math.abs(preClosePrice - min)) {
//                    min = preClosePrice - Math.abs(preClosePrice - max);
//                } else {
//                    max = preClosePrice + Math.abs(preClosePrice - min);
//                }
//            }
//        }
//
//        float priceRange =
//                BigDecimal.valueOf(max).subtract(new BigDecimal(min))
//                        .divide(new BigDecimal(baselines.length - 1), mSettings.getNumberScale() + 2, RoundingMode.HALF_EVEN)
//                        .floatValue();
//
//        baselines[0] = max;
//        baselines[baselines.length - 1] = min;
//        for (int i = baselines.length - 2; i > 0; i--) {
//            baselines[i] = baselines[i + 1] + priceRange;
//        }
//    }
//
//    @Override
//    protected void calculateIndexesBaseLines(long[] indexesBaseLines) {
//        if (mDataList != null && mDataList.size() > 0) {
//            long maxVolume = mDataList.get(0).getNowVolume();
//            for (int i = 1; i < mDataList.size(); i++) {
//                if (maxVolume < mDataList.get(i).getNowVolume()) {
//                    maxVolume = mDataList.get(i).getNowVolume();
//                }
//            }
//            long volumeRange = maxVolume / (indexesBaseLines.length - 1);
//            indexesBaseLines[0] = maxVolume;
//            indexesBaseLines[indexesBaseLines.length - 1] = 0;
//            for (int i = indexesBaseLines.length - 2; i > 0; i--) {
//                indexesBaseLines[i] = indexesBaseLines[i + 1] + volumeRange;
//            }
//        }
//    }
//
//    @Override
//    protected void drawBaseLines(boolean indexesEnable,
//                                 float[] baselines, int left, int top, int width, int height,
//                                 long[] indexesBaseLines, int left2, int top2, int width2, int height2,
//                                 Canvas canvas) {
//        if (baselines == null || baselines.length < 2) return;
//
//        float verticalInterval = height * 1.0f / (baselines.length - 1);
//        mPriceAreaWidth = calculatePriceWidth(baselines[0]);
//        float topY = top;
//        for (int i = 0; i < baselines.length; i++) {
//            Path path = getPath();
//            path.moveTo(left, topY);
//            path.lineTo(left + (i == 0 ? width + getPaddingRight() : width), topY);
//            setBaseLinePaint(sPaint);
//            canvas.drawPath(path, sPaint);
//
//            if (i != 0) {
//                setDefaultTextPaint(sPaint);
//                String baseLineValue = formatNumber(baselines[i]);
//                float textWidth = sPaint.measureText(baseLineValue);
//                float x = left + width - mPriceAreaWidth + (mPriceAreaWidth - textWidth) / 2;
//                float y = topY - mTextMargin - mFontHeight / 2 + mOffset4CenterText;
//                canvas.drawText(baseLineValue, x, y, sPaint);
//            }
//
//            topY += verticalInterval;
//        }
//
////        float preClosePrice = mSettings.getPreClosePrice();
////        if (preClosePrice < baselines[0] && preClosePrice > baselines[baselines.length - 1]) {
////            setDashLinePaint(sPaint);
////            topY = getChartY(preClosePrice);
////            Path path = getPath();
////            path.moveTo(left, topY);
////            path.lineTo(left + width, topY);
////            canvas.drawPath(path, sPaint);
////
////            setDefaultTextPaint(sPaint);
////            String preClosePriceStr = formatNumber(preClosePrice);
////            float x = left + mTextMargin;
////            float y = topY - mTextMargin - mFontHeight / 2 + mOffset4CenterText;
////            if (topY - mTextMargin - mFontHeight <= top) { // preClosePriceText beyond top
////                y = topY + mTextMargin + mFontHeight / 2 + mOffset4CenterText;
////            }
////            canvas.drawText(preClosePriceStr, x, y, sPaint);
////        }
//    }
//
//
//    @Override
//    protected void drawRealTimeData(boolean indexesEnable,
//                                    int left, int top, int width, int height,
//                                    int left2, int top2, int width2, int height2, Canvas canvas) {
//        float firstChartX = 0;
//        if (mDataList != null && mDataList.size() > 0) {
//            int size = mDataList.size();
//            Path path = getPath();
//            float chartX = 0;
//            float chartY = 0;
//            for (int i = 0; i < size; i++) {
//                chartX = getChartX(mDataList.get(i));
//                chartY = getChartY(mDataList.get(i).getClosePrice());
//                if (path.isEmpty()) {
//                    firstChartX = chartX;
//                    path.moveTo(chartX, chartY);
//                } else {
//                    path.lineTo(chartX, chartY);
//                }
//            }
//
//            setRealTimeLinePaint(sPaint);
//            canvas.drawPath(path, sPaint);
//
//            if (indexesEnable) {
//                for (int i = 0; i < size; i++) {
//                    ChartColor color = ChartColor.RED;
//                    chartX = getChartX(mDataList.get(i));
//                    if (i > 0 && mDataList.get(i).getClosePrice() < mDataList.get(i - 1).getClosePrice()) {
//                        color = ChartColor.GREEN;
//                    }
//                    setCandleBodyPaint(sPaint, color.get());
//                    RectF rectf = getRectF();
//                    rectf.left = chartX - mVolumeWidth / 2;
//                    rectf.top = getIndexesChartY(mDataList.get(i).getNowVolume());
//                    rectf.right = chartX + mVolumeWidth / 2;
//                    rectf.bottom = getIndexesChartY(0);
//                    canvas.drawRect(rectf, sPaint);
//                }
//            }
//
//            path.lineTo(chartX, top + height);
//            path.lineTo(firstChartX, top + height);
//            path.close();
//            setRealTimeFillPaint(sPaint);
//            canvas.drawPath(path, sPaint);
//        }
//    }
//
//
//    @Override
//    protected void drawUnstableData(boolean indexesEnable,
//                                    int left, int top, int width, int topPartHeight,
//                                    int left2, int top2, int width1, int bottomPartHeight,
//                                    Canvas canvas) {
//        if (mUnstableData != null) {
//            // last point open to unstable point
//            Path path = getPath();
//            int unstableIndex = mDataList != null ? mDataList.size() : 0;
//            float chartX = getChartX(mUnstableData);
//            float chartY = getChartY(mUnstableData.getClosePrice());
//
//            if (mDataList != null && mDataList.size() > 0) {
//                DigitalCashTrendData lastData = mDataList.get(mDataList.size() - 1);
//                path.moveTo(getChartX(lastData), getChartY(lastData.getClosePrice()));
//                path.lineTo(chartX, chartY);
//                setRealTimeLinePaint(sPaint);
//                canvas.drawPath(path, sPaint);
//            }
//
//            // dash line
//            path = getPath();
//            path.moveTo(chartX, chartY);
//            path.lineTo(left + width - mPriceAreaWidth, chartY);
//            setDashLinePaint(sPaint);
//            canvas.drawPath(path, sPaint);
//
//            // unstable price
//            setUnstablePricePaint(sPaint);
//            String unstablePrice = formatNumber(mUnstableData.getClosePrice());
//            float priceWidth = sPaint.measureText(unstablePrice);
//            float priceMargin = (mPriceAreaWidth - priceWidth) / 2;
//            float priceX = left + width - priceMargin - priceWidth;
//            RectF blueRect = getBigFontBgRectF(priceX, chartY + mOffset4CenterBigText, priceWidth);
//            //// the center of rect is connected to dashLine
//            //// add offset and let the bottom of rect open to dashLine
//            float rectHeight = blueRect.height();
//            blueRect.top -= rectHeight / 2;
//            blueRect.bottom -= rectHeight / 2;
//            setUnstablePriceBgPaint(sPaint);
//            canvas.drawRoundRect(blueRect, 2, 2, sPaint);
//            float priceY = chartY - rectHeight / 2 + mOffset4CenterBigText;
//            setUnstablePricePaint(sPaint);
//            canvas.drawText(unstablePrice, priceX, priceY, sPaint);
//        }
//    }
//
//    @Override
//    protected int getIndexOfXAxis(float chartX) {
//        float width = getWidth() - getPaddingLeft() - getPaddingRight() - mPriceAreaWidth;
//        chartX = chartX - getPaddingLeft();
//        return (int) (chartX * mSettings.getXAxis() / width);
//    }
//
//    private int getIndexFromDate(String hhmm) {
//        String[] timeLines = mSettings.getOpenMarketTimes();
//        int size = timeLines.length;
//        size = (size % 2 == 0 ? size : size - 1);
//
//        int index = 0;
//        for (int i = 0; i < size; i += 2) {
//            if (TrendView.Util.isBetweenTimesClose(timeLines[i], timeLines[i + 1], hhmm)) {
//                index = TrendView.Util.getDiffMinutes(timeLines[i], hhmm);
//                for (int j = 0; j < i; j += 2) {
//                    // the total points of this period
//                    index += TrendView.Util.getDiffMinutes(timeLines[j], timeLines[j + 1]);
//                }
//            }
//        }
//        return index;
//    }
//
//    protected float getChartX(DigitalCashTrendData data) {
//        int indexOfXAxis = getIndexFromDate(data.getHHmm());
//        updateFirstLastVisibleIndex(indexOfXAxis);
//        mVisibleList.put(indexOfXAxis, data);
//        return getChartX(indexOfXAxis);
//    }
//
//    @Override
//    protected float getChartX(int index) {
//        float width = getWidth() - getPaddingLeft() - getPaddingRight() - mPriceAreaWidth;
//        float chartX = getPaddingLeft() + index * width * 1.0f / mSettings.getXAxis();
//        return chartX;
//    }
//
//    @Override
//    protected int calculateTouchIndex(MotionEvent e) {
//        float touchX = e.getX();
//        int touchIndex = getIndexOfXAxis(touchX);
//        return touchIndex;
//    }
//
//    private void updateFirstLastVisibleIndex(int indexOfXAxis) {
//        mFirstVisibleIndex = Math.min(indexOfXAxis, mFirstVisibleIndex);
//        mLastVisibleIndex = Math.max(indexOfXAxis, mLastVisibleIndex);
//    }
//
//    @Override
//    protected void drawTimeLine(int left, int top, int width, Canvas canvas) {
//
//        setDefaultTextPaint(sPaint);
//
//        int horizontalOffset = (int) dp2Px(32);
//        width = width - horizontalOffset;
//
//        float textWidth = sPaint.measureText(mTimeLine[0]);
//        float textY = top + mTextMargin * 2.5f + mFontHeight / 2 + mOffset4CenterText;
//        float textX = width / 5 - textWidth;
//        canvas.drawText(mTimeLine[0], textX, textY, sPaint);
//
//
//        textWidth = sPaint.measureText(mTimeLine[1]);
//        textX = width / 5 * 2 - textWidth;
//        canvas.drawText(mTimeLine[1], textX, textY, sPaint);
//
//        textWidth = sPaint.measureText(mTimeLine[2]);
//        textX = width / 5 * 3 - textWidth;
//        canvas.drawText(mTimeLine[2], textX, textY, sPaint);
//
//        textWidth = sPaint.measureText(mTimeLine[3]);
//        textX = width / 5 * 4 - textWidth;
//        canvas.drawText(mTimeLine[3], textX, textY, sPaint);
//
//        textWidth = sPaint.measureText(mTimeLine[4]);
//        textX = width - textWidth;
//        canvas.drawText(mTimeLine[4], textX, textY, sPaint);
//    }
//
//    @Override
//    protected void drawTouchLines(boolean indexesEnable, int touchIndex,
//                                  int left, int top, int width, int height,
//                                  int left2, int top2, int width2, int height2,
//                                  Canvas canvas) {
//        if (hasThisTouchIndex(touchIndex)) {
//            DigitalCashTrendData data = mVisibleList.get(touchIndex);
//            float touchX = getChartX(touchIndex);
//            float touchY = getChartY(data.getClosePrice());
//            float touchY2 = getIndexesChartY(data.getNowVolume());
//
//            // draw cross line: vertical line and horizontal line
//            setTouchLinePaint(sPaint);
//            Path path = getPath();
//            path.moveTo(touchX, top);
//            path.lineTo(touchX, top + height);
//            canvas.drawPath(path, sPaint);
//            path = getPath();
//            path.moveTo(left, touchY);
//            path.lineTo(left + width - mPriceAreaWidth, touchY);
//            canvas.drawPath(path, sPaint);
//
//            // draw date open to vertical line
//            String date = data.getHHmm();
//            setTouchLineTextPaint(sPaint);
//            float dateWidth = sPaint.measureText(date);
//            RectF redRect = getBigFontBgRectF(0, 0, dateWidth);
//            float rectHeight = redRect.height();
//            float rectWidth = redRect.width();
//            redRect.left = touchX - rectWidth / 2;
//            redRect.top = top + height;
//            if (redRect.left < left) { // rect will touch left border
//                redRect.left = left;
//            }
//            if (redRect.left + rectWidth > left + width) { // rect will touch right border
//                redRect.left = left + width - rectWidth;
//            }
//            redRect.right = redRect.left + rectWidth;
//            redRect.bottom = redRect.top + rectHeight;
//            setRectBgPaint(sPaint);
//            canvas.drawRoundRect(redRect, 2, 2, sPaint);
//            float dateX = redRect.left + (rectWidth - dateWidth) / 2;
//            float dateY = top + height + rectHeight / 2 + mOffset4CenterBigText;
//            setTouchLineTextPaint(sPaint);
//            canvas.drawText(date, dateX, dateY, sPaint);
//
//            // draw price open to horizontal line
//            String price = formatNumber(data.getClosePrice());
//            setTouchLineTextPaint(sPaint);
//            float priceWidth = sPaint.measureText(price);
//            float priceMargin = (mPriceAreaWidth - priceWidth) / 2;
////            float priceX = left + width - priceMargin - priceWidth;
//            float priceX = left;
//            redRect = getBigFontBgRectF(priceX, touchY + mOffset4CenterBigText + mBigFontHeight / 2, priceWidth);
//            rectHeight = redRect.height();
//            redRect.top -= rectHeight / 2;
//            if (redRect.top < top) {
//                redRect.top = top;
//            }
//            redRect.bottom = redRect.top + rectHeight;
//            setRectBgPaint(sPaint);
//            canvas.drawRoundRect(redRect, 2, 2, sPaint);
//            float priceY = redRect.top + rectHeight / 2 + mOffset4CenterBigText;
//            setTouchLineTextPaint(sPaint);
//            canvas.drawText(price, priceX, priceY, sPaint);
//
//
//            setTouchAnnulusPointPaint(sPaint);
//            canvas.drawCircle(touchX, touchY, 7, sPaint);
//            setTouchCirclePointPaint(sPaint);
//            canvas.drawCircle(touchX, touchY, 5, sPaint);
//
//            if (indexesEnable) {
//                // draw cross line: vertical line and horizontal line
//                setTouchLinePaint(sPaint);
//                path = getPath();
//                path.moveTo(touchX, top2);
//                path.lineTo(touchX, top2 + height2);
//                canvas.drawPath(path, sPaint);
//                path = getPath();
//                path.moveTo(left2, touchY2);
//                path.lineTo(left2 + width2 - mPriceAreaWidth, touchY2);
//                canvas.drawPath(path, sPaint);
//
//                // draw volume open to horizontal line
//                String volume = String.valueOf(data.getNowVolume());
//                setTouchLineTextPaint(sPaint);
//                float volumeWidth = sPaint.measureText(volume);
//                redRect = getBigFontBgRectF(0, 0, volumeWidth);
//                rectHeight = redRect.height();
//                rectWidth = redRect.width();
//                float volumeMargin = (rectWidth - volumeWidth) / 2;
//                float volumeX = left2 + width2 - volumeMargin - volumeWidth;
//                redRect.top = touchY2 - rectHeight;
//                redRect.left = left2 + width2 - rectWidth;
//                if (redRect.top < top2) {
//                    redRect.top = top2;
//                }
//                redRect.bottom = redRect.top + rectHeight;
//                redRect.right = redRect.left + rectWidth;
//                setRectBgPaint(sPaint);
//                canvas.drawRoundRect(redRect, 2, 2, sPaint);
//                float volumeY = redRect.top + rectHeight / 2 + mOffset4CenterBigText;
//                setTouchLineTextPaint(sPaint);
//                canvas.drawText(volume, volumeX, volumeY, sPaint);
//            }
//        }
//    }
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        setBaseLinePaint(sPaint);
//        canvas.drawLine(getWidth(), getTop(), getWidth(), getHeight(), sPaint);
//    }
//}
