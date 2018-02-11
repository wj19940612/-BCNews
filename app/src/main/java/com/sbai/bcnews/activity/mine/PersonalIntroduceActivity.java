package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
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

        mPersonalIntroduce.addTextChangedListener(mValidationWatcher);
        mPersonalIntroduce.setText(introduce);
    }

    @OnClick(R.id.submitIntroduce)
    public void onViewClicked() {
        final String s = mPersonalIntroduce.getText().toString();
        Apic.submitUserIntroduce(s)
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
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
                mSubmitIntroduce.setEnabled(submitEnable);
            }
        }
    };

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
