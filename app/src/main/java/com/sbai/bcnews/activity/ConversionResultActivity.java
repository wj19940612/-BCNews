package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.ConversionGoodsFragment;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_ALIPAY;
import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_DIGITAL_COIN;
import static com.sbai.bcnews.fragment.ConversionGoodsFragment.PAGE_TELEPHONE_CHARGE;

public class ConversionResultActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.conversionPrice)
    TextView mConversionPrice;
    @BindView(R.id.resultType)
    TextView mResultType;
    @BindView(R.id.conversionName)
    TextView mConversionName;
    @BindView(R.id.sureBtn)
    TextView mSureBtn;
    @BindView(R.id.lookDetail)
    TextView mLookDetail;

    private double mPrice;
    private String mName;
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_result);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mType = getIntent().getIntExtra(ExtraKeys.CONVERSION_TYPE, PAGE_DIGITAL_COIN);
        mPrice = getIntent().getDoubleExtra(ExtraKeys.CONVERSION_PRICE, 0);
        mName = getIntent().getStringExtra(ExtraKeys.CONVERSION_NAME);

        mConversionPrice.setText(getString(R.string.qkc_x, FinanceUtil.trimTrailingZero(mPrice)));
        mConversionName.setText(mName);
        switch (mType){
            case PAGE_DIGITAL_COIN:
                mTitleBar.setTitle(R.string.digital_coin_conversion);
                break;
            case PAGE_ALIPAY:
                mTitleBar.setTitle(R.string.ali_pay_conversion);
                break;
            case PAGE_TELEPHONE_CHARGE:
                mTitleBar.setTitle(R.string.telephone_fee_conversion);
                break;
        }
    }

    @OnClick({R.id.sureBtn, R.id.lookDetail})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sureBtn:
                finish();
                break;
            case R.id.lookDetail:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }
}
