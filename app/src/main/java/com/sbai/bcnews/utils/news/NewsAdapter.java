package com.sbai.bcnews.utils.news;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.glide.GlideRoundAndCenterCropTransform;
import com.sbai.glide.GlideApp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zhang on 2018\2\23 0023.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(NewsDetail newsDetail);
    }

    private Context mContext;
    private List<NewsWrap> items;
    private NewsAdapter.OnItemClickListener mOnItemClickListener;
    private boolean mSplitViewVisible;
    private boolean mHasFoot;
    private String mSearchContent;

    public NewsAdapter(Context context, List<NewsWrap> newsWraps, OnItemClickListener onItemClickListener) {
        mContext = context;
        items = newsWraps;
        mOnItemClickListener = onItemClickListener;
        mSplitViewVisible = false;
    }

    public NewsAdapter(Context context, List<NewsWrap> newsWraps, OnItemClickListener onItemClickListener, boolean splitViewVisible) {
        mContext = context;
        items = newsWraps;
        mOnItemClickListener = onItemClickListener;
        mSplitViewVisible = splitViewVisible;
    }

    public void setSearchContent(String searchContent) {
        mSearchContent = searchContent;
    }

    public void setHasFoot(boolean hasFoot) {
        mHasFoot = hasFoot;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getImgType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NewsWrap.DISPLAY_TYPE_NO_IMAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_none, parent, false);
            return new NewsAdapter.NoneHolder(view);
        } else if (viewType == NewsWrap.DISPLAY_TYPE_SINGLE_IMAGE) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_single, parent, false);
            return new NewsAdapter.SingleHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_three, parent, false);
            return new NewsAdapter.ThreeHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsAdapter.NoneHolder) {
            ((NewsAdapter.NoneHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener, mSplitViewVisible, mHasFoot, mSearchContent);
        } else if (holder instanceof NewsAdapter.SingleHolder) {
            ((NewsAdapter.SingleHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener, mSplitViewVisible, mHasFoot, mSearchContent);
        } else {
            ((NewsAdapter.ThreeHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener, mHasFoot, mSearchContent);
        }
    }

    static class NoneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView)
        View mRootView;
        @BindView(R.id.splitView)
        View mSplitView;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.original)
        ImageView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.footer)
        LinearLayout mFooter;

        NoneHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count,
                                final OnItemClickListener onItemClickListener, boolean splitViewVisible, boolean mHasFoot, String searchContent) {
            if (position == 0 && splitViewVisible) {
                mSplitView.setVisibility(View.VISIBLE);
            } else {
                mSplitView.setVisibility(View.GONE);
            }

            setBasicView(context, item, position, count, onItemClickListener, mHasFoot, mTitle, mSource, mOriginal, mContentRL, mLine, mFooter, searchContent);
        }
    }

    static class SingleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView)
        View mRootView;
        @BindView(R.id.splitView)
        View mSplitView;
        @BindView(R.id.img)
        ImageView mImg;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.original)
        ImageView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.footer)
        LinearLayout mFooter;

        SingleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count,
                                final OnItemClickListener onItemClickListener, boolean splitViewVisible, boolean mHasFoot, String searchContent) {
            if (position == 0 && splitViewVisible) {
                mSplitView.setVisibility(View.VISIBLE);
            } else {
                mSplitView.setVisibility(View.GONE);
            }
            setBasicView(context, item, position, count, onItemClickListener, mHasFoot, mTitle, mSource, mOriginal, mContentRL, mLine, mFooter, searchContent);

            if (item.getImgs() != null && item.getImgs().size() > 0) {
                mImg.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(0))
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .placeholder(R.drawable.ic_default_news)
                        .into(mImg);
            } else {
                mImg.setVisibility(View.GONE);
            }
        }
    }

    static class ThreeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView)
        View mRootView;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.img1)
        ImageView mImg1;
        @BindView(R.id.img2)
        ImageView mImg2;
        @BindView(R.id.img3)
        ImageView mImg3;
        @BindView(R.id.original)
        ImageView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;
        @BindView(R.id.footer)
        LinearLayout mFooter;

        ThreeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count,
                                final NewsAdapter.OnItemClickListener onItemClickListener, boolean mHasFoot, String searchContent) {

            setBasicView(context, item, position, count, onItemClickListener, mHasFoot, mTitle, mSource, mOriginal, mContentRL, mLine, mFooter, searchContent);

            if (item.getImgs() != null && item.getImgs().size() > 0) {
                mImg1.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(0))
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .placeholder(R.drawable.ic_default_news)
                        .into(mImg1);
            } else {
                mImg1.setVisibility(View.INVISIBLE);
            }

            if (item.getImgs() != null && item.getImgs().size() > 1) {
                mImg2.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(1))
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .placeholder(R.drawable.ic_default_news)
                        .into(mImg2);
            } else {
                mImg2.setVisibility(View.INVISIBLE);
            }

            if (item.getImgs() != null && item.getImgs().size() > 2) {
                mImg3.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(2))
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .placeholder(R.drawable.ic_default_news)
                        .into(mImg3);
            } else {
                mImg3.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 显示三种Type都需要显示的View的内容
     */
    public static void setBasicView(final Context context, final NewsDetail item, int position, int count,
                                    final NewsAdapter.OnItemClickListener onItemClickListener, boolean mHasFoot, final TextView mTitle, TextView mSource, ImageView mOriginal, RelativeLayout mContentRL, View mLine, View mFooter, String searchContent) {
        if (item.getIsAdvert() > 0) {
            mTitle.setText(item.getAdvertCopyWriter());
            mSource.setText(context.getString(R.string.source_x_time_x_read_x, item.getAdvertName(), context.getString(R.string.advert), ""));
        } else {
            if (TextUtils.isEmpty(searchContent)) {
                mTitle.setText(item.getTitle());
            } else {
                mTitle.setText(StrUtil.changeSpecialTextColor(item.getTitle().trim(), searchContent, ContextCompat.getColor(context, R.color.colorPrimary)));
            }
            String readCount;
            if (item.getShowReadCount() > 99999) {
                readCount = context.getString(R.string.x_ten_thousand_people_read, item.getShowReadCount() / 10000);
            } else {
                readCount = context.getString(R.string.x_people_read, item.getShowReadCount());
            }
            if (TextUtils.isEmpty(item.getAuthor())) {
                mSource.setText(context.getString(R.string.time_x_read_x, DateUtil.formatDefaultStyleTime(item.getReleaseTime()), readCount));
            } else {
                mSource.setText(context.getString(R.string.source_x_time_x_read_x, item.getAuthor(), DateUtil.formatDefaultStyleTime(item.getReleaseTime()), readCount));
            }
        }
        mTitle.setTextColor(NewsReadCache.isRead(item.getId()) ? ContextCompat.getColor(context, R.color.text_999) : ContextCompat.getColor(context, R.color.text_222));
        mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);

        mContentRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    mTitle.setTextColor(ContextCompat.getColor(context, R.color.text_999));
                    onItemClickListener.onItemClick(item);
                }
            }
        });

        if (count - 1 == position) {
            mLine.setVisibility(View.GONE);
        } else {
            mLine.setVisibility(View.VISIBLE);
        }

        if (mHasFoot && count - 1 == position) {
            mFooter.setVisibility(View.VISIBLE);
        } else {
            mFooter.setVisibility(View.GONE);
        }
    }
}
