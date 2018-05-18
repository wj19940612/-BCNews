package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConversionResultActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.conversionContent)
    TextView mConversionContent;
    @BindView(R.id.resultType)
    TextView mResultType;
    @BindView(R.id.conversionType)
    TextView mConversionType;
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    @BindView(R.id.lookDetail)
    TextView mLookDetail;

    private String mConversionValue;
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_result);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mConversionValue = getIntent().getStringExtra(ExtraKeys.CONVERSION_CONTENT);
        mType = getIntent().getStringExtra(ExtraKeys.CONVERSION_TYPE);

        mConversionContent.setText(mConversionValue);
        mConversionType.setText(mType);
    }

    @OnClick({R.id.sureBtn, R.id.lookDetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sureBtn:
                break;
            case R.id.lookDetail:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
