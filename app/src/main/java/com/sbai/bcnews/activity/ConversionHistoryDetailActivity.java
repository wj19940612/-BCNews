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
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.activity.BindingAddressActivity.CONVERSION_TYPE_ALI_PAY;
import static com.sbai.bcnews.activity.BindingAddressActivity.CONVERSION_TYPE_DIGITAL;
import static com.sbai.bcnews.activity.BindingAddressActivity.CONVERSION_TYPE_TEL;


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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion_history_detail);
        ButterKnife.bind(this);
        initData();
        loadData();
    }


    private void initData() {
        mAcceptType = getIntent().getIntExtra(ExtraKeys.BINDING_TYPE, CONVERSION_TYPE_DIGITAL);

        switch (mAcceptType) {
            case CONVERSION_TYPE_DIGITAL:
                mAcceptUserName.setVisibility(View.GONE);
                mAcceptAddressLine.setVisibility(View.GONE);
                break;
            case CONVERSION_TYPE_ALI_PAY:
                break;
            case CONVERSION_TYPE_TEL:
                break;
        }
    }

    private void loadData() {

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
    }
}
