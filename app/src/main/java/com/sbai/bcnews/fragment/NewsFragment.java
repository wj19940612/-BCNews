package com.sbai.bcnews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.MainActivity;
import com.sbai.bcnews.activity.SubTextActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 资讯
 * <p>
 * APIs:
 */
public class NewsFragment extends BaseFragment {

    private Unbinder mBind;
    @BindView(R.id.news)
    TextView mNewsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.news})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(),SubTextActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }
}
