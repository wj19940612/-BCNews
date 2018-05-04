package com.sbai.bcnews.view.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.utils.StrUtil;

import java.util.List;

/**
 * Modified by $nishuideyu$ on 2018/5/2
 * <p>
 * Description:  评论列表中回复的内容
 * </p>
 */
public class ViewPointReviewContentView extends LinearLayout {

    private static final int MAX_REVIEW_SIZE = 2;

    public ViewPointReviewContentView(Context context) {
        this(context, null);
    }

    public ViewPointReviewContentView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPointReviewContentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public void setReviewData(NewViewPointAndReview newViewPointAndReview) {
        List<NewsViewpoint> vos = newViewPointAndReview.getVos();
        if (vos.isEmpty()) {
            setVisibility(GONE);
            return;
        } else {
            setVisibility(VISIBLE);
        }
        removeAllViews();

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 10);
        int itemCount = vos.size() > MAX_REVIEW_SIZE ? MAX_REVIEW_SIZE : vos.size();
        for (int i = 0; i < itemCount; i++) {
            TextView text = createText();
            text.setText(getCommentContent(vos.get(i)));
            addView(text, layoutParams);
        }

        if (vos.size() > MAX_REVIEW_SIZE) {
            TextView textView = new TextView(getContext());
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_476E92));
            if (newViewPointAndReview.getReplayCount() <= 10) {
                textView.setText(R.string.look_all_review);
            } else {
                textView.setText(getContext().getString(R.string.look_all_review_count, newViewPointAndReview.getReplayCount()));
            }
            addView(textView, layoutParams);
        }
        requestLayout();
    }


    private SpannableString getCommentContent(NewsViewpoint newsViewpoint) {
        SpannableString spannableString = StrUtil.mergeTextWithColor(getContext().getString(R.string.user_name_, newsViewpoint.getUsername()),
                newsViewpoint.getContent(),
                ContextCompat.getColor(getContext(), R.color.text_4949));
        return spannableString;
    }

    private TextView createText() {
        TextView textView = new TextView(getContext());
        textView.setLineSpacing(3, 1);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.text_476E92));
        return textView;
    }
}
