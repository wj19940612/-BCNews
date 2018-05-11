package com.sbai.bcnews.view;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

/**
 * Modified by $nishuideyu$ on 2018/5/11
 * <p>
 * Description: 获取
 * </p>
 */
public class MeasureTextView extends AppCompatTextView {

    private OnTextLineCountListener mOnTextLineCountListener;

    private static final String TAG = "MeasureTextView";

    public void setOnTextLineCountListener(OnTextLineCountListener onTextLineCountListener) {
        mOnTextLineCountListener = onTextLineCountListener;
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
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        Layout layout = getLayout();
        if(layout!=null){
            Log.d(TAG, "哈哈哈哈d: "+layout.getLineCount());
        }
        int lineCount1 = getLineCount();
        Log.d(TAG, "o: "+lineCount1);

        int lineCount = getLineCount(text, getPaint(), getTextSize(), getWidth() - getPaddingLeft() - getPaddingRight(), getResources().getDisplayMetrics());
        Log.d(TAG, "onTextChanged: " + lineCount + "  "+text);
        if (mOnTextLineCountListener != null) {
            mOnTextLineCountListener.onTextLineCount(lineCount);
        }
    }

    private int getLineCount(CharSequence text, TextPaint paint, float size, float width,
                             DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics));
        StaticLayout layout = new StaticLayout(text, paint, (int) width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        return layout.getLineCount();
    }

    public interface OnTextLineCountListener {
        void onTextLineCount(int lineCount);
    }


}
