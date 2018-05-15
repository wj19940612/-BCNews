package com.sbai.bcnews.view.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.ViewPointComment;
import com.sbai.bcnews.model.news.ViewPointCommentReview;
import com.sbai.bcnews.utils.StrUtil;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/4
 * <p>
 * Description: 评论详情页面的3级评论
 * </p>
 */
public class CommentReviewView extends LinearLayout {

    private static final int MAX_REVIEW_SIZE = 2;

    public CommentReviewView(Context context) {
        this(context, null);
    }

    public CommentReviewView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentReviewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }


    public void setReviewData(final ViewPointComment viewPointComment) {
        final List<ViewPointCommentReview> vos = viewPointComment.getVos();
        if (vos.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
//        removeAllViewsInLayout();
        removeAllViews();


//        final LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(0, 0, 0, 10);
        final int itemCount = vos.size() > MAX_REVIEW_SIZE ? MAX_REVIEW_SIZE : vos.size();
        for (int i = 0; i < itemCount; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_review_content, null);
            initContentView(view, vos.get(i), viewPointComment);
            addView(view);
        }

        if (vos.size() > MAX_REVIEW_SIZE) {
            final TextView textView = new TextView(getContext());
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_476E92));
            textView.setText(getContext().getString(R.string.spared_all_review_count, vos.size()));
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentReviewView.this.removeView(textView);
                    for (int i = 2; i < vos.size(); i++) {
                        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_review_content, null);
                        initContentView(view, vos.get(i), viewPointComment);
                        addView(view);
                    }
//                    requestLayout();
                }
            });
            addView(textView);
        }
//        requestLayout();
    }


    private void initContentView(final View contentView, final ViewPointCommentReview fatherViewPointComment, final ViewPointComment viewPointComment) {
        TextView userName = contentView.findViewById(R.id.userName);
        TextView praiseCount = contentView.findViewById(R.id.praiseCount);
        TextView content = contentView.findViewById(R.id.content);

        userName.setText(formatUserNameContent(fatherViewPointComment));
        updatePraiseView(praiseCount, fatherViewPointComment);
        content.setText(fatherViewPointComment.getContent());

        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReviewListener != null) {
                    mOnReviewListener.onClick(contentView, viewPointComment, fatherViewPointComment);
                }
            }
        });

        praiseCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReviewListener != null) {
                    mOnReviewListener.onPraise(viewPointComment, fatherViewPointComment);
                }
            }
        });
    }

    private void updatePraiseView(TextView praiseCount, ViewPointComment viewPointComment) {
        if (viewPointComment.getPraiseCount() != 0) {
            praiseCount.setText(String.valueOf(viewPointComment.getPraiseCount()));
        } else {
            praiseCount.setText(R.string.praise);
        }

        boolean isPraise = viewPointComment.getIsPraise() == NewsViewpoint.ALREADY_PRAISE;
        praiseCount.setSelected(isPraise);
        if (isPraise)
            praiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
        else
            praiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);
    }

    private CharSequence formatUserNameContent(ViewPointCommentReview viewPointComment) {
        if (!TextUtils.isEmpty(viewPointComment.getReplayUsername())) {
            return StrUtil.mergeTextWithColor(viewPointComment.getUsername(), " 回复 " + getContext().getString(R.string.user_name_, viewPointComment.getUsername()), ContextCompat.getColor(getContext(), R.color.text_4949));
        }
        return getContext().getString(R.string.user_name_, viewPointComment.getUsername());
    }

    private OnReviewListener mOnReviewListener;

    public void setOnReviewListener(OnReviewListener onReviewListener) {
        mOnReviewListener = onReviewListener;
    }

    public interface OnReviewListener {

        void onClick(View view, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment);

        void onPraise(ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment);

    }
}
