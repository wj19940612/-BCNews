package com.sbai.bcnews.activity.mine;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.PermissionUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.IconTextRow;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.TitleBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.notificationSwitch)
    ImageView mNotificationSwitch;
    @BindView(R.id.receiveNotification)
    LinearLayout mReceiveNotification;
    @BindView(R.id.personalData)
    IconTextRow mPersonalData;
    @BindView(R.id.accountManager)
    IconTextRow mAccountManager;
    @BindView(R.id.aboutBcnews)
    IconTextRow mAboutBcnews;
    @BindView(R.id.logout)
    TextView mLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mNotificationSwitch.setSelected(PermissionUtil.isNotificationEnabled(this));
        if (LocalUser.getUser().isLogin()) {
            mLogout.setVisibility(View.VISIBLE);
        } else {
            mLogout.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.notificationSwitch, R.id.personalData, R.id.accountManager, R.id.aboutBcnews, R.id.logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notificationSwitch:
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
            case R.id.personalData:
                umengEventCount(UmengCountEventId.SETTING_PERSONAL_DATE);
                if (LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), PersonalDataActivity.class).execute();
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
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
                        .putExtra(WebActivity.EX_URL, Apic.url.WEB_URI_ABOUT_PAGE)
                        .putExtra(WebActivity.EX_TITLE, getString(R.string.about_bcnews))
                        .execute();
                break;
            case R.id.logout:
                logout();
                break;
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
}
