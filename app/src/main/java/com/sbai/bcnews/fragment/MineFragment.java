package com.sbai.bcnews.fragment;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.sbai.bcnews.activity.mine.SettingActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.SmartDialog;
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


    public void refreshUserData() {
        // TODO: 2018/2/8  我的tab：点击可刷新头像、昵称、收藏、历史等数据，当然是登录状态下；

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
        mCollect.setText(spannableString);
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
                umengEventCount(UmengCountEventId.MINE_PORTRAIT_AND_NAME);
                if (LocalUser.getUser().isLogin()) {
                    // TODO: 2018/2/8 用户资料页
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.collect:
                umengEventCount(UmengCountEventId.MINE_COLLECT);
                if (LocalUser.getUser().isLogin()) {
                    // TODO: 2018/2/8  收藏页面
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.history:
                umengEventCount(UmengCountEventId.MINE_HISTORY);
                break;
            case R.id.contribute:
                requestOperationWetchatAccount();
                umengEventCount(UmengCountEventId.MINE_CONTRIBUTE);
                break;
            case R.id.feedBack:
                break;
            case R.id.setting:
                umengEventCount(UmengCountEventId.MINE_SETTING);
                Launcher.with(getActivity(), SettingActivity.class).execute();
                break;
        }
    }

    private void requestOperationWetchatAccount() {
        Apic.requestOperationWetchatAccount()
                .tag(TAG)
                .callback(new Callback2D<Resp<String>, String>() {
                    @Override
                    protected void onRespSuccessData(String data) {
                        showAddWetchatAccountDialog(data);
                    }
                })
                .fire();
    }

    private void showAddWetchatAccountDialog(final String data) {
        SmartDialog.with(getActivity(), getString(R.string.please_add_us_wechat_account, data))
                .setPositive(R.string.copy_us_wechat_account, new SmartDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog) {
                        dialog.dismiss();
                        copyWeChatAccount(data);
                    }
                })
                .show();
    }

    private void copyWeChatAccount(String data) {
        ClipboardManager clipboardManager = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, data);
        clipboardManager.setPrimaryClip(clipData);
    }
}
