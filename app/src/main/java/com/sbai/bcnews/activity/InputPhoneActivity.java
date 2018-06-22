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
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrFormatter;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputPhoneActivity extends BaseActivity {
    private static final int REQ_CODE_IMAGE_AUTH_CODE = 889;

    public static final int PAGE_TYPE_FORGET_PSD = 108;
    public static final int TYPE_HAS_ACCOUNT = 1;
    public static final int TYPE_NO_ACCOUNT = 0;

    private static final int REQ_CODE_SUB_OPERATION = 999;


    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.pageTitle)
    TextView mPageTitle;
    @BindView(R.id.phoneNumber)
    EditText mPhoneNumber;
    @BindView(R.id.phoneNumberClear)
    ImageView mPhoneNumberClear;
    @BindView(R.id.next)
    TextView mNext;
    @BindView(R.id.rootView)
    LinearLayout mRootView;
    @BindView(R.id.loading)
    ImageView mLoading;

    private int mCounter;
    private boolean mFreezeObtainAuthCode;

    private ValidationWatcher mPhoneValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {

            formatPhoneNumber();

            mPhoneNumberClear.setVisibility(checkClearBtnVisible() ? View.VISIBLE : View.INVISIBLE);

            boolean enable = checkNextButtonEnable();
            if (enable != mNext.isEnabled()) {
                mNext.setEnabled(enable);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone);
        ButterKnife.bind(this);

        mPhoneNumber.clearFocus();

        if (!TextUtils.isEmpty(LocalUser.getUser().getPhone())) {
            mPhoneNumber.setText(LocalUser.getUser().getPhone());
            formatPhoneNumber();
            mPhoneNumber.clearFocus();
            mNext.setEnabled(checkNextButtonEnable());
        }

        mPhoneNumber.requestFocus();
        mPhoneNumber.addTextChangedListener(mPhoneValidationWatcher);

        mPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!TextUtils.isEmpty(mPhoneNumber.getText().toString()) && hasFocus) {
                    mPhoneNumberClear.setVisibility(View.VISIBLE);
                }
            }
        });

        mPhoneNumber.postDelayed(new Runnable() {
            @Override
            public void run() {
                KeyBoardUtils.openKeyBoard(mPhoneNumber);
            }
        }, 200);
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

    private boolean checkNextButtonEnable() {
        String phone = getPhoneNumber();
        return !(TextUtils.isEmpty(phone) || phone.length() < 11);
    }

    private String getPhoneNumber() {
        return mPhoneNumber.getText().toString().trim().replaceAll(" ", "");
    }

    @OnClick({R.id.phoneNumberClear, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.phoneNumberClear:
                mPhoneNumber.setText("");
                break;
            case R.id.next:
                checkPhoneAndNext();
                break;
        }
    }

    private void checkPhoneAndNext() {
        final String phone = getPhoneNumber();
        mLoading.setVisibility(View.VISIBLE);
        mLoading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.loading));
        Apic.checkPhone(phone, TYPE_HAS_ACCOUNT).tag(TAG).callback(new Callback<Resp>() {
            @Override
            protected void onRespSuccess(Resp resp) {
                Launcher.with(InputPhoneActivity.this, AuthCodeActivity.class).putExtra(ExtraKeys.PHONE, phone).executeForResult(REQ_CODE_SUB_OPERATION);
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
    protected void onDestroy() {
        super.onDestroy();
        mPhoneNumber.removeTextChangedListener(mPhoneValidationWatcher);
        mLoading.clearAnimation();
    }

    //    private void postAuthCodeRequested() {
//        mFreezeObtainAuthCode = true;
//        startScheduleJob(1000);
//        mCounter = 60;
//    }
//
//    @Override
//    public void onTimeUp(int count) {
//        mCounter--;
//        if (mCounter <= 0) {
//            mFreezeObtainAuthCode = false;
//            stopScheduleJob();
//        }
//    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SUB_OPERATION && resultCode == RESULT_OK) { // 修改密码成功
            finish();
        }
    }
}
