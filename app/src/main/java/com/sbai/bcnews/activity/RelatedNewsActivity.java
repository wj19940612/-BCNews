package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;

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
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.news.NewsAdapter;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedNewsActivity extends RecycleViewSwipeLoadActivity {

    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;

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
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

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

        requestRelatedNews(REFRESH);
    }

    private void initData(Intent intent) {
        mMarketData = intent.getParcelableExtra(ExtraKeys.DIGITAL_CURRENCY);
        mTag = mMarketData.getName();
    }

    private void initViews() {
        SpannableString spannableString = StrUtil.mergeTextWithColor(
                mMarketData.getName().toUpperCase() + "/" + mMarketData.getCurrencyMoney().toUpperCase(),
                " " + mMarketData.getExchangeCode(), ContextCompat.getColor(getActivity(), R.color.luckyText));
                mTitleBar.setBackText(spannableString);

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
                requestRelatedNews(REFRESH);
            }
        });
    }

    private void requestRelatedNews(final int operation) {
        Apic.getRelatedNews(mTag, mPage).tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
                    @Override
                    protected void onRespSuccessData(News data) {
                        List<NewsDetail> newsDetailList = data.getContent();
                        if (newsDetailList == null) {
                            newsDetailList = new ArrayList<>();
                        }

                        if (operation == REFRESH) {
                            boolean isLatest = false;
                            if (!newsDetailList.isEmpty()) { // replace
                                if (!mNewsWrapList.isEmpty()) {
                                    isLatest = mNewsWrapList.get(0).getNewsDetail().getId()
                                            .equals(newsDetailList.get(0).getId());
                                }
                                mNewsWrapList.clear();
                                mNewsWrapList.addAll(NewsWrap.updateImgType(newsDetailList));
                                NewsSummaryCache.markNewsSummarys(mTag, newsDetailList);
                            }

                            if (newsDetailList.size() < Apic.DEFAULT_PAGE_SIZE) {
                                mSwipeToLoadLayout.setLoadMoreEnabled(false);
                            }

                            if (mNewsWrapList.isEmpty()) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mSwipeToLoadLayout.setRefreshEnabled(false);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mSwipeToLoadLayout.setRefreshEnabled(true);
                            }

                            mPage++;
                            mNewsAdapter.notifyDataSetChanged();
                            if (isLatest) {
                                refreshComplete(R.string.no_more_new_news);
                            } else {
                                refreshSuccess();
                            }
                        } else if (operation == LOAD_MORE) {
                            if (newsDetailList.size() < Apic.DEFAULT_PAGE_SIZE) {
                                mSwipeToLoadLayout.setLoadMoreEnabled(false);
                            }

                            mPage++;
                            mNewsWrapList.addAll(NewsWrap.updateImgType(newsDetailList));
                            mNewsAdapter.notifyDataSetChanged();
                        }
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
        requestRelatedNews(LOAD_MORE);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestRelatedNews(REFRESH);
    }
}
