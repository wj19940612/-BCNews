package com.sbai.bcnews.activity.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.utils.AppInfo;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.autofit.AutofitTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于区块财经
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.weChat)
    AutofitTextView mWeChat;
    @BindView(R.id.qq)
    AutofitTextView mQq;
    @BindView(R.id.version)
    TextView mVersion;
    @BindView(R.id.agree)
    TextView mAgree;
    @BindView(R.id.companyInfo)
    TextView mCompanyInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_bcnews);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mVersion.setText(getString(R.string.version_, AppInfo.getVersionName(getActivity())));
    }

    @OnClick({R.id.weChat, R.id.qq, R.id.agree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.weChat:
                clipToBoard("lunar_0904");
                break;
            case R.id.qq:
                clipToBoard("1358453452");
                break;
            case R.id.agree:
                break;
        }
    }

    private void clipToBoard(String content) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("Label", content));
        ToastUtil.show(R.string.copy_success);
    }
}
