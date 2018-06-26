package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.fragment.ConversionGoodsActivity;
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
    @BindView(R.id.mining)
    TextView mMining;
    @BindView(R.id.qkcNumber)
    TextView mQkcNumber;
    @BindView(R.id.rate)
    TextView mRate;
    @BindView(R.id.howPlusHashRate)
    TextView mHowPlusHashRate;
    @BindView(R.id.randomLayout)
    RandomLocationLayout mRandomLayout;
    @BindView(R.id.exchange)
    ImageView mExchange;
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

    @Override
    protected void onResume() {
        super.onResume();
        requestQKCNumber();
    }

    private void initView() {
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.with(getActivity(), WebActivity.class)
                        .putExtra(WebActivity.EX_URL, Apic.url.QKC_INTRODUCE_PAGE_URL)
                        .execute();
            }
        });
    }

    private void requestNotGet() {
        requestQKCNumber();
        requestCanGetQkcList();
    }

    private void requestQKCNumber() {
        Apic.requestIntegral()
                .tag(TAG)
                .callback(new Callback2D<Resp<MyIntegral>, MyIntegral>() {
                    @Override
                    protected void onRespSuccessData(MyIntegral data) {
                        setQKCAndRate(data);
                    }
                })
                .fire();
    }

    private void requestCanGetQkcList() {
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
//        ArrayList<QKC> qkcs = new ArrayList<>();
//        for (int i = 0; i < 22; i++) {
//            QKC qkc = new QKC();
//            qkc.setIntegral(i);
//            qkcs.add(qkc);
//        }
//        mRandomLayout.setQksList(qkcs);
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
                            mRandomLayout.removeCoin(v, qks);
                            mMyIntegral.setIntegral(mMyIntegral.getIntegral() + qks.getIntegral());
                            setQKCAndRate(mMyIntegral);
                        }
                    })
                    .fire();

        } else {
            ToastUtil.show(R.string.http_error_network);
        }

    }

    @OnClick({R.id.qkcNumber, R.id.howPlusHashRate, R.id.exchange})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.qkcNumber:
                Launcher.with(getActivity(), QKCDetailActivity.class).execute();
                break;
            case R.id.howPlusHashRate:
                new PromoteHashRateWayDialogFragment().show(getSupportFragmentManager());
                break;
            case R.id.exchange:
                Launcher.with(getActivity(), ConversionGoodsActivity.class).executeForResult(ConversionGoodsActivity.REQ_CODE_EXCHANGE_QKC);
                overridePendingTransition(R.anim.slide_in_from_bottom, 0);
                break;
        }
    }
}
