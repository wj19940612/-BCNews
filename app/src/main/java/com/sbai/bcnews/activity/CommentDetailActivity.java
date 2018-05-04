package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.model.news.ViewPointComment;
import com.sbai.bcnews.model.news.ViewPointCommentReview;
import com.sbai.bcnews.model.news.ViewpointType;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.view.CommentPopupWindow;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.news.CommentContentView;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentDetailActivity extends NewsShareOrCommentBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    EmptyRecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.writeComment)
    TextView mWriteComment;
    @BindView(R.id.collectAndShareLayout)
    LinearLayout mCollectAndShareLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    private long mViewpointId;
    private String mNewsId;
    private int mPageSize;
    private int mPage;
    private ReviewListAdapter mReviewListAdapter;

    @Override
    protected int getPageType() {
        return PAGE_TYPE_VIEWPOINT_DETAIL;
    }

    @Override
    protected void onReceiveBroadcast(Context context, Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        ButterKnife.bind(this);

        initData();
        initView();
        requestCommentList();
    }

    private void initView() {
        mSwipeToLoadLayout.setRefreshEnabled(false);
        mReviewListAdapter = new ReviewListAdapter(getActivity(), new ArrayList<ViewPointComment>());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mReviewListAdapter);

        initListener();
    }

    private void initListener() {
        mReviewListAdapter.setOnReviewCallBack(new ReviewListAdapter.OnCommentCallBack() {
            @Override
            public void onClick(View view, int position, ViewPointComment viewPointComment) {
                showPopupWindow(view, viewPointComment, position, null);
            }

            @Override
            public void onSecondReviewPraise(ViewPointComment viewPointComment, int position) {
                praiseReview(viewPointComment,position,null);
            }

            @Override
            public void onSecondReview(View view, ViewPointComment viewPointComment, int position) {
                showPopupWindow(view, viewPointComment, position, null);
            }


            @Override
            public void onThirdReviewClick(View view, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                showPopupWindow(view, viewPointComment, 0, sonViewPointComment);
            }

            @Override
            public void onThirdReviewPraise(int position, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                praiseReview(viewPointComment,position,sonViewPointComment);
            }
        });
    }

    private void praiseReview(ViewPointComment viewPointComment, int position, ViewPointCommentReview sonViewPointComment) {
//        praiseComment(viewPointComment.getId(), mNewsDetail.getId(), viewPointComment.getUserId(), viewPointComment.getFirstId(), null, ViewpointType.THIRD_COMMENT, null, viewPointComment);
//        praiseComment(viewPointComment.getId(), mNewsDetail.getId(), viewPointComment.getUserId(), viewPointComment.getFirstId(), viewPointComment.getReplayUserId(), ViewpointType.THIRD_COMMENT, null, viewPointComment);

//        Apic.praiseComment(newViewPointAndReview.getId(), mNewsDetail.getId(), (long) newViewPointAndReview.getUserId(), null, null, ViewpointType.SECOND_COMMENT)
//                .tag(TAG)
//                .callback(new Callback<Object>() {
//                    @Override
//                    protected void onRespSuccess(Object resp) {
//                        // TODO: 2018/5/4 先写成这样
////                        if (newViewPointAndReview.getIsPraise() == 0) {
////                            newViewPointAndReview.setPraiseCount(NewViewPointAndReview.ALREADY_PRAISE);
////                            int newPraiseCount = newViewPointAndReview.getPraiseCount() + 1;
////                            newViewPointAndReview.setPraiseCount(newPraiseCount);
////                            mAdapter.notifyDataSetChanged();
////                        }
//                    }
//
//                    @Override
//                    public void onSuccess(Object o) {
//                        super.onSuccess(o);
//                        // TODO: 2018/5/3 随便写的
////                        newViewPointAndReview.setIsPraise(1);
////                        newViewPointAndReview.setPraiseCount(555);
////                        updateCommentPraise(newViewPointAndReview);
//                    }
//                })
//                .fire();
    }

    private void showPopupWindow(View view, final ViewPointComment viewPointComment, int position, final ViewPointCommentReview sonViewPointComment) {
        CommentPopupWindow.with(view, getActivity()).setOnItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onCopy() {
                if (sonViewPointComment != null) {
                    ClipboardUtils.clipboardText(getActivity(), sonViewPointComment.getContent(), R.string.copy_success);
                } else if (viewPointComment != null) {
                    ClipboardUtils.clipboardText(getActivity(), viewPointComment.getContent(), R.string.copy_success);
                }
            }

            @Override
            public void oReview() {

            }

            @Override
            public void onWhistleBlowing() {
                String dataId;
                if (sonViewPointComment != null) {
                    dataId = String.valueOf(sonViewPointComment.getId());
                } else {
                    dataId = String.valueOf(viewPointComment.getId());
                }
                WhistleBlowingDialogFragment.newInstance(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_COMMENT, dataId);
            }
        }).showPopupWindow();
    }

    private void requestCommentList() {
        Apic.requestCommentList(mNewsId, mPage, mPageSize, mViewpointId)
                .tag(TAG)
                .callback(new Callback<ListResp<ViewPointComment>>() {
                    @Override
                    protected void onRespSuccess(ListResp<ViewPointComment> resp) {

                        ArrayList<ViewPointComment> listData = resp.getListData();
                        if (listData != null) {
                            updateReview(listData);
                        }
                    }
                })
                .fire();
    }

    private void updateReview(ArrayList<ViewPointComment> listData) {

        if (listData.size() < ViewpointType.NEWS_LOAD_MORE_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mReviewListAdapter.addAll(listData);

    }

    private void initData() {
        Intent intent = getIntent();
        mViewpointId = intent.getLongExtra(ExtraKeys.TAG, -1);
        mNewsId = intent.getStringExtra(ExtraKeys.NEWS_ID);
        mPage = 0;
        mPageSize = ViewpointType.NEWS_PAGE_SIZE;
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        mPageSize = ViewpointType.NEWS_LOAD_MORE_PAGE_SIZE;
        requestCommentList();
    }

    @Override
    public void onRefresh() {

    }

    @OnClick(R.id.writeComment)
    public void onViewClicked() {

    }

    static class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

        public interface OnCommentCallBack extends CommentContentView.OnReviewCallBack {

            void onClick(View view, int position, ViewPointComment viewPointComment);
        }

        private Context mContext;
        private ArrayList<ViewPointComment> mViewPointCommentList;
        private OnCommentCallBack mOnReviewCallBack;

        public void setOnReviewCallBack(OnCommentCallBack onReviewCallBack) {
            mOnReviewCallBack = onReviewCallBack;
        }

        public ReviewListAdapter(Context context, ArrayList<ViewPointComment> viewPointCommentList) {
            mContext = context;
            mViewPointCommentList = viewPointCommentList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(mViewPointCommentList.get(position), position, mContext, mOnReviewCallBack);
        }

        public void addAll(ArrayList<ViewPointComment> viewPointCommentList) {
            mViewPointCommentList.addAll(viewPointCommentList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mViewPointCommentList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.commentContentView)
            CommentContentView mCommentContentView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ViewPointComment viewPointComment, final int position, Context context, final OnCommentCallBack onReviewCallBack) {
                mCommentContentView.setViewpointList(viewPointComment);
                mCommentContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onClick(mCommentContentView, position, viewPointComment);
                        }
                    }
                });
                mCommentContentView.setOnReviewCallBack(new CommentContentView.OnReviewCallBack() {
                    @Override
                    public void onSecondReviewPraise(ViewPointComment viewPointComment, int position) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onSecondReviewPraise(viewPointComment, position);
                        }
                    }

                    @Override
                    public void onSecondReview(View view, ViewPointComment viewPointComment, int position) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onSecondReview(mCommentContentView, viewPointComment, position);
                        }
                    }


                    @Override
                    public void onThirdReviewClick(View view, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onThirdReviewClick(view, viewPointComment, sonViewPointComment);
                        }
                    }

                    @Override
                    public void onThirdReviewPraise(int position, ViewPointComment viewPointComment, ViewPointCommentReview sonViewPointComment) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onThirdReviewPraise(position,viewPointComment, sonViewPointComment);
                        }
                    }

                });
            }
        }
    }
}
