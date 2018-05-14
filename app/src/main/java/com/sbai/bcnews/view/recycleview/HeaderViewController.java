package com.sbai.bcnews.view.recycleview;

import android.view.View;

/**
 * Modified by $nishuideyu$ on 2018/4/19
 * <p>
 * Description:
 * </p>
 */
public interface HeaderViewController extends RecycleViewType{


    void addHeaderView(View view);

    void addFooterView(View view);

    boolean removeHeaderView();

    boolean removeFooterView();

    boolean hasFooterView();

}
