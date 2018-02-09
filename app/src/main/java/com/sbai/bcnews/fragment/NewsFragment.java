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
import com.sbai.bcnews.view.EmptyView;
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
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;

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
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                loadData(true);
            }
        });
    }

    @OnClick({R.id.titleBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleBar:
                scrollToFirstView();
                break;
        }
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
                if (data != null && data.getContent() != null && data.getContent().size() != 0) {
                    if (mNewsDetails.size() > 0 && data.getContent().get(0).getId().equals(mNewsDetails.get(0).getId())) {
                        refreshSuccess(R.string.no_more_new_news);
                    } else {
                        refreshSuccess();
                    }
                } else {
                    refreshSuccess();
                }
                updateData(data.getContent(), refresh);
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                refreshFailure();
                loadCacheData();
            }

        }).fireFreely();
    }

    private void loadCacheData() {
        List<NewsDetail> newsDetails = NewsSummaryCache.getNewsSummaryCache();
        if (mNewsDetails.size() == 0) {
            newsDetails = NewsReadCache.filterReadCache(newsDetails);
            if (newsDetails == null || newsDetails.size() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mNewsDetails.addAll(newsDetails);
                mNewsAdapter.refresh();
            }

        }
    }

    private void updateData(List<NewsDetail> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            mEmptyView.setVisibility(View.VISIBLE);
            return;
        }
        mEmptyView.setVisibility(View.GONE);
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
        mNewsAdapter.refresh();
    }

    private void scrollToFirstView() {
        int firstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition > 20) {
            mRecyclerView.scrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(0);
        }
        mNewsAdapter.refresh();
    }

    public static class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_NONE = 1;
        public static final int TYPE_SINGLE = 2;
        public static final int TYPE_THREE = 3;

        interface OnItemClickListener {
            public void onItemClick(NewsDetail newsDetail);
        }

        private Context mContext;
        private List<NewsDetail> items;
        private OnItemClickListener mOnItemClickListener;


        public NewsAdapter(Context context, List<NewsDetail> newsDetails, OnItemClickListener onItemClickListener) {
            mContext = context;
            items = newsDetails;
            mOnItemClickListener = onItemClickListener;
        }

        public void refresh() {
            notifyDataSetChanged();
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
            if (thePicNum == 0) {
                return TYPE_NONE;
            } else if (thePicNum == 1) {
                return TYPE_SINGLE;
            } else {
                if (position <= 4) {
                    return TYPE_SINGLE;
                }
                //前面五张全是单张模式，这里才显示3张图片
                if (judgeFiveSingleMode(position)) {
                    return TYPE_THREE;
                } else {
                    return TYPE_SINGLE;
                }
            }
        }

        //前面五item是否全为单张模式
        private boolean judgeFiveSingleMode(int position) {
            if (position <= 4) {
                return true;
            }
            for (int i = position - 1; i > position - 5; i--) {
                if (items.get(i).getImgs().size() > 1 && !judgeFiveSingleMode(i)) {
                    return false;
                }
            }
            return true;
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.blackPrimary));
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.blackPrimary));
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.blackPrimary));
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
