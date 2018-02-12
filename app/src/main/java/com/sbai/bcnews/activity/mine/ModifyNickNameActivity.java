package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyNickNameActivity extends BaseActivity {

    private static final int maxLength = 16;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.userNameInput)
    EditText mUserNameInput;
    @BindView(R.id.clear)
    AppCompatImageView mClear;
    @BindView(R.id.input)
    LinearLayout mInput;
    @BindView(R.id.submitNickName)
    TextView mSubmitNickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modiify_nick_name);
        ButterKnife.bind(this);

        mUserNameInput.addTextChangedListener(mValidationWatcher);
        mUserNameInput.setFilters(new InputFilter[]{filter});

        String nickName = getIntent().getStringExtra(ExtraKeys.NICK_NAME);
        mUserNameInput.setText(nickName);

    }

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            boolean submitBenEnable = checkSubmitBenEnable();
            if (mSubmitNickName.isEnabled() != submitBenEnable) {
                mUserNameInput.setSelection(s.length());
                mSubmitNickName.setEnabled(submitBenEnable);
                if (submitBenEnable) {
                    mClear.setVisibility(View.VISIBLE);
                } else {
                    mClear.setVisibility(View.GONE);
                }
            }
        }
    };

    private boolean checkSubmitBenEnable() {
        String s = mUserNameInput.getText().toString();
        return !TextUtils.isEmpty(s);
    }

    /**
     * 中文按照2个字符算  英文按1个字符
     */
    private static InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence src, int start, int end, Spanned dest, int dstart, int dend) {
            int dindex = 0;
            int count = 0;

            while (count <= maxLength && dindex < dest.length()) {
                char c = dest.charAt(dindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLength) {
                return dest.subSequence(0, dindex - 1);
            }

            int sindex = 0;
            while (count <= maxLength && sindex < src.length()) {
                char c = src.charAt(sindex++);
                if (c < 128) {
                    count = count + 1;
                } else {
                    count = count + 2;
                }
            }

            if (count > maxLength) {
                sindex--;
            }

            return src.subSequence(0, sindex);
        }
    };


    @OnClick({R.id.clear, R.id.submitNickName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mUserNameInput.setText("");
                break;
            case R.id.submitNickName:
                submitNickName();
                break;
        }
    }

    private void submitNickName() {
        final String nickName = mUserNameInput.getText().toString();
        Apic.submitNickName(nickName)
                .tag(TAG)
                .indeterminate(this)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        ToastUtil.show(R.string.update_success);
                        UserInfo userInfo = LocalUser.getUser().getUserInfo();
                        userInfo.setUserName(nickName);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .fire();
    }
}
