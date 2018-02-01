package com.sbai.bcnews.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class NewsScrollView extends ScrollView {
    private OnScrollListener listener;
    private int scrollY;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public NewsScrollView(Context context) {
        this(context,null);
    }

    public NewsScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NewsScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnTouchListener();
    }

    // 滑动距离监听器
    public interface OnScrollListener {

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);

        void onScrollState(boolean scrolling);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (listener != null) {
            listener.onScroll(getScrollY());
        }
    }

    public void setOnTouchListener() {
        OnTouchListener onTouchListener = new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollY = getScrollY();
                    postDelayed(scrollCheckTask, 300);
                }
                return false;
            }
        };
        setOnTouchListener(onTouchListener);
    }

    Runnable scrollCheckTask = new Runnable() {
        @Override
        public void run() {
            int newScroll = getScrollY();
            if (scrollY == newScroll) {
                if (listener != null) {
                    listener.onScrollState(false);
                }
            } else {
                scrollY = getScrollY();
                listener.onScrollState(true);
                postDelayed(scrollCheckTask, 300);
            }
        }
    };
}
