package com.sbai.bcnews.utils;

import android.view.View;

/**
 * Created by ${wangJie} on 2018/2/27.
 */

public interface OnItemLongClickListener<T> {
    boolean onLongClick(View v, int position, T t);
}
