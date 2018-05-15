package com.sbai.bcnews.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.model.system.NotificationStatus;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.PermissionUtil;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NotificationManageActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.notificationSwitch)
    ImageView mNotificationSwitch;
    @BindView(R.id.receiveNotification)
    LinearLayout mReceiveNotification;
    @BindView(R.id.commentOnReplySwitch)
    ImageView mCommentOnReplySwitch;
    @BindView(R.id.commentOnReplyLL)
    LinearLayout mCommentOnReplyLL;
    @BindView(R.id.split)
    View mSplit;
    @BindView(R.id.praiseNotificationSwitch)
    ImageView mPraiseNotificationSwitch;
    @BindView(R.id.praiseLL)
    LinearLayout mPraiseLL;
    private boolean mNotificationEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manage);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mNotificationEnabled = PermissionUtil.isNotificationEnabled(this);

        mNotificationSwitch.setSelected(mNotificationEnabled);

        updateSwitchStatus();

    }

    private void updateSwitchStatus() {
        if (!LocalUser.getUser().isLogin()) {
            mCommentOnReplySwitch.setSelected(false);
            mPraiseNotificationSwitch.setSelected(false);
        } else if (!mNotificationEnabled) {
            mCommentOnReplySwitch.setSelected(false);
            mPraiseNotificationSwitch.setSelected(false);
        } else if (LocalUser.getUser().isLogin()) {
            UserInfo userInfo = LocalUser.getUser().getUserInfo();
            mCommentOnReplySwitch.setSelected(userInfo.isReceiveCommentOnReplyNotification());
            mPraiseNotificationSwitch.setSelected(userInfo.isReceivePraiseNotification());
        }
    }

    @OnClick({R.id.notificationSwitch, R.id.commentOnReplySwitch, R.id.praiseNotificationSwitch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notificationSwitch:
                openSystemSettingPage();
                break;
            case R.id.commentOnReplySwitch:
                if (!mNotificationEnabled) {
                    openSystemSettingPage();
                } else if (LocalUser.getUser().isLogin()) {
                    int status = LocalUser.getUser().getUserInfo().isReceiveCommentOnReplyNotification() ? 0 : NotificationStatus.USER_RECEIVE_COMMENT_ON_REPLY_NOTIFICATION;
                    switchNotificationStatus(NotificationStatus.NOTIFICATION_TYPE_RECEIVE_COMMENT_ON_REPLY, status);
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
            case R.id.praiseNotificationSwitch:
                if (!mNotificationEnabled) {
                    openSystemSettingPage();
                } else if (LocalUser.getUser().isLogin()) {
                    int status = LocalUser.getUser().getUserInfo().isReceivePraiseNotification() ? 0 : NotificationStatus.USER_RECEIVE_PRAISE_NOTIFICATION;
                    switchNotificationStatus(NotificationStatus.NOTIFICATION_TYPE_RECEIVE_PRAISE, status);
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
        }
    }


    private void switchNotificationStatus(int notificationType, int status) {
        Apic.switchNotificationStatus(notificationType, status)
                .tag(TAG)
                .callback(new Callback2D<Resp<NotificationStatus>, NotificationStatus>() {
                    @Override
                    protected void onRespSuccessData(NotificationStatus data) {
                        UserInfo userInfo = LocalUser.getUser().getUserInfo();
                        userInfo.setDiscuss(data.getDiscuss());
                        userInfo.setPraise(data.getPraise());
                        LocalUser.getUser().setUserInfo(userInfo);
                        updateSwitchStatus();
                    }
                })
                .fire();
    }

    private void openSystemSettingPage() {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);
    }
}
