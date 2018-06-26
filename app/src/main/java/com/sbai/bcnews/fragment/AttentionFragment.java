package com.sbai.bcnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.sbai.bcnews.activity.MyAttentionActivity;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.author.AuthorActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsAdapter;
import com.sbai.bcnews.utils.news.NewsWithHeaderAdapter;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.dialog.AttentionDialog;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static com.sbai.bcnews.ExtraKeys.CHANNEL;

public class AttentionFragment extends RecycleViewSwipeLoadFragment {

    public static final int HEADER_HEIGHT = 80;

    public static final int INTENT_REQUEST_CODE_ATTENTIONLIST = 996;

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
    private List<Author> mNewsAuthorList;

    private List<NewsWrap> mNewsWraps;
    private NewsWithHeaderAdapter mNewsAdapter;

    private LinearLayout mEmptyView;
    private RecyclerView mEmptyRecyclerView;
    private RelativeLayout mHeaderView;
    private String mChannel;
    private int mPage;

    public interface OnItemClickListener {
        public void onItemClick(Author author);

        public void onAttention(Author newsAuthor, boolean isAttention, int position);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshReadStatus();
        mPage = 0;
        loadData(true);
        requestMyAttention();
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
            public void onItemClick(Author author) {
                Launcher.with(AttentionFragment.this.getActivity(), AuthorActivity.class).putExtra(ExtraKeys.ID, author.getId()).execute();
            }

            @Override
            public void onAttention(final Author newsAuthor, final boolean isAttention, final int position) {
                if (isAttention) {
                    AttentionDialog.with(getActivity()).setOnSureClickListener(new AttentionDialog.OnClickListener() {
                        @Override
                        public void onClick() {
                            cancelOrAttention(false, newsAuthor, position);
                        }
                    }).setTitle(R.string.sure_cancel_attention).show();
                } else if (LocalUser.getUser().isLogin()) {
                    cancelOrAttention(true, newsAuthor, position);
                } else {
                    Launcher.with(getContext(), LoginActivity.class).execute();
                }
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
        mEmptyView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mEmptyRecyclerView = mEmptyView.findViewById(R.id.emptyRecyclerView);
    }

    private void initHeadView() {
        mHeaderView = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(R.layout.view_my_attention, null);
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.with(AttentionFragment.this, MyAttentionActivity.class).excuteForResultFragment(INTENT_REQUEST_CODE_ATTENTIONLIST);
            }
        });
    }

    private void loadData(final boolean refresh) {
        Apic.requestAttentionArticle(mPage)
                .tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
                    @Override
                    protected void onRespSuccessData(News data) {
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
                        if (refresh) {
                            refreshFailure();
                        }
                    }

                }).fireFreely();
    }

    private void setEmptyView(boolean isEmpty) {
        if (isEmpty) {
            mNewsAdapter.setHeaderView(mEmptyView, isEmpty);
            mNewsAdapter.notifyDataSetChanged();
            loadEmptyData();
        } else {
            mNewsAdapter.setHeaderView(null);
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    private void updateData(List<NewsDetail> data, boolean refresh) {
        if (refresh) {
            mNewsWraps.clear();
        }
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            refreshFoot(0);
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
        refreshFoot(data.size());
        mNewsAdapter.notifyDataSetChanged();
    }

    private void refreshFoot(int size) {
        if (mNewsWraps.size() >= Apic.DEFAULT_PAGE_SIZE && size < Apic.DEFAULT_PAGE_SIZE) {
            mNewsAdapter.setHasFoot(true);
        } else {
            mNewsAdapter.setHasFoot(false);
        }
    }

    private void requestMyAttention() {
        Apic.requestAttentionAuthor().tag(TAG).callback(new Callback2D<Resp<List<Author>>, List<Author>>() {
            @Override
            protected void onRespSuccessData(List<Author> data) {
                updateMyAttentionData(data);
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                setEmptyView(true);
            }
        }).fireFreely();
    }

    private void updateMyAttentionData(List<Author> newsAuthorList) {
        if (newsAuthorList == null || newsAuthorList.size() == 0) {
            setEmptyView(true);
            return;
        }
        setEmptyView(false);
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
                            .load(newsAuthorList.get(i).getUserPortrait())
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
        }
    }

    private void loadEmptyData() {
        Apic.requestRecommendAuthorList().tag(TAG).callback(new Callback2D<Resp<List<Author>>, List<Author>>() {
            @Override
            protected void onRespSuccessData(List<Author> data) {
                updateEmptyView(data);
            }
        }).fireFreely();
    }

    private void updateEmptyView(List<Author> newsAuthorList) {
        mNewsAuthorList.clear();
        if (newsAuthorList != null && newsAuthorList.size() > 4) {
            newsAuthorList = newsAuthorList.subList(0, 4);
        }
        mNewsAuthorList.addAll(newsAuthorList);
        mEmptyRecyclerView.setFocusableInTouchMode(false);
        mRecommendAdapter.notifyDataSetChanged();
    }

    private void refreshReadStatus() {
        if (mNewsWraps != null && mNewsWraps.size() != 0) {
            mNewsAdapter.notifyDataSetChanged();
        }
    }

    private void cancelOrAttention(final boolean isAttention, final Author selectAuthor, final int selectPosition) {
        int type = isAttention ? 1 : 0;
        Apic.requestConcernAuthor(selectAuthor.getId(), type).tag(TAG).callback(new Callback<Resp>() {
            @Override
            protected void onRespSuccess(Resp resp) {
                if (!isAttention) {
                    selectAuthor.setIsConcern(0);
                } else {
                    selectAuthor.setIsConcern(1);
                }
                mRecommendAdapter.notifyItemChanged(selectPosition);
            }
        }).fire();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTENT_REQUEST_CODE_ATTENTIONLIST && resultCode == RESULT_OK) {
            loadData(true);
            requestMyAttention();
        }
    }

    static class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Author> mNewsAuthors;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;

        public RecommendAdapter(List<Author> newsAuthors, Context context, OnItemClickListener onItemClickListener) {
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
            ((ViewHolder) holder).bindingData(mNewsAuthors.get(position), mContext, mOnItemClickListener, position);
        }

        @Override
        public int getItemCount() {
            return mNewsAuthors == null ? 0 : mNewsAuthors.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.head)
            HasLabelLayout mHead;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.attentionBtn)
            TextView mAttentionBtn;
            @BindView(R.id.attentionBtnLayout)
            LinearLayout mAttentionBtnLayout;
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(final Author newsAuthor, final Context context, final OnItemClickListener onItemClickListener, final int position) {
                mHead.setImageSrc(newsAuthor.getUserPortrait());
                if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_OFFICIAL) {
                    mHead.setLabelImageViewVisible(true);
                    mHead.setLabelSelected(true);
                } else if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_SPECIAL) {
                    mHead.setLabelImageViewVisible(true);
                    mHead.setLabelSelected(false);
                } else {
                    mHead.setLabelImageViewVisible(false);
                }

                mName.setText(newsAuthor.getUserName());
                if (!LocalUser.getUser().isLogin()) {
                    setNoAttentionBtn(mAttentionBtn, false, context);
                } else {
                    setNoAttentionBtn(mAttentionBtn, newsAuthor.getIsConcern() > 0, context);
                }
                mAttentionBtnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onAttention(newsAuthor, newsAuthor.getIsConcern() > 0, position);
                        }
                    }
                });
                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(newsAuthor);
                        }
                    }
                });
            }

            private void setNoAttentionBtn(TextView attentionBtn, boolean hasAttention, Context context) {
                attentionBtn.setSelected(hasAttention);
                if (hasAttention) {
                    attentionBtn.setTextColor(ContextCompat.getColor(context, R.color.text_d9));
                    attentionBtn.setText(R.string.has_attention);
                } else {
                    attentionBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    attentionBtn.setText(R.string.attention);
                }
            }
        }
    }
}
