package com.sbai.bcnews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.R;


/**
 * Modified by john on 24/01/2018
 * <p>
 * Description:
 * <p>
 * APIs:
 */
public class MarketFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market, container, false);
    }
}
