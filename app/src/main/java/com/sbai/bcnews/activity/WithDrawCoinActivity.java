package com.sbai.bcnews.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.CoinCount;
import com.sbai.bcnews.model.CoinDetail;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.ValidationWatcher;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.zxing.activity.CaptureActivity;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithDrawCoinActivity extends BaseActivity {

    public static final int CODE_SCAN = 10123;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.usable)
    TextView mUsable;
    @BindView(R.id.usableLayout)
    RelativeLayout mUsableLayout;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.addressLayout)
    RelativeLayout mAddressLayout;
    @BindView(R.id.count)
    TextView mCount;
    @BindView(R.id.allBtn)
    TextView mAllBtn;
    @BindView(R.id.countLayout)
    RelativeLayout mCountLayout;
    @BindView(R.id.statement)
    TextView mStatement;
    @BindView(R.id.scanBtn)
    ImageView mScanBtn;
    @BindView(R.id.addressEdit)
    EditText mAddressEdit;
    @BindView(R.id.countEdit)
    EditText mCountEdit;
    @BindView(R.id.confirm)
    TextView mConfirm;
    @BindView(R.id.usableCount)
    TextView mUsableCount;

    private double mUsableCoin;
    private String mType;

    private double mMinExtractCoin = -1;

    private ValidationWatcher mAddressEditWatcher = new ValidationWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            mAddressEdit.setSelection(s.toString().length());
            checkoutBtnStatus();
        }
    };

    private ValidationWatcher mCountEditWatcher = new ValidationWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            mCountEdit.setSelection(s.toString().length());
            checkoutBtnStatus();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_coin);
        ButterKnife.bind(this);
        initView();
        initData();
        initTitle();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAddressEdit.removeTextChangedListener(mAddressEditWatcher);
        mCountEdit.removeTextChangedListener(mCountEditWatcher);
    }

    private void initView() {
        mAddressEdit.addTextChangedListener(mAddressEditWatcher);
        mCountEdit.addTextChangedListener(mCountEditWatcher);
    }

    private void initTitle() {
        mTitleBar.setTitle(getString(R.string.get_coin_x, mType));
    }

    private void initData() {
        mType = getIntent().getStringExtra(ExtraKeys.TYPE);
    }

    private void loadData() {
        Apic.requestCoinDetail(mType).tag(TAG).callback(new Callback2D<Resp<CoinDetail>, CoinDetail>() {
            @Override
            protected void onRespSuccessData(CoinDetail data) {
                updateCoinDetail(data);
            }
        }).fireFreely();

        Apic.requestCoinCount(mType).tag(TAG).callback(new Callback2D<Resp<CoinCount>, CoinCount>() {
            @Override
            protected void onRespSuccessData(CoinCount data) {
                if (data != null) {
                    mUsableCoin = data.getAbleCoin();
                    mUsableCount.setText(String.valueOf(mUsableCoin) + "   " + mType);
                }
            }
        }).fireFreely();
    }

    private void updateCoinDetail(CoinDetail data) {
        if (data != null) {
            mMinExtractCoin = data.getMinExtractCoin();
            loadViewData();
        }
    }

    private void loadViewData() {
        mStatement.setText(getString(R.string.coin_statement, FinanceUtil.subZeroAndDot(mMinExtractCoin, 8) + "   " + mType));
        mCountEdit.setHint(getString(R.string.min_get_coin_count_x, FinanceUtil.subZeroAndDot(mMinExtractCoin, 8) + "   " + mType));
    }

    private void inputAllCoin() {
        mCountEdit.setText(String.valueOf(mUsableCoin));
    }

    private void confirm() {
        if (mMinExtractCoin <= 0) {
            return;
        }

        if (mAddressEdit.getText().toString().length() > 100) {
            ToastUtil.show(R.string.address_too_large);
            return;
        }


        if (Double.valueOf(mCountEdit.getText().toString()) < mMinExtractCoin) {
            ToastUtil.show(R.string.under_count);
            return;
        }

        if (Double.valueOf(mCountEdit.getText().toString()) > mUsableCoin) {
            ToastUtil.show(R.string.too_much_coin);
            return;
        }

        Apic.requestGetCoin(mType, mCountEdit.getText().toString(), mAddressEdit.getText().toString()).tag(TAG).callback(new Callback<Resp>() {
            @Override
            protected void onRespSuccess(Resp resp) {
                showSuccess();
            }
        }).fire();
    }

    private void checkoutBtnStatus() {
        if (TextUtils.isEmpty(mAddressEdit.getText().toString()) || TextUtils.isEmpty(mCountEdit.getText().toString())) {
            mConfirm.setEnabled(false);
        } else {
            mConfirm.setEnabled(true);
        }
    }

    private void showSuccess() {
        SmartDialog.with(getActivity(), getString(R.string.withdraw_coin_success)).setNegativeVisible(View.GONE).setOnDismissListener(new SmartDialog.OnDismissListener() {
            @Override
            public void onDismiss(Dialog dialog) {
                finish();
            }
        }).show();
    }

    @OnClick({R.id.scanBtn, R.id.allBtn, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanBtn:
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, CODE_SCAN);
                break;
            case R.id.allBtn:
                inputAllCoin();
                break;
            case R.id.confirm:
                confirm();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_SCAN && resultCode == RESULT_OK && data != null) {
            String result = data.getStringExtra(CaptureActivity.RESULT);
            if (!checkCoinAddress(result)) {
                ToastUtil.show(R.string.input_correct_address);
                return;
            } else {
                mAddressEdit.setText(result);
            }
        }
    }

    public static boolean checkCoinAddress(String account) {
        String all = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(all);
        return pattern.matches(all, account);
    }
}
