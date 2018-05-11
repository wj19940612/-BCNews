package com.sbai.bcnews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Modified by $nishuideyu$ on 2018/5/11
 * <p>
 * Description: 获取
 * </p>
 */
public class MeasureTextView extends AppCompatTextView {

    private static final String TAG = "MeasureTextView";

    public interface OnLineCountListener {
        void onLineCount(int lineCount);
    }

    public OnLineCountListener mOnLineCountListener;

    public void setOnLineCountListener(OnLineCountListener onLineCountListener) {
        mOnLineCountListener = onLineCountListener;
    }

    public MeasureTextView(Context context) {
        this(context, null);
    }

    public MeasureTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeasureTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnLineCountListener != null) {
            mOnLineCountListener.onLineCount(getLineCount());
        }
    }
}
