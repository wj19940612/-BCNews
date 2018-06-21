package com.sbai.bcnews.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.activity.ModifyPassActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.AppInfo;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.nightOrLightModeSwitch)
    ImageView mNightOrLightModeSwitch;
    @BindView(R.id.nightOrLightModeLL)
    LinearLayout mNightOrLightModeLL;
    @BindView(R.id.notificationManage)
    IconTextRow mNotificationManage;
    @BindView(R.id.accountManager)
    IconTextRow mAccountManager;
    @BindView(R.id.aboutBcnews)
    IconTextRow mAboutBcnews;
    @BindView(R.id.recommendToFriend)
    IconTextRow mRecommendToFriend;
    @BindView(R.id.logout)
    TextView mLogout;
    @BindView(R.id.modifyPass)
    IconTextRow mModifyPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initPassView();
    }

    private void initPassView() {
        if (LocalUser.getUser().isLogin()) {
            mModifyPass.setVisibility(View.VISIBLE);
            if (LocalUser.getUser().getUserInfo().getIsPassword() > 0) {
                mModifyPass.setText(R.string.modify_pass);
            } else {
                mModifyPass.setText(R.string.set_login_password);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (LocalUser.getUser().isLogin()) {
            mLogout.setVisibility(View.VISIBLE);
        } else {
            mLogout.setVisibility(View.GONE);
        }
    }


    private void logout() {
        SmartDialog.with(getActivity(), R.string.affirm_logout)
                .setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog) {
                        Apic.logout()
                                .tag(TAG)
                                .callback(new Callback<Resp<Object>>() {

                                    @Override
                                    protected void onRespSuccess(Resp<Object> resp) {
                                        LocalUser.getUser().logout();
                                        finish();
                                    }
                                })
                                .fire();
                    }
                })
                .show();

    }

    @OnClick({R.id.nightOrLightModeSwitch, R.id.notificationManage, R.id.accountManager, R.id.aboutBcnews, R.id.recommendToFriend, R.id.logout,R.id.modifyPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.nightOrLightModeSwitch:
                break;
            case R.id.notificationManage:
                Launcher.with(getActivity(), NotificationManageActivity.class).execute();
                break;
            case R.id.recommendToFriend:
                break;
            case R.id.notificationSwitch:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.accountManager:
                umengEventCount(UmengCountEventId.SETTING_ACCOUNT_MANAGER);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), AccountManagerActivity.class).execute();
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.aboutBcnews:
                umengEventCount(UmengCountEventId.SETTING_ABOUT_APP);
                Launcher.with(getActivity(), WebActivity.class)
                        .putExtra(WebActivity.EX_URL, String.format(Apic.url.WEB_URI_ABOUT_PAGE, AppInfo.getVersionName(getActivity())))
                        .putExtra(WebActivity.EX_TITLE, getString(R.string.about_bcnews))
                        .execute();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.modifyPass:
                Launcher.with(this, ModifyPassActivity.class).putExtra(ExtraKeys.HAS_LOGIN_PSD, LocalUser.getUser().getUserInfo().getIsPassword() > 0).execute();
                break;
        }
    }
}
