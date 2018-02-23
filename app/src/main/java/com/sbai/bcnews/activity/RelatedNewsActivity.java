package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.market.MarketData;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedNewsActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;

    private View mView;

    private int mPage;
    private String mTag;
    private NewsAdapter mNewsAdapter;
    private List<NewsWrap> mNewsWrapList;
    private MarketData mMarketData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_related_news, null);
        setContentView(mView);
        ButterKnife.bind(this);

        initData(getIntent());
        mPage = 0;
        mNewsWrapList = NewsWrap.updateImgType(NewsSummaryCache.getNewsSummaryCache(mTag));

        initViews();

        requestRelatedNews();
    }

    private void initData(Intent intent) {
        mMarketData = intent.getParcelableExtra(ExtraKeys.DIGITAL_CURRENCY);
        mTag = mMarketData.getName();
    }

    private void initViews() {
        mNewsAdapter = new NewsAdapter(getActivity(), mNewsWrapList, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                NewsReadCache.markNewsRead(newsDetail);
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId())
                        .putExtra(ExtraKeys.TAG, mTag).execute();
            }
        });
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mNewsAdapter);
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                requestRelatedNews();
            }
        });
    }

    private void requestRelatedNews() {
        Apic.getRelatedNews(mTag, mPage).tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
                    @Override
                    protected void onRespSuccessData(News data) {
                        List<NewsDetail> newsDetailList = data.getContent();

                        if (newsDetailList == null || newsDetailList.isEmpty()) {
                            return;
                        }

                        if (!mNewsWrapList.isEmpty()) { // 与当前第一条 id 相同，显示已经是最新
                            NewsDetail firstNews = mNewsWrapList.get(0).getNewsDetail();
                            if (firstNews.getId().equals(newsDetailList.get(0).getId())) {
                                refreshComplete(R.string.no_more_new_news);
                                return;
                            }
                        }

                        if (mPage == 0) { // refresh
                            mNewsWrapList.clear();
                            NewsSummaryCache.markNewsSummarys(mTag, newsDetailList);
                            refreshSuccess();
                        }

                        mNewsWrapList.addAll(NewsWrap.updateImgType(newsDetailList)) ;
                        mPage++;
                        mNewsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                        refreshFailure();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                }).fireFreely();
    }

    @Override
    public View getContentView() {
        return mView;
    }

    @Override
    public void onLoadMore() {
        requestRelatedNews();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestRelatedNews();
    }

    public static class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        interface OnItemClickListener {
            void onItemClick(NewsDetail newsDetail);
        }

        private Context mContext;
        private List<NewsWrap> items;
        private OnItemClickListener mOnItemClickListener;

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
                return new NoneHolder(view);
            } else if (viewType == NewsWrap.DISPLAY_TYPE_SINGLE_IMAGE) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_single, parent, false);
                return new SingleHolder(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_three, parent, false);
                return new ThreeHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NoneHolder) {
                ((NoneHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
            } else if (holder instanceof SingleHolder) {
                ((SingleHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
            } else {
                ((ThreeHolder) holder).bindingData(mContext, items.get(position).getNewsDetail(), position, getItemCount(), mOnItemClickListener);
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

            public void bindingData(final Context context, final NewsDetail item, int position, int count, final OnItemClickListener onItemClickListener) {
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

            public void bindingData(final Context context, final NewsDetail item, int position, int count, final OnItemClickListener onItemClickListener) {
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

            public void bindingData(final Context context, final NewsDetail item, int position, int count, final OnItemClickListener onItemClickListener) {
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
}
