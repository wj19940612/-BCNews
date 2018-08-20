package com.sbai.bcnews.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.ProjectGradeActivity;
import com.sbai.bcnews.activity.ProjectRecommendActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.activity.mine.QKCActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.Banner;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.service.DownloadService;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.PermissionUtil;
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
import static com.sbai.bcnews.view.HomeBanner.AdvertisementAdapter.IMAGE_CENTER_CROP;

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
    private LinearLayout mHeader;

    private int mPage;
    private boolean mHasBanner;
    private String mChannel;
    private boolean mLoaded;

    public static NewsFragment newsInstance(boolean hasBanner, String channel) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(HAS_BANNER, hasBanner);
        bundle.putString(CHANNEL, channel);
        newsFragment.setArguments(bundle);
        return newsFragment;
    }

    private BroadcastReceiver mDownloadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtil.show(R.string.download_complete);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDownloadBroadcastReceiver,
                new IntentFilter(DownloadService.ACTION_DOWNLOAD_COMPLETE));
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
        requestBanners();
        if (mHasBanner) {
            loadFirstData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshReadStatus();
        startScheduleJob(TIME_ONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScheduleJob();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(getActivity())
                    .unregisterReceiver(mDownloadBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
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
        if (isVisibleToUser && isAdded()) {
            loadFirstData();
        }
    }

    private void loadFirstData() {
        if (!mLoaded) {
            mLoaded = true;
            loadCacheData();
            loadData(true);
        }
    }

    private void initView() {
        mNewsWraps = new ArrayList<>();
        mEmptyView.setVisibility(View.VISIBLE);
        mEmptyView.setNoData("");
        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), mNewsWraps, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                if (newsDetail.getIsAdvert() > 0) {
                    if (newsDetail.getUrlType() > 0 && !TextUtils.isEmpty(newsDetail.getAndroidUrl())) {
                        downLoadApp(newsDetail.getAndroidUrl());
                    } else if (!TextUtils.isEmpty(newsDetail.getAndroidUrl())) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_URL, newsDetail.getAndroidUrl())
                                .execute();
                    }
                    NewsReadCache.markNewsRead(newsDetail.getId());
                } else {
                    Launcher.with(getActivity(), NewsDetailActivity.class)
                            .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId())
                            .putExtra(CHANNEL, mChannel).execute();
                }
            }
        });
        if (mHasBanner) {
            NewsWithHeaderAdapter newsWithHeaderAdapter = new NewsWithHeaderAdapter(newsAdapter);
            mNewsAdapter = newsWithHeaderAdapter;
            ((NewsWithHeaderAdapter) mNewsAdapter).setHasFoot(true);
        } else {
            mNewsAdapter = newsAdapter;
            ((NewsAdapter) mNewsAdapter).setHasFoot(true);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
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
            mHeader = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_banner, null);
            mHomeBanner = mHeader.findViewById(R.id.banner);
//            mHomeBanner.setLayoutParams(new RecyclerView.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(BANNER_HEIGHT, getResources())));
            mHomeBanner.setOnViewClickListener(new HomeBanner.OnViewClickListener() {
                @Override
                public void onBannerClick(Banner information) {
                    if (information.getJumpType() == Banner.JUMP_TYPE_RICH_TEXT && !TextUtils.isEmpty(information.getJumpContent())) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_HTML, information.getJumpContent())
                                .putExtra(WebActivity.EX_TITLE, information.getTitle())
                                .execute();
                    } else if (information.getJumpType() == Banner.JUMP_TYPE_HTML) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_URL, information.getJumpContent())
                                .putExtra(WebActivity.EX_TITLE, information.getTitle())
                                .execute();
                    } else if (information.getJumpType() == Banner.JUMP_TYPE_NEWS) {
                        Launcher.with(getActivity(), NewsDetailActivity.class)
                                .putExtra(ExtraKeys.NEWS_ID, information.getJumpContent())
                                .execute();
                    } else if (information.getJumpType() == Banner.JUMP_TYPE_ACTIVITY) {
                        Launcher.with(getActivity(), WebActivity.class)
                                .putExtra(WebActivity.EX_URL, Apic.url.ACTIVITY_URL + "?id=" + information.getId())
//                                .putExtra(WebActivity.EX_TITLE, information.getActivityName())
                                .execute();
                    }
                    Apic.requesBannerUpdate(information.getId()).tag(TAG).fireFreely();
                }
            });

            mHeader.findViewById(R.id.recommendBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Launcher.with(getActivity(), ProjectRecommendActivity.class).execute();
                }
            });

            mHeader.findViewById(R.id.gradeBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Launcher.with(getActivity(), ProjectGradeActivity.class).execute();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHomeBanner != null) {
            mHomeBanner.detachFromWindow();
        }
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
        Apic.requestNewsListWithChannel(channelParam, mPage)
                .tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
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
                        if (mNewsWraps.size() == 0)
                            mEmptyView.setVisibility(View.VISIBLE);
                        else
                            mEmptyView.setVisibility(View.GONE);
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
            refreshFoot(0);
            mNewsAdapter.notifyDataSetChanged();
            return;
        }
        if (refresh) {
            NewsSummaryCache.markNewsSummarys(mChannel, data);
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
        refreshFoot(data.size());
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
                            ((NewsWithHeaderAdapter) mNewsAdapter).setHeaderView(null);
                            mNewsAdapter.notifyDataSetChanged();
                        }
                    } else if (mHomeBanner != null) {
                        if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                            ((NewsWithHeaderAdapter) mNewsAdapter).setHeaderView(mHeader);
                            mHomeBanner.setHomeAdvertisement(data, IMAGE_CENTER_CROP);
                            mNewsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }).fireFreely();
        }
    }

    private void refreshFoot(int size) {
        if (mNewsWraps.size() >= Apic.DEFAULT_PAGE_SIZE && size < Apic.DEFAULT_PAGE_SIZE) {
            if (mNewsAdapter instanceof NewsAdapter) {
                ((NewsAdapter) mNewsAdapter).setHasFoot(true);
            } else if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                ((NewsWithHeaderAdapter) mNewsAdapter).setHasFoot(true);
            }
        } else {
            if (mNewsAdapter instanceof NewsAdapter) {
                ((NewsAdapter) mNewsAdapter).setHasFoot(false);
            } else if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                ((NewsWithHeaderAdapter) mNewsAdapter).setHasFoot(false);
            }
        }
    }

    private void refreshReadStatus() {
        if (mNewsWraps != null && mNewsWraps.size() != 0) {
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    private void downLoadApp(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        if (isStoragePermissionGranted()) {
            Intent intent = new Intent(getActivity(), DownloadService.class);
            intent.putExtra(DownloadService.KEY_DOWN_LOAD_URL, downloadUrl);
            getActivity().startService(intent);
        }
    }

    private boolean isStoragePermissionGranted() {
        return PermissionUtil.isStoragePermissionGranted(getActivity(), PermissionUtil.REQ_CODE_ASK_PERMISSION);
    }

}
