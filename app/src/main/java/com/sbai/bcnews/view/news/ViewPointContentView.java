package com.sbai.bcnews.view.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description:
 * </p>
 */
public class ViewPointContentView extends LinearLayout {

    private static final String TAG = "CommentContentView";

    private static final int CONTENT_SPREAD_LINE = 5;
    private static final int CONTENT_ALL_CONTENT_LINE = 10;

//    ImageView mUserHeadImage;
//    TextView mUserName;
//    TextView mPraiseCount;
//    TextView mPointContent;
//    TextView mTimeLine;
//    TextView mReview;
//    TextView mShrink;
//    ReviewContentView mReviewContent;


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
    @BindView(R.id.shrink)
    TextView mShrink;
    @BindView(R.id.reviewContent)
    ViewPointReviewContentView mReviewContent;

    private OnCommentClickListener mOnClickListener;

    private NewViewPointAndReview mNewViewPointAndReview;

    public void setOnCommentClickListener(OnCommentClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    private int mContentLines;

    public ViewPointContentView(Context context) {
        super(context);
        init();
    }


    public ViewPointContentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ViewPointContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_viewpoint_content, this, false);
        addView(view);
        ButterKnife.bind(this);
    }


    public void setViewpointList(NewViewPointAndReview newViewPointAndReview) {
        GlideApp.with(getContext())
                .load(newViewPointAndReview.getUserPortrait())
                .placeholder(R.drawable.ic_default_head_portrait)
                .circleCrop()
                .into(mUserHeadImage);
        mUserName.setText(newViewPointAndReview.getUsername());

        updatePointContent(newViewPointAndReview);
        updatePointPraise(newViewPointAndReview);

    }

    private void updatePointPraise(NewViewPointAndReview newViewPointAndReview) {

        mPraiseCount.setText(String.valueOf(newViewPointAndReview.getPraiseCount()));

        boolean isPraise = newViewPointAndReview.getIsPraise() == NewsViewpoint.ALREADY_PRAISE;
        mPraiseCount.setSelected(isPraise);
        if (isPraise)
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
        else
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);

    }

    private void updatePointContent(NewViewPointAndReview newViewPointAndReview) {
        mPointContent.setText(newViewPointAndReview.getContent());

        mPointContent.post(new Runnable() {
            @Override
            public void run() {
                mContentLines = mPointContent.getLineCount();
                if (mContentLines > 5) {
                    mPointContent.setMaxLines(CONTENT_SPREAD_LINE);
                    mPointContent.setEllipsize(TextUtils.TruncateAt.END);
                    mShrink.setVisibility(VISIBLE);
                    if (mContentLines >= 10) {
                        mShrink.setText(R.string.spread);
                    } else {
                        mShrink.setText(R.string.all_content);
                    }
                } else {
//                    mPointContent.setMaxLines(Integer.MAX_VALUE);
                    mShrink.setVisibility(GONE);
                }
            }
        });


        mTimeLine.setText(DateUtil.formatNewsStyleTime(newViewPointAndReview.getReplayTime()));
        if (newViewPointAndReview.getVos() != null) {
            mReviewContent.setReviewData(newViewPointAndReview);
        } else {
            mReviewContent.setVisibility(GONE);
        }
    }


    @OnClick({R.id.review, R.id.shrink, R.id.praiseCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.review:
                if (mOnClickListener != null) {
                    mOnClickListener.onReview();
                }
                break;
            case R.id.shrink:
                mPointContent.setMaxLines(Integer.MAX_VALUE);
                mShrink.setVisibility(GONE);
                break;
            case R.id.praiseCount:
                if (mOnClickListener != null) {
                    mOnClickListener.onPraise();
                }
                break;
        }
    }


    public interface OnCommentClickListener {

        void onReview();

        void onPraise();

    }
}
