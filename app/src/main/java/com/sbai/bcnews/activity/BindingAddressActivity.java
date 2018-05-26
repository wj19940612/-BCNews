package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private int mAcceptType;
    private boolean mFreezeObtainAuthCode;
    private int mCounter;

    private String mBindingAddressData;
    private String mUserNameData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_address);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mAcceptType = getIntent().getIntExtra(ExtraKeys.BINDING_TYPE, PAGE_DIGITAL_COIN);
        mBindingAddressData = getIntent().getStringExtra(ExtraKeys.BINDING_ADDRESS);
        mUserNameData = getIntent().getStringExtra(ExtraKeys.BINDING_USER_NAME);

        switch (mAcceptType) {
            case PAGE_DIGITAL_COIN:
                mTitleBar.setTitle(R.string.binding_currency_address);
                mBindingName.setText(R.string.currency_address);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
                }
                break;
            case PAGE_ALIPAY:
                mTitleBar.setTitle(R.string.binding_ali_pay_account);
                mBindingName.setText(R.string.ali_pay_address);
                mBindingAddress.setHint(R.string.please_input_your_ali_pay_account);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
                }
                mUserLayout.setVisibility(View.VISIBLE);
                mUserName.setText(R.string.ali_pay_user_name);
                mUserNameInput.setHint(R.string.please_input_ali_pay_user_name);
                if (!TextUtils.isEmpty(mUserNameData)) {
                    mUserNameInput.setText(mUserNameData);
                }
                break;
            case PAGE_TELEPHONE_CHARGE:
                mTitleBar.setTitle(R.string.binding_tel);
                mBindingName.setText(R.string.tel);
                mBindingAddress.setHint(R.string.please_input_your_tel);
                if (!TextUtils.isEmpty(mBindingAddressData)) {
                    mBindingAddress.setText(mBindingAddressData);
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

    private void submitBindingData() {
        if (mAcceptType == PAGE_DIGITAL_COIN) {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString())) {
                Apic.submitCoinAddress(mBindingAddress.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                    }
                }).fireFreely();
            }
        } else if (mAcceptType == PAGE_ALIPAY) {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString()) && !TextUtils.isEmpty(mUserNameInput.getText().toString())) {
                Apic.submitAliPayAddress(mBindingAddress.getText().toString(), mUserNameInput.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                    }
                }).fireFreely();
            }
        } else {
            if (!TextUtils.isEmpty(mBindingAddress.getText().toString()) && !TextUtils.isEmpty(mUserNameInput.getText().toString()) && !TextUtils.isEmpty(mAuthCodeInput.getText().toString())) {
                Apic.submitTelephoneAddress(mBindingAddress.getText().toString(), mUserNameInput.getText().toString(), mAuthCodeInput.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
                    @Override
                    protected void onRespSuccess(Resp resp) {
                        finish();
                    }
                }).fireFreely();
            }
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
}
