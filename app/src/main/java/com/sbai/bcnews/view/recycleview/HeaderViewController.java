package com.sbai.bcnews.view.recycleview;

import android.view.View;

/**
 * Modified by $nishuideyu$ on 2018/4/19
 * <p>
 * Description:
 * </p>
 */
public interface HeaderViewController {

    int HEADER_VIEW_TYPE = 1;
    int FOOTER_VIEW_TYPE = 2;

    void addHeaderView(View view);

    int getHeaderViewsCount();

    boolean removeHeaderView();

    void addFooterView(View view);

    int getFooterViewsCount();

    boolean removeFooterView();

    boolean hasFooterView();


}
