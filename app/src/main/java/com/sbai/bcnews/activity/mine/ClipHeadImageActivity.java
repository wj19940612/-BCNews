package com.sbai.bcnews.activity.mine;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
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
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.image.ImageUtils;
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
        Bitmap clipBitmap = mClipImageLayout.clip(Bitmap.Config.RGB_565);
        String picture = ImageUtils.bitmapToBase64(clipBitmap);
        submitPortraitPath(picture);
    }

    private void submitPortraitPath(final String data) {
        Apic.submitPortraitPath(data)
                .timeout(10_000)
                .indeterminate(this)
                .callback(new Callback<Resp<Object>>() {
                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        requestUserInfo();
                    }
                })
                .fire();
    }

    private void requestUserInfo() {
        Apic.requestUserInfo()
                .tag(TAG)
                .callback(new Callback2D<Resp<UserInfo>, UserInfo>() {
                    @Override
                    protected void onRespSuccessData(UserInfo data) {
                        LocalUser.getUser().setUserInfo(data);
                        ToastUtil.show(R.string.modify_success);
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .fire();
    }

}
