package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountManagerActivity extends BaseActivity {

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

        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        if (userInfo != null) {
            if (userInfo.getWxBound() == UserInfo.WECHAT_BIND_STATUS_NOT_BIND) {
                mCloseWeChat.setVisibility(View.GONE);
            } else {
                mCloseWeChat.setVisibility(View.VISIBLE);
            }
            changeUserWeChatBindStatus(userInfo.getWxBound());

            if (!TextUtils.isEmpty(userInfo.getUserPhone())) {
                mPhoneNumber.setSubText(userInfo.getUserPhone());
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
            if (userInfo.getWxBound() == UserInfo.WECHAT_BIND_STATUS_BIND) {
                unBindWeChat();
            } else {
                bindWeChat(userInfo);
            }
        }
    }

    private void bindWeChat(UserInfo userInfo) {
        Apic.requestBindWeChat(userInfo.getWxOpenId(), userInfo.getWxName(), null, 0)
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        changeUserWeChatBindStatus(UserInfo.WECHAT_BIND_STATUS_BIND);
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
}
