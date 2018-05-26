package com.sbai.bcnews.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ConversionHistory;
import com.sbai.bcnews.model.system.Operation;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_ALIPAY;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_DIGITAL_COIN;
import static com.sbai.bcnews.fragment.ConversionGoodsActivity.PAGE_TELEPHONE_CHARGE;


public class ConversionHistoryDetailActivity extends BaseActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.conversionTime)
    TextView mConversionTime;
    @BindView(R.id.conversionType)
    TextView mConversionType;
    @BindView(R.id.conversionCount)
    TextView mConversionCount;
    @BindView(R.id.payCount)
    TextView mPayCount;
    @BindView(R.id.acceptAddress)
    TextView mAcceptAddress;
    @BindView(R.id.acceptUserName)
    TextView mAcceptUserName;
    @BindView(R.id.status)
    TextView mStatus;
    @BindView(R.id.tips)
    TextView mTips;
    @BindView(R.id.wx)
    TextView mWx;
    @BindView(R.id.acceptAddressLine)
    View mAcceptAddressLine;

    private int mAcceptType;
    private ConversionHistory mConversionHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_history_detail);
        ButterKnife.bind(this);
        initData();
        loadData();
    }


    private void initData() {
        mConversionHistory = getIntent().getParcelableExtra(ExtraKeys.CONVERSION_HISTORY);

        if (mConversionHistory == null) {
            return;
        }
        mAcceptType = mConversionHistory.getType();
        switch (mAcceptType) {
            case PAGE_DIGITAL_COIN:
                displayCoinHistory();
                break;
            case PAGE_ALIPAY:
                displayAliPayHistory();
                break;
            case PAGE_TELEPHONE_CHARGE:
                displayTelePhoneFeeHistory();
                break;
        }
    }

    private void displayCoinHistory() {
        mAcceptUserName.setVisibility(View.GONE);
        mAcceptAddressLine.setVisibility(View.GONE);

        mConversionTime.setText(getString(R.string.exchange_time_x, DateUtil.format(mConversionHistory.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH)));
        mConversionType.setText(R.string.exchange_type_digital_coin);
        mConversionCount.setText(getString(R.string.exchange_name_x, mConversionHistory.getPName()));
        mPayCount.setText(getString(R.string.pay_price_x, FinanceUtil.trimTrailingZero(mConversionHistory.getPrice())));
        mAcceptAddress.setText(getString(R.string.get_coin_address_x, mConversionHistory.getExtractCoinAddress()));
        String[] status = getResources().getStringArray(R.array.order_status);
        mStatus.setText(status[mConversionHistory.getStatus()]);
        if (TextUtils.isEmpty(mConversionHistory.getRemark())) {
            mTips.setVisibility(View.GONE);
        } else {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(mConversionHistory.getRemark());
        }
    }

    private void displayAliPayHistory() {
        mConversionTime.setText(getString(R.string.exchange_time_x, DateUtil.format(mConversionHistory.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH)));
        mConversionType.setText(R.string.exchange_type_ali_pay);
        mConversionCount.setText(getString(R.string.exchange_name_x, mConversionHistory.getPName()));
        mPayCount.setText(getString(R.string.pay_price_x, FinanceUtil.trimTrailingZero(mConversionHistory.getPrice())));
        mAcceptAddress.setText(getString(R.string.ali_pay_name_x, mConversionHistory.getAccountAliPay()));
        mAcceptUserName.setText(getString(R.string.ali_pay_user_name_x, mConversionHistory.getAccountAliPayName()));
        String[] status = getResources().getStringArray(R.array.order_status);
        mStatus.setText(status[mConversionHistory.getStatus()]);
        if (TextUtils.isEmpty(mConversionHistory.getRemark())) {
            mTips.setVisibility(View.GONE);
        } else {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(mConversionHistory.getRemark());
        }
    }

    private void displayTelePhoneFeeHistory() {
        mConversionTime.setText(getString(R.string.exchange_time_x, DateUtil.format(mConversionHistory.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH)));
        mConversionType.setText(R.string.exchange_type_telephone_fee);
        mConversionCount.setText(getString(R.string.exchange_name_x, mConversionHistory.getPName()));
        mPayCount.setText(getString(R.string.pay_price_x, FinanceUtil.trimTrailingZero(mConversionHistory.getPrice())));
        mAcceptAddress.setText(getString(R.string.telephone_x, mConversionHistory.getAccountAliPay()));
        mAcceptUserName.setText(getString(R.string.telephone_user_name_x, mConversionHistory.getAccountAliPayName()));
        String[] status = getResources().getStringArray(R.array.order_status);
        mStatus.setText(status[mConversionHistory.getStatus()]);
        if (TextUtils.isEmpty(mConversionHistory.getRemark())) {
            mTips.setVisibility(View.GONE);
        } else {
            mTips.setVisibility(View.VISIBLE);
            mTips.setText(mConversionHistory.getRemark());
        }
    }

    private void loadData() {
        requestOperationWeChatAccount();
    }

    private void requestOperationWeChatAccount() {
        Apic.requestOperationSetting(Operation.OPERATION_TYPE_WE_CHAT)
                .tag(TAG)
                .callback(new Callback2D<Resp<Operation>, Operation>() {
                    @Override
                    protected void onRespSuccessData(Operation data) {
                        showWx(data.getSYS_OPERATE_WX());
                    }
                })
                .fire();
    }

    private void showWx(String sys_operate_wx) {
        if (!TextUtils.isEmpty(sys_operate_wx)) {
            mWx.setText(sys_operate_wx);
        }
    }

    @OnClick({R.id.wx})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx:
                copyWxNumber();
                break;
        }
    }

    private void copyWxNumber() {
        if (TextUtils.isEmpty(mWx.getText())) {
            return;
        }
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, mWx.getText().toString());
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
        ToastUtil.show(R.string.wx_has_copy);
    }
}
