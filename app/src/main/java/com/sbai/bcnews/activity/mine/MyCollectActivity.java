package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.view.ThreeImageLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyCollectActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    ConstraintLayout mRootView;

    private int mPage;
    private HashSet<String> mSet;
    private MyCollectAdapter mMyCollectAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mSet = new HashSet<>();
        mMyCollectAdapter = new MyCollectAdapter(this, new ArrayList<ReadHistoryOrMyCollect>());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.setAdapter(mMyCollectAdapter);
    }

    private void requestCollectNews() {
        Apic.requestReadHistoryOrMyCollectData(ReadHistoryOrMyCollect.MESSAGE_TYPE_MYCOLLECT, mPage)
                .tag(TAG)
                .callback(new Callback<ListResp<ReadHistoryOrMyCollect>>() {

                    @Override
                    protected void onRespSuccess(ListResp<ReadHistoryOrMyCollect> resp) {
                        List<ReadHistoryOrMyCollect> listData = resp.getListData();
                        if (listData != null && !listData.isEmpty()) {
                            updateMyCollectData(listData);
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

    private void updateMyCollectData(List<ReadHistoryOrMyCollect> data) {
        if (mSet.isEmpty()) {
            mMyCollectAdapter.clear();
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        for (ReadHistoryOrMyCollect readHistoryOrMyCollect : data) {
            if (mSet.add(readHistoryOrMyCollect.getId())) {
                mMyCollectAdapter.add(readHistoryOrMyCollect);
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        onRefresh();
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        requestCollectNews();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mSet.clear();
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
        requestCollectNews();
    }

    public static class MyCollectAdapter extends RecyclerView.Adapter {

        private static final int ITEM_TYPE_NONE_OR_SINGLE = 0;
        private static final int ITEM_TYPE_THREE_IMAGE = 1;


        private ArrayList<ReadHistoryOrMyCollect> mReadHistoryOrMyCollectList;
        private Context mContext;
        private OnItemClickListener<ReadHistoryOrMyCollect> mOnItemClickListener;

        public MyCollectAdapter(Context context, ArrayList<ReadHistoryOrMyCollect> readHistoryOrMyCollectList) {
            mReadHistoryOrMyCollectList = readHistoryOrMyCollectList;
            mContext = context;
        }

        public void add(ReadHistoryOrMyCollect readHistoryOrMyCollect) {
            mReadHistoryOrMyCollectList.add(readHistoryOrMyCollect);
            notifyDataSetChanged();
        }

        public void clear() {
            mReadHistoryOrMyCollectList.clear();
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_TYPE_NONE_OR_SINGLE:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_mycollect_single_or_none_image, parent, false);
                    return new MyCollectAdapter.NoneOrSingleImageViewHolder(view);
                case ITEM_TYPE_THREE_IMAGE:
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.row_mycollect_three_image, parent, false);
                    return new MyCollectAdapter.ThreeImageViewHolder(inflate);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyCollectAdapter.NoneOrSingleImageViewHolder) {
                MyCollectAdapter.NoneOrSingleImageViewHolder noneOrSingleImageViewHolder = (MyCollectAdapter.NoneOrSingleImageViewHolder) holder;
                noneOrSingleImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemClickListener);
            } else if (holder instanceof MyCollectAdapter.ThreeImageViewHolder) {
                MyCollectAdapter.ThreeImageViewHolder threeImageViewHolder = (MyCollectAdapter.ThreeImageViewHolder) holder;
                threeImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemClickListener);
            }
        }


        @Override
        public int getItemCount() {
            return mReadHistoryOrMyCollectList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (!mReadHistoryOrMyCollectList.isEmpty()) {
                ReadHistoryOrMyCollect readHistoryOrMyCollect = mReadHistoryOrMyCollectList.get(position);
                List<String> readHistoryImgs = readHistoryOrMyCollect.getImgs();
                if (readHistoryImgs != null
                        && !readHistoryImgs.isEmpty()
                        && readHistoryImgs.size() > 1) {
                    return ITEM_TYPE_THREE_IMAGE;
                }
                return ITEM_TYPE_NONE_OR_SINGLE;
            }
            return super.getItemViewType(position);
        }

        static class NoneOrSingleImageViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.firstSplit)
            View mFirstSplit;
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.cover)
            ImageView mCover;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.contentRoot)
            RelativeLayout mContentRoot;

            NoneOrSingleImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ReadHistoryOrMyCollect item, int position, final Context context, OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {

                if (position == 0) {
                    mFirstSplit.setVisibility(View.VISIBLE);
                } else {
                    mFirstSplit.setVisibility(View.GONE);
                }

                mContentRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Launcher.with(context, NewsDetailActivity.class)
                                .putExtra(ExtraKeys.NEWS_ID, item.getDataId())
                                .putExtra(ExtraKeys.TAG, (item.getChannel() == null || item.getChannel().isEmpty()) ? null : item.getChannel().get(0))
                                .execute();
                    }
                });

                mNewsTitle.setText(item.getTitle());
                mSource.setText(item.getSource());
                mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);
                mSource.setVisibility(TextUtils.isEmpty(item.getSource()) ? View.GONE : View.VISIBLE);
                List<String> itemImgs = item.getImgs();
                if (itemImgs != null && !itemImgs.isEmpty()) {
                    mCover.setVisibility(View.VISIBLE);
                    GlideApp.with(context)
                            .load(itemImgs.get(0))
                            .into(mCover);
                } else {
                    mCover.setVisibility(View.GONE);
                }
            }
        }

        static class ThreeImageViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.firstSplit)
            View mFirstSplit;
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.threeImageLayout)
            ThreeImageLayout mThreeImageLayout;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.contentRoot)
            RelativeLayout mContentRoot;

            ThreeImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ReadHistoryOrMyCollect item, int position, final Context context, OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {
                if (position == 0) {
                    mFirstSplit.setVisibility(View.VISIBLE);
                } else {
                    mFirstSplit.setVisibility(View.GONE);
                }

                mContentRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Launcher.with(context, NewsDetailActivity.class)
                                .putExtra(ExtraKeys.NEWS_ID, item.getDataId())
                                .putExtra(ExtraKeys.TAG, (item.getChannel() == null || item.getChannel().isEmpty()) ? null : item.getChannel().get(0))
                                .execute();
                    }
                });

                mNewsTitle.setText(item.getTitle());
                mSource.setText(item.getSource());
                mOriginal.setVisibility(item.getOriginal() > 0 ? View.VISIBLE : View.GONE);
                mSource.setVisibility(TextUtils.isEmpty(item.getSource()) ? View.GONE : View.VISIBLE);
                List<String> itemImgs = item.getImgs();
                if (itemImgs != null && !itemImgs.isEmpty()) {
                    mThreeImageLayout.setVisibility(View.VISIBLE);
                    mThreeImageLayout.setImagePath(itemImgs);
                } else {
                    mThreeImageLayout.setVisibility(View.GONE);
                }
            }
        }
    }
}
