package com.sbai.bcnews.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018\1\26 0026.
 */

public class NewsScrollView extends NestedScrollView{
    private OnScrollListener listener;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public NewsScrollView(Context context) {
        super(context);
    }

    public NewsScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 滑动距离监听器
    public interface OnScrollListener{

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(listener!=null){
            listener.onScroll(getScrollY());
        }
    }
}
