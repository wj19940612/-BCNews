package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BindingAddressActivity extends BaseActivity {
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;

    public static final int BINDING_TYPE_DIGITAL = 0;
    public static final int BINDING_TYPE_ALI_PAY = 1;
    public static final int BINDING_TYPE_TEL = 2;


    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.bindingAddress)
    EditText mBindingAddress;
    @BindView(R.id.bindingName)
    EditText mBindingName;
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

    private int mBindingType;
    private boolean mFreezeObtainAuthCode;
    private int mCounter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_address);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mBindingType = getIntent().getIntExtra(ExtraKeys.BINDING_TYPE, BINDING_TYPE_DIGITAL);

        switch (mBindingType) {
            case BINDING_TYPE_DIGITAL:
                mTitleBar.setTitle(R.string.binding_currency_address);
                mBindingName.setText(R.string.currency_address);
                break;
            case BINDING_TYPE_ALI_PAY:
                mTitleBar.setTitle(R.string.binding_ali_pay_account);
                mBindingName.setText(R.string.ali_pay_address);
                mBindingAddress.setHint(R.string.please_input_your_ali_pay_account);
                mUserLayout.setVisibility(View.VISIBLE);
                mUserName.setText(R.string.ali_pay_user_name);
                mUserName.setHint(R.string.please_input_ali_pay_user_name);
                break;
            case BINDING_TYPE_TEL:
                mTitleBar.setTitle(R.string.binding_tel);
                mBindingName.setText(R.string.tel);
                mBindingAddress.setHint(R.string.please_input_your_tel);
                mUserLayout.setVisibility(View.VISIBLE);
                mUserName.setText(R.string.tel_user_name);
                mUserName.setHint(R.string.please_input_tel_user_name);
                mAuthCodeLayout.setVisibility(View.VISIBLE);
                mAuthCode.setText(R.string.auth_code);
                mAuthCodeInput.setHint(R.string.please_input_auth_code);
                break;
        }
    }

    @OnClick({R.id.bindingBtn,R.id.getAuthCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bindingBtn:
                break;
            case R.id.getAuthCode:
                requestAuthCode();
                mAuthCode.requestFocus();
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
