package com.sbai.bcnews.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ${wangJie} on 2017/11/14.
 * 按下的时候 alpha会变暗的view
 */

public class TouchAlphaChangeImageView extends AppCompatImageView implements View.OnTouchListener {
    public TouchAlphaChangeImageView(Context context) {
        this(context, null);
    }

    public TouchAlphaChangeImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchAlphaChangeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:
                setAlpha(1f);
                break;
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
