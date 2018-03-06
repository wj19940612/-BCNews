package com.sbai.bcnews.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.SmartDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

/**
 * 微信
 */

public abstract class WeChatActivity extends BaseActivity {
    private String mWeChatOpenid;
    private String mWeChatName;
    private String mWeChatIconUrl;
    private int mWeChatGender;

    public void requestWeChatInfo() {
        if (!UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {
            ToastUtil.show(R.string.you_not_install_weixin);
            return;
        }
        UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                mWeChatOpenid = map.get("openid");
                mWeChatName = map.get("name");
                mWeChatIconUrl = map.get("iconurl");
                mWeChatGender = map.get("gender").equals("女") ? 1 : 2;
                bindSuccess();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败:" + throwable.getMessage());
                bindFailure();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
                bindFailure();
            }
        });
    }

    protected abstract void bindSuccess();

    protected abstract void bindFailure();


    protected boolean isWeChatLogin() {
        return !TextUtils.isEmpty(mWeChatOpenid);
    }

    public String getWeChatOpenid() {
        return mWeChatOpenid;
    }

    public void setWeChatOpenid(String weChatOpenid) {
        mWeChatOpenid = weChatOpenid;
    }

    public String getWeChatName() {
        return mWeChatName;
    }

    public String getWeChatIconUrl() {
        return mWeChatIconUrl;
    }

    public int getWeChatGender() {
        return mWeChatGender;
    }

    private void requestUseWeChatInfo() {
        Apic.reqUseWxInfo().tag(TAG)
                .callback(new Callback<Resp<UserInfo>>() {
                    @Override
                    protected void onRespSuccess(Resp<UserInfo> resp) {
                        LocalUser.getUser().setUserInfo(resp.getData());
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                }).fireFreely();
    }

    protected void postLogin() {
        sendLoginSuccessBroadcast();
        if (isWeChatLogin() && Preference.get().isFirstLogin() && LocalUser.getUser().getUserInfo().isModifyPortrait()) {
            Preference.get().setFirstLogin(false);
            SmartDialog.single(getActivity())
                    .setMessage(getString(R.string.use_wechat_portrait_and_name))
                    .setPositive(R.string.yes, new SmartDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            dialog.dismiss();
                            requestUseWeChatInfo();
                        }
                    })
                    .setNegative(R.string.no, new SmartDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog) {
                            dialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                        }
                    })
                    .setCancelableOnTouchOutside(false)
                    .show();
        } else {
            setResult(RESULT_OK);
            finish();
        }

    }

    protected void sendLoginSuccessBroadcast() {
        LocalBroadcastManager.getInstance(getActivity())
                .sendBroadcast(new Intent(ACTION_LOGIN_SUCCESS));
    }
}
