package com.sbai.bcnews.activity.mine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.InputPhoneActivity;
import com.sbai.bcnews.activity.ModifyPassActivity;
import com.sbai.bcnews.activity.RegisterActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.KeyBoardHelper;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrFormatter;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.PasswordEditText;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.http.Resp.CODE_NOT_SET_PASSWORD;


public class LoginActivity extends WeChatActivity {

    private static final int REQ_CODE_REGISTER = 888;
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;

    private static final int WE_CHAT_BIND = 1;

    @BindView(R.id.rootView)
    RelativeLayout mRootView;

    @BindView(R.id.phoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.phoneNumberClear)
    ImageView mPhoneNumberClear;
    @BindView(R.id.authCode)
    EditText mAuthCode;
    @BindView(R.id.getAuthCode)
    TextView mGetAuthCode;
    @BindView(R.id.pageTitle)
    TextView mPageTitle;

    @BindView(R.id.login)
    TextView mLogin;
    @BindView(R.id.loading)
    ImageView mLoading;

    @BindView(R.id.authCodeArea)
    LinearLayout mAuthCodeArea;

    @BindView(R.id.weChatLogin)
    ImageView mWeChatLogin;
    @BindView(R.id.closePage)
    ImageView mClosePage;
    @BindView(R.id.weChatAvatar)
    ImageView mWeChatAvatar;
    @BindView(R.id.weChatName)
    TextView mWeChatName;
    @BindView(R.id.weChatArea)
    LinearLayout mWeChatArea;
    @BindView(R.id.contentArea)
    LinearLayout mContentArea;
    @BindView(R.id.agree)
    TextView mAgree;
    @BindView(R.id.agreeWrapper)
    LinearLayout mAgreeWrapper;
    @BindView(R.id.registerBtn)
    TextView mRegisterBtn;
    @BindView(R.id.password)
    PasswordEditText mPassword;
    @BindView(R.id.passLogin)
    TextView mPassLogin;
    @BindView(R.id.forgetPass)
    TextView mForgetPass;
    private KeyBoardHelper mKeyBoardHelper;

    private int mCounter;
    private boolean mFreezeObtainAuthCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        switchLoginMode();//默认登录应为密码登录

        translucentStatusBar();
        // 第一次登录不需要弹出软键盘
        mPhoneNumber.clearFocus();

        if (!TextUtils.isEmpty(LocalUser.getUser().getPhone())) {
            mPhoneNumber.setText(LocalUser.getUser().getPhone());
            formatPhoneNumber();
            mPhoneNumber.clearFocus();
            mGetAuthCode.setEnabled(checkObtainAuthCodeEnable());
        }
        mPhoneNumber.addTextChangedListener(mPhoneValidationWatcher);
        mAuthCode.addTextChangedListener(mValidationWatcher);
        mPassword.addTextChangedListener(mValidationWatcher);
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    KeyBoardUtils.closeKeyboard(mRootView);
                    return true;
                }
                return false;
            }
        });

        initListener();

        setKeyboardHelper();
    }

    private void initListener() {
        mPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!TextUtils.isEmpty(mPhoneNumber.getText().toString()) && hasFocus) {
                    mPhoneNumberClear.setVisibility(View.VISIBLE);
                } else {
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        mAuthCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
                mAuthCode.requestFocus();
            }
        });
        mAuthCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
                mPassword.requestFocus();
            }
        });
        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_to_bottom);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhoneNumber.removeTextChangedListener(mPhoneValidationWatcher);
        mAuthCode.removeTextChangedListener(mValidationWatcher);
        mPassword.removeTextChangedListener(mValidationWatcher);
        mKeyBoardHelper.onDestroy();
        mLoading.clearAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_REGISTER && resultCode == RESULT_OK) { // 注册成功 发送广播 以及 关闭页面
            postLogin();
        }

        if (requestCode == REQ_CODE_IMAGE_AUTH_CODE && resultCode == RESULT_OK) { // 发送图片验证码去 获取验证码 成功
            postAuthCodeRequested();
        }
    }


    private void setKeyboardHelper() {
        mKeyBoardHelper = new KeyBoardHelper(this);
        mKeyBoardHelper.onCreate();
        mKeyBoardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
    }

    private KeyBoardHelper.OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener = new KeyBoardHelper.OnKeyBoardStatusChangeListener() {

        @Override
        public void OnKeyBoardPop(int keyboardHeight) {
        }

        @Override
        public void OnKeyBoardClose(int oldKeyboardHeight) {
        }
    };

    private ValidationWatcher mPhoneValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            mValidationWatcher.afterTextChanged(s);

            formatPhoneNumber();

            mPhoneNumberClear.setVisibility(checkClearBtnVisible() ? View.VISIBLE : View.INVISIBLE);

            if (isAuthCodeLogin()) {
                if (getPhoneNumber().length() == 11) {
                    mPhoneNumber.clearFocus();
                    mAuthCode.requestFocus();
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }

                boolean authCodeEnable = checkObtainAuthCodeEnable();
                if (mGetAuthCode.isEnabled() != authCodeEnable) {
                    mGetAuthCode.setEnabled(authCodeEnable);
                }
            } else {
                if (getPhoneNumber().length() == 11) {
                    mPhoneNumber.clearFocus();
                    mAuthCode.requestFocus();
                    mPhoneNumberClear.setVisibility(View.INVISIBLE);
                }
            }

        }
    };

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean enable = checkSignInButtonEnable();
            if (enable != mLogin.isEnabled()) {
                mLogin.setEnabled(enable);
            }
        }
    };

    private boolean checkSignInButtonEnable() {
        String phone = getPhoneNumber();
        String authCode = mAuthCode.getText().toString().trim();
        String password = mPassword.getPassword();


        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            return false;
        }

        if (isAuthCodeLogin() && (TextUtils.isEmpty(authCode) || authCode.length() < 4)) {
            return false;
        }

        return !(!isAuthCodeLogin() && (TextUtils.isEmpty(password) || password.length() < 6));

    }

    private boolean checkClearBtnVisible() {
        String phone = mPhoneNumber.getText().toString();
        return !TextUtils.isEmpty(phone);
    }

    private void formatPhoneNumber() {
        String oldPhone = mPhoneNumber.getText().toString();
        String phoneNoSpace = oldPhone.replaceAll(" ", "");
        String newPhone = StrFormatter.getFormatPhoneNumber(phoneNoSpace);
        if (!newPhone.equalsIgnoreCase(oldPhone)) {
            mPhoneNumber.setText(newPhone);
            mPhoneNumber.setSelection(newPhone.length());
        }
    }

    private boolean checkObtainAuthCodeEnable() {
        String phone = getPhoneNumber();
        return (!TextUtils.isEmpty(phone) && phone.length() > 10 && !mFreezeObtainAuthCode);
    }

    private String getPhoneNumber() {
        return mPhoneNumber.getText().toString().trim().replaceAll(" ", "");
    }

    @OnClick({R.id.closePage, R.id.phoneNumberClear, R.id.getAuthCode, R.id.login, R.id.rootView, R.id.weChatLogin, R.id.agree, R.id.passLogin, R.id.registerBtn, R.id.forgetPass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.closePage:
                finish();
                break;
            case R.id.phoneNumberClear:
                mPhoneNumber.setText("");
                break;
            case R.id.getAuthCode:
                umengEventCount(UmengCountEventId.LOGIN_AUTHCODE);
                requestAuthCode();
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
                mAuthCode.requestFocus();
                break;
            case R.id.login:
                login();
                break;
            case R.id.weChatLogin:
                umengEventCount(UmengCountEventId.LOGIN_WECHAT);
                weChatLogin();
                break;
            case R.id.agree:
                Launcher.with(getActivity(), WebActivity.class)
                        .putExtra(WebActivity.EX_URL, Apic.url.WEB_URI_AGREEMENT)
                        .execute();
                break;
            case R.id.passLogin:
                switchLoginMode();
                break;
            case R.id.registerBtn:
                Launcher.with(getActivity(), RegisterActivity.class).executeForResult(REQ_CODE_REGISTER);
                break;
            case R.id.forgetPass:
                Launcher.with(getActivity(), InputPhoneActivity.class).execute();
                break;
            default:
                break;
        }
    }

    private void weChatLogin() {
        requestWeChatInfo();
    }

    @Override
    protected void bindSuccess() {
        Apic.requestWeChatLogin(getWeChatOpenid()).tag(TAG)
                .callback(new Callback<Resp<UserInfo>>() {
                    @Override
                    protected void onRespSuccess(Resp<UserInfo> resp) {
                        LocalUser.getUser().setUserInfo(resp.getData());
//                        ToastUtil.show(R.string.login_success);
                        postLogin();
                    }

                    @Override
                    protected void onRespFailure(Resp failedResp) {
                        if (failedResp.getCode() == Resp.CODE_NO_BIND_WE_CHAT) {
                            updateBindPhoneViews();
                        } else {
                            if (failedResp.getCode() == Resp.CODE_ACCOUNT_EXCEPTION) {
                                ToastUtil.show(R.string.this_account_forbid_login);
                            }
                            setWeChatOpenid(null);
                        }
                    }
                }).fireFreely();
    }

    @Override
    protected void bindFailure() {
        ToastUtil.show(R.string.cancel_login);
    }

    private void updateBindPhoneViews() {
        mPageTitle.setText(R.string.password_login);
        mAuthCodeArea.setVisibility(View.GONE);
        mPassword.setVisibility(View.VISIBLE);
        mPassLogin.setText(R.string.tel_auth_code_login);
        mAuthCode.setText("");
        if (!isAuthCodeLogin()) { // 当前是密码登录 -> 验证码登录
            mAuthCodeArea.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.GONE);
            mPassLogin.setText(R.string.tel_pass_login);
            mPassword.setPassword("");
        }
        stopScheduleJob();
        mFreezeObtainAuthCode = false;

        mWeChatLogin.setVisibility(View.GONE);
        mWeChatName.setText(getWeChatName());
        mWeChatArea.setVisibility(View.VISIBLE);
        GlideApp.with(getActivity()).load(getWeChatIconUrl())
                .placeholder(R.drawable.ic_default_news)
                .centerCrop()
                .into(mWeChatAvatar);

        mPhoneNumber.setText("");
        mPhoneNumber.clearFocus();
        mAuthCode.setText("");
        mAuthCode.clearFocus();
        mGetAuthCode.setText(R.string.get_auth_code);
        mGetAuthCode.setEnabled(false);
        mLogin.setText(getString(R.string.ok));
        mPageTitle.setText(getString(R.string.bind_phone));
        mAgreeWrapper.setVisibility(View.GONE);
        mClosePage.setImageResource(R.drawable.ic_tb_back_black);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins((int) Display.dp2Px(50, getResources()), (int) Display.dp2Px(32, getResources()),
                (int) Display.dp2Px(50, getResources()), 0);
        mContentArea.setLayoutParams(params);
    }

    private void login() {
        KeyBoardUtils.closeKeyboard(mLogin);

        final String phoneNumber = getPhoneNumber();
        final String authCode = mAuthCode.getText().toString().trim();
        final String passWord = md5Encrypt(mPassword.getPassword());

        mLogin.setText(R.string.login_ing);
        mLoading.setVisibility(View.VISIBLE);
        mLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading));
        if (isAuthCodeLogin()) {
            if (isWeChatLogin()) {
                Apic.requestAuthCodeLogin(phoneNumber, authCode, getWeChatOpenid(), getWeChatName(), getWeChatIconUrl(), getWeChatGender()).tag(TAG)
                        .callback(new Callback<Resp<UserInfo>>() {
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                resetLoginButton();
                            }

                            @Override
                            protected void onRespSuccess(Resp<UserInfo> resp) {
                                LocalUser.getUser().setUserInfo(resp.getData(), phoneNumber);
//                            ToastUtil.show(R.string.login_success);
                                postLogin();
                            }
                        }).fire();
            } else {
                Apic.requestAuthCodeLogin(phoneNumber, authCode).tag(TAG)
                        .callback(new Callback<Resp<UserInfo>>() {
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                resetLoginButton();
                            }

                            @Override
                            protected void onRespSuccess(Resp<UserInfo> resp) {
                                LocalUser.getUser().setUserInfo(resp.getData(), phoneNumber);
//                            ToastUtil.show(R.string.login_success);
                                postLogin();
                            }
                        }).fire();
            }
        } else {
            Apic.passLogin(phoneNumber, passWord).tag(TAG)
                    .callback(new Callback<Resp<UserInfo>>() {
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            resetLoginButton();
                        }

                        @Override
                        protected void onRespSuccess(Resp<UserInfo> resp) {
                            LocalUser.getUser().setUserInfo(resp.getData(), phoneNumber);
                            postLogin();
                        }

                        @Override
                        protected void onRespFailure(Resp failedResp) {
                            super.onRespFailure(failedResp);
                            if (failedResp.getCode() == CODE_NOT_SET_PASSWORD) {
                                showSwitchDialog();
                            } else {
                                ToastUtil.show(failedResp.getMsg());
                            }
                        }

                        @Override
                        protected boolean toastError() {
                            return false;
                        }
                    }).fire();
        }
    }

    private void showSwitchDialog() {
        SmartDialog.single(getActivity(), getString(R.string.switch_to_fast_login)).setPositive(R.string.ok, new SmartDialog.OnClickListener() {
            @Override
            public void onClick(Dialog dialog) {
                dialog.dismiss();
                switchLoginMode();
            }
        }).show();
    }


    private void resetLoginButton() {
        if (isBindPhone()) {
            mLogin.setText(getString(R.string.ok));
        } else if (isAuthCodeLogin()) {
            mLogin.setText(R.string.fast_login);
        } else {
            mLogin.setText(R.string.login);
        }
        mLoading.setVisibility(View.GONE);
        mLoading.clearAnimation();
    }

    private boolean isBindPhone() {
        return mPageTitle.getText().toString().equals(getString(R.string.bind_phone));
    }

    private void requestAuthCode() {
        final String phoneNumber = getPhoneNumber();
        Apic.getAuthCode(phoneNumber)
                .tag(TAG)
                .callback(new Callback<Resp<JsonObject>>() {
                    @Override
                    protected void onRespSuccess(Resp<JsonObject> resp) {
                        postAuthCodeRequested();
                    }

                    @Override
                    protected void onRespFailure(Resp failedResp) {
                        if (failedResp.getCode() == Resp.CODE_IMAGE_AUTH_CODE_REQUIRED) {
                            Launcher.with(getActivity(), ImageAuthCodeActivity.class)
                                    .putExtra(ExtraKeys.PHONE, phoneNumber)
                                    .executeForResult(REQ_CODE_IMAGE_AUTH_CODE);
                        } else {
                            super.onRespFailure(failedResp);
                        }
                    }
                }).fire();
    }

    private void postAuthCodeRequested() {
        mFreezeObtainAuthCode = true;
        startScheduleJob(1000);
        mCounter = 60;
        mGetAuthCode.setEnabled(false);
        mGetAuthCode.setText(getString(R.string._seconds, mCounter));
    }

    @Override
    public void onTimeUp(int count) {
        mCounter--;
        if (mCounter <= 0) {
            mFreezeObtainAuthCode = false;
            mGetAuthCode.setEnabled(true);
            mGetAuthCode.setText(R.string.obtain_auth_code_continue);
            stopScheduleJob();
        } else {
            mGetAuthCode.setText(getString(R.string._seconds, mCounter));
        }
    }

    private void switchLoginMode() {
        Log.e("zzz", "switchLoginMode");
        if (isAuthCodeLogin()) { // 当前是验证码登录 -> 密码登录
            mPageTitle.setText(R.string.password_login);
            mAuthCodeArea.setVisibility(View.GONE);
            mPassword.setVisibility(View.VISIBLE);
            mPassLogin.setText(R.string.tel_auth_code_login);
            mLogin.setText(R.string.login);

            mAuthCode.setText("");
            if (getPhoneNumber().length() == 11) {
                mPhoneNumber.clearFocus();
                mPassword.requestFocus();
            } else {
                mPhoneNumber.requestFocus();
            }
        } else {
            mPageTitle.setText(R.string.auth_code_login);
            mPassLogin.setText(R.string.tel_pass_login);
            mAuthCodeArea.setVisibility(View.VISIBLE);
            mPassword.setVisibility(View.GONE);
            mLogin.setText(R.string.fast_login);

            mGetAuthCode.setEnabled(checkObtainAuthCodeEnable());
            mPassword.setPassword("");

            if (getPhoneNumber().length() == 11) {
                mPhoneNumber.clearFocus();
                mAuthCode.requestFocus();
            } else {
                mPhoneNumber.requestFocus();
            }
        }
    }

    private boolean isAuthCodeLogin() {
        String pageTitle = mPageTitle.getText().toString();
        String authCodeLoginTitle = getString(R.string.auth_code_login);
        String bindPhone = getString(R.string.bind_phone);
        return !TextUtils.isEmpty(pageTitle) && (pageTitle.equals(authCodeLoginTitle) || pageTitle.equals(bindPhone));
    }
}
