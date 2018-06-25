package com.sbai.bcnews.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.TypefaceUtils;

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

    private static final int MINING_VIEW_INDEX = 1;
    private static final int EMPTY_VIEW_INDEX = 0;

    private static final int MINTING_VIEW_DURATION = 2000;

    private final int maxBrightCount = 10;

    private List<QKC> mQksList;

    private ArrayList<Integer> randomIndex;

    private Random mRandom;

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
        mRandom = new Random();
        resetView();
    }

    private void resetView() {
        int childCount = getChildCount();
        Log.d(TAG, "resetView: " + childCount);
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
        while (index < stopSize) {
            Integer e = mRandom.nextInt(parent.getChildCount());
            if (!randomIndex.contains(e)) {
                randomIndex.add(e);
                index++;
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
        ((TextView) v).setText(" ");
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

    private void setRandomTranslate(View view, int i) {
        int marginLeft = 0;
        int marginRight = 0;
        int marginTop = 0;
        int marginBottom = 0;
        if (i == 0) {
            marginLeft = mDefaultRandomMargin * 3;
        } else if (i == 1) {
            marginRight = mDefaultRandomMargin * 3;
        } else if (i == 5) {
            marginLeft = mDefaultRandomMargin * 4;
        } else if (i == 7) {
            marginLeft = mDefaultRandomMargin * 3;
        } else if (i == 10) {
            marginTop = mDefaultRandomMargin * 2;
        }

        marginLeft = getMarginLeft(marginLeft);
        marginTop = getMarginTop(marginTop);
        marginBottom = getMarginBottom(marginBottom);
        marginRight = getMarginRight(marginRight);

        if (i == 0) {
            marginLeft = 40 - mRandom.nextInt(dp2px(mDefaultRandomMargin * 4));
        } else if (i == 4) {
            marginTop = 20 - mRandom.nextInt(dp2px(mDefaultRandomMargin * 3));
            marginRight = mRandom.nextInt(dp2px(mDefaultRandomMargin * 2));
        } else if (i == 8) {
            marginLeft = 10 - mRandom.nextInt(dp2px(mDefaultRandomMargin * 3));
        } else if (i == 9) {
            marginTop = 10 - mRandom.nextInt(dp2px(mDefaultRandomMargin * 4));
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.leftMargin += marginLeft;
        layoutParams.topMargin += marginTop;
        layoutParams.bottomMargin += marginBottom;
        layoutParams.rightMargin += marginRight;
        view.setLayoutParams(layoutParams);
    }


    private int getMarginTop(int randomMarginTop) {
        if (randomMarginTop == 0) randomMarginTop = mDefaultRandomMargin;
        int randomValues = mRandom.nextInt(dp2px(randomMarginTop));
        return randomValues;
    }

    private int getMarginLeft(int randomMarginLeft) {
        if (randomMarginLeft == 0) randomMarginLeft = mDefaultRandomMargin;
        return mRandom.nextInt(dp2px(randomMarginLeft));
    }

    private int getMarginBottom(int randomMarginBottom) {
        if (randomMarginBottom == 0) randomMarginBottom = mDefaultRandomMargin;
        return mRandom.nextInt(dp2px(randomMarginBottom));
    }

    public int getMarginRight(int randomMarginRight) {
        if (randomMarginRight == 0) randomMarginRight = 1;
        return mRandom.nextInt(dp2px(randomMarginRight));
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

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };


}
