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

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.FeedbackActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.activity.mine.MessageActivity;
import com.sbai.bcnews.activity.mine.MyCollectActivity;
import com.sbai.bcnews.activity.mine.PersonalDataActivity;
import com.sbai.bcnews.activity.mine.ReadHistoryActivity;
import com.sbai.bcnews.activity.mine.ReviewActivity;
import com.sbai.bcnews.activity.mine.SettingActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.model.mine.MsgNumber;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.model.system.Operation;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.glide.GlideApp;

import java.util.List;

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
    @BindView(R.id.message)
    IconTextRow mMessage;
    @BindView(R.id.review)
    IconTextRow mReview;

    Unbinder unbinder;

    private int mNotReadMessageCount;


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
        refreshUserData();
        requestNotReadMessageCount();
        requestWhetherHasNotReedFeedBackMessage();
        requestWhetherHasAllNotReadMessage();
    }

    private void requestWhetherHasAllNotReadMessage() {
        Apic.requestWhetherHasAllNotReadMessage()
                .tag(TAG)
                .fire();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshUserData();
            requestNotReadMessageCount();
            requestWhetherHasNotReedFeedBackMessage();
        }
    }

    public void refreshUserData() {
        if (!LocalUser.getUser().isLogin()) return;
        Apic.requestUserReadOrCollectNumber()
                .tag(TAG)
                .callback(new Callback2D<Resp<MsgNumber>, MsgNumber>() {
                    @Override
                    protected void onRespSuccessData(MsgNumber data) {
                        updateUserCollectNumber(data.getCollect());
                        updateUserReadHistory(data.getRead()
                        );
                    }
                })
                .fire();
    }

    private void requestNotReadMessageCount() {
        if (LocalUser.getUser().isLogin())
            Apic.requestNotReadMessageCount()
                    .tag(TAG)
                    .callback(new Callback2D<Resp<Integer>, Integer>() {
                        @Override
                        protected void onRespSuccessData(Integer data) {
                            updateNotReadMessage(data);
                        }
                    })
                    .fire();
    }

    private void requestWhetherHasNotReedFeedBackMessage() {
        Apic.requestWhetherHasNotReedFeedBackMessage()
                .tag(TAG)
                .callback(new Callback2D<Resp<Integer>, Integer>() {
                    @Override
                    protected void onRespSuccessData(Integer data) {
                        updateNotReadFeedBackMessage(data);
                    }
                })
                .fire();
    }

    private void updateNotReadFeedBackMessage(int data) {
        if (data == 0) {
            mFeedBack.setSubTextVisible(View.GONE);
        } else {
            mFeedBack.setSubTextVisible(View.VISIBLE);
        }
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
            int readHistorySize = 0;
            List<ReadHistoryOrMyCollect> data = NewsCache.getReadHistory();
            if (data != null) {
                readHistorySize = data.size();
            }
            updateUserReadHistory(readHistorySize);
            updateNotReadMessage(0);
        }
    }

    private void updateNotReadMessage(int count) {
        mMessage.setSubText(String.valueOf(count));
        mNotReadMessageCount = count;
        if (count == 0) {
            mMessage.setSubTextVisible(View.VISIBLE);
        } else {
            mMessage.setSubTextVisible(View.GONE);
        }
    }

    private void updateUserReadHistory(int readHistory) {
        SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.history),
                " " + readHistory,
                ContextCompat.getColor(getActivity(), R.color.text_476E92));
        mHistory.setText(spannableString);
    }

    private void updateUserCollectNumber(int collectNumber) {
        SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.collect),
                " " + collectNumber,
                ContextCompat.getColor(getActivity(), R.color.text_476E92));
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

    @OnClick({R.id.headPortrait, R.id.userName, R.id.collect, R.id.history,
            R.id.contribute, R.id.feedBack, R.id.setting, R.id.message, R.id.review})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headPortrait:
            case R.id.userName:
                umengEventCount(UmengCountEventId.MINE_PORTRAIT_AND_NAME);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), PersonalDataActivity.class).execute();
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.collect:
                umengEventCount(UmengCountEventId.MINE_COLLECT);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), MyCollectActivity.class).execute();
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.history:
                umengEventCount(UmengCountEventId.MINE_HISTORY);
                Launcher.with(getActivity(), ReadHistoryActivity.class).execute();
                break;
            case R.id.contribute:
                requestOperationWeChatAccount();
                umengEventCount(UmengCountEventId.MINE_CONTRIBUTE);
                break;
            case R.id.feedBack:
                Launcher.with(getActivity(), FeedbackActivity.class).execute();
                break;
            case R.id.setting:
                umengEventCount(UmengCountEventId.MINE_SETTING);
                Launcher.with(getActivity(), SettingActivity.class).execute();
                break;
            case R.id.message:
                Launcher.with(getActivity(), MessageActivity.class).putExtra(ExtraKeys.DATA, mNotReadMessageCount).execute();
                break;
            case R.id.review:
                Launcher.with(getActivity(), ReviewActivity.class).execute();
                break;
        }
    }

    private void requestOperationWeChatAccount() {
        Apic.requestOperationSetting(Operation.OPERATION_TYPE_WE_CHAT)
                .tag(TAG)
                .callback(new Callback2D<Resp<Operation>, Operation>() {
                    @Override
                    protected void onRespSuccessData(Operation data) {
                        showAddWeChatAccountDialog(data.getSYS_OPERATE_WX());
                    }
                })
                .fire();
    }

    private void showAddWeChatAccountDialog(final String data) {
        SmartDialog.with(getActivity(), getString(R.string.please_add_us_wechat_account, data))
                .setPositive(R.string.copy_us_wechat_account, new SmartDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog) {
                        dialog.dismiss();
                        copyWeChatAccount(data);
                        ToastUtil.show(R.string.copy_success);
                    }
                })
                .show();
    }

    private void copyWeChatAccount(String data) {
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, data);
        clipboardManager.setPrimaryClip(clipData);
    }

}
