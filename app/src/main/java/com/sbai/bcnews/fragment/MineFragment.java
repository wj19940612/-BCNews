package com.sbai.bcnews.fragment;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
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
        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        if (LocalUser.getUser().isLogin()) {
            mUserName.setText(userInfo.getUserName());
            updateUserCollectNumber(userInfo.getCollectCount());
            updateUserReadHistory(userInfo.getReadCount());
        } else {
            mUserName.setText(R.string.click_login);
            updateUserCollectNumber(0);
        }
    }

    private void updateUserReadHistory(int readHistory) {
        SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.history),
                " " + readHistory,
                ContextCompat.getColor(getActivity(), R.color.cyan));
        mHistory.setText(spannableString);
    }

    private void updateUserCollectNumber(int collectNumber) {
        SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.collect),
                " " + collectNumber,
                ContextCompat.getColor(getActivity(), R.color.cyan));
        mHistory.setText(spannableString);
    }

    private void updateUserHeadPortrait() {
        if (LocalUser.getUser().isLogin()) {
            GlideApp.with(getActivity())
                    .load(LocalUser.getUser().getUserInfo().getUserPortrait())
                    .placeholder(R.drawable.ic_default_head_portrait)
                    .circleCrop()
                    .into(mHeadPortrait);
        } else {
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
            case R.id.userName:
                if (LocalUser.getUser().isLogin()) {
                    // TODO: 2018/2/8 用户资料页
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.collect:
                if (LocalUser.getUser().isLogin()) {
                    // TODO: 2018/2/8  收藏页面
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
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
