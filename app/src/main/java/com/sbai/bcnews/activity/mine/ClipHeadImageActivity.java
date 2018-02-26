package com.sbai.bcnews.activity.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BaseActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.utils.image.ImageUtils;
import com.sbai.bcnews.view.clipimage.ClipImageLayout;

import java.io.File;

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
        String imagePath = getIntent().getStringExtra(ExtraKeys.IMAGE_PATH);
        String forMi5 = imagePath.replace("/raw//", "");
        mClipImageLayout.setZoomImageViewImage(forMi5);
    }

    @OnClick({R.id.cancel, R.id.complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.complete:
                submitFile();
                break;
        }
    }

    private void submitFile() {
//        Bitmap clipBitmap = mClipImageLayout.clip(Bitmap.Config.RGB_565);
//        File file = ImageUtils.bitmapToFile(clipBitmap, "image");
//        Log.d(TAG, "onViewClicked: " + file.length());
//        Apic.submitFile(file, "image")
//                .tag(TAG)
//                .timeout(10_000)
//                .callback(new Callback2D<Resp<String>, String>() {
//                    @Override
//                    protected void onRespSuccessData(String data) {
////                                UserInfo userInfo = LocalUser.getUser().getUserInfo();
////                                userInfo.setUserPortrait();
//                        submitPortraitPath(data);
//                    }
//                })
//                .fire();
        Bitmap clipBitmap = mClipImageLayout.clip(Bitmap.Config.RGB_565);
        String picture = ImageUtils.bitmapToBase64(clipBitmap);
        Apic.uploadImage(picture)
                .tag(TAG)
                .timeout(10_000)
                .callback(new Callback2D<Resp<String>, String>() {
                    @Override
                    protected void onRespSuccessData(String data) {
                        submitPortraitPath(data);
                    }
                })
                .fire();
    }

    private void submitPortraitPath(final String data) {
        Apic.submitPortraitPath(data)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        UserInfo userInfo = LocalUser.getUser().getUserInfo();
                        userInfo.setUserPortrait(data);
                        LocalUser.getUser().setUserInfo(userInfo);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .fire();
    }

}
