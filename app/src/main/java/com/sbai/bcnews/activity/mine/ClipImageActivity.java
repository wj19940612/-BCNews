package com.sbai.bcnews.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.bcnews.view.clipimage.ClipImageLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClipImageActivity extends BaseActivity {

    public static final String KEY_CLIP_USER_IMAGE = "CLIP_USER_IMAGE";
    @BindView(R.id.clipImageLayout)
    ClipImageLayout mClipImageLayout;
    @BindView(R.id.cancel)
    AppCompatTextView mCancel;
    @BindView(R.id.complete)
    AppCompatTextView mComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        ButterKnife.bind(this);
        translucentStatusBar();

        Intent intent = getIntent();
        String bitmapPath = intent.getStringExtra(KEY_CLIP_USER_IMAGE);
        //修复米5的图片显示问题
        String forMi5 = bitmapPath.replace("/raw//", "");
        Log.d(TAG, "传入的地址" + bitmapPath);
        mClipImageLayout.setZoomImageViewImage(forMi5);
    }


    @OnClick({R.id.cancel, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                Bitmap clipBitmap = mClipImageLayout.clip(Bitmap.Config.RGB_565);
                if (clipBitmap != null) {
//                    String bitmapToBase64 = ImageUtils.compressImageToBase64(clipBitmap);
                    String bitmapToBase64 = ImageUtils.bitmapToBase64(clipBitmap);
                    clipBitmap.recycle();
                }
                break;
        }
    }
}
