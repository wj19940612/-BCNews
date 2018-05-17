package com.sbai.bcnews.fragment.dialog;

import android.view.Gravity;

import com.sbai.bcnews.R;

/**
 * 底部弹窗基础类
 */
public class BottomDialogFragment extends BaseDialogFragment {

    protected float getWidthRatio() {
        return 1f;
    }

    protected int getWindowGravity() {
        return Gravity.BOTTOM;
    }

    protected int getDialogTheme() {
        return R.style.BaseDialog_Bottom;
    }

}
