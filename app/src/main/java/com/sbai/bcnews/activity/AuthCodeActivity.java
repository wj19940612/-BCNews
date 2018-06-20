package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
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
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.PasswordEditText;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthCodeActivity extends BaseActivity {
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.pageTitle)
    TextView mPageTitle;
    @BindView(R.id.authCode)
    EditText mAuthCode;
    @BindView(R.id.getAuthCode)
    TextView mGetAuthCode;
    @BindView(R.id.authCodeArea)
    LinearLayout mAuthCodeArea;
    @BindView(R.id.password)
    PasswordEditText mPassword;
    @BindView(R.id.complete)
    TextView mComplete;
    @BindView(R.id.rootView)
    LinearLayout mRootView;
    @BindView(R.id.loading)
    ImageView mLoading;

    private String mPhoneNumber;

    private int mCounter;
    private boolean mFreezeObtainAuthCode;

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean enable = checkCompleteButtonEnable();
            if (enable != mComplete.isEnabled()) {
                mComplete.setEnabled(enable);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_code);
        ButterKnife.bind(this);

        initData();

        mAuthCode.addTextChangedListener(mValidationWatcher);
        mPassword.addTextChangedListener(mValidationWatcher);
        mGetAuthCode.performClick();

        mAuthCode.postDelayed(new Runnable() {
            @Override
            public void run() {
                mGetAuthCode.clearFocus();
                mAuthCode.requestFocus();
                KeyBoardUtils.openKeyBoard(mAuthCode);
            }
        }, 200);
    }

    private void initData() {
        mPhoneNumber = getIntent().getStringExtra(ExtraKeys.PHONE);
    }

    private boolean checkCompleteButtonEnable() {
        String authCode = mAuthCode.getText().toString().trim();
        String password = mPassword.getPassword();

        if (TextUtils.isEmpty(authCode) || authCode.length() < 4) {
            return false;
        }

        return !(TextUtils.isEmpty(password) || password.length() < 6);

    }

    @OnClick({R.id.getAuthCode, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getAuthCode:
                requestAuthCode();
                break;
            case R.id.complete:
                modifyPassword();
                break;
        }
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

    private void modifyPassword(){
        final String phone = getPhoneNumber();
        final String code = mAuthCode.getText().toString().trim();
        final String password = md5Encrypt(mPassword.getPassword());
        mLoading.setVisibility(View.VISIBLE);
        mLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading));
        Apic.forgetPass(phone,code,password).tag(TAG).callback(new Callback<Resp>() {
            @Override
            protected void onRespSuccess(Resp resp) {
                ToastUtil.show(R.string.update_success);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                resetButton();
            }
        }).fire();
    }

    private void resetButton() {
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

    private String getPhoneNumber() {
        return mPhoneNumber.trim().replaceAll(" ", "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuthCode.removeTextChangedListener(mValidationWatcher);
        mPassword.removeTextChangedListener(mValidationWatcher);
        mLoading.clearAnimation();
    }
}
