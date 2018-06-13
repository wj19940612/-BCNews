package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.KeyBoardUtils;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.PasswordEditText;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPassActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.pageTitle)
    TextView mPageTitle;
    @BindView(R.id.oldPassword)
    PasswordEditText mOldPassword;
    @BindView(R.id.password)
    PasswordEditText mPassword;
    @BindView(R.id.complete)
    TextView mComplete;

    private boolean mHasLoginPassword;

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
        setContentView(R.layout.activity_modify_pass);
        ButterKnife.bind(this);
        initData(getIntent());
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPassword.removeTextChangedListener(mValidationWatcher);
        mOldPassword.removeTextChangedListener(mValidationWatcher);
    }

    private void initData(Intent intent) {
        mHasLoginPassword = intent.getBooleanExtra(ExtraKeys.HAS_LOGIN_PSD, false);
    }

    private void initView() {
        if (mHasLoginPassword) { // modify password view
            mTitleBar.setTitle(R.string.modify_pass);
            mOldPassword.setHint(R.string.old_password);
            mPassword.setHint(R.string.new_password);

            mOldPassword.addTextChangedListener(mValidationWatcher);
            mPassword.addTextChangedListener(mValidationWatcher);

            mOldPassword.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOldPassword.requestFocus();
                    KeyBoardUtils.openKeyBoard(mOldPassword.getEditText());
                }
            }, 200);

        } else {
            mTitleBar.setTitle(R.string.set_login_password);
            mOldPassword.setHint(R.string.set_login_password);
            mPassword.setHint(R.string.set_login_password_again);
            mPassword.addTextChangedListener(mValidationWatcher);

            mPassword.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mOldPassword.requestFocus();
                    KeyBoardUtils.openKeyBoard(mPassword.getEditText());
                }
            }, 200);
        }
    }

    private boolean checkCompleteButtonEnable() {
        if (mHasLoginPassword) {
            String password = mPassword.getPassword();
            String oldPassword = mOldPassword.getPassword();
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                return false;
            }

            if (TextUtils.isEmpty(oldPassword) || oldPassword.length() < 6) {
                return false;
            }
        } else {
            String password = mOldPassword.getPassword();
            String againPassword = mPassword.getPassword();
            if (TextUtils.isEmpty(password) || password.length() < 6 || TextUtils.isEmpty(againPassword) || againPassword.length() < 6) {
                return false;
            }
            return password.equals(againPassword);
        }
        return true;
    }

    @OnClick({R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.complete:
                //TODO 修改密码或者更新密码
                break;
        }
    }
}
