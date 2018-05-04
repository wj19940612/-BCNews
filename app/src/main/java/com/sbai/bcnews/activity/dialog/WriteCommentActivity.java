package com.sbai.bcnews.activity.dialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteCommentActivity extends DialogBaseActivity {

    @BindView(R.id.commentInput)
    EditText mCommentInput;
    @BindView(R.id.submitComment)
    TextView mSubmitComment;

    private WriteComment mWriteComment;
    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);
        ButterKnife.bind(this);

        initData();


        mCommentInput.addTextChangedListener(mValidationWatcher);
    }

    private void initData() {
        mWriteComment = getIntent().getParcelableExtra(ExtraKeys.DATA);
        mUserName = getIntent().getStringExtra(ExtraKeys.TAG);

        if (!TextUtils.isEmpty(mUserName)) {
            mCommentInput.setHint(getString(R.string.review_user, mUserName));
        }
    }

    private ValidationWatcher mValidationWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            boolean submitEnable = checkSubmitEnable();
            if (mSubmitComment.isEnabled() != submitEnable) {
                mSubmitComment.setEnabled(submitEnable);
            }
        }
    };

    private boolean checkSubmitEnable() {
        return !TextUtils.isEmpty(mCommentInput.getText());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommentInput.removeTextChangedListener(mValidationWatcher);
    }

    @Override
    protected float getWidthRatio() {
        return 1;
    }

    @Override
    protected int getWindowGravity() {
        return Gravity.BOTTOM;
    }

    @OnClick(R.id.submitComment)
    public void onViewClicked() {
        if (mWriteComment != null) {
            mWriteComment.setContent(mCommentInput.getText().toString());
            Apic.submitComment(mWriteComment)
                    .tag(TAG)
                    .callback(new Callback<Resp<Object>>() {
                        @Override
                        protected void onRespSuccess(Resp<Object> resp) {
                            ToastUtil.show("" + resp.getMsg());
                        }
                    })
                    .fire();
        }
    }
}
