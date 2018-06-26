package com.sbai.bcnews.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.TypefaceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 */
public class RandomLocationLayout extends LinearLayout {

    private static final String TAG = "RandomLocationLayout";

    private static final int MINING_VIEW_INDEX = 1;
    private static final int EMPTY_VIEW_INDEX = 0;

    private static final int MINTING_VIEW_DURATION = 2000;

    private final int maxBrightCount = 10;

    private List<QKC> mQksList;

    private ArrayList<Integer> randomIndex;


    private OnCoinClickListener mOnCoinClickListener;


    private int receiveCount;
    private int mDefaultRandomMargin = 10;
    private int mViewWidth;
    private TextView mEmptyTextView;

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
        resetView();
    }

    private void resetView() {
        int childCount = getChildCount();
        ViewGroup parent = (ViewGroup) getChildAt(MINING_VIEW_INDEX);
        for (int i = 0; i < parent.getChildCount(); i++) {
            TextView qkcCoin = (TextView) parent.getChildAt(i);
            qkcCoin.setText(null);
            qkcCoin.setEnabled(true);
            qkcCoin.setVisibility(INVISIBLE);
            TypefaceUtils.setMiningTextTypeface(qkcCoin);
//            setRandomTranslate(qkcCoin, i);
        }
    }

    private void init() {
        randomIndex = new ArrayList<>();
        mViewWidth = (int) (Display.getScreenWidth() - Display.dp2Px(50, getResources()) * 2);
        mDefaultRandomMargin = (int) (mViewWidth * 0.013);
        createEmptyView();
    }

    private void createEmptyView() {
        if (mEmptyTextView == null) {
            mEmptyTextView = new TextView(getContext());
            mEmptyTextView.setText(R.string.mining);
            mEmptyTextView.setTextColor(Color.WHITE);
            mEmptyTextView.setGravity(Gravity.CENTER);
            mEmptyTextView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_mining, 0, 0);
            mEmptyTextView.setCompoundDrawablePadding((int) Display.dp2Px(4, getResources()));
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            addView(mEmptyTextView, layoutParams);
        }

        startEmptyViewAnimator();
    }

    private void startEmptyViewAnimator() {
        if (mEmptyTextView == null) return;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mEmptyTextView, "translationY", 100, -100);
        translationY.setDuration(MINTING_VIEW_DURATION).setRepeatMode(ValueAnimator.REVERSE);
        translationY.setRepeatCount(ObjectAnimator.INFINITE);
        translationY.start();
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
        ViewGroup parent = (ViewGroup) getChildAt(MINING_VIEW_INDEX);
//        while (index < stopSize) {
//            Integer e = mRandom.nextInt(parent.getChildCount());
//            if (!randomIndex.contains(e)) {
//                randomIndex.add(e);
//                index++;
//            }
//        }

        for (int i = 0; i < stopSize; i++) {
            if (stopSize < 3) {
                randomIndex.add(i + 3);
            } else if (stopSize < 5) {
                randomIndex.add(i + 2);
            } else {
                randomIndex.add(i);
            }
        }
        if (mQksList.isEmpty()) {
            showEmptyView();
        }
        setCoinStyle(stopSize);
    }

    public void setQksList(List<QKC> qksList) {
        if (qksList != null && !qksList.isEmpty()) {
            showMiningView();
            mQksList = qksList;
            randomDataIndex();
        } else {
            showEmptyView();
        }
    }

    private void showMiningView() {
        mEmptyTextView.setVisibility(GONE);
        mEmptyTextView.clearAnimation();
        getChildAt(MINING_VIEW_INDEX).setVisibility(VISIBLE);
    }

    private void showEmptyView() {
        getChildAt(MINING_VIEW_INDEX).setVisibility(GONE);
        mEmptyTextView.setVisibility(VISIBLE);
        startEmptyViewAnimator();
    }

    private void setCoinStyle(int stopSize) {
        removeView(getChildAt(MINING_VIEW_INDEX));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_random_location_qkc, this, false);
        addView(view, MINING_VIEW_INDEX);
        resetView();
        ViewGroup parent = (ViewGroup) getChildAt(MINING_VIEW_INDEX);
        final int listSize = randomIndex.size();
        for (int i = 0; i < listSize; i++) {
            final TextView qkcCoin = (TextView) parent.getChildAt(randomIndex.get(i));
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
            int time = 1200 + i * 30;
//            if (i == 2) {
            ObjectAnimator translationY = ObjectAnimator.ofFloat(qkcCoin, "translationY", -10, 10);
            translationY.setDuration(time).setRepeatMode(ValueAnimator.REVERSE);
            translationY.setRepeatCount(ObjectAnimator.INFINITE);
            translationY.start();

//            }
        }
    }

    /**
     * 领取成功后调用该方法移除对应的按钮
     *
     * @param v   被点击的按钮
     * @param qks QKS数据
     */
    public void removeCoin(final View v, final QKC qks) {

        final AnimationDrawable animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.animation_mining_cancel);
        ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(null, animationDrawable, null, null);
        ((TextView) v).setText("");

        animationDrawable.start();

        int duration = 0;
        for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
            duration += animationDrawable.getDuration(i);
        }
        v.setEnabled(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationDrawable.stop();
                v.setEnabled(true);
                mQksList.remove(qks);
                ((TextView) v).setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_qkc, 0, 0);
                (v).setVisibility(INVISIBLE);
                receiveCount++;
                if (receiveCount >= randomIndex.size() || receiveCount == maxBrightCount) {
                    randomDataIndex();
                }
            }
        }, duration);

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

    private int dp2px(int values) {
        return (int) Display.dp2Px(values, getResources());
    }

    public void setOnCoinClickListener(OnCoinClickListener onCoinClickListener) {
        mOnCoinClickListener = onCoinClickListener;
    }

}
