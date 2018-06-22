package com.sbai.bcnews.utils.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.sbai.bcnews.utils.Display;

import java.security.MessageDigest;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description:  圆角和centerCrop都支持的transfrom
 * </p>
 */
public class GlideRoundAndCenterCropTransform extends BitmapTransformation {

    private static final String ID = "com.sbai.bcnews.utils.glide.GlideRoundTransform";
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private int radius = 4;

    public GlideRoundAndCenterCropTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundAndCenterCropTransform(Context context, int radius) {
        this.radius = (int) Display.dp2Px(radius, context.getResources());
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Bitmap centerCrop = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
        return TransformationUtils.roundedCorners(pool, centerCrop, radius);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideRoundAndCenterCropTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }

}
