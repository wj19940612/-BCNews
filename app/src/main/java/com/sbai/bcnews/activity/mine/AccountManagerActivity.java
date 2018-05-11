package com.sbai.bcnews.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.StrFormatter;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountManagerActivity extends WeChatActivity {

    private static final int REQ_CODE_WE_CHAT_BIND = 5004;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.phoneNumber)
    IconTextRow mPhoneNumber;
    @BindView(R.id.weChatSwitch)
    ImageView mWeChatSwitch;
    @BindView(R.id.closeWeChat)
    LinearLayout mCloseWeChat;
    @BindView(R.id.weChat)
    TextView mWeChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
        ButterKnife.bind(this);

        updateUserInfo();
    }

    private void updateUserInfo() {
        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        if (userInfo != null) {
            changeUserWeChatBindStatus(userInfo.getWxBound());
            if (!TextUtils.isEmpty(userInfo.getUserPhone())) {
                mPhoneNumber.setSubText(StrFormatter.getFormatSafetyPhoneNumber(userInfo.getUserPhone()));
            }
        }
    }


    private void changeUserWeChatBindStatus(int bindStatus) {
        if (bindStatus == UserInfo.WECHAT_BIND_STATUS_BIND) {
            mWeChatSwitch.setSelected(true);
        } else {
            mWeChatSwitch.setSelected(false);
        }
        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        if (userInfo.getWxBound() != bindStatus) {
            userInfo.setWxBound(bindStatus);
            LocalUser.getUser().setUserInfo(userInfo);
        }
    }

    @OnClick(R.id.weChatSwitch)
    public void onViewClicked() {
        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        if (userInfo != null) {
            switch (userInfo.getWxBound()) {
                case UserInfo.WECHAT_BIND_STATUS_NOT_BIND:
                    requestWeChatInfo();
                    break;
                case UserInfo.WECHAT_BIND_STATUS_BIND:
                    unBindWeChat();
                    break;
                case UserInfo.WECHAT_BIND_STATUS_UNBIND:
                    requestWeChatInfo();
                    break;
            }
        }
    }

    private void bindWeChat() {
        Apic.requestBindWeChat(getWeChatOpenid(), getWeChatName(), getWeChatIconUrl())
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        Apic.requestUserInfo()
                                .tag(TAG)
                                .callback(new Callback2D<Resp<UserInfo>, UserInfo>() {
                                    @Override
                                    protected void onRespSuccessData(UserInfo data) {
                                        LocalUser.getUser().setUserInfo(data);
                                        changeUserWeChatBindStatus(UserInfo.WECHAT_BIND_STATUS_BIND);
                                        postLogin();
                                    }
                                })
                                .fire();
                    }
                })
                .fire();
    }

    private void unBindWeChat() {
        Apic.unbindWeChatAccount()
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        changeUserWeChatBindStatus(UserInfo.WECHAT_BIND_STATUS_UNBIND);
                    }
                })
                .fire();
    }

    @Override
    protected void bindSuccess() {
        if (isWeChatLogin()) {
            bindWeChat();
        }
    }

    @Override
    protected void bindFailure() {
        ToastUtil.show(R.string.cancel_bind);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_WE_CHAT_BIND && resultCode == RESULT_OK) {
            updateUserInfo();
        }
    }
}
