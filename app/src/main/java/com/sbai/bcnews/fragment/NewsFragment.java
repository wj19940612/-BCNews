package com.sbai.bcnews.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.Banner;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.news.NewsAdapter;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.utils.news.NewsWithHeaderAdapter;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.HomeBanner;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.ExtraKeys.CHANNEL;
import static com.sbai.bcnews.ExtraKeys.HAS_BANNER;
import static com.sbai.bcnews.fragment.HomeNewsFragment.SCROLL_GLIDING;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 资讯
 * <p>
 * APIs:
 */
public class NewsFragment extends RecycleViewSwipeLoadFragment {

    public static final int BANNER_HEIGHT = 170;
    public static final int TIME_ONE = 1000;//1次轮询为1s
    public static final int TIME_HANDLER_THREE = 3;//3次轮询时间

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

    private RecyclerView.Adapter mNewsAdapter;
    private List<NewsWrap> mNewsWraps;

    private HomeBanner mHomeBanner;

    private int mPage;
    private boolean mHasBanner;
    private OnScrollListener mOnScrollListener;
    private String mChannel;
    private boolean mIsVisible;

    public interface OnScrollListener {
        void onScroll(int dy);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public static NewsFragment newsInstance(boolean hasBanner, String channel) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(HAS_BANNER, hasBanner);
        bundle.putString(CHANNEL, channel);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mHasBanner = getArguments().getBoolean(HAS_BANNER);
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
        loadCacheData();
        loadData(true);
        requestBanners();
    }

    @Override
    public void onResume() {
        super.onResume();
        startScheduleJob(TIME_ONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScheduleJob();
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        if (count % TIME_HANDLER_THREE == 0 && mHomeBanner != null) {
            mHomeBanner.nextAdvertisement();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mIsVisible = true;
        } else {
            mIsVisible = false;
        }
    }

    private void initView() {
        mNewsWraps = new ArrayList<>();
        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), mNewsWraps, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                NewsReadCache.markNewsRead(newsDetail);
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId())
                        .putExtra(CHANNEL, mChannel).execute();
            }
        });
        if (mHasBanner) {
            NewsWithHeaderAdapter newsWithHeaderAdapter = new NewsWithHeaderAdapter(newsAdapter);
            mNewsAdapter = newsWithHeaderAdapter;
        } else {
            mNewsAdapter = newsAdapter;
        }
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
                loadData(true);
            }
        });
        initBannerView();
    }

    private void initBannerView() {
        if (mHasBanner) {
            mHomeBanner = (HomeBanner) LayoutInflater.from(getActivity()).inflate(R.layout.item_banner, null);
            mHomeBanner.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(BANNER_HEIGHT, getResources())));
            mHomeBanner.setOnViewClickListener(new HomeBanner.OnViewClickListener() {
                @Override
                public void onBannerClick(Banner information) {
                    if (information.getJumpType() == Banner.JUMP_TYPE_RICH_TEXT && !TextUtils.isEmpty(information.getContent())) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_HTML, information.getJumpContent())
                                .execute();
                    } else if (information.getJumpType() == Banner.JUMP_TYPE_HTML) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_URL, information.getJumpContent())
                                .putExtra(WebActivity.EX_TITLE, information.getTitle())
                                .execute();
                    }
                }
            });
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
        requestBanners();
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
                    if (mNewsWraps.size() > 0 && data.getContent().get(0).getId().equals(mNewsWraps.get(0).getNewsDetail().getId())) {
                        refreshComplete(R.string.no_more_new_news);
                    } else {
                        refreshSuccess();
                    }
                } else {
                    refreshSuccess();
                }
                if (mNewsWraps.size() == 0 && data.getContent().size() == 0) {
                    mEmptyView.setNoData(getString(R.string.no_main_news));
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                }
                updateData(data.getContent(), refresh);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                mEmptyView.setNoNet();
                mEmptyView.setVisibility(View.VISIBLE);
                if (refresh)
                    refreshFailure();
            }

        }).fireFreely();
    }

    private void loadCacheData() {
        List<NewsDetail> newsDetails = NewsSummaryCache.getNewsSummaryCache(mChannel);
        if (mNewsWraps.size() == 0) {
            if (newsDetails == null || newsDetails.size() == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
                mNewsWraps.addAll(NewsWrap.updateImgType(newsDetails));
                mNewsAdapter.notifyDataSetChanged();
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
        NewsSummaryCache.markNewsSummarys(mChannel, data);
        if (refresh) {
            mNewsWraps.clear();
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mNewsWraps.addAll(NewsWrap.updateImgType(data));
        mNewsAdapter.notifyDataSetChanged();
    }

    private void requestBanners() {
        if (mHasBanner) {
            Apic.requestBanners().tag(TAG).callback(new Callback2D<Resp<List<Banner>>, List<Banner>>() {
                @Override
                protected void onRespSuccessData(List<Banner> data) {
//                    data = getTestBanner();
                    if (data == null || data.size() == 0) {
                        if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                            ((NewsWithHeaderAdapter) mNewsAdapter).addHeaderView(null);
                            mNewsAdapter.notifyDataSetChanged();
                        }
                    } else if (mHomeBanner != null) {
                        if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                            ((NewsWithHeaderAdapter) mNewsAdapter).addHeaderView(mHomeBanner);
                            mHomeBanner.setHomeAdvertisement(data);
                            mNewsAdapter.notifyDataSetChanged();
                        }
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

}
