package com.sbai.bcnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.FinanceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 */
public class RandomLocationLayout extends LinearLayout {

    private static final String TAG = "RandomLocationLayout";

    private final int maxBrightCount = 5;

    private List<QKC> mQksList;

    private ArrayList<Integer> randomIndex;

    private Random mRandom;

    private OnCoinClickListener mOnCoinClickListener;

    private int mViewMaxWidth;

    private int receiveCount;

    public RandomLocationLayout(Context context) {
        this(context, null);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_random_location_qkc, this, false);
        ButterKnife.bind(view);
        addView(view);
        mRandom = new Random();
        resetView();
    }

    private void resetView() {
        ViewGroup parent = (ViewGroup) getChildAt(0);
        for (int i = 0; i < parent.getChildCount(); i++) {
            TextView qkcCoin = (TextView) parent.getChildAt(i);
            qkcCoin.setText(null);
            qkcCoin.setEnabled(false);
            qkcCoin.setVisibility(INVISIBLE);
            setRandomTranslate(qkcCoin);
        }
    }

    private void init() {

        randomIndex = new ArrayList<>();

        post(new Runnable() {
            @Override
            public void run() {
                mViewMaxWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3;
            }
        });
    }

    /**
     * 1、将10个子view进行随机排序
     * 2、取出前几个高亮显示（1-5个）
     * 3、领取一个之后 移除数据 变灰，多屏情况下领取10个在次调用此方法重新设置ui和数据
     */
    private void randomDataIndex() {
        receiveCount = 0;
        randomIndex.clear();
        int index = 0;
        int stopSize = 10;
        if (stopSize > mQksList.size()) {
            stopSize = mQksList.size();
        }
        ViewGroup parent = (ViewGroup) getChildAt(0);
        while (index < stopSize) {
            Integer e = mRandom.nextInt(parent.getChildCount());
            if (!randomIndex.contains(e)) {
                randomIndex.add(e);
                index++;
            }
        }

        setCoinStyle(stopSize);
    }

    public void setQksList(List<QKC> qksList) {
        mQksList = qksList;
        randomDataIndex();
    }

    private void setCoinStyle(int stopSize) {
        removeAllViews();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_random_location_qkc, this, false);
        addView(view);
        //0-5   6-9    10-~
        resetView();
        ViewGroup parent = (ViewGroup) getChildAt(0);
        final int listSize = randomIndex.size();
        for (int i = 0; i < listSize; i++) {
            TextView qkcCoin = (TextView) parent.getChildAt(randomIndex.get(i));
            final QKC qks = mQksList.get(i);
            qkcCoin.setVisibility(VISIBLE);
            qkcCoin.setText(getContext().getString(R.string.plus_qks, FinanceUtil.formatWithScaleRemoveTailZero(qks.getIntegral())));
            qkcCoin.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (mOnCoinClickListener != null) {
                        mOnCoinClickListener.onCoinClick(v, qks);
                    }

                }
            });
            if (i < Math.min(maxBrightCount, stopSize)) {
                qkcCoin.setEnabled(true);
            } else {
                qkcCoin.setEnabled(false);
            }
        }
    }

    /**
     * 领取成功后调用该方法移除对应的按钮
     *
     * @param v   被点击的按钮
     * @param qks QKS数据
     */
    public void removeCoin(View v, QKC qks) {
        mQksList.remove(qks);
        ((TextView) v).setText(null);
        v.setVisibility(INVISIBLE);
        receiveCount++;

        resetViewEnable();

    }

    private void resetViewEnable() {
        if (receiveCount == maxBrightCount && receiveCount < randomIndex.size()) {
            ViewGroup parent = (ViewGroup) getChildAt(0);
            final int listSize = randomIndex.size();

            for (int i = 0; i < listSize; i++) {
                TextView childAt = (TextView) parent.getChildAt(i);
                if (!childAt.isEnabled()) {
                    childAt.setEnabled(true);
                }
            }
        } else if (receiveCount >= randomIndex.size()) {
            randomDataIndex();
        }
    }

    private void setRandomTranslate(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin += getMarginLeft();
        layoutParams.topMargin += getMarginTop();
        layoutParams.bottomMargin += getMarginBottom();
        view.setLayoutParams(layoutParams);
    }


    private int getMarginTop() {
        int i = (int) Display.dp2Px(mRandom.nextInt(10), getResources());
        return i;
    }

    private int getMarginLeft() {
        int i = (int) Display.dp2Px(mRandom.nextInt(10), getResources());
        return i;
    }

    private int getMarginBottom() {
        int i = (int) Display.dp2Px(mRandom.nextInt(10), getResources());
        return i;
    }

    public interface OnCoinClickListener {
        /**
         * 领取QKC回调
         *
         * @param v   当前领取的view
         * @param qks QKC
         */
        void onCoinClick(View v, QKC qks);
    }

    public void setOnCoinClickListener(OnCoinClickListener onCoinClickListener) {
        mOnCoinClickListener = onCoinClickListener;
    }
}
