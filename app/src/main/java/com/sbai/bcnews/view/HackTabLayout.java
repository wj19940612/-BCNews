package com.sbai.bcnews.view;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.sbai.bcnews.utils.Display;

import java.lang.reflect.Field;

/**
 * Modified by john on 26/10/2017
 */
public class HackTabLayout extends TabLayout {

    private static final float MARGIN_DP = 10;

    public HackTabLayout(Context context) {
        super(context);
    }

    public HackTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HackTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hack();
    }

    public void hack() {
        Class<?> tabLayoutClass = getClass().getSuperclass();
        Field field = null;
        try {
            field = tabLayoutClass.getDeclaredField("mTabStrip");
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout tabStrip = null;
        try {
            if (field != null) {
                tabStrip = (LinearLayout) field.get(this);
            }

            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View child = tabStrip.getChildAt(i);

//                Field declaredField = child.getClass().getDeclaredField("mTextView");
//                declaredField.setAccessible(true);
//
//                TextView textView = (TextView) declaredField.get(child);
//                int width = textView.getWidth();
//                if (width == 0) {
//                    textView.measure(0, 0);
//                    width = textView.getMeasuredWidth();
//                }

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
                //params.width = width;

                child.setPadding(0, 0, 0, 0);
                int margin = (int) Display.dp2Px(MARGIN_DP, getResources());
                if (i == 0) {
                    params.setMargins(0, 0, margin, 0);
                } else if (i == tabStrip.getChildCount() - 1) {
                    params.setMargins(margin, 0, 0, 0);
                } else {
                    params.setMargins(margin, 0, margin, 0);
                }
                child.invalidate();
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
