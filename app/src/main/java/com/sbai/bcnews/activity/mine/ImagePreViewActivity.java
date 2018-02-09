package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePreViewActivity extends BaseActivity {


    @BindView(R.id.imageview)
    ImageView mImageview;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

    private String mImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mImagePath = getIntent().getStringExtra(ExtraKeys.IMAGE);
        Glide.with(this).load("file://" + mImagePath).into(mImageview);

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通知上一个页面
                notifyRefresh();
            }
        });

    }

    private void notifyRefresh() {
        setResult(RESULT_OK);
        finish();
    }


}
