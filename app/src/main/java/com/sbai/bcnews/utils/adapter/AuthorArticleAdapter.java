package com.sbai.bcnews.utils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.glide.GlideRoundAndCenterCropTransform;
import com.sbai.bcnews.view.ThreeImageLayout;
import com.sbai.bcnews.view.recycleview.HeaderViewRecycleViewAdapter;
import com.sbai.glide.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/6/8
 * <p>
 * Description:
 * </p>
 */
public class AuthorArticleAdapter extends HeaderViewRecycleViewAdapter<AuthorArticle, RecyclerView.ViewHolder> {

    private static final int ITEM_TYPE_NONE_OR_SINGLE = 1;
    private static final int ITEM_TYPE_THREE_IMAGE = 2;

    private static final int PAGE_TYPE_AUTHOR_WORKBENCH = 0; //工作台
    public static final int PAGE_TYPE_AUTHOR_INFO = 1;

    private int pageType;

    private OnItemClickListener<AuthorArticle> mItemClickListener;

    private Context mContext;

    public AuthorArticleAdapter(Context context) {
        mContext = context;
    }

    public void setItemClickListener(OnItemClickListener<AuthorArticle> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_NONE_OR_SINGLE:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_author_article_single_or_noe, parent, false);
                return new SingleOrNoneImageViewHolder(view);
            case ITEM_TYPE_THREE_IMAGE:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_author_article_three, parent, false);
                return new MultiImageViewHolder(view1);
        }
        return null;
    }

    @Override
    public void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, AuthorArticle data, int position) {
        if (holder instanceof SingleOrNoneImageViewHolder) {
            SingleOrNoneImageViewHolder singleOrNoneImageViewHolder = (SingleOrNoneImageViewHolder) holder;
            singleOrNoneImageViewHolder.bindDataWithView(data, position, mItemClickListener, mContext, pageType);
        } else if (holder instanceof MultiImageViewHolder) {
            MultiImageViewHolder multiImageViewHolder = (MultiImageViewHolder) holder;
            multiImageViewHolder.bindDataWithView(data, position, mItemClickListener, mContext, pageType);
        }
    }


    @Override
    public int getContentItemViewType(int position) {
        AuthorArticle itemData = getItemData(position);
        if (itemData != null) {
            List<String> articleImgs = itemData.getImgs();
            if (articleImgs != null
                    && !articleImgs.isEmpty()
                    && articleImgs.size() > 1) {
                return ITEM_TYPE_THREE_IMAGE;
            }
            return ITEM_TYPE_NONE_OR_SINGLE;
        }
        return super.getContentItemViewType(position);
    }

    static class SingleOrNoneImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.image)
        ImageView mImage;
        @BindView(R.id.timeLine)
        TextView mTimeLine;
        @BindView(R.id.readNumber)
        TextView mReadNumber;
        @BindView(R.id.reviewNumber)
        TextView mReviewNumber;
        @BindView(R.id.rootView)
        ConstraintLayout mRootView;

        SingleOrNoneImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(final AuthorArticle data, final int position, final OnItemClickListener<AuthorArticle> itemClickListener, Context context, int pageType) {
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(data, position);
                }
            });

            mTitle.setText(data.getTitle());
            mTimeLine.setText(DateUtil.formatDefaultStyleTime(data.getReleaseTime()));
            if (pageType == PAGE_TYPE_AUTHOR_INFO) {
                mReviewNumber.setVisibility(View.GONE);
            }
            mReadNumber.setText(context.getString(R.string.read_number, data.getShowReadCount()));
            mReviewNumber.setText(context.getString(R.string.review_number, data.getDiscussCount()));
            List<String> imgs = data.getImgs();
            if (imgs != null && !imgs.isEmpty()) {
                mImage.setVisibility(View.VISIBLE);
                GlideApp.with(context)
                        .load(imgs.get(0))
                        .placeholder(R.drawable.ic_default_news)
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .into(mImage);
            } else {
                mImage.setVisibility(View.GONE);
            }

        }
    }

    static class MultiImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
//        @BindView(R.id.threeImageLayout)
//        LinearLayout mThreeImageLayout;

        @BindView(R.id.threeImageLayout)
        ThreeImageLayout mThreeImageLayout;
        @BindView(R.id.timeLine)
        TextView mTimeLine;
        @BindView(R.id.readNumber)
        TextView mReadNumber;
        @BindView(R.id.reviewNumber)
        TextView mReviewNumber;
        @BindView(R.id.rootView)
        ConstraintLayout mRootView;

        MultiImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindDataWithView(final AuthorArticle data, final int position, final OnItemClickListener<AuthorArticle> itemClickListener, Context context, int pageType) {
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(data, position);
                }
            });

            mTitle.setText(data.getTitle());
            mTimeLine.setText(DateUtil.formatDefaultStyleTime(data.getReleaseTime()));
            mReadNumber.setText(context.getString(R.string.read_number, data.getShowReadCount()));
            mReviewNumber.setText(context.getString(R.string.review_number, data.getDiscussCount()));
            if (pageType == PAGE_TYPE_AUTHOR_INFO) {
                mReviewNumber.setVisibility(View.GONE);
            }

            if (data.getImgs() != null && data.getImgs().size() > 1) {
                mThreeImageLayout.setImagePath(data.getImgs());
            }
        }
    }
}
