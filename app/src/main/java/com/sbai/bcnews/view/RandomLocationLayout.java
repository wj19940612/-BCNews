package com.sbai.bcnews.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.mine.QKS;
import com.sbai.bcnews.utils.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Modified by $nishuideyu$ on 2018/5/18
 * <p>
 * Description:
 * </p>
 */
public class RandomLocationLayout extends LinearLayout {

    private static final String TAG = "RandomLocationLayout";

    private List<QKS> mQksList;

    private ArrayList<TextView> mTextViewList;
    private Random mRandom;

    private LinearLayout mFirstLinearLayout;
    private LinearLayout mSecondLinearLayout;
    private LinearLayout mThirdLinearLayout;

    private int mViewMaxWidth;

    public RandomLocationLayout(Context context) {
        this(context, null);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);

        init();

        mRandom = new Random();
    }


    private void init() {
        LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;

        mFirstLinearLayout = new LinearLayout(getContext());
        mFirstLinearLayout.setOrientation(VERTICAL);

        addView(mFirstLinearLayout, layoutParams);

        mSecondLinearLayout = new LinearLayout(getContext());
//        mSecondLinearLayout.setBackgroundColor(Color.WHITE);
        mSecondLinearLayout.setOrientation(VERTICAL);
        addView(mSecondLinearLayout, layoutParams);


        mThirdLinearLayout = new LinearLayout(getContext());
        mThirdLinearLayout.setOrientation(VERTICAL);
        addView(mThirdLinearLayout, layoutParams);


        mTextViewList = new ArrayList<>();

        post(new Runnable() {
            @Override
            public void run() {
                mViewMaxWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3;
            }
        });
    }

    private TextView createTextView(int position, QKS qks) {
        TextView textView = new TextView(getContext());
        textView.setText("第 " + position + " 个");
        textView.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        textView.setTextColor(Color.GREEN);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public void setQksList(List<QKS> qksList) {
        mQksList = qksList;
        initTextView(qksList);
    }

    private void initTextView(List<QKS> qksList) {
        int viewCount = qksList.size() > 10 ? 10 : qksList.size();
        for (int i = 0; i < viewCount; i++) {
            TextView textView = createTextView(i, qksList.get(i));
            mTextViewList.add(textView);
        }
        addViewInViewGroup();
    }

    private void addViewInViewGroup() {
//        int anInt = mRandom.nextInt(mTextViewList.size() - 1);

        for (int i = 0; i < mTextViewList.size(); i++) {
            TextView textView = mTextViewList.get(i);
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i < 3) {
                layoutParams.setMargins(getMarginLeft(50), getMarginTop(), 0, getMarginBottom());
                mFirstLinearLayout.addView(textView, layoutParams);
            } else if (i < 7) {
                layoutParams.setMargins(getMarginLeft(50), getMarginTop(), 0, getMarginBottom());
                mSecondLinearLayout.addView(textView, layoutParams);
            } else {
                layoutParams.setMargins(getMarginLeft(50), getMarginTop(), 0, getMarginBottom());
                mThirdLinearLayout.addView(textView, layoutParams);
            }
        }

    }

    private int getMarginTop() {
        int i = (int) Display.dp2Px(mRandom.nextInt(100), getResources());
        Log.d(TAG, "getMarginTop: " + i);
        return i;
    }

    private int getMarginLeft(int rang) {
        int i = (int) Display.dp2Px(mRandom.nextInt(rang), getResources());
        Log.d(TAG, "getMarginLeft: " + i);
        return i;
    }

    private int getMarginBottom() {
        int i = (int) Display.dp2Px(mRandom.nextInt(50), getResources());
        Log.d(TAG, "getMarginBottom: " + i);
        return i;
    }

}
