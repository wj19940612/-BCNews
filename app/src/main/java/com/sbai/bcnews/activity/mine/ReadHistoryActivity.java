package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
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
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.view.EmptyRecyclerView;
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

public class ReadHistoryActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.adsorb_text)
    TextView mAdsorbText;
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
    @BindView(R.id.rootView)
    RelativeLayout mRootView;
    private ReadHistoryAdapter mReadHistoryAdapter;

    private int mPage;
    private HashSet<String> mSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_history);
        ButterKnife.bind(this);

        initView();
        mSet = new HashSet<>();
        if (LocalUser.getUser().isLogin()) {
            requestReadHistoryData();
        } else {
            mSwipeToLoadLayout.setRefreshEnabled(false);
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            List<ReadHistoryOrMyCollect> data = NewsCache.getReadHistory();

            if (data != null && data.size() != 0) {
                updateReadHistoryData(data, true);
            }
        }

    }

    private void requestReadHistoryData() {
        Apic.requestReadHistoryOrMyCollectData(ReadHistoryOrMyCollect.MESSAGE_TYPE_READ_HISTORY, mPage)
                .tag(TAG)
                .callback(new Callback<ListResp<ReadHistoryOrMyCollect>>() {

                    @Override
                    protected void onRespSuccess(ListResp<ReadHistoryOrMyCollect> resp) {
                        List<ReadHistoryOrMyCollect> listData = resp.getListData();
                        if (listData != null && !listData.isEmpty()) {
                            updateReadHistoryData(listData, false);
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

    private void updateReadHistoryData(List<ReadHistoryOrMyCollect> data, boolean cache) {
        if (mSet.isEmpty()) {
            mReadHistoryAdapter.clear();
        }
        if (!cache) {
            if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
                mSwipeToLoadLayout.setLoadMoreEnabled(false);
            } else {
                mPage++;
            }
        }

        for (ReadHistoryOrMyCollect readHistoryOrMyCollect : data) {
            if (mSet.add(readHistoryOrMyCollect.getDataId())) {
                mReadHistoryAdapter.add(readHistoryOrMyCollect);
            }
        }
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    private void initView() {
        mTitleBar.setTitle(R.string.read_history);

        mReadHistoryAdapter = new ReadHistoryAdapter(this, new ArrayList<ReadHistoryOrMyCollect>());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.setAdapter(mReadHistoryAdapter);

        mReadHistoryAdapter.setOnItemClickListener(new OnItemClickListener<ReadHistoryOrMyCollect>() {
            @Override
            public void onItemClick(ReadHistoryOrMyCollect item, int position) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, item.getDataId())
                        .putExtra(ExtraKeys.CHANNEL, (item.getChannel() == null || item.getChannel().isEmpty()) ? null : item.getChannel().get(0))
                        .execute();
            }
        });
    }


    @Override
    public void onLoadMore() {
        requestReadHistoryData();
    }

    @Override
    public void onRefresh() {
        mSet.clear();
        mPage = 0;
        requestReadHistoryData();
        mSwipeToLoadLayout.setLoadMoreEnabled(true);
    }


    @Override
    protected void onRecycleViewScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onRecycleViewScrolled(recyclerView, dx, dy);
        View stickyInfoView = recyclerView.findChildViewUnder(
                mAdsorbText.getMeasuredWidth() / 2, 5);

        if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
            mAdsorbText.setText(String.valueOf(stickyInfoView.getContentDescription()));
        }

        View transInfoView = recyclerView.findChildViewUnder(
                mAdsorbText.getMeasuredWidth() / 2, mAdsorbText.getMeasuredHeight() + 1);
        if (transInfoView != null && transInfoView.getTag() != null) {
            int transViewStatus = (int) transInfoView.getTag();
            int dealtY = transInfoView.getTop() - mAdsorbText.getMeasuredHeight();

//            Log.d(TAG, "onRecycleViewScrollStateChanged: " + dealtY);

            if (transViewStatus == ReadHistoryAdapter.HAS_STICKY_VIEW) {
                if (transInfoView.getTop() > 0) {
                    mAdsorbText.setTranslationY(dealtY);
                } else {
                    mAdsorbText.setTranslationY(0);
                }
            } else if (transViewStatus == ReadHistoryAdapter.NONE_STICKY_VIEW) {
                mAdsorbText.setTranslationY(0);
            }
        }
    }

    public static class ReadHistoryAdapter extends RecyclerView.Adapter {

        private static final int ITEM_TYPE_NONE_OR_SINGLE = 0;
        private static final int ITEM_TYPE_THREE_IMAGE = 1;

        public static final int HAS_STICKY_VIEW = 2;
        public static final int NONE_STICKY_VIEW = 3;


        private ArrayList<ReadHistoryOrMyCollect> mReadHistoryOrMyCollectList;
        private Context mContext;
        private OnItemClickListener<ReadHistoryOrMyCollect> mOnItemClickListener;

        public ReadHistoryAdapter(Context context, ArrayList<ReadHistoryOrMyCollect> readHistoryOrMyCollectList) {
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

        public void setOnItemClickListener(OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_TYPE_NONE_OR_SINGLE:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_read_history_single_or_none_image, parent, false);
                    return new NoneOrSingleImageViewHolder(view);
                case ITEM_TYPE_THREE_IMAGE:
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.row_read_history_three_image, parent, false);
                    return new ThreeImageViewHolder(inflate);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            boolean theDifferentDayNews = isTheDifferentNews(position);
            if (holder instanceof NoneOrSingleImageViewHolder) {
                NoneOrSingleImageViewHolder noneOrSingleImageViewHolder = (NoneOrSingleImageViewHolder) holder;
                noneOrSingleImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemClickListener, theDifferentDayNews);
            } else if (holder instanceof ThreeImageViewHolder) {
                ThreeImageViewHolder threeImageViewHolder = (ThreeImageViewHolder) holder;
                threeImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemClickListener, theDifferentDayNews);
            }
            if (theDifferentDayNews) {
                holder.itemView.setTag(HAS_STICKY_VIEW);
            } else {
                holder.itemView.setTag(NONE_STICKY_VIEW);
            }
            holder.itemView.setContentDescription(DateUtil.formatNewsStyleTime(mReadHistoryOrMyCollectList.get(position).getReadTime()));
        }

        private boolean isTheDifferentNews(int position) {
            if (position == 0) return false;
            ReadHistoryOrMyCollect last = mReadHistoryOrMyCollectList.get(position - 1);
            ReadHistoryOrMyCollect nex = mReadHistoryOrMyCollectList.get(position);
            long createTime = last.getReadTime();
            long nexTime = nex.getReadTime();
            return !DateUtil.isInThisDay(nexTime, createTime);
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
            @BindView(R.id.adsorb_text)
            TextView mAdsorbText;
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

            public void bindDataWithView(final ReadHistoryOrMyCollect item, final int position, Context context, final OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener, boolean theDifferentDayNews) {

                if (theDifferentDayNews) {
                    mAdsorbText.setText(DateUtil.formatNewsStyleTime(item.getReadTime()));
                    mAdsorbText.setVisibility(View.VISIBLE);
                } else {
                    mAdsorbText.setVisibility(View.GONE);
                }

                mContentRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(item, position);
                        }
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
            @BindView(R.id.adsorb_text)
            TextView mAdsorbText;
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

            public void bindDataWithView(final ReadHistoryOrMyCollect item, final int position, Context context, final OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener, boolean theDifferentDayNews) {
                if (theDifferentDayNews) {
                    mAdsorbText.setVisibility(View.VISIBLE);
                    mAdsorbText.setText(DateUtil.formatNewsStyleTime(item.getReadTime()));
                } else {
                    mAdsorbText.setVisibility(View.GONE);
                }

                mContentRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(item, position);
                        }
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
