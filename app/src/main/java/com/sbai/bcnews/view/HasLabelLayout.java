package com.sbai.bcnews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sbai.bcnews.R;
import com.sbai.glide.GlideApp;

/**
 * Modified by $nishuideyu$ on 2018/6/7
 * <p>
 * Description:
 * </p>
 */
public class HasLabelLayout extends RelativeLayout {

    private Drawable mLabelDrawable;
    private Drawable mDrawable;
    private int mLabelWidth;
    private int mLabelHeight;
    private ImageView mMainImageView;
    private ImageView mLabelImageView;

    public HasLabelLayout(Context context) {
        this(context, null);
    }

    public HasLabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HasLabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        processAttrs(attrs);
        init();
    }


    private void processAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HasLabelLayout);
        mDrawable = typedArray.getDrawable(R.styleable.HasLabelLayout_android_src);
        mLabelDrawable = typedArray.getDrawable(R.styleable.HasLabelLayout_labelDrawable);
        mLabelWidth = typedArray.getDimensionPixelOffset(R.styleable.HasLabelLayout_labelWidth, 0);
        mLabelHeight = typedArray.getDimensionPixelOffset(R.styleable.HasLabelLayout_labelHeight, 0);
        typedArray.recycle();
    }

    private void init() {

        mMainImageView = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mMainImageView, layoutParams);


        mLabelImageView = new ImageView(getContext());
        if (mLabelWidth != 0 && mLabelHeight != 0) {
            layoutParams = new LayoutParams(mLabelWidth, mLabelHeight);
        } else {
            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        }
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(mLabelImageView, layoutParams);
        setLabel();
        setImageSrc(mDrawable);
    }

    private void setImageSrc(String url, Drawable drawable) {
        GlideApp.with(getContext())
                .load(!TextUtils.isEmpty(url) ? url : drawable)
                .placeholder(R.drawable.ic_default_head_portrait)
                .circleCrop()
                .into(mMainImageView);
    }

    public void setImageSrc(Drawable drawable) {
        mDrawable = drawable;
        setImageSrc("", mDrawable);
    }

    public void setImageSrc(String url) {
        setImageSrc(url, null);
    }

    public void setImageSrc(@DrawableRes int resId) {
        setImageSrc(ContextCompat.getDrawable(getContext(), resId));
    }

    public void setLabelDrawable(Drawable labelDrawable) {
        mLabelDrawable = labelDrawable;
        setLabel();
    }

    private void setLabel() {
        mLabelImageView.setImageDrawable(mLabelDrawable);
    }

    public void setLabelImageViewVisible(boolean viewVisible) {
        if (viewVisible) {
            mLabelImageView.setVisibility(VISIBLE);
        } else {
            mLabelImageView.setVisibility(GONE);
        }
    }

    public void setLabelSelected(boolean isOfficialAuthor) {
        mLabelImageView.setSelected(isOfficialAuthor);
    }
}
