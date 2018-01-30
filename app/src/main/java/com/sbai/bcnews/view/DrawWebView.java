package com.sbai.bcnews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public class DrawWebView extends WebView {
    public interface DisplayFinishListener {
        void After();
    }

    DisplayFinishListener df;

    public void setDisplayFinishListener(DisplayFinishListener displayFinishListener) {
        df = displayFinishListener;
    }

    public void removeFinishListener() {
        df = null;
    }

    public DrawWebView(Context context) {
        super(context);
    }

    public DrawWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //onDraw表示显示完毕
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (df != null)
            df.After();
    }
}
