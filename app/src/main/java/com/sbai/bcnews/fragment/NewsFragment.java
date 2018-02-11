package com.sbai.bcnews.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.Banner;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.HomeBanner;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.ExtraKeys.CHANNEL;
import static com.sbai.bcnews.ExtraKeys.HEADER_COUNT;
import static com.sbai.bcnews.fragment.HomeNewsFragment.SCROLL_GLIDING;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 资讯
 * <p>
 * APIs:
 */
public class NewsFragment extends RecycleViewSwipeLoadFragment {

    public static final int HAS_BANNER = 1;
    public static final int NO_BANNER = 0;

    public static final int BANNER_HEIGHT = 170;

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
    private List<Banner> mBanners;

    private HomeBanner mHomeBanner;

    private int mPage;
    private int mHeaderCount;
    private OnScrollListener mOnScrollListener;
    private String mChannel;

    public interface OnScrollListener {
        public void onScroll(int dy);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public static NewsFragment newsInstance(int headerCount, String channel) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(HEADER_COUNT, headerCount);
        bundle.putString(CHANNEL, channel);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHeaderCount = getArguments().getInt(HEADER_COUNT);
            mChannel = getArguments().getString(CHANNEL);
        }
    }

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
        requestBanners();
    }

    private void initView() {
        mNewsDetails = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getActivity(), mNewsDetails, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                NewsReadCache.markNewsRead(newsDetail);
                Launcher.with(getActivity(), NewsDetailActivity.class).putExtra(ExtraKeys.NEWS_ID, newsDetail.getId()).putExtra(CHANNEL, mChannel).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(dy);
                }
            }
        });
        mSwipeRefreshHeader.setStartRefreshListener(new RefreshHeaderView.OnStartRefreshListener() {
            @Override
            public void onStartRefresh() {
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(-SCROLL_GLIDING);
                }
            }
        });
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                mEmptyView.setSelected(true);
                loadData(true);
            }
        });
        initBannerView();
    }

    private void initBannerView() {
        if (mHeaderCount > 0) {
            mHomeBanner = (HomeBanner) LayoutInflater.from(getActivity()).inflate(R.layout.item_banner, null);
            mHomeBanner.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(BANNER_HEIGHT, getResources())));
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
        String channelParam = Uri.encode(mChannel);
        Apic.requestNewsListWithChannel(channelParam, mPage).tag(TAG).callback(new Callback2D<Resp<News>, News>() {
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
                if (mNewsDetails.size() == 0 && (data.getContent() == null || data.getContent().size() == 0) && mEmptyView.isSelected()) {
                    ToastUtil.show(R.string.no_news);
                }
                mEmptyView.setSelected(false);
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

    private void requestBanners() {
        if (mHeaderCount != 0) {
            Apic.requestBanners().tag(TAG).callback(new Callback2D<Resp<List<Banner>>, List<Banner>>() {
                @Override
                protected void onRespSuccessData(List<Banner> data) {
//                    data = getTestBanner();
                    if (data == null || data.size() == 0) {
                        mNewsAdapter.setHeaderView(null);
                    } else if (mHomeBanner != null) {
                        mNewsAdapter.setHeaderView(mHomeBanner);
                        mHomeBanner.setHomeAdvertisement(data);
                        mNewsAdapter.refresh();
                    }
                }
            }).fireFreely();
        }
    }

    private List<Banner> getTestBanner() {
        Banner banner = new Banner();
        banner.setClicks(455);
        banner.setContent("https://lemi.ailemi.com/lm/activityk.html");
        banner.setCover("https://esongb.oss-cn-shanghai.aliyuncs.com/ueditor/1514517627375015440.png");
        banner.setId("5a45b48a87761dd50d4e52ec");

        Banner banner1 = new Banner();
        banner1.setClicks(455);
        banner1.setContent("https://lemi.ailemi.com/lm/banner/acer.html");
        banner1.setCover("https://esongb.oss-cn-shanghai.aliyuncs.com/ueditor/1513563771235023353.png");
        banner1.setId("5a3726af87761961bba3372c");
        List<Banner> homeBanners = new ArrayList<>();
        homeBanners.add(banner);
        homeBanners.add(banner1);
        return homeBanners;
    }

    public static class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int TYPE_NONE = 1;
        public static final int TYPE_SINGLE = 2;
        public static final int TYPE_THREE = 3;
        public static final int TYPE_BANNER = 4;

        interface OnItemClickListener {
            public void onItemClick(NewsDetail newsDetail);
        }

        private Context mContext;
        private List<NewsDetail> items;
        private OnItemClickListener mOnItemClickListener;
        private View mHeadView;
        private int mHeaderCount;

        public NewsAdapter(Context context, List<NewsDetail> newsDetails, OnItemClickListener onItemClickListener) {
            mContext = context;
            items = newsDetails;
            mOnItemClickListener = onItemClickListener;
        }

        public void setHeaderView(View homeBanner) {
            //没banner，不要再插入头
            if (homeBanner == null) {
                mHeaderCount = NO_BANNER;
                return;
            }
            mHeadView = homeBanner;
            mHeaderCount = HAS_BANNER;
            notifyItemInserted(0);//插入下标0位置
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
            if (mHeaderCount > 0)
                return items == null ? 0 : items.size() + 1;
            else
                return items == null ? 0 : items.size();
        }


        @Override
        public int getItemViewType(int position) {
            if (mHeadView != null && position == 0) {
                return TYPE_BANNER;
            }
            NewsDetail news = items.get(position - mHeaderCount);
            int thePicNum = news.getImgs().size();
            if (thePicNum == 0) {
                return TYPE_NONE;
            } else if (thePicNum < 3) {
                return TYPE_SINGLE;
            } else {
                if (position <= 5 - (1 - mHeaderCount)) {
                    return TYPE_SINGLE;
                }
                //前面五张全是单张模式，这里才显示3张图片
                if (judgeFiveSingleMode(position - 1)) {
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
                if (items.get(i).getImgs().size() > 2 && !judgeFiveSingleMode(i)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_BANNER) {
                return new BannerHolder(mHeadView);
            } else if (viewType == TYPE_NONE) {
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
            if (holder instanceof BannerHolder) {

            } else if (holder instanceof NoneHolder) {
                ((NoneHolder) holder).bindingData(mContext, items.get(position - mHeaderCount), position, getItemCount(), mOnItemClickListener);
            } else if (holder instanceof SingleHolder) {
                ((SingleHolder) holder).bindingData(mContext, items.get(position - mHeaderCount), position, getItemCount(), mOnItemClickListener);
            } else {
                ((ThreeHolder) holder).bindingData(mContext, items.get(position - mHeaderCount), position, getItemCount(), mOnItemClickListener);
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
                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.primaryText));
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

                mTitle.setTextColor(item.isRead() ? ContextCompat.getColor(context, R.color.unluckyText) : ContextCompat.getColor(context, R.color.primaryText));
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

        static class BannerHolder extends RecyclerView.ViewHolder {
            public BannerHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
