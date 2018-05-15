package com.sbai.bcnews.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * Modified by $nishuideyu$ on 2018/4/16
 * <p>
 * Description:
 * </p>
 */
public class ClipboardUtils {

    public static void clipboardText(Context context, CharSequence charSequence) {
        clipboardText(context, charSequence, null);
    }

    public static void clipboardText(Context context, CharSequence charSequence, CharSequence toastText) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("", charSequence);
            clipboardManager.setPrimaryClip(clipData);
            if (!TextUtils.isEmpty(toastText)) {
                ToastUtil.show(toastText.toString());
            }
        }
    }

    public static void clipboardText(Context context, CharSequence charSequence, int toastTextId) {
        clipboardText(context, charSequence, context.getString(toastTextId));
    }
}
