package com.sbai.bcnews.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 我的界面
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.headPortrait)
    ImageView mHeadPortrait;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.collect)
    TextView mCollect;
    @BindView(R.id.history)
    TextView mHistory;
    @BindView(R.id.headLayout)
    RelativeLayout mHeadLayout;
    @BindView(R.id.contribute)
    IconTextRow mContribute;
    @BindView(R.id.feedBack)
    IconTextRow mFeedBack;
    @BindView(R.id.setting)
    IconTextRow mSetting;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserLoginStatus();
    }

    private void updateUserLoginStatus() {
        updateUserHeadPortrait();

    }

    private void updateUserHeadPortrait() {
        if (LocalUser.getUser().isLogin()) {
            GlideApp.with(getActivity())
                    .load(LocalUser.getUser().getUserInfo().getUserPortrait())
                    .placeholder(R.drawable.ic_default_head_portrait)
                    .circleCrop()
                    .into(mHeadPortrait);
        }else {
            GlideApp.with(getActivity())
                    .load(R.drawable.ic_default_head_portrait)
                    .circleCrop()
                    .into(mHeadPortrait);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.headPortrait, R.id.userName, R.id.collect, R.id.history, R.id.contribute, R.id.feedBack, R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headPortrait:
                break;
            case R.id.userName:
                break;
            case R.id.collect:
                break;
            case R.id.history:
                break;
            case R.id.contribute:
                break;
            case R.id.feedBack:
                break;
            case R.id.setting:
                break;
        }
    }
}
