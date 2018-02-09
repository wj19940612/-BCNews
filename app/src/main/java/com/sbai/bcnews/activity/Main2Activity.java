package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.sbai.bcnews.R;
import com.sbai.bcnews.swipeload.ListSwipeLoadActivity;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Main2Activity extends ListSwipeLoadActivity {

    @BindView(R.id.split)
    View mSplit;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.container)
    LinearLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);

        getSwipeTargetView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.blackPrimary));
    }

    @Override
    public View getView() {
        return mContainer;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
