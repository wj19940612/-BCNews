package com.sbai.bcnews.activity.mine;

import android.os.Bundle;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.model.mine.QKS;
import com.sbai.bcnews.view.RandomLocationLayout;
import com.sbai.bcnews.view.TitleBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QKCActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.randomLayout)
    RandomLocationLayout mRandomLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qkc);
        ButterKnife.bind(this);

        ArrayList<QKS> qksList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            QKS qks = new QKS();
            qks.setNumber(i * i);
            qksList.add(qks);
        }

        mRandomLayout.setQksList(qksList);

    }

}
