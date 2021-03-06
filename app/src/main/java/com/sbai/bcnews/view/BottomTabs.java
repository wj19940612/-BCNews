package com.sbai.bcnews.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;

public class BottomTabs extends LinearLayout {

    private static final int KEY_POSITION = -2;

    private int mLength;
    private CharSequence[] mTexts;
    private int[] mIcons;

    private int mTextSize;
    private ColorStateList mTextColor;
    private int mDrawablePadding;
    private int mPaddingTop;
    private int mPaddingBottom;

    private OnTabClickListener mOnTabClickListener;

    private int mHasReadPointTabPosition;

    private RedPointTipTextView mRedPointTipTextView;


    public interface OnTabClickListener {
        void onTabClick(int position);
    }

    public BottomTabs(Context context, AttributeSet attrs) {
        super(context, attrs);

        processAttrs(attrs);

        init();
    }

    private void processAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomTabs);

        mTextSize = typedArray.getDimensionPixelOffset(R.styleable.BottomTabs_textSize, 14);
        mTextColor = typedArray.getColorStateList(R.styleable.BottomTabs_textColor);
        mDrawablePadding = typedArray.getDimensionPixelOffset(R.styleable.BottomTabs_drawablePadding, 0);
        mPaddingTop = typedArray.getDimensionPixelOffset(R.styleable.BottomTabs_drawablePadding, 10);
        mPaddingBottom = typedArray.getDimensionPixelOffset(R.styleable.BottomTabs_drawablePadding, 6);
        mTexts = typedArray.getTextArray(R.styleable.BottomTabs_textArray);
        mHasReadPointTabPosition = typedArray.getInt(R.styleable.BottomTabs_hasReadPointPosition, -1);

        if (isInEditMode()) {
            mIcons = new int[]{R.drawable.tab_news, R.drawable.tab_flash_news, R.drawable.tab_market};
        } else {
            int iconsArrayRes = typedArray.getResourceId(R.styleable.BottomTabs_iconArray, 0);
            if (iconsArrayRes != 0) {
                TypedArray array = getResources().obtainTypedArray(iconsArrayRes);
                mIcons = new int[array.length()];
                for (int i = 0; i < mIcons.length; i++) {
                    mIcons[i] = array.getResourceId(i, 0);
                }
                array.recycle();
            }
        }

        typedArray.recycle();
    }

    private void init() {
        setOrientation(HORIZONTAL);

        if (mIcons != null && mTexts != null) {
            mLength = Math.min(mIcons.length, mTexts.length);
        }

        for (int i = 0; i < mLength; i++) {
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            if (i == mHasReadPointTabPosition) {
                addView(createPointTab(i), params);
            } else {
                addView(createTab(i), params);
            }
        }
        selectTab(0);

        //设置红点位置
        if (mRedPointTipTextView != null)
            post(new Runnable() {
                @Override
                public void run() {
                    int padding = (int) (mRedPointTipTextView.getMeasuredWidth() *0.4);
                    setRedPointTextHorizontalPadding(padding);
                }
            });
    }

    private View createTab(int i) {
        TextView text = new TextView(getContext());
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        text.setTextColor(mTextColor != null ? mTextColor : ColorStateList.valueOf(0));
        text.setText(mTexts[i]);
        text.setPadding(0, mPaddingTop, 0, mPaddingBottom);
        text.setCompoundDrawablePadding(mDrawablePadding);
        text.setCompoundDrawablesWithIntrinsicBounds(0, mIcons[i], 0, 0);
        text.setGravity(Gravity.CENTER);
        text.setTag(KEY_POSITION, i);
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag(KEY_POSITION);
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClick(pos);
                }
            }
        });
        return text;
    }

    public void setRedPointVisibility(int pointVisibility) {
        if (mRedPointTipTextView != null) {
            mRedPointTipTextView.setRedPointVisibility(pointVisibility);
        }
    }

    public void setRedPointTextHorizontalPadding(int padding) {
        if (mRedPointTipTextView != null) {
            mRedPointTipTextView.setPointHorizontalPadding(padding);
        }
    }


    public void setTabVisibility(int index, int visible) {
        View childAt = getChildAt(index);
        childAt.setVisibility(visible);
    }

    private View createPointTab(int i) {
        mRedPointTipTextView = new RedPointTipTextView(getContext());
        mRedPointTipTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mRedPointTipTextView.setTextColor(mTextColor != null ? mTextColor : ColorStateList.valueOf(0));
        mRedPointTipTextView.setText(mTexts[i]);
        mRedPointTipTextView.setPadding(0, mPaddingTop, 0, mPaddingBottom);
        mRedPointTipTextView.setCompoundDrawablePadding(mDrawablePadding);
        mRedPointTipTextView.setCompoundDrawablesWithIntrinsicBounds(0, mIcons[i], 0, 0);
        mRedPointTipTextView.setGravity(Gravity.CENTER);
        mRedPointTipTextView.setTag(KEY_POSITION, i);
        mRedPointTipTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag(KEY_POSITION);
                if (mOnTabClickListener != null) {
                    mOnTabClickListener.onTabClick(pos);
                }
            }
        });
        return mRedPointTipTextView;
    }

    public void selectTab(int index) {
        if (index < 0 || index >= mLength) return;
        unSelectAll();
        getChildAt(index).setSelected(true);
    }

    public void setOnTabClickListener(OnTabClickListener listener) {
        mOnTabClickListener = listener;
    }

    public void setTabDrawable(int index, int resid) {
        if (index < 0 || index >= mLength) return;
        TextView child = (TextView) getChildAt(index);
        if (child != null) {
            child.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(getContext(), resid));
        }
    }

    public void setClearTabDrawable(int index) {
        if (index < 0 || index >= mLength) return;
        TextView child = (TextView) getChildAt(index);
        if (child != null) {
            child.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }


    private void unSelectAll() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(false);
        }
    }
}
