package com.sbai.bcnews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sbai.bcnews.utils.Display;
import com.sbai.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${wangJie} on 2018/2/11.
 */

public class ThreeImageLayout extends LinearLayout {

    private ArrayList<ImageView> mImageViewList;

    public ThreeImageLayout(Context context) {
        this(context, null);
    }

    public ThreeImageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThreeImageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int defaultHeight = (int) Display.dp2Px(80, getResources());
        int defaultMargin = (int) Display.dp2Px(4, getResources());

        mImageViewList = new ArrayList<>();
        LayoutParams layoutParams = new LayoutParams(0, defaultHeight);
        layoutParams.weight = 1;
        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (i == 1) {
                layoutParams.setMargins(defaultMargin, 0, defaultMargin, 0);
            } else {
                layoutParams.setMargins(0, 0, 0, 0);
            }
            addView(imageView, layoutParams);
            imageView.setVisibility(GONE);
            mImageViewList.add(imageView);
        }
    }

    public void setImagePath(List<String> content) {
        if (content == null || content.isEmpty()) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            int size = content.size();
            int contentLength = size > 3 ? 3 : size;
            for (int i = 0; i < contentLength; i++) {
                final String imagePath = content.get(i);
                ImageView imageView = mImageViewList.get(i);
                imageView.setVisibility(VISIBLE);
                GlideApp.with(getContext())
                        .load(imagePath)
                        .into(imageView);
            }
        }
    }
}
