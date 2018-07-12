package com.sbai.bcnews.view.search;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.glide.GlideRoundAndCenterCropTransform;
import com.sbai.bcnews.view.ThreeImageLayout;
import com.sbai.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/7/6
 * <p>
 * Description: 综合搜索页面 文章部分 会有三种样式
 * </p>
 */
public class SearchArticleContent extends FrameLayout {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.bottomText)
    TextView mBottomText;
    @BindView(R.id.rootView)
    ConstraintLayout mRootView;

    ThreeImageLayout mThreeImageLayout;
    TextView mMultipleImageTitle;
    TextView mMultipleImageBottomText;


    private View mSingleOrNoneImageView;
    private View mMultipleImageView;

    private String mHighlightText;
    private int mHighlightTextColor = Color.RED;

    public SearchArticleContent(@NonNull Context context) {
        this(context, null);
    }

    public SearchArticleContent(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchArticleContent(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSingleOrNoneImageView = LayoutInflater.from(getContext()).inflate(R.layout.row_author_article_single_image, null);
        ButterKnife.bind(this, mSingleOrNoneImageView);
        addView(mSingleOrNoneImageView);


        mMultipleImageView = LayoutInflater.from(getContext()).inflate(R.layout.row_author_article_three_image, null);
        mThreeImageLayout = mMultipleImageView.findViewById(R.id.threeImageLayout);
        mMultipleImageTitle = mMultipleImageView.findViewById(R.id.title);
        mMultipleImageBottomText = mMultipleImageView.findViewById(R.id.bottomText);
        addView(mMultipleImageView);
    }

    public void setAuthorArticle(AuthorArticle authorArticle) {
        if (authorArticle.getImgs() == null || authorArticle.getImgs().size() <= 1) {
            mSingleOrNoneImageView.setVisibility(VISIBLE);
            mMultipleImageView.setVisibility(GONE);
            updateArticleInfo(authorArticle, mTitle, mBottomText);
            updateSingleOrNoneImage(authorArticle, mImage);
        } else {
            mSingleOrNoneImageView.setVisibility(GONE);
            mMultipleImageView.setVisibility(VISIBLE);
            updateArticleInfo(authorArticle, mMultipleImageTitle, mMultipleImageBottomText);
            mThreeImageLayout.setImagePath(authorArticle.getImgs());
        }
    }

    private void updateSingleOrNoneImage(AuthorArticle authorArticle, ImageView image) {
        if (authorArticle.getImgs() == null || authorArticle.getImgs().isEmpty()) {
            image.setVisibility(GONE);
            return;
        } else {
            image.setVisibility(VISIBLE);
        }

        GlideApp.with(getContext())
                .load(authorArticle.getImgs().get(0))
                .transform(new GlideRoundAndCenterCropTransform(getContext()))
                .placeholder(R.drawable.ic_default_news)
                .into(image);
    }


    private void updateArticleInfo(AuthorArticle authorArticle, TextView title, TextView bottomText) {
        title.setText(StrUtil.changeSpecialTextColor(authorArticle.getTitle(), mHighlightText, mHighlightTextColor));
        String readCount = getContext().getString(R.string.read_number, authorArticle.getShowReadCount());

        if (TextUtils.isEmpty(authorArticle.getAuthor())) {
            bottomText.setText(getContext().getString(R.string.time_x_read_x, DateUtil.formatDefaultStyleTime(authorArticle.getReleaseTime()), readCount));
        } else {
            bottomText.setText(getContext().getString(R.string.source_x_time_x_read_x, authorArticle.getAuthor(), DateUtil.formatDefaultStyleTime(authorArticle.getReleaseTime()), readCount));
        }

//        bottomText.setText(readCount);
    }

    public void setHighlightColor(int searchTextColor) {
        mHighlightTextColor = searchTextColor;
    }

    public void setHighlightText(String searchContent) {
        mHighlightText = searchContent;
    }
}
