package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.view.clipimage.ClipImageLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClipHeadImageActivity extends BaseActivity {

    @BindView(R.id.clipImageLayout)
    ClipImageLayout mClipImageLayout;
    @BindView(R.id.cancel)
    AppCompatTextView mCancel;
    @BindView(R.id.complete)
    AppCompatTextView mComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_head_image);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.cancel, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                break;
        }
    }
}
