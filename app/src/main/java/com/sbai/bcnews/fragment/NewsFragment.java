package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 资讯
 * <p>
 * APIs:
 */
public class NewsFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    private Unbinder mBind;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private NewsAdapter mNewsAdapter;
    private List<NewsDetail> mNewsDetails;

    private int mPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData(true);
    }

    private void initView() {
        mNewsDetails = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getActivity(), mNewsDetails, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                NewsReadCache.markNewsRead(newsDetail);
                Launcher.with(getActivity(), NewsDetailActivity.class).putExtra(ExtraKeys.NEWS_ID, newsDetail.getId()).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @OnClick({})
    public void onViewClicked(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData(true);
    }

    @Override
    public void triggerRefresh() {
        super.triggerRefresh();
        smoothScrollToTop();
        onRefresh();
    }

    private void loadData(final boolean refresh) {
        Apic.getNewsList(mPage).tag(TAG).callback(new Callback2D<Resp<News>, News>() {
            @Override
            protected void onRespSuccessData(News data) {
                updateData(data.getContent(), refresh);
                mSwipeRefreshHeader.refreshSuccess();
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                loadCacheData();
                mSwipeRefreshHeader.refreshFail();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }
        }).fireFreely();
    }

    private void loadCacheData() {
        List<NewsDetail> newsDetails = NewsSummaryCache.getNewsSummaryCache();
        if (mNewsDetails.size() == 0) {
            newsDetails = NewsReadCache.filterReadCache(newsDetails);
            mNewsDetails.addAll(newsDetails);
            mNewsAdapter.notifyDataSetChanged();
        }
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }

    private void updateData(List<NewsDetail> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            return;
        }
        NewsSummaryCache.markNewsSummarys(data);
        data = NewsReadCache.filterReadCache(data);
        if (refresh) {
            mNewsDetails.clear();
        }
        if (data.size() < Apic.NORMAL_PAGESIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        mPage++;
        mNewsDetails.addAll(data);
        mNewsAdapter.notifyDataSetChanged();
    }

    public static class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_NONE = 1;
        public static final int TYPE_SINGLE = 2;
        public static final int TYPE_THREE = 3;

        interface OnItemClickListener {
            public void onItemClick(NewsDetail newsDetail);
        }

        private int continuesPic;
        private Context mContext;
        private List<NewsDetail> items;
        private OnItemClickListener mOnItemClickListener;


        public NewsAdapter(Context context, List<NewsDetail> newsDetails, OnItemClickListener onItemClickListener) {
            mContext = context;
            items = newsDetails;
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
            NewsDetail news = items.get(position);
            int thePicNum = news.getImgs().size();
            if (continuesPic <= 4) {
                continuesPic++;
                if (thePicNum == 0)
                    return TYPE_NONE;
                else
                    return TYPE_SINGLE;
            } else {
                if (thePicNum == 3) {
                    continuesPic = 0;
                    return TYPE_THREE;
                } else if (thePicNum == 1) {
                    continuesPic++;
                    return TYPE_SINGLE;
                } else {
                    continuesPic++;
                    return TYPE_NONE;
                }
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_NONE) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_none, parent, false);
                return new NoneHolder(view);
            } else if (viewType == TYPE_SINGLE) {
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
                ((NoneHolder) holder).bindingData(mContext, items.get(position), position, getItemCount(), mOnItemClickListener);
            } else if (holder instanceof SingleHolder) {
                ((SingleHolder) holder).bindingData(mContext, items.get(position), position, getItemCount(), mOnItemClickListener);
            } else {
                ((ThreeHolder) holder).bindingData(mContext, items.get(position), position, getItemCount(), mOnItemClickListener);
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.primaryText));
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.primaryText));
                if (item.getImgs() != null && item.getImgs().size() > 0) {
                    GlideApp.with(context).load(item.getImgs().get(0))
                            .placeholder(R.drawable.ic_default_news)
                            .centerCrop()
                            .into(mImg);
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.primaryText));
                if (item.getImgs() != null && item.getImgs().size() > 0) {
                    GlideApp.with(context).load(item.getImgs().get(0))
                            .placeholder(R.drawable.ic_default_news)
                            .centerCrop()
                            .into(mImg1);
                }

                if (item.getImgs() != null && item.getImgs().size() > 1) {
                    GlideApp.with(context).load(item.getImgs().get(1))
                            .placeholder(R.drawable.ic_default_news)
                            .centerCrop()
                            .into(mImg2);
                }

                if (item.getImgs() != null && item.getImgs().size() > 2) {
                    GlideApp.with(context).load(item.getImgs().get(2))
                            .placeholder(R.drawable.ic_default_news)
                            .centerCrop()
                            .into(mImg3);
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
                }
            }
        }
    }
}
