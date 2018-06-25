package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.ImageAuthCodeActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.KeyBoardHelper;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrFormatter;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.PasswordEditText;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;

    @BindView(R.id.rootView)
    LinearLayout mRootView;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.phoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.phoneNumberClear)
    ImageView mPhoneNumberClear;
    @BindView(R.id.authCode)
    EditText mAuthCode;
    @BindView(R.id.getAuthCode)
    TextView mGetAuthCode;
    @BindView(R.id.authCodeArea)
    LinearLayout mAuthCodeArea;
    @BindView(R.id.password)
    PasswordEditText mPassword;
    @BindView(R.id.agreeBtn)
    ImageView mAgreeBtn;
    @BindView(R.id.agree)
    TextView mAgree;
    @BindView(R.id.agreeWrapper)
    LinearLayout mAgreeWrapper;
    @BindView(R.id.register)
    TextView mRegister;
    @BindView(R.id.loading)
    ImageView mLoading;

    private KeyBoardHelper mKeyBoardHelper;
    private boolean mFreezeObtainAuthCode;
    private int mCounter;


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

            if (getPhoneNumber().length() == 11) {
                mPhoneNumber.clearFocus();
                mAuthCode.requestFocus();
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
            }

            if (getPhoneNumber().length() == 11) {
                mPhoneNumber.clearFocus();
                mAuthCode.requestFocus();
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
            }

            boolean authCodeEnable = checkObtainAuthCodeEnable();
            if (mGetAuthCode.isEnabled() != authCodeEnable) {
                mGetAuthCode.setEnabled(authCodeEnable);
            }
        }
    };

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean enable = checkSignInButtonEnable();
            if (enable != mRegister.isEnabled()) {
                mRegister.setEnabled(enable);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mAgreeBtn.setSelected(true);
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

    private void setKeyboardHelper() {
        mKeyBoardHelper = new KeyBoardHelper(this);
        mKeyBoardHelper.onCreate();
        mKeyBoardHelper.setOnKeyBoardStatusChangeListener(onKeyBoardStatusChangeListener);
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

    private boolean checkClearBtnVisible() {
        String phone = mPhoneNumber.getText().toString();
        return !TextUtils.isEmpty(phone);
    }

    private String getPhoneNumber() {
        return mPhoneNumber.getText().toString().trim().replaceAll(" ", "");
    }

    private boolean checkObtainAuthCodeEnable() {
        String phone = getPhoneNumber();
        return (!TextUtils.isEmpty(phone) && phone.length() > 10 && !mFreezeObtainAuthCode);
    }

    private boolean checkSignInButtonEnable() {
        String phone = getPhoneNumber();
        String authCode = mAuthCode.getText().toString().trim();
        String password = mPassword.getPassword();


        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            return false;
        }

        if (TextUtils.isEmpty(authCode) || authCode.length() < 4) {
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            return false;
        }

        return mAgreeBtn.isSelected();

    }

    @OnClick({R.id.phoneNumberClear, R.id.getAuthCode, R.id.agreeBtn, R.id.register,R.id.agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phoneNumberClear:
                mPhoneNumber.setText("");
                break;
            case R.id.getAuthCode:
                requestAuthCode();
                mPhoneNumberClear.setVisibility(View.INVISIBLE);
                mAuthCode.requestFocus();
                break;
            case R.id.agreeBtn:
                mAgreeBtn.setSelected(!mAgreeBtn.isSelected());
                boolean enable = checkSignInButtonEnable();
                if (enable != mRegister.isEnabled()) {
                    mRegister.setEnabled(enable);
                }
                break;
            case R.id.register:
                register();
                break;
            case R.id.agree:
                Launcher.with(getActivity(), WebActivity.class)
                        .putExtra(WebActivity.EX_URL, Apic.url.WEB_URI_AGREEMENT)
                        .execute();
        }
    }

    private void requestAuthCode() {
        final String phoneNumber = getPhoneNumber();
        Apic.getAuthCode(phoneNumber)
                .tag(TAG).indeterminate(this)
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

    private void register() {
        final String phone = getPhoneNumber();
        String password = md5Encrypt(mPassword.getPassword());

        mLoading.setVisibility(View.VISIBLE);
        mLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading));
        Apic.register(getPhoneNumber(), mAuthCode.getText().toString().trim(), password).tag(TAG).callback(new Callback2D<Resp<UserInfo>, UserInfo>() {

            @Override
            protected void onRespSuccessData(UserInfo data) {
                if (data != null) {
                    LocalUser.getUser().setUserInfo(data, phone);
                }
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                requestRegisterButton();
            }
        }).fire();
    }

    private void requestRegisterButton() {
        mLoading.setVisibility(View.GONE);
        mLoading.clearAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_IMAGE_AUTH_CODE && resultCode == RESULT_OK) { // 发送图片验证码去 获取验证码 成功
            postAuthCodeRequested();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhoneNumber.addTextChangedListener(mPhoneValidationWatcher);
        mAuthCode.addTextChangedListener(mValidationWatcher);
        mPassword.addTextChangedListener(mValidationWatcher);
        mLoading.clearAnimation();
    }
}
