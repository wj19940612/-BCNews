package com.sbai.bcnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by ${wangJie} on 2018/1/25.
 */

public abstract class BaseSwipeLoadFragment extends BaseFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = refreshOrLoadView();

    }

    /**
     * 设置需要刷新和加载的的view
     *
     * @return
     */
    protected abstract View refreshOrLoadView();
}
