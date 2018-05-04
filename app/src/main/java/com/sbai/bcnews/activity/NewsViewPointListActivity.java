package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.sbai.bcnews.fragment.dialog.WhistleBlowingDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpointAndComment;
import com.sbai.bcnews.model.news.ViewpointType;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.CommentPopupWindow;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.news.ViewPointContentView;
import com.sbai.bcnews.view.recycleview.HeaderViewRecycleViewAdapter;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

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
    private String mId;
    private int mPageSize = 50;
    private ViewpointReviewAdapter mAdapter;

    private boolean mHasNormalLabel;

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
        setContentView(R.layout.activity_news_comment_list);
        ButterKnife.bind(this);
        initData();
        initView();
        requestNewsViewpointList();
    }

    private void initView() {
        mSwipeToLoadLayout.setRefreshEnabled(false);


        mAdapter = new ViewpointReviewAdapter(getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mAdapter);

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsShareAndSettingDialog();
            }
        });

//        initHeaderView();

        mAdapter.setOnViewPointClickListener(new OnViewPointClickListener() {
            @Override
            public void onReview(NewViewPointAndReview newViewPointAndReview, View view) {
                showPopupWindow(view, newViewPointAndReview);
            }

            @Override
            public void onPraise(NewViewPointAndReview newViewPointAndReview, int position) {
                praiseComment(newViewPointAndReview);
            }

            @Override
            public void onClick(NewViewPointAndReview newViewPointAndReview) {
                if (mNewsDetail != null) {
                    Launcher.with(getActivity(), CommentDetailActivity.class)
                            .putExtra(ExtraKeys.TAG, newViewPointAndReview.getDataId())
                            .putExtra(ExtraKeys.NEWS_ID, mNewsDetail.getId())
                            .execute();
                }
            }
        });
    }

    private void praiseComment(final NewViewPointAndReview newViewPointAndReview) {
        Apic.praiseComment(newViewPointAndReview.getId(), mNewsDetail.getId(), (long) newViewPointAndReview.getUserId(), null, null, ViewpointType.SECOND_COMMENT)
                .tag(TAG)
                .callback(new Callback<Object>() {
                    @Override
                    protected void onRespSuccess(Object resp) {
                        // TODO: 2018/5/4 先写成这样
                        if (newViewPointAndReview.getIsPraise() == 0) {
                            newViewPointAndReview.setPraiseCount(NewViewPointAndReview.ALREADY_PRAISE);
                            int newPraiseCount = newViewPointAndReview.getPraiseCount() + 1;
                            newViewPointAndReview.setPraiseCount(newPraiseCount);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onSuccess(Object o) {
                        super.onSuccess(o);
                        // TODO: 2018/5/3 随便写的
//                        newViewPointAndReview.setIsPraise(1);
//                        newViewPointAndReview.setPraiseCount(555);
//                        updateCommentPraise(newViewPointAndReview);
                    }
                })
                .fire();
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
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getSecondWriteComment(mNewsDetail, newViewPointAndReview))
                            .putExtra(ExtraKeys.TAG, newViewPointAndReview.getUsername())
                            .execute();
            }

            @Override
            public void onWhistleBlowing() {
                WhistleBlowingDialogFragment.newInstance(WhistleBlowingDialogFragment.WHISTLE_BLOWING_TYPE_COMMENT, String.valueOf(newViewPointAndReview.getDataId())).show(getSupportFragmentManager());
            }
        }).showPopupWindow();
    }

    private void initHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.layout_news_viewpoint_header, null);
        TextView newsTitle = view.findViewById(R.id.newsTitle);
        TextView timeLine = view.findViewById(R.id.timeLine);
        TextView source = view.findViewById(R.id.source);
        if (mNewsDetail != null) {
            newsTitle.setText(mNewsDetail.getTitle());
            source.setText(mNewsDetail.getSource());
            timeLine.setText(DateUtil.formatNewsStyleTime(mNewsDetail.getReleaseTime()));
        }
        mAdapter.addHeaderView(view);
    }

    @Override
    protected void onRecycleViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onRecycleViewScrolled(recyclerView, dx, dy);

    }

    private void initData() {
        mHasNormalLabel = false;
        mNewsDetail = getIntent().getParcelableExtra(ExtraKeys.DATA);
        if (mNewsDetail != null) {
            mId = mNewsDetail.getId();
        }
        // TODO: 2018/5/3 先写死id
        mId = "960799530167795713";
        mPageSize = 50;
        requestData(mId);
    }

    private void requestNewsViewpointList() {
        Apic.requestNewsViewpointList(mId, 0, mPageSize)
                .callback(new Callback2D<Resp<NewsViewpointAndComment>, NewsViewpointAndComment>() {
                    @Override
                    protected void onRespSuccessData(NewsViewpointAndComment data) {
                        if (data != null) {
                            updateNewsViewpointList(data);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
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

    }

    private void updateNewsViewpointList(NewsViewpointAndComment data) {
        List<NewViewPointAndReview> hot = data.getHot();
        if (hot != null && !hot.isEmpty()) {
            NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
            newViewPointAndReview.setTag(NewViewPointAndReview.TAG_HOT);
            mAdapter.add(newViewPointAndReview);
            mAdapter.addAll(hot);
        }

        List<NewViewPointAndReview> dataNormal = data.getNormal();
        if (dataNormal != null && !dataNormal.isEmpty()) {
            if (!mHasNormalLabel) {
                NewViewPointAndReview newViewPointAndReview = new NewViewPointAndReview();
                newViewPointAndReview.setTag(NewViewPointAndReview.TAG_NORMAL);
                mAdapter.add(newViewPointAndReview);
                mHasNormalLabel = true;
            }
            mAdapter.addAll(dataNormal);
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
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getWriteComment(mNewsDetail))
                            .execute();
                }
                break;
            case R.id.collectIcon:

                break;
            case R.id.bottomShareIcon:
                break;
            case R.id.mainBody:
                finish();
                break;
        }
    }

    interface OnViewPointClickListener {

        void onReview(NewViewPointAndReview newViewPointAndReview, View view);

        void onPraise(NewViewPointAndReview newViewPointAndReview, int position);

        void onClick(NewViewPointAndReview newViewPointAndReview);
    }

    static class ViewpointReviewAdapter extends HeaderViewRecycleViewAdapter<NewViewPointAndReview, RecyclerView.ViewHolder> {

        private static final int TAG_LABEL = 252;

        private Context mContext;

        private OnViewPointClickListener mOnViewPointClickListener;

        public void setOnViewPointClickListener(OnViewPointClickListener onViewPointClickListener) {
            mOnViewPointClickListener = onViewPointClickListener;
        }

        public ViewpointReviewAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (viewType) {
                case TAG_LABEL:
                    View labelView = LayoutInflater.from(mContext).inflate(R.layout.layout_news_label, parent, false);
                    return new LabelViewHolder(labelView);
                default:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_viewpoint, parent, false);
                    return new ViewHolder(view);
            }
        }

        @Override
        public void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof LabelViewHolder) {
                NewViewPointAndReview itemData = getItemData(position);
                String label;
                if (itemData.getTag() == NewViewPointAndReview.TAG_HOT) {
                    label = mContext.getString(R.string.hot_viewpoint);
                } else {
                    // TODO: 2018/5/2 随便写的值
                    label = mContext.getString(R.string.all_viewpoint_count, 20);
                }
                ((LabelViewHolder) holder).mLabel.setText(label);
            } else if (holder instanceof ViewHolder) {
                ViewHolder viewHolder = (ViewHolder) holder;
                viewHolder.bindDataWithView(position, getItemData(position), mContext, mOnViewPointClickListener);
            }
        }

        @Override
        public int getItemViewType(int position) {
            NewViewPointAndReview itemData = getItemData(position);
            if (itemData.getTag() == NewViewPointAndReview.TAG_HOT || itemData.getTag() == NewViewPointAndReview.TAG_NORMAL) {
                return TAG_LABEL;
            }
            return super.getItemViewType(position);
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
    }

}
