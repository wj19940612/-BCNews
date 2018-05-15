package com.sbai.bcnews.view.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.ViewPointComment;
import com.sbai.bcnews.model.news.ViewPointCommentReview;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/5/4
 * <p>
 * Description:
 * </p>
 */
public class CommentContentView extends LinearLayout {
    @BindView(R.id.userHeadImage)
    ImageView mUserHeadImage;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.praiseCount)
    TextView mPraiseCount;
    @BindView(R.id.pointContent)
    TextView mPointContent;
    @BindView(R.id.timeLine)
    TextView mTimeLine;
    @BindView(R.id.review)
    TextView mReview;
    @BindView(R.id.reviewContent)
    CommentReviewView mReviewContent;


    public CommentContentView(Context context) {
        this(context, null);
    }

    public CommentContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_comment_content, this, false);
        addView(view);
        ButterKnife.bind(this);
    }

    public void setViewpointList(ViewPointComment viewPointComment) {
        GlideApp.with(getContext())
                .load(viewPointComment.getUserPortrait())
                .placeholder(R.drawable.ic_default_head_portrait)
                .circleCrop()
                .into(mUserHeadImage);
        mUserName.setText(viewPointComment.getUsername());

        updatePointContent(viewPointComment);
        updatePointPraise(viewPointComment);
    }

    private void updatePointPraise(final ViewPointComment viewPointComment) {
        if (viewPointComment.getPraiseCount() != 0) {
            mPraiseCount.setText(String.valueOf(viewPointComment.getPraiseCount()));
        }

        boolean isPraise = viewPointComment.getIsPraise() == NewsViewpoint.ALREADY_PRAISE;
        mPraiseCount.setSelected(isPraise);
        if (isPraise)
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
        else
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);

        mPraiseCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReviewCallBack != null) {
                    mOnReviewCallBack.onSecondReviewPraise(viewPointComment, 0);
                }
            }
        });

        mReview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReviewCallBack != null) {
                    mOnReviewCallBack.onSecondReview(mReview, viewPointComment, 0);
                }
            }
        });
    }


    private void updatePointContent(ViewPointComment viewPointComment) {
        mPointContent.setText(viewPointComment.getContent());

        mTimeLine.setText(DateUtil.formatDefaultStyleTime(viewPointComment.getReplayTime()));
        if (viewPointComment.getVos() != null) {
            mReviewContent.setReviewData(viewPointComment);
            mReviewContent.setOnReviewListener(new CommentReviewView.OnReviewListener() {
                @Override
                public void onClick(View view, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                    if (mOnReviewCallBack != null) {
                        mOnReviewCallBack.onThirdReviewClick(view, viewPointComment, sonViewPointComment);
                    }
                }

                @Override
                public void onPraise(ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                    if (mOnReviewCallBack != null) {
                        mOnReviewCallBack.onThirdReviewPraise(0, viewPointComment, sonViewPointComment);
                    }
                }
            });
        } else {
            mReviewContent.setVisibility(GONE);
        }
    }

    private OnReviewCallBack mOnReviewCallBack;

    public void setOnReviewCallBack(OnReviewCallBack onReviewCallBack) {
        mOnReviewCallBack = onReviewCallBack;
    }

    public interface OnReviewCallBack {

        void onSecondReviewPraise(ViewPointComment viewPointComment, int position);

        void onSecondReview(View view, ViewPointComment viewPointComment, int position);

        void onThirdReviewClick(View view, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment);

        void onThirdReviewPraise(int position, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment);

    }

}
