package com.sbai.bcnews.view.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.view.MeasureTextView;
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
public class ViewPointContentView extends LinearLayout implements  MeasureTextView.OnLineCountListener {

    private static final String TAG = "CommentContentView";

    private static final int CONTENT_SPREAD_LINE = 5;
    private static final int CONTENT_ALL_CONTENT_LINE = 10;


    @BindView(R.id.userHeadImage)
    ImageView mUserHeadImage;
    @BindView(R.id.userName)
    TextView mUserName;
    @BindView(R.id.praiseCount)
    TextView mPraiseCount;
    @BindView(R.id.pointContent)
    MeasureTextView mPointContent;
    @BindView(R.id.timeLine)
    TextView mTimeLine;
    @BindView(R.id.review)
    TextView mReview;
    @BindView(R.id.shrink)
    TextView mShrink;
    @BindView(R.id.reviewContent)
    ViewPointReviewContentView mReviewContent;

    private OnCommentClickListener mOnCommentClickListener;

    private NewViewPointAndReview mNewViewPointAndReview;
    private String mSpread;
    private String mFullText;
    private TextPaint mTextPaint;

    public void setOnCommentClickListener(OnCommentClickListener onClickListener) {
        mOnCommentClickListener = onClickListener;
    }

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
        mTextPaint = new TextPaint();
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_viewpoint_content, this, false);
        addView(view);
        ButterKnife.bind(this);
        mSpread = getContext().getString(R.string.spread);
        mFullText = getContext().getString(R.string.all_content);

        mPointContent.setOnLineCountListener(this);
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
        if (isPraise) {
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
        } else {
            mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);
        }

    }

    private void updatePointContent(final NewViewPointAndReview newViewPointAndReview) {
        mPointContent.setText(newViewPointAndReview.getContent());

        mTimeLine.setText(DateUtil.formatDefaultStyleTime(newViewPointAndReview.getReplayTime()));
        if (newViewPointAndReview.getVos() != null) {
            mReviewContent.setReviewData(newViewPointAndReview);
        } else {
            mReviewContent.setVisibility(GONE);
        }
    }

    private int getLineCount(CharSequence text, TextPaint paint, float size, float width,
                             DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, size,
                displayMetrics));
        DynamicLayout layout = new DynamicLayout(text, paint, (int) width,
                Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        return layout.getLineCount();
    }

    private void handleContent(int lineCount) {
        Log.d(TAG, "handleContent: "+lineCount+"  "+mPointContent.getText().toString());
        if (lineCount > 5) {
            mPointContent.setMaxLines(CONTENT_SPREAD_LINE);
            mPointContent.setEllipsize(TextUtils.TruncateAt.END);
            mShrink.setVisibility(VISIBLE);
            if (lineCount < 10) {
                mShrink.setText(mSpread);
            } else {
                mShrink.setText(mFullText);
            }
        } else {
            mShrink.setVisibility(GONE);
        }

    }


    @OnClick({R.id.review, R.id.shrink, R.id.praiseCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.review:
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onReview();
                }
                break;
            case R.id.shrink:
                String pointText = mShrink.getText().toString();
                if (!TextUtils.isEmpty(pointText)) {
                    if (pointText.equalsIgnoreCase(mFullText)) {
                        if (mOnCommentClickListener != null) {
                            mOnCommentClickListener.onFullText();
                        }
                    } else {
                        mPointContent.setMaxLines(Integer.MAX_VALUE);
                        mShrink.setVisibility(GONE);
                    }
                }
                break;
            case R.id.praiseCount:
                if (mOnCommentClickListener != null) {
                    mOnCommentClickListener.onPraise();
                }
                break;
        }
    }


    @Override
    public void onLineCount(int lineCount) {
        handleContent(lineCount);
    }


    public interface OnCommentClickListener {

        void onReview();

        void onPraise();

        void onFullText();

    }
}
