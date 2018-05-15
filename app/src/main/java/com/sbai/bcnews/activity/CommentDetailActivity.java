package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;
import com.sbai.bcnews.activity.dialog.WriteCommentActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.ViewPointComment;
import com.sbai.bcnews.model.news.ViewPointCommentReview;
import com.sbai.bcnews.model.news.ViewpointType;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.model.news.WriteCommentResponse;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.CommentPopupWindow;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.news.CommentContentView;
import com.sbai.bcnews.view.recycleview.RecycleViewType;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentDetailActivity extends NewsShareOrCommentBaseActivity {


    public static final int REQ_COMMENT_DETAIL = 5555;
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

    private NewsDetail mNewsDetail;
    private int mPageSize;
    private int mPage;
    private NewViewPointAndReview mNewViewPointAndReview;
    private ReviewListAdapter mReviewListAdapter;

    private ArrayList<ViewPointComment> mViewPointCommentList;

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

    private void initData() {
        Intent intent = getIntent();
        mNewViewPointAndReview = intent.getParcelableExtra(ExtraKeys.DATA);
        mNewsDetail = intent.getParcelableExtra(ExtraKeys.NEWS_DETAIL);
        mPage = 0;
        mPageSize = ViewpointType.NEWS_PAGE_SIZE;
    }

    private void initView() {
        mSwipeToLoadLayout.setRefreshEnabled(false);
        //进去加载太慢了 先把把上拉禁止了
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        mViewPointCommentList = new ArrayList<>();
        mReviewListAdapter = new ReviewListAdapter(getActivity(), mViewPointCommentList);
        mReviewListAdapter.setNewViewPointAndReview(mNewViewPointAndReview);
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
                praiseReview(viewPointComment, position);
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
                praiseReview(sonViewPointComment, position);
            }


            @Override
            public void onCommentViewpoint(View view, NewViewPointAndReview newViewPointAndReview) {
                showPopupWindow(view, null, null, 0, newViewPointAndReview);
            }

            @Override
            public void onPraiseViewpoint(NewViewPointAndReview newViewPointAndReview) {
                praise(newViewPointAndReview);
            }
        });
    }

    @Override
    protected void updateViewpointPraiseStatus(NewViewPointAndReview newViewPointAndReview) {
        super.updateViewpointPraiseStatus(newViewPointAndReview);
        if (newViewPointAndReview != null) {
            if (mReviewListAdapter.getNewViewPointAndReview() != null) {
                mReviewListAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra(ExtraKeys.DATA, newViewPointAndReview);
                setResult(RESULT_OK, intent);
            }
        }
    }

    private void praiseReview(ViewPointComment viewPointComment, int position) {
        praise(viewPointComment);
    }

    @Override
    protected void updateViewpointPraiseStatus(ViewPointComment viewPointComment) {
        super.updateViewpointPraiseStatus(viewPointComment);
        mReviewListAdapter.notifyDataSetChanged();
    }

    private void showPopupWindow(View view, final ViewPointComment viewPointComment, int position, final ViewPointCommentReview sonViewPointComment) {
        showPopupWindow(view, viewPointComment, sonViewPointComment, position, null);
    }

    private void showPopupWindow(View view, final ViewPointComment viewPointComment, final ViewPointCommentReview sonViewPointComment, int position, final NewViewPointAndReview newViewPointAndReview) {
        CommentPopupWindow.with(view, getActivity()).setOnItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onCopy() {
                if (sonViewPointComment != null) {
                    ClipboardUtils.clipboardText(getActivity(), sonViewPointComment.getContent(), R.string.copy_success);
                } else if (viewPointComment != null) {
                    ClipboardUtils.clipboardText(getActivity(), viewPointComment.getContent(), R.string.copy_success);
                } else if (newViewPointAndReview != null) {
                    ClipboardUtils.clipboardText(getActivity(), newViewPointAndReview.getContent(), R.string.copy_success);
                }
            }

            @Override
            public void oReview() {
                if (!LocalUser.getUser().isLogin()) {
                    Launcher.with(getActivity(), LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
                    return;
                }
                if (newViewPointAndReview != null) {
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getSecondWriteComment(newViewPointAndReview))
                            .putExtra(ExtraKeys.TAG, newViewPointAndReview.getUsername())
                            .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_VIEWPOINT);
                } else if (sonViewPointComment == null && viewPointComment != null) {
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getThirdWriteComment(viewPointComment))
                            .putExtra(ExtraKeys.TAG, viewPointComment.getUsername())
                            .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_COMMENT);
                } else if (sonViewPointComment != null) {
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getThirdReplyComment(sonViewPointComment))
                            .putExtra(ExtraKeys.TAG, sonViewPointComment.getUsername())
                            .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_REPLY_FOR_COMMENT);
                }

            }

            @Override
            public void onWhistleBlowing() {
                String dataId = null;
                if (sonViewPointComment != null) {
                    dataId = sonViewPointComment.getId();
                } else if (viewPointComment != null) {
                    dataId = viewPointComment.getId();
                } else if (newViewPointAndReview != null) {
                    dataId = newViewPointAndReview.getDataId();
                }
                requestWhistleBlowingReason(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_COMMENT, dataId);
            }
        }).showPopupWindow();
    }


    private void requestCommentList() {
        if (mNewViewPointAndReview != null) {
            Apic.requestCommentList(mNewViewPointAndReview.getDataId(), mPage, mPageSize, mNewViewPointAndReview.getId())
                    .tag(TAG)
                    .callback(new Callback<ListResp<ViewPointComment>>() {
                        @Override
                        protected void onRespSuccess(ListResp<ViewPointComment> resp) {
                            mSwipeToLoadLayout.setLoadMoreEnabled(true);
                            ArrayList<ViewPointComment> listData = resp.getListData();
                            if (listData != null) {
                                updateReview(listData);
                            }
                            stopFreshOrLoadAnimation();
                        }

                        @Override
                        public void onFailure(ReqError reqError) {
                            super.onFailure(reqError);
                            stopFreshOrLoadAnimation();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            stopFreshOrLoadAnimation();
                        }
                    })
                    .fire();
        }

    }

    private void updateReview(ArrayList<ViewPointComment> listData) {

        if (listData.size() < ViewpointType.NEWS_LOAD_MORE_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mReviewListAdapter.addAll(listData);

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

    @Override
    protected void onStop() {
        super.onStop();
        GlideApp.with(getActivity()).onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_VIEWPOINT:
                    if (data != null) {
                        WriteCommentResponse writeCommentResponse = data.getParcelableExtra(ExtraKeys.DATA);
                        if (writeCommentResponse != null) {
                            ViewPointComment viewPointComment = writeCommentResponse.getViewPointComment();
                            viewPointComment.setUsername(LocalUser.getUser().getUserInfo().getUserName());
                            if (mViewPointCommentList != null) {
                                mViewPointCommentList.add(0, viewPointComment);
                                mReviewListAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                    break;
                case WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_COMMENT:
                case WriteCommentActivity.REQ_CODE_WRITE_REPLY_FOR_COMMENT:
                    WriteCommentResponse writeCommentResponse = data.getParcelableExtra(ExtraKeys.DATA);
                    if (writeCommentResponse != null) {
                        ViewPointCommentReview viewPointCommentReview = writeCommentResponse.getViewPointCommentReview();
                        if (viewPointCommentReview != null && !TextUtils.isEmpty(viewPointCommentReview.getSecondId()))
                            for (ViewPointComment result : mViewPointCommentList) {
                                if (viewPointCommentReview.getSecondId().equalsIgnoreCase(result.getId())) {
                                    if (result.getVos() != null) {
                                        result.getVos().add(0, viewPointCommentReview);
                                    } else {
                                        result.setVos(new ArrayList<ViewPointCommentReview>());
                                        result.getVos().add(0, viewPointCommentReview);
                                    }
                                    mReviewListAdapter.notifyDataSetChanged();
                                }
                            }
                    }
                    break;
                case LoginActivity.REQ_CODE_LOGIN:
                    if (mNewViewPointAndReview != null)
                        requestViewpointPraiseStatus(mNewViewPointAndReview.getId());
                    break;
            }
        }
    }

    private void requestViewpointPraiseStatus(String id) {
        Apic.requestViewpointPraiseStatus(id)
                .tag(TAG)
                .callback(new Callback2D<Resp<NewViewPointAndReview>, NewViewPointAndReview>() {
                    @Override
                    protected void onRespSuccessData(NewViewPointAndReview data) {
                        mNewViewPointAndReview.setIsPraise(data.getIsPraise());
//                        mNewViewPointAndReview.setPraiseCount(data.getPraiseCount());
                        mReviewListAdapter.notifyDataSetChanged();
                    }
                })
                .fire();
    }

    @OnClick(R.id.writeComment)
    public void onViewClicked() {
        writeNewsComment(mNewViewPointAndReview);
    }

    private void writeNewsComment(NewViewPointAndReview newViewPointAndReview) {
        if (mNewsDetail != null)
            if (!LocalUser.getUser().isLogin()) {
                Launcher.with(getActivity(), LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
                return;
            }
        Launcher.with(getActivity(), WriteCommentActivity.class)
                .putExtra(ExtraKeys.DATA, WriteComment.getSecondWriteComment(newViewPointAndReview))
                .putExtra(ExtraKeys.TAG, newViewPointAndReview.getUsername())
                .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_VIEWPOINT);
    }


    static class ReviewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecycleViewType {


        public interface OnCommentCallBack extends CommentContentView.OnReviewCallBack {

            void onClick(View view, int position, ViewPointComment viewPointComment);

            void onCommentViewpoint(View view, NewViewPointAndReview newViewPointAndReview);

            void onPraiseViewpoint(NewViewPointAndReview newViewPointAndReview);
        }

        public NewViewPointAndReview getNewViewPointAndReview() {
            return mNewViewPointAndReview;
        }

        private Context mContext;
        private ArrayList<ViewPointComment> mViewPointCommentList;
        private OnCommentCallBack mOnReviewCallBack;
        private NewViewPointAndReview mNewViewPointAndReview;


        public void setNewViewPointAndReview(NewViewPointAndReview newViewPointAndReview) {
            mNewViewPointAndReview = newViewPointAndReview;
        }

        public void setOnReviewCallBack(OnCommentCallBack onReviewCallBack) {
            mOnReviewCallBack = onReviewCallBack;
        }

        public ReviewListAdapter(Context context, ArrayList<ViewPointComment> viewPointCommentList) {
            mContext = context;
            mViewPointCommentList = viewPointCommentList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER_VIEW_TYPE:
                    View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_head, parent, false);
                    return new HeadViewHolder(headView);
                default:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review_content, parent, false);
                    return new ViewHolder(view);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeadViewHolder) {
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                headViewHolder.bindDataWithView(mNewViewPointAndReview, mContext, mOnReviewCallBack);
            } else if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.bindDataWithView(mViewPointCommentList.get(position - 1), position, mContext, mOnReviewCallBack);
            }
        }

        public void addAll(ArrayList<ViewPointComment> viewPointCommentList) {
            mViewPointCommentList.addAll(viewPointCommentList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mViewPointCommentList.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_VIEW_TYPE;
            }
            return super.getItemViewType(position);
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
                            onReviewCallBack.onThirdReviewPraise(position, viewPointComment, sonViewPointComment);
                        }
                    }

                });
            }
        }

        class HeadViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.userHeadImage)
            ImageView mUserHeadImage;
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
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            HeadViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final NewViewPointAndReview newViewPointAndReview, Context context, final OnCommentCallBack onReviewCallBack) {
                GlideApp.with(context)
                        .load(newViewPointAndReview.getUserPortrait())
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .circleCrop()
                        .into(mUserHeadImage);
                mUserName.setText(newViewPointAndReview.getUsername());
                mTimeLine.setText(DateUtil.formatDefaultStyleTime(newViewPointAndReview.getReplayTime()));
                mPointContent.setText(newViewPointAndReview.getContent());
                if (newViewPointAndReview.getPraiseCount() != 0) {
                    mPraiseCount.setText(String.valueOf(newViewPointAndReview.getPraiseCount()));
                }else {
                    mPraiseCount.setText(R.string.praise);
                }

                boolean isPraise = newViewPointAndReview.getIsPraise() == NewsViewpoint.ALREADY_PRAISE;
                mPraiseCount.setSelected(isPraise);
                if (isPraise)
                    mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_selected, 0);
                else
                    mPraiseCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_common_praise_normal, 0);

                mReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onCommentViewpoint(mRootView, newViewPointAndReview);
                        }
                    }
                });

                mPraiseCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onReviewCallBack != null) {
                            onReviewCallBack.onPraiseViewpoint(newViewPointAndReview);
                        }
                    }
                });
            }
        }
    }
}
