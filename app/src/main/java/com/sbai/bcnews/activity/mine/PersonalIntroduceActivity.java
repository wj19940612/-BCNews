package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
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

public class PersonalIntroduceActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.personalIntroduce)
    EditText mPersonalIntroduce;
    @BindView(R.id.wordsNumber)
    TextView mWordsNumber;
    @BindView(R.id.submitIntroduce)
    TextView mSubmitIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_introduce);
        ButterKnife.bind(this);
        String introduce = getIntent().getStringExtra(ExtraKeys.PERSONAL_INTRODUCE);


        if (LocalUser.getUser().getUserInfo().isAuthor()) {
            mPersonalIntroduce.setEnabled(false);
            mSubmitIntroduce.setEnabled(false);
            mPersonalIntroduce.setText(LocalUser.getUser().getUserInfo().getAuthInfo());
            return;
        }

        mPersonalIntroduce.addTextChangedListener(mValidationWatcher);
        mPersonalIntroduce.setText(introduce);


    }

    @OnClick(R.id.submitIntroduce)
    public void onViewClicked() {
        final String s = mPersonalIntroduce.getText().toString();
        if (s.length() > 30) {
            ToastUtil.show(R.string.input_text_is_more_rich);
            return;
        }
        Apic.submitUserIntroduce(s)
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        ToastUtil.show(R.string.update_success);
                        UserInfo userInfo = LocalUser.getUser().getUserInfo();
                        userInfo.setIntroduction(s);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .fire();
    }

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {

            boolean submitEnable = checkSubmitEnable();
            if (mSubmitIntroduce.isEnabled() != submitEnable) {
                mPersonalIntroduce.setSelection(s.length());
                mSubmitIntroduce.setEnabled(submitEnable);
            }

            checkInputLengthIsLegal(s);
        }
    };

    private void checkInputLengthIsLegal(Editable s) {

        int enoughInputLength = 30 - s.length();
        if (enoughInputLength > -1) {
            mWordsNumber.setText(String.valueOf(enoughInputLength));
        } else {
            mWordsNumber.setText(String.valueOf(0));
        }

        if (s.length() > 30) {
            mWordsNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.redPrimary));
        } else {
            mWordsNumber.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_999));
        }
    }

    private boolean checkSubmitEnable() {
        String s = mPersonalIntroduce.getText().toString();
        return !TextUtils.isEmpty(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPersonalIntroduce.removeTextChangedListener(mValidationWatcher);
    }
}
