package com.sbai.bcnews.utils;

import android.graphics.Typeface;
import android.widget.TextView;

import com.sbai.bcnews.App;

/**
 * Modified by $nishuideyu$ on 2018/6/21
 * <p>
 * Description:
 * </p>
 */
public class TypefaceUtils {


    public static Typeface getMiningTextTypeface() {
        return Typeface.createFromAsset(App.getAppContext().getAssets(),
                "fonts/mining_text.TTF");
    }

    public static void setTypeface(TextView textView, Typeface typeface) {
        textView.setTypeface(typeface);
    }

    public static void setMiningTextTypeface(TextView textView) {
        setTypeface(textView, getMiningTextTypeface());
    }

}
