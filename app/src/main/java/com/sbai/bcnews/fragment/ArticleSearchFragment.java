package com.sbai.bcnews.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsAdapter;
import com.sbai.bcnews.utils.news.NewsSummaryCache;
import com.sbai.bcnews.utils.news.NewsWithHeaderAdapter;
import com.sbai.bcnews.view.dialog.AttentionDialog;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.ExtraKeys.CHANNEL;

public class ArticleSearchFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private Unbinder mBind;

    private int mPage;
    private String mSearchContent;
    private List<NewsWrap> mData;
    private NewsAdapter mNewsAdapter;

    public static AuthorSearchFragment newsInstance(String searchContent) {
        AuthorSearchFragment authorSearchFragment = new AuthorSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraKeys.SEARCH_CONTENT, searchContent);
        authorSearchFragment.setArguments(bundle);
        return authorSearchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchContent = getArguments().getString(ExtraKeys.SEARCH_CONTENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        requestNews(true);
    }

    private void initView() {
        mData = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getActivity(), mData, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId()).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @Override
    public void onLoadMore() {
        requestNews(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestNews(true);
    }

    private void requestNews(final boolean isRefresh) {
        Apic.requestSearchNews(mSearchContent, mPage).tag(TAG).callback(new Callback2D<Resp<List<NewsDetail>>, List<NewsDetail>>() {
            @Override
            protected void onRespSuccessData(List<NewsDetail> data) {
                updateData(data,isRefresh);
            }
        }).fireFreely();
    }

    private void updateData(List<NewsDetail> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            refreshFoot(0);
            mNewsAdapter.notifyDataSetChanged();
            return;
        }
        if (refresh) {
            mData.clear();
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mData.addAll(NewsWrap.updateImgType(data));
        refreshFoot(data.size());
        mNewsAdapter.notifyDataSetChanged();
    }

    private void refreshFoot(int size) {
        if (mData.size() >= Apic.DEFAULT_PAGE_SIZE && size < Apic.DEFAULT_PAGE_SIZE) {
            mNewsAdapter.setHasFoot(true);
        } else {
            mNewsAdapter.setHasFoot(false);
        }
    }

}
