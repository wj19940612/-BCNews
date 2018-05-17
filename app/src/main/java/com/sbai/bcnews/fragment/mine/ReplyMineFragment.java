package com.sbai.bcnews.fragment.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.dialog.WriteCommentActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.mine.ReplyNews;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.ViewpointType;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.CommentPopupWindow;
import com.sbai.bcnews.view.recycleview.HeaderViewRecycleViewAdapter;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReplyMineFragment extends RecycleViewSwipeLoadFragment implements WhistleBlowingDialogFragment.OnWhistleBlowingReasonListener {

    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    private Unbinder mBind;

    public static final int NEWS_TYPE_REPLY_TO_MINE = 0;
    public static final int NEWS_TYPE_MINE_COMMENT = 1;

    private int mPageType;
    private int mPage;
    private int mPageSize;
    private ReplyMineAdapter mReplyMineAdapter;
    private TextView mFooterView;

    public interface OnRecycleViewScrollListener {
        void onScrollRecycleViewTop(boolean scrollRecycleViewTop);
    }

    private OnRecycleViewScrollListener mOnRecycleViewScrollListener;


    public static ReplyMineFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ExtraKeys.TAG, type);
        ReplyMineFragment fragment = new ReplyMineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRecycleViewScrollListener) {
            mOnRecycleViewScrollListener = (OnRecycleViewScrollListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageType = getArguments().getInt(ExtraKeys.TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_minek, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPage = 0;
        mPageSize = ViewpointType.NEWS_PAGE_SIZE;
        init();
        requestViewpointList();
    }


    private void init() {

        initView();

        mReplyMineAdapter = new ReplyMineAdapter(getActivity(), mPageType);
        mSwipeTargetView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTargetView.setAdapter(mReplyMineAdapter);
        mReplyMineAdapter.setOnReplyClickListener(new ReplyMineAdapter.OnReplyClickListener() {
            @Override
            public void onPraise(ReplyNews replyNews) {
                praise(replyNews);
            }

            @Override
            public void onReply(View view, ReplyNews replyNews) {
                showPopupWindow(view, replyNews, false);
            }

            @Override
            public void onClick(View view, ReplyNews replyNews) {
                if (mPageType == NEWS_TYPE_MINE_COMMENT)
                    showPopupWindow(view, replyNews, true);
            }

            @Override
            public void onArticleClick(ReplyNews replyNews) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, replyNews.getDataId())
                        .execute();
            }
        });

    }

    private void initView() {
        if (mPageType == NEWS_TYPE_REPLY_TO_MINE) {
            mEmptyView.setText(R.string.now_not_review);
        } else {
            mEmptyView.setText(R.string.now_not_comment);
        }

        createFootView();
    }

    private void createFootView() {
        mFooterView = new TextView(getActivity());
        mFooterView.setText(R.string.there_is_no_more_data);
        mFooterView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mFooterView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_666));
        mFooterView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(40, getResources()));
        layoutParams.gravity = Gravity.CENTER;
        mFooterView.setLayoutParams(layoutParams);
    }

    private void showPopupWindow(View view, final ReplyNews replyNews, final boolean isDeleteReview) {
        CommentPopupWindow.with(view, getActivity())
                .setDeleteReview(isDeleteReview)
                .setOnItemClickListener(new CommentPopupWindow.OnItemClickListener() {
                    @Override
                    public void onCopy() {
                        String content;
//                        if (mPageType == NEWS_TYPE_REPLY_TO_MINE) {
                        content = replyNews.getContent();
//                        } else {
//                            content = replyNews.getRelayContent();
//                        }

                        ClipboardUtils.clipboardText(getActivity(), content, R.string.copy_success);

                    }

                    @Override
                    public void oReview() {
                        if (isDeleteReview) {
                            deleteReview(replyNews);
                        } else {
                            writeComment(replyNews);
                        }
                    }

                    @Override
                    public void onWhistleBlowing() {
                        requestWhistleBlowingReason(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_COMMENT, replyNews.getId());

                    }
                })
                .showPopupWindow();
    }


    protected void requestWhistleBlowingReason(final int type, final String id) {
        Apic.requestWhistleBlowingList(type)
                .tag(TAG)
                .callback(new Callback2D<Resp<LinkedHashMap<String, String>>, LinkedHashMap<String, String>>() {
                    @Override
                    protected void onRespSuccessData(LinkedHashMap<String, String> data) {
                        WhistleBlowingDialogFragment.newInstance(type, id, data)
                                .setOnWhistleBlowingReasonListener(ReplyMineFragment.this)
                                .show(getChildFragmentManager());
                    }
                })
                .fire();


    }

    private void writeComment(ReplyNews replyNews) {
        if (!LocalUser.getUser().isLogin()) {
            Launcher.with(getActivity(), LoginActivity.class).execute();
            return;
        }
        WriteComment replyComment = WriteComment.getReplyComment(replyNews);
        Launcher.with(getContext(), WriteCommentActivity.class)
                .putExtra(ExtraKeys.DATA, replyComment)
                .putExtra(ExtraKeys.TAG, replyNews.getUsername())
                .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_MINE_REPLY);
    }

    private void deleteReview(final ReplyNews replyNews) {
        Apic.deleteReview(replyNews.getType(), replyNews.getDataId(), replyNews.getId())
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        mReplyMineAdapter.remove(replyNews);
                    }
                })
                .fire();
    }


    protected void praise(final ReplyNews replyNews) {
        Integer userId;
//        if (mPageType == NEWS_TYPE_REPLY_TO_MINE) {
        userId = replyNews.getUserId();
//        } else {
//            userId = replyNews.getReplayUserId();
//        }

        Apic.praiseComment(replyNews.getId(), replyNews.getDataId(), userId, replyNews.getType())
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {
                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        updatePraise(replyNews);
                    }

                    @Override
                    protected void onRespFailure(Resp failedResp) {
                        super.onRespFailure(failedResp);
                        if (failedResp.getCode() == Resp.CODE_ALREADY_PRAISE) {
                            updatePraise(replyNews);
                        }
                    }
                })
                .fire();
    }

    private void updatePraise(ReplyNews replyNews) {
        if (replyNews.getIsPraise() == 0) {
            replyNews.setIsPraise(ViewpointType.ALREADY_PRAISE);
            replyNews.setPraiseCount(replyNews.getPraiseCount() + 1);
            mReplyMineAdapter.notifyDataSetChanged();
        }
    }

    private void requestViewpointList() {
        if (isAdded())
            Apic.requestMineReplyOrCommentViewpointList(mPage, mPageType, mPageSize)
                    .tag(TAG)
                    .timeout(5000)
                    .callback(new Callback2D<Resp<List<ReplyNews>>, List<ReplyNews>>() {
                        @Override
                        protected void onRespSuccessData(List<ReplyNews> data) {
                            updateReplyList(data);
                            refreshSuccess();
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
                    })
                    .fireFreely();
    }

    private void updateReplyList(List<ReplyNews> data) {
        if (mPage == 0) {
            mReplyMineAdapter.clear();
            if (data.isEmpty()) {
                mScrollView.setVisibility(View.VISIBLE);
                mSwipeToLoadLayout.setVisibility(View.GONE);
            } else {
                mScrollView.setVisibility(View.GONE);
                mSwipeToLoadLayout.setVisibility(View.VISIBLE);
            }
        }

        mReplyMineAdapter.addAll(data);

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            if (!mReplyMineAdapter.isEmpty()) {
                if (!mReplyMineAdapter.hasFooterView()) {
                    mReplyMineAdapter.addFooterView(mFooterView);
                }
            }
        } else {
            mPage++;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onRecycleViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onRecycleViewScrolled(recyclerView, dx, dy);
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        boolean isTop = layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
        if (mOnRecycleViewScrollListener != null) {
            mOnRecycleViewScrollListener.onScrollRecycleViewTop(isTop);
        }

        if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
            triggerLoadMore();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onLoadMore() {
        mPageSize = ViewpointType.NEWS_LOAD_MORE_PAGE_SIZE;
        requestViewpointList();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mPageSize = ViewpointType.NEWS_PAGE_SIZE;
        requestViewpointList();
        mReplyMineAdapter.removeFooterView();
    }

    @Override
    public boolean isUseDefaultLoadMoreConditions() {
        return false;
    }

    @Override
    public void onChooseWhistleBlowingReason(final String reason, int type, String id) {
        Apic.submitWhistleBlowing(id, type, reason)
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        ToastUtil.show(R.string.whistle_blowing_success);
                    }
                })
                .fire();
    }


    static class ReplyMineAdapter extends HeaderViewRecycleViewAdapter<ReplyNews, ReplyMineAdapter.ViewHolder> {


        public interface OnReplyClickListener {

            void onPraise(ReplyNews replyNews);

            void onReply(View view, ReplyNews replyNews);

            void onClick(View view, ReplyNews replyNews);

            void onArticleClick(ReplyNews replyNews);
        }

        private OnReplyClickListener mOnReplyClickListener;
        private Context mContext;
        private int mPageType;

        public ReplyMineAdapter(Context context, int pageType) {
            mContext = context;
            mPageType = pageType;
        }

        public void setOnReplyClickListener(OnReplyClickListener onReplyClickListener) {
            mOnReplyClickListener = onReplyClickListener;
        }

        @NonNull
        @Override
        public ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_review_mine, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(@NonNull ViewHolder holder, ReplyNews data, int position) {
            holder.bindDataWithView(data, position, mContext, mPageType, mOnReplyClickListener);
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.portrait)
            ImageView mPortrait;
            @BindView(R.id.userName)
            TextView mUserName;
            @BindView(R.id.praiseCount)
            TextView mPraiseCount;
            @BindView(R.id.pointContent)
            TextView mPointContent;
            @BindView(R.id.timeLine)
            TextView mTimeLine;
            @BindView(R.id.review)
            TextView mReview;
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.newsImage)
            ImageView mNewsImage;
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.rootView)
            CardView mCardView;
            @BindView(R.id.reviewRL)
            RelativeLayout mReviewRL;
            @BindView(R.id.writeRl)
            RelativeLayout mWriteRl;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ReplyNews itemData, int position, Context context, int pageType, final OnReplyClickListener onReplyClickListener) {
                String portraitUrl;
                String userName;
                String content;
                if (pageType == NEWS_TYPE_REPLY_TO_MINE) {
                    portraitUrl = itemData.getUserPortrait();
                    userName = itemData.getUsername();
                    content = itemData.getContent();

                } else {
                    portraitUrl = itemData.getUserPortrait();
                    userName = itemData.getUsername();
                    content = itemData.getContent();
                }

                GlideApp.with(mPortrait)
                        .load(portraitUrl)
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .circleCrop()
                        .into(mPortrait);
                mNewsTitle.setText(itemData.getTitle());
                mUserName.setText(userName);
                mPointContent.setText(content);

                boolean isPraise = itemData.getIsPraise() == NewsViewpoint.ALREADY_PRAISE;
                mPraiseCount.setSelected(isPraise);
                if (isPraise) {
//                    mPraiseCount.setEnabled(false);
                    mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
                } else {
//                    mPraiseCount.setEnabled(true);
                    mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);
                }
                if (itemData.getPraiseCount() != 0) {
                    mPraiseCount.setText(String.valueOf(itemData.getPraiseCount()));
                } else {
                    mPraiseCount.setText(R.string.praise);
                }

                GlideApp.with(mNewsImage)
                        .load(itemData.getImg())
                        .placeholder(R.drawable.ic_default_image)
                        .into(mNewsImage);
                mTimeLine.setText(DateUtil.formatDefaultStyleTime(itemData.getReplayTime()));

                if (pageType == NEWS_TYPE_REPLY_TO_MINE) {
                    mReview.setVisibility(View.VISIBLE);
                } else {
                    mReview.setVisibility(View.GONE);
                }


                SpannableString spannableString = formatReviewContent(pageType, itemData, context);
                if (spannableString == null) {
                    mTitle.setVisibility(View.GONE);
                } else {
                    mTitle.setVisibility(View.VISIBLE);
                    mTitle.setText(spannableString);
                }

                initListener(itemData, onReplyClickListener);
            }

            private SpannableString formatReviewContent(int pageType, ReplyNews itemData, Context context) {
                if (pageType == NEWS_TYPE_REPLY_TO_MINE) {
                    return StrUtil.mergeTextWithColor("我：", ContextCompat.getColor(context, R.color.textColorPrimary), itemData.getReplayContent());
                } else if (itemData.getType() != ViewpointType.FIRST_COMMENT) {
                    return StrUtil.mergeTextWithColor("回复 " + itemData.getReplayUsername() + ": ", ContextCompat.getColor(context, R.color.text_476E92), itemData.getReplayContent());
                }
                return null;
            }

            private void initListener(final ReplyNews itemData, final OnReplyClickListener onReplyClickListener) {
                mWriteRl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReplyClickListener.onClick(mWriteRl, itemData);
                    }
                });

                mPraiseCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReplyClickListener.onPraise(itemData);
                    }
                });

                mReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReplyClickListener.onReply(mCardView, itemData);
                    }
                });

                mReviewRL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReplyClickListener.onArticleClick(itemData);
                    }
                });
            }
        }
    }
}
