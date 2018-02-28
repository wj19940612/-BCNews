package com.sbai.bcnews.view.tabLayout;

import android.content.Context;
import android.content.res.TypedArray;

/**
 * Modified by john on 28/02/2018
 * <p>
 * Description:
 * <p>
 * APIs:
 */
public class ThemeUtils {

    private static final int[] APPCOMPAT_CHECK_ATTRS = {
            android.support.v7.appcompat.R.attr.colorPrimary
    };

    static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }
}
