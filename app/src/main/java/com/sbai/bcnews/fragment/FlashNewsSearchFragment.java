package com.sbai.bcnews.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ShareNewsFlashActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.search.SearchEditText;
import com.umeng.analytics.MobclickAgent;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FlashNewsSearchFragment extends RecycleViewSwipeLoadFragment {

    private Unbinder mBind;

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private NewsAdapter mNewsAdapter;
    private String mSearchContent;
    private List<NewsFlash> mData;

    private int mPage;

    private SearchEditText.OnSearchContentResultListener mSearchContentResultListener;

    public static FlashNewsSearchFragment newsInstance(String searchContent) {
        FlashNewsSearchFragment flashNewsSearchFragment = new FlashNewsSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraKeys.SEARCH_CONTENT, searchContent);
        flashNewsSearchFragment.setArguments(bundle);
        return flashNewsSearchFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchEditText.OnSearchContentResultListener) {
            mSearchContentResultListener = (SearchEditText.OnSearchContentResultListener) context;
        }
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
        requestNewsFlash(true);
    }

    private void initView() {
        mData = new ArrayList<>();
        mNewsAdapter = new NewsAdapter(getActivity(), mData);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }

    @Override
    public void onLoadMore() {
        requestNewsFlash(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestNewsFlash(true);
    }

    public void setSearchContent(String searchContent) {
        mSearchContent = searchContent;
        requestNewsFlash(true);
    }

    private void requestNewsFlash(final boolean isRefresh) {
        if (!TextUtils.isEmpty(mSearchContent)) {
            String searchContent = Uri.encode(mSearchContent);
            Apic.requestSearchFlashNews(searchContent, mPage).tag(TAG).callback(new Callback<ListResp<NewsFlash>>() {
                @Override
                protected void onRespSuccess(ListResp<NewsFlash> resp) {
                    updateData(mSearchContent, resp.getListData(), isRefresh);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    stopFreshOrLoadAnimation();
                }

            }).fireFreely();
        }
    }

    private void updateData(String searchContent, List<NewsFlash> data, boolean isRefresh) {
        if (TextUtils.isEmpty(searchContent) || data == null) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            mNewsAdapter.notifyDataSetChanged();
            return;
        }

        if (mSearchContentResultListener != null) {
            mSearchContentResultListener.onSearchFinish(searchContent, data);
        }

        if (isRefresh) {
            mData.clear();
        }
        mNewsAdapter.setSearchContent(searchContent);
        mData.addAll(data);
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (mData.size() >= Apic.DEFAULT_PAGE_SIZE && data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mNewsAdapter.showFooterView(true);
        } else {
            mNewsAdapter.showFooterView(false);
        }
        mNewsAdapter.notifyDataSetChanged();
    }

    static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private boolean showFooterView;
        private List<NewsFlash> mDataList;
        private Context mContext;
        private String mSearchContent;

        public NewsAdapter(Context context, List<NewsFlash> dataList) {
            super();
            mContext = context;
            this.mDataList = dataList;
        }

        public void showFooterView(boolean isShow) {
            showFooterView = isShow;
        }

        public void setSearchContent(String searchContent) {
            mSearchContent = searchContent;
        }

        public List<NewsFlash> getDataList() {
            return mDataList;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return mDataList.isEmpty();
        }

        public NewsFlash getFirst() {
            return mDataList.isEmpty() ? null : mDataList.get(0);
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_flash, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(mSearchContent, showFooterView && position == mDataList.size() - 1, mDataList.get(position), mContext, position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.share)
            LinearLayout mShare;
            @BindView(R.id.content)
            TextView mContent;
            @BindView(R.id.split)
            View mSplit;
            @BindView(R.id.footer)
            TextView mFooter;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void bindDataWithView(String searchContent, boolean showFooterView, final NewsFlash newsFlash, final Context context, int position, int count) {
                if (showFooterView) {
                    mSplit.setVisibility(View.GONE);
                    mFooter.setVisibility(View.VISIBLE);
                } else {
                    mSplit.setVisibility(View.VISIBLE);
                    mFooter.setVisibility(View.GONE);
                }
                mTime.setText(DateUtil.getFormatTime(newsFlash.getReleaseTime()));
                if (TextUtils.isEmpty(newsFlash.getTitle())) {
                    mTitle.setVisibility(View.GONE);
                    mContent.setText(newsFlash.getContent().trim());
                } else {
                    setSearchTitle(mTitle, searchContent, newsFlash.getTitle(), context);
                    mTitle.setVisibility(View.VISIBLE);
                    mContent.setText(newsFlash.getContent());
                }
                mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(context, UmengCountEventId.NEWS_FLASH_SHARE);
                        Launcher.with(context, ShareNewsFlashActivity.class)
                                .putExtra(ExtraKeys.NEWS_FLASH, newsFlash)
                                .execute();
                    }
                });

                mContent.setMaxLines(6);
                mContent.setEllipsize(TextUtils.TruncateAt.END);
                mContent.setOnClickListener(new View.OnClickListener() {
                    boolean flag = true;

                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            flag = false;
                            mContent.setMaxLines(Integer.MAX_VALUE);
                            mContent.setEllipsize(null);
                        } else {
                            flag = true;
                            mContent.setMaxLines(6);
                            mContent.setEllipsize(TextUtils.TruncateAt.END);
                        }
                    }
                });
            }

            private void setSearchTitle(TextView titleView, String searchContent, String title, Context context) {
                title = title.replace("【", "").replace("】", "").trim();
                titleView.setText(StrUtil.changeSpecialTextColor(title, searchContent, ContextCompat.getColor(context, R.color.colorPrimary)));
            }
        }
    }
}
