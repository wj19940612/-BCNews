package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.fragment.dialog.PromoteHashRateWayDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.MyIntegral;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.Network;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.RandomLocationLayout;
import com.sbai.bcnews.view.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QKCActivity extends BaseActivity implements RandomLocationLayout.OnCoinClickListener {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.randomLayout)
    RandomLocationLayout mRandomLayout;
    @BindView(R.id.qkcNumber)
    TextView mQkcNumber;
    @BindView(R.id.rate)
    TextView mRate;
    @BindView(R.id.hashrate)
    TextView mHashrate;
    @BindView(R.id.lookDetail)
    TextView mLookDetail;
    @BindView(R.id.qkcDetail)
    RelativeLayout mQkcDetail;
    private MyIntegral mMyIntegral;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qkc);
        ButterKnife.bind(this);
        translucentStatusBar();
        initView();
        requestNotGet();
        mRandomLayout.setOnCoinClickListener(this);
    }

    private void initView() {
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2018/5/25 打开h5承接页 

            }
        });
    }

    private void requestNotGet() {
        Apic.requestIntegral()
                .tag(TAG)
                .callback(new Callback2D<Resp<MyIntegral>, MyIntegral>() {
                    @Override
                    protected void onRespSuccessData(MyIntegral data) {
                        setQKCAndRate(data);
                    }
                })
                .fire();
        Apic.requestQKC()
                .tag(TAG)
                .callback(new Callback2D<Resp<List<QKC>>, List<QKC>>() {
                    @Override
                    protected void onRespSuccessData(List<QKC> data) {
                        mRandomLayout.setQksList(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                })
                .fire();
    }

    private void setQKCAndRate(MyIntegral data) {
        mMyIntegral = data;
        mQkcNumber.setText(getString(R.string.qkc_number, FinanceUtil.formatWithScaleRemoveTailZero(data.getIntegral())));
        mRate.setText(getString(R.string.now_hashrate, String.valueOf(data.getRate())));
    }


    @Override
    public void onCoinClick(final View v, final QKC qks) {
        //有网络时默认领取成功
        if (Network.isNetworkAvailable()) {
            Apic.getQKC(qks.getId())
                    .tag(TAG)
                    .callback(new Callback<Resp<Object>>() {
                        @Override
                        protected void onRespSuccess(Resp<Object> resp) {
                        }
                    })
                    .fire();
            mRandomLayout.removeCoin(v, qks);
            mMyIntegral.setIntegral(mMyIntegral.getIntegral() + qks.getIntegral());
            setQKCAndRate(mMyIntegral);
        } else {
            ToastUtil.show(R.string.http_error_network);
        }
    }

    @OnClick({R.id.lookDetail, R.id.hashrate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lookDetail:
                Launcher.with(getActivity(), QKCDetailActivity.class).execute();
                break;
            case R.id.hashrate:
                new PromoteHashRateWayDialogFragment().show(getSupportFragmentManager());
                break;
        }
    }
}
