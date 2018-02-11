package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
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

        // TODO: 2018/2/9 绑定的微信 缺少微信绑定状态

        if (true) {
            mCloseWeChat.setVisibility(View.GONE);
        } else {
            mCloseWeChat.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.weChatSwitch)
    public void onViewClicked() {
        Apic.unbindWeChatAccount()
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {

                    }
                })
                .fire();
    }
}
