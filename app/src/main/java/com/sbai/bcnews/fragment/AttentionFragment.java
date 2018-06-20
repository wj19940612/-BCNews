package com.sbai.bcnews.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sbai.bcnews.model.NewsAuthor;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsAdapter;
import com.sbai.bcnews.utils.news.NewsWithHeaderAdapter;
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

public class AttentionFragment extends RecycleViewSwipeLoadFragment {

    public static final int HEADER_HEIGHT = 60;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private Unbinder mBind;

    private RecommendAdapter mRecommendAdapter;
    private List<NewsAuthor> mNewsAuthorList;

    private List<NewsWrap> mNewsWraps;
    private NewsWithHeaderAdapter mNewsAdapter;

    private LinearLayout mEmptyView;
    private RecyclerView mEmptyRecyclerView;
    private RelativeLayout mHeaderView;
    private String mChannel;
    private int mPage;

    public interface OnItemClickListener {
        public void onItemClick();

        public void onAttention(NewsAuthor newsAuthor,boolean isAttention,int position);
    }

    public static AttentionFragment newsIntance(String channel) {
        AttentionFragment attentionFragment = new AttentionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CHANNEL, channel);
        attentionFragment.setArguments(bundle);
        return attentionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChannel = getArguments().getString(CHANNEL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initEmptyView();
        initView();
        loadData(true);
        requestMyAttention();
        loadEmptyData();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshReadStatus();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData(true);
        requestMyAttention();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    private void initView() {
        mNewsAuthorList = new ArrayList<>();
        mEmptyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        mRecommendAdapter = new RecommendAdapter(mNewsAuthorList, getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick() {

            }

            @Override
            public void onAttention(NewsAuthor newsAuthor,boolean isAttention,int position) {
            }
        });
        mEmptyRecyclerView.setAdapter(mRecommendAdapter);

        mNewsWraps = new ArrayList<>();

        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), mNewsWraps, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId())
                        .putExtra(ExtraKeys.CHANNEL, (newsDetail.getChannel() == null || newsDetail.getChannel().isEmpty()) ? null : newsDetail.getChannel().get(0))
                        .execute();
            }
        });

        mNewsAdapter = new NewsWithHeaderAdapter(newsAdapter);
        mNewsAdapter.setHasFoot(true);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeTarget.setAdapter(mNewsAdapter);
        initHeadView();
    }

    private void initEmptyView() {
        mEmptyView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_empty_candy, null);
        mEmptyRecyclerView = mEmptyView.findViewById(R.id.emptyRecyclerView);
    }

    private void initHeadView() {
        mHeaderView = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_my_attention, null);
        mHeaderView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(HEADER_HEIGHT, getResources())));
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void loadData(final boolean refresh) {
        String channelParam = Uri.encode(mChannel);
        Apic.requestNewsListWithChannel(channelParam, mPage)
                .tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
                    @Override
                    protected void onRespSuccessData(News data) {
                        if (mNewsWraps.size() == 0 && data.getContent().size() == 0) {
                            setEmptyView(true);
                        } else {
                            setEmptyView(false);
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
                        if (mNewsWraps.size() == 0) {
                            setEmptyView(true);
                        } else {
                            setEmptyView(false);
                        }
                        if (refresh) {
                            refreshFailure();
                        }
                    }

                }).fireFreely();
    }

    private void setEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mNewsAdapter.setHeaderView(mEmptyView);
            mNewsAdapter.notifyDataSetChanged();
        } else {
            mNewsAdapter.setHeaderView(null);
        }
    }

    private void updateData(List<NewsDetail> data, boolean refresh) {
        if (refresh) {
            mNewsWraps.clear();
        }
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            mNewsAdapter.notifyDataSetChanged();
            return;
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

    private void requestMyAttention() {
//        Apic.requestAttentionAuthor().tag(TAG).callback(new Callback2D<Resp<List<>>>() {
//        })
    }

    private void loadMyAttentionData(List<NewsAuthor> newsAuthorList) {
        if (newsAuthorList != null && newsAuthorList.size() > 0) {
            ImageView[] imageViews = new ImageView[4];
            imageViews[0] = mHeaderView.findViewById(R.id.head1);
            imageViews[1] = mHeaderView.findViewById(R.id.head2);
            imageViews[2] = mHeaderView.findViewById(R.id.head3);
            imageViews[3] = mHeaderView.findViewById(R.id.head4);

            int displaySize = newsAuthorList.size() > 0 ? newsAuthorList.size() : 0;
            for (int i = 0; i < 4; i++) {
                if (i < displaySize) {
                    imageViews[i].setVisibility(View.VISIBLE);
                    GlideApp.with(getActivity())
                            .load("")
                            .placeholder(R.drawable.ic_default_head_portrait)
                            .circleCrop()
                            .into(imageViews[i]);
                } else {
                    imageViews[i].setVisibility(View.GONE);
                }
            }
            if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                if (mNewsAdapter.getHeaderView() == null || mNewsAdapter.getHeaderView() != mEmptyView) {
                    mNewsAdapter.setHeaderView(mHeaderView);
                    mNewsAdapter.notifyDataSetChanged();
                }
            }
        } else {
            if (mNewsAdapter instanceof NewsWithHeaderAdapter) {
                mNewsAdapter.setHeaderView(null);
                mNewsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void loadEmptyData() {

    }

    private void updateEmptyView(List<NewsAuthor> newsAuthorList) {
        mNewsAuthorList.clear();
        mNewsAuthorList.addAll(newsAuthorList);
        mRecommendAdapter.notifyDataSetChanged();
    }

    private void refreshReadStatus() {
        if (mNewsWraps != null && mNewsWraps.size() != 0) {
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    static class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<NewsAuthor> mNewsAuthors;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;

        public RecommendAdapter(List<NewsAuthor> newsAuthors, Context context, OnItemClickListener onItemClickListener) {
            mNewsAuthors = newsAuthors;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_attention, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bindingData(mNewsAuthors.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            return mNewsAuthors == null ? 0 : mNewsAuthors.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.head)
            ImageView mHead;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.attentionBtn)
            TextView mAttentionBtn;
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(NewsAuthor newsAuthor, Context context) {

            }
        }
    }
}
