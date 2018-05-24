package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.MyIntegral;
import com.sbai.bcnews.model.mine.QKC;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.RandomLocationLayout;
import com.sbai.bcnews.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QKCActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.randomLayout)
    RandomLocationLayout mRandomLayout;
    @BindView(R.id.qkcNumber)
    TextView mQkcNumber;
    @BindView(R.id.rate)
    TextView mRate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qkc);
        ButterKnife.bind(this);
        translucentStatusBar();
        requestNotGet();
        ArrayList<QKC> qksList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            QKC qks = new QKC();
            qks.setIntegral(200 + i);
            qksList.add(qks);
        }
        mRandomLayout.setQksList(qksList);
    }

    private void requestNotGet() {
        Apic.requestIntegral()
                .tag(TAG)
                .callback(new Callback2D<Resp<MyIntegral>, MyIntegral>() {
                    @Override
                    protected void onRespSuccessData(MyIntegral data) {
                        mQkcNumber.setText(getString(R.string.qkc_number,String.valueOf(data.getIntegral())));
                        mRate.setText(getString(R.string.now_hashrate,String.valueOf(data.getRate())));
                    }
                })
                .fire();
        Apic.requestQKC()
                .tag(TAG)
                .callback(new Callback2D<Resp<List<QKC>>, List<QKC>>() {
                    @Override
                    protected void onRespSuccessData(List<QKC> data) {
//                        mRandomLayout.setQksList(data);
                    }
                })
                .fire();
    }

    @OnClick(R.id.qkcDetail)
    public void onViewClicked() {
        Launcher.with(getActivity(), QKCDetailActivity.class).execute();
    }
}
