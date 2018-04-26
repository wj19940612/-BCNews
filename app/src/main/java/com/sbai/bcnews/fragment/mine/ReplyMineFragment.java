package com.sbai.bcnews.fragment.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReplyMineFragment extends BaseFragment {

    private Unbinder mBind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_minek, container, false);
        mBind = ButterKnife.bind(this, view);
        return view ;
    }
    
}
