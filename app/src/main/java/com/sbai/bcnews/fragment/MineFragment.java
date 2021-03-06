package com.sbai.bcnews.fragment;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.BuildConfig;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ModifyPassActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.activity.author.AuthorWorkbenchActivity;
import com.sbai.bcnews.activity.mine.FeedbackActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.activity.mine.MessageActivity;
import com.sbai.bcnews.activity.mine.MyCollectActivity;
import com.sbai.bcnews.activity.mine.PersonalDataActivity;
import com.sbai.bcnews.activity.mine.QKCActivity;
import com.sbai.bcnews.activity.mine.ReadHistoryActivity;
import com.sbai.bcnews.activity.mine.ReviewActivity;
import com.sbai.bcnews.activity.mine.SettingActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.mine.MsgNumber;
import com.sbai.bcnews.model.mine.MyIntegral;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.model.news.NotReadMessage;
import com.sbai.bcnews.model.system.MintTabStatus;
import com.sbai.bcnews.model.system.Operation;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.SmartDialog;

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
    HasLabelLayout mHeadPortrait;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.collect)
    TextView mCollect;
    @BindView(R.id.history)
    TextView mHistory;
    @BindView(R.id.headLayout)
    RelativeLayout mHeadLayout;
    @BindView(R.id.contribute)
    LinearLayout mContribute;
    @BindView(R.id.feedBack)
    IconTextRow mFeedBack;
    @BindView(R.id.setting)
    IconTextRow mSetting;
    @BindView(R.id.message)
    IconTextRow mMessage;
    @BindView(R.id.review)
    IconTextRow mReview;

    Unbinder unbinder;
    @BindView(R.id.qkc)
    IconTextRow mQkc;
    @BindView(R.id.invite)
    IconTextRow mInvite;
    @BindView(R.id.notCheckLabel)
    ImageView mNotCheckLabel;

    private int mNotReadMessageCount;


    private BroadcastReceiver mLoginBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(LoginActivity.ACTION_LOGIN_SUCCESS)) {
                if (getUserVisibleHint()) {
                    showUpdateSetLoginDialog();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerLoginBroadcast();
    }

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
        requestQKCAndInviteHasGiftTabVisible();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mLoginBroadcastReceiver);
    }

    private void registerLoginBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginActivity.ACTION_LOGIN_SUCCESS);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mLoginBroadcastReceiver, intentFilter);
    }

    /**
     * 控制qkc和邀请有礼显示的接口
     */
    private void requestQKCAndInviteHasGiftTabVisible() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            mInvite.setVisibility(View.VISIBLE);
            mQkc.setVisibility(View.VISIBLE);
        } else {
            Apic.requestQKCAndInviteHasGiftTabVisible()
                    .tag(TAG)
                    .callback(new Callback2D<Resp<MintTabStatus>, MintTabStatus>() {
                        @Override
                        protected void onRespSuccessData(MintTabStatus data) {
                            updateTabStatus(data);
                        }
                    })
                    .fire();
        }
    }

    private void updateTabStatus(MintTabStatus data) {
        if (data.getPromoterShow() == MintTabStatus.MINE_INVITE_HAS_GIFT_TAB_SHOW) {
            mInvite.setVisibility(View.VISIBLE);
        } else {
            mInvite.setVisibility(View.GONE);
        }

        if (data.getIntegralShow() == MintTabStatus.MINE_QKC_TAB_SHOW) {
            mQkc.setVisibility(View.VISIBLE);
        } else {
            mQkc.setVisibility(View.GONE);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            refreshUserData();
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
        requestQKCNumber();
    }

    private void requestQKCNumber() {
        Apic.requestIntegral()
                .tag(TAG)
                .callback(new Callback2D<Resp<MyIntegral>, MyIntegral>() {
                    @Override
                    protected void onRespSuccessData(MyIntegral data) {
                        updateQKCNumber(data.getIntegral());
                    }
                })
                .fire();
    }

    private void updateQKCNumber(double integral) {
        mQkc.setSubText(FinanceUtil.formatWithScaleRemoveTailZero(integral));
    }

    public void updateNotReadMessage(NotReadMessage data) {
        updateNotReadMessage(data.getMsg());
        updateNotReadFeedBackMessage(data.getFeedBack());
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
            if (userInfo.isAuthor()) {
                mNotCheckLabel.setVisibility(View.GONE);
                mHeadPortrait.setLabelImageViewVisible(true);
                boolean isOfficialAuthor = userInfo.getAuthorType() == Author.AUTHOR_STATUS_OFFICIAL;
                mHeadPortrait.setLabelSelected(isOfficialAuthor);

            } else {
                mHeadPortrait.setLabelImageViewVisible(false);
                mNotCheckLabel.setVisibility(View.VISIBLE);
            }
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
            mQkc.setSubText("");
            mHeadPortrait.setLabelImageViewVisible(false);
            mNotCheckLabel.setVisibility(View.VISIBLE);
        }
    }

    private void updateNotReadMessage(int count) {
        if (count < 100)
            mMessage.setSubText(String.valueOf(count));
        else
            mMessage.setSubText("99+");
        mNotReadMessageCount = count;
        if (count != 0) {
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
            mHeadPortrait.setImageSrc(LocalUser.getUser().getUserInfo().getUserPortrait());
        } else {
            mHeadPortrait.setImageSrc(R.drawable.ic_default_head_portrait);
        }
    }

    public void showUpdateSetLoginDialog() {
        if (LocalUser.getUser().isLogin() && LocalUser.getUser().getUserInfo().getIsPassword() == 0) {
            SmartDialog.with(getActivity(), getString(R.string.please_set_login_password)).setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                @Override
                public void onClick(Dialog dialog) {
                    dialog.dismiss();
                    Launcher.with(getActivity(), ModifyPassActivity.class).putExtra(ExtraKeys.HAS_LOGIN_PSD, LocalUser.getUser().getUserInfo().getIsPassword() > 0).execute();
                }
            }).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.headPortrait, R.id.userName, R.id.collect, R.id.history,
            R.id.contribute, R.id.feedBack, R.id.setting, R.id.message,
            R.id.review, R.id.qkc, R.id.invite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.headPortrait:
            case R.id.userName:
                umengEventCount(UmengCountEventId.MINE_PORTRAIT_AND_NAME);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), PersonalDataActivity.class).execute();
                } else {
                    login();
                }
                break;
            case R.id.collect:
                umengEventCount(UmengCountEventId.MINE_COLLECT);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), MyCollectActivity.class).execute();
                } else {
                    login();
                }
                break;
            case R.id.history:
                umengEventCount(UmengCountEventId.MINE_HISTORY);
                Launcher.with(getActivity(), ReadHistoryActivity.class).execute();
                break;
            case R.id.contribute:
                if (LocalUser.getUser().isLogin() && LocalUser.getUser().getUserInfo().isAuthor()) {
                    Launcher.with(getActivity(), AuthorWorkbenchActivity.class).execute();
                } else {
                    requestOperationWeChatAccount();
                }
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
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), MessageActivity.class).putExtra(ExtraKeys.DATA, mNotReadMessageCount).execute();
                } else {
                    login();
                }
                break;
            case R.id.review:
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), ReviewActivity.class).execute();
                } else {
                    login();
                }
                break;
            case R.id.qkc:
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), QKCActivity.class).execute();
                } else {
                    login();
                }
                break;
            case R.id.invite:
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), WebActivity.class)
                            .putExtra(WebActivity.EX_URL, Apic.url.MINE_INVITE_HAS_GIFT)
                            .execute();
                } else {
                    login();
                }
                break;
        }
    }

    private void login() {
        Launcher.with(getActivity(), LoginActivity.class).execute();
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
                        ClipboardUtils.clipboardText(getActivity(), data, R.string.copy_success);
                    }
                })
                .show();
    }
}
