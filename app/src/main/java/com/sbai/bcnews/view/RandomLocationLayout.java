package com.sbai.bcnews.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.mine.QKS;
import com.sbai.bcnews.utils.Display;

import java.util.HashSet;
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

    private List<QKS> mQksList;

    private Random mRandom;


    private int mViewMaxWidth;
    private HashSet<Integer> mDataIndexSet;

    public RandomLocationLayout(Context context) {
        this(context, null);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomLocationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_random_location_qkc, null);
        ButterKnife.bind(view);
        addView(view);
        mRandom = new Random();
    }


    private void init() {

        mDataIndexSet = new HashSet<>();

        post(new Runnable() {
            @Override
            public void run() {
                mViewMaxWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 3;
            }
        });
    }

    private void randomDataIndex() {
        int setSize = mDataIndexSet.size();
        int index = -1;
        int stopSize = 10;
        if (stopSize > mQksList.size() - setSize) {
            stopSize = mQksList.size() - setSize;
        }

        while (index > stopSize) {
            boolean add = mDataIndexSet.add(mRandom.nextInt(mQksList.size()));
            if (add) {
                index++;
            }
        }
        for (Integer result : mDataIndexSet) {
            Log.d(TAG, "randomDataIndex: " + result);
        }
    }


    public void setQksList(List<QKS> qksList) {
        mQksList = qksList;
        randomDataIndex();
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
