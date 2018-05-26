package com.sbai.bcnews.activity.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.DialogBaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.model.news.WriteCommentResponse;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteCommentActivity extends DialogBaseActivity {

    public static final int REQ_CODE_WRITE_VIEWPOINT_FOR_NEWS = 7211;//写一级观点  为新闻写观点
    public static final int REQ_CODE_WRITE_COMMENT_FOR_VIEWPOINT = 7311;//写二级观点  为新闻的观点写评论
    public static final int REQ_CODE_WRITE_VIEWPOINT_FOR_COMMENT = 7411;//写3级观点  为2及观点写评论
    public static final int REQ_CODE_WRITE_REPLY_FOR_COMMENT = 7511;//写3级回复  为3及观点写回复


    public static final int REQ_CODE_WRITE_COMMENT_FOR_MINE_REPLY = 7611;//我的页面的评论


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
            String content = mCommentInput.getText().toString();
            content=content.replaceAll("\n"," ");
            mWriteComment.setContent(content);
            Apic.submitComment(mWriteComment)
                    .tag(TAG)
                    .callback(new Callback2D<Resp<WriteCommentResponse>,WriteCommentResponse>() {
                        @Override
                        protected void onRespSuccessData(WriteCommentResponse data) {
                            ToastUtil.show(R.string.publish_success);
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKeys.DATA, data);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    })
                    .fire();
        }
    }
}
