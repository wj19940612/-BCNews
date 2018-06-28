package com.sbai.bcnews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;

/**
 * Modified by $nishuideyu$ on 2018/6/28
 * <p>
 * Description: 列表底部的提示
 * </p>
 */
public class FooterView extends LinearLayout {


    private float mLineHeight = 1f;
    private float mLineWidth = 100;

    public FooterView(Context context) {
        this(context, null);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FooterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(HORIZONTAL);

        setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams(dp2px(mLineWidth), dp2px(mLineHeight));

        View leftLine = createLine();
        addView(leftLine, layoutParams);


        TextView text = createText();
        LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(dp2px(5), 0, dp2px(5), 0);
        addView(text, textLayoutParams);

        View rightLine = createLine();
        addView(rightLine, layoutParams);

    }

    private TextView createText() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
        textView.setText(R.string.i_have_a_bottom_line);
        return textView;
    }

    private View createLine() {
        View view = new View(getContext());
        view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bg_EA));
        return view;
    }

    private int dp2px(float dp) {
        return (int) com.sbai.bcnews.utils.Display.dp2Px(dp, getResources());
    }
}
