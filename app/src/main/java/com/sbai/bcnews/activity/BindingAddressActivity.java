package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.ImageAuthCodeActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_ALIPAY;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_DIGITAL_COIN;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_TELEPHONE_CHARGE;

public class BindingAddressActivity extends BaseActivity {
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;


    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.bindingAddress)
    EditText mBindingAddress;
    @BindView(R.id.bindingName)
    TextView mBindingName;
    @BindView(R.id.bindingBtn)
    TextView mBindingBtn;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.userNameInput)
    EditText mUserNameInput;
    @BindView(R.id.userLayout)
    LinearLayout mUserLayout;
    @BindView(R.id.authCode)
    TextView mAuthCode;
    @BindView(R.id.authCodeInput)
    EditText mAuthCodeInput;
    @BindView(R.id.getAuthCode)
    TextView mGetAuthCode;
    @BindView(R.id.authCodeLayout)
    LinearLayout mAuthCodeLayout;
    @BindView(R.id.addressLine)
    View mAddressLine;
    @BindView(R.id.nameLine)
    View mNameLine;

    private int mAcceptType;
    private boolean mFreezeObtainAuthCode;
    private int mCounter;

    private String mBindingAddressData;
    private String mUserNameData;

    private boolean mIsModify;

    private ValidationWatcher mUserNameWatcher = new ValidationWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (mAcceptType == PAGE_ALIPAY) {
                boolean submitBtnEnabled = checkAliEnabled();
                if (mBindingBtn.isEnabled() != submitBtnEnabled) {
                    mBindingBtn.setEnabled(submitBtnEnabled);
                }
            } else if (mAcceptType == PAGE_TELEPHONE_CHARGE) {
                boolean enable = checkSignInButtonEnable();
                if (enable != mBindingBtn.isEnabled()) {
                    mBindingBtn.setEnabled(enable);
                }
            }
        }
    };

    private ValidationWatcher mPhoneValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (mAcceptType == PAGE_DIGITAL_COIN) {
                boolean enabled = checkCoinEnabled();
                if (enabled != mBindingBtn.isEnabled()) {
                    mBindingBtn.setEnabled(enabled);
                }
            } else if (mAcceptType == PAGE_ALIPAY) {
                mUserNameWatcher.afterTextChanged(s);
            } else {
                mValidationWatcher.afterTextChanged(s);
                mUserNameWatcher.afterTextChanged(s);

                if (getPhoneNumber().length() == 11) {
                    mUserName.clearFocus();
                    mAuthCode.requestFocus();
                }

                boolean authCodeEnable = checkObtainAuthCodeEnable();
                if (mGetAuthCode.isEnabled() != authCodeEnable) {
                    mGetAuthCode.setEnabled(authCodeEnable);
                }
            }

        }
    };

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable editable) {
            boolean enable = checkSignInButtonEnable();
            if (enable != mBindingBtn.isEnabled()) {
                mBindingBtn.setEnabled(enable);
            }
        }
    };

    private boolean checkSignInButtonEnable() {
        String phone = getPhoneNumber();
        String phoneName = mUserNameInput.getText().toString();
        String authCode = mAuthCodeInput.getText().toString().trim();

        if (TextUtils.isEmpty(phone) || phone.length() < 11) {
            return false;
        }

        if (TextUtils.isEmpty(phoneName)) {
            return false;
        }

        if ((TextUtils.isEmpty(authCode) || authCode.length() < 4)) {
            return false;
        }
        return true;

    }

    private boolean checkObtainAuthCodeEnable() {
        String phone = getPhoneNumber();
        return (!TextUtils.isEmpty(phone) && phone.length() > 10 && !mFreezeObtainAuthCode);
    }

    private boolean checkCoinEnabled() {
        return (!TextUtils.isEmpty(mBindingAddress.getText().toString()));
    }

    private boolean checkAliEnabled() {
        return (!TextUtils.isEmpty(mBindingAddress.getText().toString()) && !TextUtils.isEmpty(mUserNameInput.getText().toString()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_address);
        ButterKnife.bind(this);
        mAcceptType = getIntent().getIntExtra(ExtraKeys.BINDING_TYPE, PAGE_DIGITAL_COIN);
        initView();
        initData();
    }

    private void initData() {
        mBindingAddressData = getIntent().getStringExtra(ExtraKeys.BINDING_ADDRESS);
        mUserNameData = getIntent().getStringExtra(ExtraKeys.BINDING_USER_NAME);

        switch (mAcceptType) {
            case PAGE_DIGITAL_COIN:
                mTitleBar.setTitle(R.string.binding_currency_address);
                mBindingName.setText(R.string.currency_address);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
                    mBindingAddress.setSelection(mBindingAddressData.length());
                    InputFilter[] filters = {new InputFilter.LengthFilter(200)};
                    mBindingAddress.setFilters(filters);
                    mIsModify = true;
                }
                break;
            case PAGE_ALIPAY:
                mAddressLine.setVisibility(View.VISIBLE);
                mTitleBar.setTitle(R.string.binding_ali_pay_account);
                mBindingName.setText(R.string.ali_pay_address);
                mBindingAddress.setHint(R.string.please_input_your_ali_pay_account);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
                    mBindingAddress.setSelection(mBindingAddressData.length());
                    mIsModify = true;
                }
                mUserLayout.setVisibility(View.VISIBLE);
                mUserName.setText(R.string.ali_pay_user_name);
                mUserNameInput.setHint(R.string.please_input_ali_pay_user_name);
                if (!TextUtils.isEmpty(mUserNameData)) {
                    mUserNameInput.setText(mUserNameData);
                }
                break;
            case PAGE_TELEPHONE_CHARGE:
                mAddressLine.setVisibility(View.VISIBLE);
                mNameLine.setVisibility(View.VISIBLE);
                mTitleBar.setTitle(R.string.binding_tel);
                mBindingName.setText(R.string.tel);
                mBindingAddress.setHint(R.string.please_input_your_tel);
                mBindingAddress.setInputType(InputType.TYPE_CLASS_NUMBER);
                InputFilter[] filters = {new InputFilter.LengthFilter(11)};
                mBindingAddress.setFilters(filters);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
                    mBindingAddress.setSelection(mBindingAddressData.length());
                    mIsModify = true;
                }
                mUserLayout.setVisibility(View.VISIBLE);
                mUserName.setText(R.string.tel_user_name);
                mUserNameInput.setHint(R.string.please_input_tel_user_name);
                if (!TextUtils.isEmpty(mUserNameData)) {
                    mUserNameInput.setText(mUserNameData);
                }
                mAuthCodeLayout.setVisibility(View.VISIBLE);
                mAuthCode.setText(R.string.auth_code);
                mAuthCodeInput.setHint(R.string.please_input_auth_code);
                break;
        }
    }

    private void initView() {
        mBindingAddress.addTextChangedListener(mPhoneValidationWatcher);
        mUserNameInput.addTextChangedListener(mUserNameWatcher);
        mAuthCodeInput.addTextChangedListener(mValidationWatcher);
    }

    @OnClick({R.id.bindingBtn, R.id.getAuthCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bindingBtn:
                submitBindingData();
                break;
            case R.id.getAuthCode:
                requestAuthCode();
                mAuthCode.requestFocus();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBindingAddress.removeTextChangedListener(mPhoneValidationWatcher);
        mAuthCodeInput.removeTextChangedListener(mValidationWatcher);
        mUserNameInput.removeTextChangedListener(mUserNameWatcher);
    }

    private void submitBindingData() {
        if (mAcceptType == PAGE_DIGITAL_COIN) {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString())) {
                Apic.submitCoinAddress(mBindingAddress.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                        showToast();
                    }
                }).fireFreely();
            }
        } else if (mAcceptType == PAGE_ALIPAY) {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString()) && !TextUtils.isEmpty(mUserNameInput.getText().toString())) {
                Apic.submitAliPayAddress(mBindingAddress.getText().toString(), mUserNameInput.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                        showToast();
                    }
                }).fireFreely();
            }
        } else {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString()) && !TextUtils.isEmpty(mUserNameInput.getText().toString()) && !TextUtils.isEmpty(mAuthCodeInput.getText().toString())) {
                Apic.submitTelephoneAddress(mBindingAddress.getText().toString(), mUserNameInput.getText().toString(), mAuthCodeInput.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                        showToast();
                    }
                }).fireFreely();
            }
        }
    }

    private void showToast() {
        if (mIsModify) {
            ToastUtil.show(R.string.modify_success);
        } else {
            ToastUtil.show(R.string.binding_success);
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

    private String getPhoneNumber() {
        return mBindingAddress.getText().toString().trim().replaceAll(" ", "");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_IMAGE_AUTH_CODE && resultCode == RESULT_OK) { // 发送图片验证码去 获取验证码 成功
            postAuthCodeRequested();
        }
    }
}
