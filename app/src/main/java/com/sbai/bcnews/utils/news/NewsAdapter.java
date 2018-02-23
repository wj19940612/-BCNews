package com.sbai.bcnews.utils.news;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.utils.DateUtil;
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

    public NewsAdapter(Context context, List<NewsWrap> newsWraps, OnItemClickListener onItemClickListener) {
        mContext = context;
        items = newsWraps;
        mOnItemClickListener = onItemClickListener;
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
            ((NewsAdapter.NoneHolder) holder).bindingData(mContext, items.get(position ).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
        } else if (holder instanceof NewsAdapter.SingleHolder) {
            ((NewsAdapter.SingleHolder) holder).bindingData(mContext, items.get(position ).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
        } else {
            ((NewsAdapter.ThreeHolder) holder).bindingData(mContext, items.get(position ).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
        }
    }

    static class NoneHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView)
        View mRootView;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.original)
        TextView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.time)
        TextView mTime;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;

        NoneHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count, final NewsAdapter.OnItemClickListener onItemClickListener) {
            mTitle.setText(item.getTitle());
            mSource.setText(item.getSource());
            mTime.setText(DateUtil.formatNewsStyleTime(item.getReleaseTime()));
            mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.text));
            mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);
            mSource.setVisibility(TextUtils.isEmpty(item.getSource()) ? View.GONE : View.VISIBLE);

            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        mTitle.setTextColor(ContextCompat.getColor(context, R.color.unluckyText));
                        item.setRead(true);
                        onItemClickListener.onItemClick(item);
                    }
                }
            });

            if (count - 1 == position) {
                mLine.setVisibility(View.GONE);
            } else {
                mLine.setVisibility(View.VISIBLE);
            }
        }
    }

    static class SingleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rootView)
        View mRootView;
        @BindView(R.id.img)
        ImageView mImg;
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.original)
        TextView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.time)
        TextView mTime;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;

        SingleHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count, final NewsAdapter.OnItemClickListener onItemClickListener) {
            mTitle.setText(item.getTitle());
            mSource.setText(item.getSource());
            mTime.setText(DateUtil.formatNewsStyleTime(item.getReleaseTime()));
            mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.text));
            mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);
            mSource.setVisibility(TextUtils.isEmpty(item.getSource()) ? View.GONE : View.VISIBLE);
            if (item.getImgs() != null && item.getImgs().size() > 0) {
                mImg.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(0))
                        .placeholder(R.drawable.ic_default_news)
                        .centerCrop()
                        .into(mImg);
            } else {
                mImg.setVisibility(View.GONE);
            }
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        mTitle.setTextColor(ContextCompat.getColor(context, R.color.unluckyText));
                        item.setRead(true);
                        onItemClickListener.onItemClick(item);
                    }
                }
            });
            if (count - 1 == position) {
                mLine.setVisibility(View.GONE);
            } else {
                mLine.setVisibility(View.VISIBLE);
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
        TextView mOriginal;
        @BindView(R.id.source)
        TextView mSource;
        @BindView(R.id.time)
        TextView mTime;
        @BindView(R.id.contentRL)
        RelativeLayout mContentRL;
        @BindView(R.id.line)
        View mLine;

        ThreeHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindingData(final Context context, final NewsDetail item, int position, int count, final NewsAdapter.OnItemClickListener onItemClickListener) {
            mTitle.setText(item.getTitle());
            mSource.setText(item.getSource());
            mTime.setText(DateUtil.formatNewsStyleTime(item.getReleaseTime()));

            mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.text));
            mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);
            mSource.setVisibility(TextUtils.isEmpty(item.getSource()) ? View.GONE : View.VISIBLE);
            if (item.getImgs() != null && item.getImgs().size() > 0) {
                mImg1.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(0))
                        .placeholder(R.drawable.ic_default_news)
                        .centerCrop()
                        .into(mImg1);
            } else {
                mImg1.setVisibility(View.INVISIBLE);
            }

            if (item.getImgs() != null && item.getImgs().size() > 1) {
                mImg2.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(1))
                        .placeholder(R.drawable.ic_default_news)
                        .centerCrop()
                        .into(mImg2);
            } else {
                mImg2.setVisibility(View.INVISIBLE);
            }

            if (item.getImgs() != null && item.getImgs().size() > 2) {
                mImg3.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(item.getImgs().get(2))
                        .placeholder(R.drawable.ic_default_news)
                        .centerCrop()
                        .into(mImg3);
            } else {
                mImg3.setVisibility(View.INVISIBLE);
            }
            mRootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        mTitle.setTextColor(ContextCompat.getColor(context, R.color.unluckyText));
                        item.setRead(true);
                        onItemClickListener.onItemClick(item);
                    }
                }
            });
            if (count - 1 == position) {
                mLine.setVisibility(View.GONE);
            } else {
                mLine.setVisibility(View.VISIBLE);
            }
        }
    }
}
