package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;
import com.sbai.bcnews.activity.dialog.WriteCommentActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.NewsViewpointAndComment;
import com.sbai.bcnews.model.news.ViewpointType;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.model.news.WriteCommentResponse;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.CommentPopupWindow;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.news.ViewPointContentView;
import com.sbai.bcnews.view.recycleview.BaseRecycleViewAdapter;
import com.sbai.glide.GlideApp;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsViewPointListActivity extends NewsShareOrCommentBaseActivity {


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
    @BindView(R.id.mainBody)
    TextView mMainBody;
    @BindView(R.id.collectIcon)
    ImageView mCollectIcon;
    @BindView(R.id.bottomShareIcon)
    ImageView mBottomShareIcon;
    @BindView(R.id.collectAndShareLayout)
    LinearLayout mCollectAndShareLayout;
    @BindView(R.id.rootView)
    ConstraintLayout mRootView;
    @BindView(R.id.emptyText)
    TextView mEmptyText;
    private String mId;
    private int mPageSize = 50;
    private ViewpointReviewAdapter mAdapter;

    private boolean mHasNormalLabel;
    private boolean mHasHotLabel;
    private int mPage = 0;
    private int mHotSize = 1;


    @Override
    protected int getPageType() {
        return PAGE_TYPE_NEWS_VIEWPOINT;
    }


    @Override
    protected void onCollectSuccess(NewsDetail newsDetail) {
        if (newsDetail.getCollect() > 0) {
            mCollectIcon.setSelected(true);
        } else {
            mCollectIcon.setSelected(false);
        }
    }

    @Override
    protected void onReceiveBroadcast(Context context, Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_viewpoint_list);
        ButterKnife.bind(this);
        initData();
        initView();
        refreshData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlideApp.with(getActivity()).onStop();
    }

    private void refreshData() {
        mPageSize = 50;
        mPage = 0;
        requestNewsViewpointList();
        mHasHotLabel = false;
        mHasNormalLabel = false;
    }


    private void initView() {
        mSwipeToLoadLayout.setRefreshEnabled(false);

        //服务器返回太慢了  先禁止上拉加载
        mSwipeToLoadLayout.setLoadMoreEnabled(false);

        mAdapter = new ViewpointReviewAdapter(getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mAdapter);
        mAdapter.setNewsDetail(mNewsDetail);

        mTitleBar.setRightViewEnable(false);
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsShareAndSettingDialog();
            }
        });


        mAdapter.setOnViewPointClickListener(new OnViewPointClickListener() {
            @Override
            public void onReview(NewViewPointAndReview newViewPointAndReview, View view) {
                showPopupWindow(view, newViewPointAndReview);
            }

            @Override
            public void onPraise(NewViewPointAndReview newViewPointAndReview, int position) {
                praise(newViewPointAndReview);
            }

            @Override
            public void onClick(NewViewPointAndReview newViewPointAndReview) {
                if (mNewsDetail != null) {
                    Launcher.with(getActivity(), CommentDetailActivity.class)
                            .putExtra(ExtraKeys.DATA, newViewPointAndReview)
                            .putExtra(ExtraKeys.NEWS_DETAIL, mNewsDetail)
                            .executeForResult(CommentDetailActivity.REQ_COMMENT_DETAIL);
                }
            }
        });
    }

    @Override
    protected void updateViewpointPraiseStatus(NewViewPointAndReview newViewPointAndReview) {
        super.updateViewpointPraiseStatus(newViewPointAndReview);
        if (newViewPointAndReview != null) {
            List<NewViewPointAndReview> dataList = mAdapter.getDataList();
            for (NewViewPointAndReview result : dataList) {
                if (newViewPointAndReview.getId().equalsIgnoreCase(result.getId())) {
                    result.setPraiseCount(newViewPointAndReview.getPraiseCount());
                    result.setIsPraise(newViewPointAndReview.getIsPraise());
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void showPopupWindow(View view, final NewViewPointAndReview newViewPointAndReview) {
        CommentPopupWindow.with(view, getActivity()).setOnItemClickListener(new CommentPopupWindow.OnItemClickListener() {
            @Override
            public void onCopy() {
                ClipboardUtils.clipboardText(getActivity(), newViewPointAndReview.getContent(), R.string.copy_success);
            }

            @Override
            public void oReview() {
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

            @Override
            public void onWhistleBlowing() {
                requestWhistleBlowingReason(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_COMMENT, newViewPointAndReview.getId());
            }
        }).showPopupWindow();
    }


    private void initData() {
        mHasNormalLabel = false;
        mNewsDetail = getIntent().getParcelableExtra(ExtraKeys.DATA);
        if (mNewsDetail != null) {
            mId = mNewsDetail.getId();
            updateCollect(mNewsDetail);
        }
    }

    private void requestNewsViewpointList() {
        Apic.requestNewsViewpointList(mId, mPage, mPageSize)
                .callback(new Callback2D<Resp<NewsViewpointAndComment>, NewsViewpointAndComment>() {
                    @Override
                    protected void onRespSuccessData(NewsViewpointAndComment data) {
                        if (data != null) {
                            updateNewsViewpointList(data);
                        }else {
                            mEmptyText.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mSwipeToLoadLayout.setLoadMoreEnabled(true);
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    @Override
    protected void updateNewsDetail(NewsDetail data) {
        super.updateNewsDetail(data);
        mNewsDetail = data;
        updateNewsStatus(data);
    }

    private void updateNewsStatus(NewsDetail data) {
        updateCollect(data);
    }

    private void updateNewsViewpointList(NewsViewpointAndComment data) {

        int dataSize = 0;
        if (mPage == 0) {
            mAdapter.clear();
            boolean hasNormalData = data.getNormal() != null && !data.getNormal().isEmpty();
            boolean hasHotData = data.getHot() != null && !data.getHot().isEmpty();

//            if (hasHotData || hasNormalData) {
            if (data.getAllCount()==0) {
                mEmptyText.setVisibility(View.VISIBLE);
            } else {
                mEmptyText.setVisibility(View.GONE);
            }
        }

        mAdapter.setNormalViewpointCount(data.getAllCount());
        List<NewViewPointAndReview> hot = data.getHot();
        if (hot != null && !hot.isEmpty()) {
            dataSize = hot.size();
            if (!mHasHotLabel) {
                NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
                newViewPointAndReview.setTag(NewViewPointAndReview.TAG_HOT);
                mAdapter.add(newViewPointAndReview);
                mHasHotLabel = true;
            }
            mHotSize = hot.size() + 2;
            mAdapter.addAll(hot);
        }

        List<NewViewPointAndReview> dataNormal = data.getNormal();
        if (dataNormal != null && !dataNormal.isEmpty()) {
            dataSize = dataSize + dataNormal.size();
            createNormalLabelData();
            mAdapter.addAll(dataNormal);
        }

        if (dataSize < ViewpointType.NEWS_LOAD_MORE_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

    }

    private void createNormalLabelData() {
        if (!mHasNormalLabel) {
            NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
            newViewPointAndReview.setTag(NewViewPointAndReview.TAG_NORMAL);
            mAdapter.add(newViewPointAndReview);
            mHasNormalLabel = true;
        }
    }

    @Override
    public View getContentView() {
        return mRootView;
    }


    @Override
    public void onLoadMore() {
        mPageSize = 30;
        requestNewsViewpointList();
    }

    @Override
    public void onRefresh() {

    }

    @OnClick({R.id.writeComment, R.id.collectIcon, R.id.bottomShareIcon, R.id.mainBody})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.writeComment:
                if (mNewsDetail != null) {
                    if (!LocalUser.getUser().isLogin()) {
                        Launcher.with(getActivity(), LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
                        return;
                    }
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getWriteComment(mNewsDetail))
                            .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_NEWS);
                }
                break;
            case R.id.collectIcon:
                if (mNewsDetail != null)
                    collect(mNewsDetail);
                break;
            case R.id.bottomShareIcon:
                showShareDialog();
                break;
            case R.id.mainBody:
                finish();
                break;
        }
    }

    @Override
    protected void updateCollect(NewsDetail newsDetail) {
        super.updateCollect(newsDetail);
        if (newsDetail.getCollect() > 0) {
            mCollectIcon.setSelected(true);
        } else {
            mCollectIcon.setSelected(false);
        }
        Intent intent = new Intent();
        intent.putExtra(ExtraKeys.TAG, newsDetail.getCollect());
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_NEWS:
                    if (mAdapter.isEmpty()) {
                        refreshData();
                    } else {
                        if (data != null) {
                            createNormalLabelData();
                            WriteCommentResponse writeCommentResponse = data.getParcelableExtra(ExtraKeys.DATA);
                            if (writeCommentResponse != null) {
                                NewViewPointAndReview newViewPointAndReview = writeCommentResponse.getNewViewPointAndReview();
                                mAdapter.add(mHotSize, newViewPointAndReview);
                                mEmptyText.setVisibility(View.GONE);

                                mAdapter.setNormalViewpointCount(mAdapter.getNormalViewpointCount() + 1);
                            }
                        }
                    }
                    break;
                case WriteCommentActivity.REQ_CODE_WRITE_COMMENT_FOR_VIEWPOINT:
                    if (data != null) {
                        WriteCommentResponse writeCommentResponse = data.getParcelableExtra(ExtraKeys.DATA);
                        if (writeCommentResponse != null) {
                            List<NewViewPointAndReview> dataList = mAdapter.getDataList();
                            NewsViewpoint newsViewpoint = writeCommentResponse.getNewsViewpoint();
                            if (dataList != null && !dataList.isEmpty() &&
                                    !TextUtils.isEmpty(writeCommentResponse.getFirstId())) {
                                for (NewViewPointAndReview result : dataList) {
                                    if (writeCommentResponse.getFirstId().equalsIgnoreCase(result.getId())) {
                                        if (result.getVos() != null) {
                                            result.getVos().add(0, newsViewpoint);
                                        } else {
                                            List<NewsViewpoint> newsViewpoints = new ArrayList<>();
                                            result.setVos(newsViewpoints);
                                            result.getVos().add(0, newsViewpoint);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }

                    }

                    break;
                case CommentDetailActivity.REQ_COMMENT_DETAIL:
                    if (data != null) {
                        NewViewPointAndReview newViewPointAndReview = data.getParcelableExtra(ExtraKeys.DATA);
                        if (newViewPointAndReview != null && !TextUtils.isEmpty(newViewPointAndReview.getId())) {
                            List<NewViewPointAndReview> dataList = mAdapter.getDataList();
                            for (NewViewPointAndReview result : dataList) {
                                if (newViewPointAndReview.getId().equalsIgnoreCase(result.getId())) {
                                    result.setIsPraise(newViewPointAndReview.getIsPraise());
                                    result.setPraiseCount(newViewPointAndReview.getPraiseCount());
                                }
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case LoginActivity.REQ_CODE_LOGIN:
                    if (mNewsDetail != null) {
                        requestData(mNewsDetail.getId());
                        refreshData();
                    }
                    break;

            }
        }
    }

    interface OnViewPointClickListener {

        void onReview(NewViewPointAndReview newViewPointAndReview, View view);

        void onPraise(NewViewPointAndReview newViewPointAndReview, int position);

        void onClick(NewViewPointAndReview newViewPointAndReview);
    }

    static class ViewpointReviewAdapter extends BaseRecycleViewAdapter<NewViewPointAndReview, RecyclerView.ViewHolder> {

        private static final int TAG_LABEL = 252;

        private Context mContext;

        private OnViewPointClickListener mOnViewPointClickListener;

        private NewsDetail mNewsDetail;

        private int mNormalViewpointCount;

        public void setNormalViewpointCount(int normalViewpointCount) {
            mNormalViewpointCount = normalViewpointCount;
            notifyDataSetChanged();
        }

        public int getNormalViewpointCount() {
            return mNormalViewpointCount;
        }

        public void setNewsDetail(NewsDetail newsDetail) {
            mNewsDetail = newsDetail;
        }

        public void setOnViewPointClickListener(OnViewPointClickListener onViewPointClickListener) {
            mOnViewPointClickListener = onViewPointClickListener;
        }

        public ViewpointReviewAdapter(Context context) {
            mContext = context;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case HEADER_VIEW_TYPE:
                    View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_news_viewpoint_header, parent, false);
                    return new HeadViewHolder(headView);
                case TAG_LABEL:
                    View labelView = LayoutInflater.from(mContext).inflate(R.layout.layout_news_label, parent, false);
                    return new LabelViewHolder(labelView);
                default:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_viewpoint, parent, false);
                    return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof HeadViewHolder) {
                HeadViewHolder headViewHolder = (HeadViewHolder) holder;
                headViewHolder.bindDataWithView(mNewsDetail);
            } else if (holder instanceof LabelViewHolder) {
                LabelViewHolder labelViewHolder = (LabelViewHolder) holder;
                NewViewPointAndReview itemData = getItemData(position - 1);
                String label;
                if (itemData.getTag() == NewViewPointAndReview.TAG_HOT) {
                    label = mContext.getString(R.string.hot_viewpoint);
                } else {
                    label = mContext.getString(R.string.all_viewpoint_count, mNormalViewpointCount);
                }
                labelViewHolder.mLabel.setText(label);
            } else if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.bindDataWithView(position, getItemData(position - 1), mContext, mOnViewPointClickListener);
            }
        }


        @Override
        public int getItemViewType(int position) {
            NewViewPointAndReview itemData = null;
            if (position > 0) {
                itemData = getItemData(position - 1);
            }
            if (position == 0) {
                return HEADER_VIEW_TYPE;
            } else if (itemData != null) {
                if (itemData.getTag() == NewViewPointAndReview.TAG_HOT
                        || itemData.getTag() == NewViewPointAndReview.TAG_NORMAL) {
                    return TAG_LABEL;
                }
                return super.getItemViewType(position);
            }

            return super.getItemViewType(position);

        }

        @Override
        public int getItemCount() {
            return super.getItemCount() + 1;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.commentContentView)
            ViewPointContentView mViewPointContentView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final int position, final NewViewPointAndReview newViewPointAndReview, final Context context, final OnViewPointClickListener onViewPointClickListener) {
                mViewPointContentView.setViewpointList(newViewPointAndReview);

                mViewPointContentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onViewPointClickListener != null) {
                            onViewPointClickListener.onClick(newViewPointAndReview);
                        }
                    }
                });

                mViewPointContentView.setOnCommentClickListener(new ViewPointContentView.OnCommentClickListener() {
                    @Override
                    public void onReview() {
                        if (onViewPointClickListener != null) {
                            onViewPointClickListener.onReview(newViewPointAndReview, mViewPointContentView);
                        }
                    }

                    @Override
                    public void onPraise() {
                        if (onViewPointClickListener != null) {
                            onViewPointClickListener.onPraise(newViewPointAndReview, position);
                        }
                    }

                    @Override
                    public void onFullText() {
                        if (onViewPointClickListener != null) {
                            onViewPointClickListener.onClick(newViewPointAndReview);
                        }
                    }
                });
            }
        }

        class LabelViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.label)
            TextView mLabel;

            LabelViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        class HeadViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.timeLine)
            TextView mTimeLine;

            HeadViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(NewsDetail newsDetail) {
                mNewsTitle.setText(newsDetail.getTitle());
                mSource.setText(newsDetail.getAuthor());
                mTimeLine.setText(DateUtil.formatNewsStyleTime(newsDetail.getReleaseTime()));
            }
        }
    }

}
