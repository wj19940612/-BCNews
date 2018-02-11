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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.ReadHistory;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.OnItemClickListener;
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
    LinearLayout mRootView;
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
        requestReadHistoryData();
    }

    private void requestReadHistoryData() {
        Apic.requestReadHistoryData(mPage)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<ReadHistory>>, List<ReadHistory>>() {
                    @Override
                    protected void onRespSuccessData(List<ReadHistory> data) {
                        updateReadHistoryData(data);
                    }
                })
                .fire();
    }

    private void updateReadHistoryData(List<ReadHistory> data) {
        if (mSet.isEmpty()) {
            mReadHistoryAdapter.clear();
        }
        mPage++;

        for (ReadHistory readHistory : data) {
            if (mSet.add(readHistory.getId())) {
                mReadHistoryAdapter.add(readHistory);
            }
        }
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    private void initView() {
        mTitleBar.setTitle(R.string.read_history);

        mReadHistoryAdapter = new ReadHistoryAdapter(this, new ArrayList<ReadHistory>());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.setAdapter(mReadHistoryAdapter);
    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        mSet.clear();
        mPage = 0;
        requestReadHistoryData();
    }

    @Override
    protected void onRecycleViewScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onRecycleViewScrollStateChanged(recyclerView, newState);
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

        private ArrayList<ReadHistory> mReadHistoryList;
        private Context mContext;
        private OnItemClickListener<ReadHistory> mOnItemClickListener;

        public ReadHistoryAdapter(Context context, ArrayList<ReadHistory> readHistoryList) {
            mReadHistoryList = readHistoryList;
            mContext = context;
        }

        public void add(ReadHistory readHistory) {
            mReadHistoryList.add(readHistory);
            notifyDataSetChanged();
        }

        public void clear() {
            mReadHistoryList.clear();
            notifyDataSetChanged();
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
            boolean theSameDayNews = isTheSameDayNews(position);
            if (holder instanceof NoneOrSingleImageViewHolder) {
                NoneOrSingleImageViewHolder noneOrSingleImageViewHolder = (NoneOrSingleImageViewHolder) holder;
                noneOrSingleImageViewHolder.bindDataWithView(mReadHistoryList.get(position), position, mContext, mOnItemClickListener, theSameDayNews);
            } else if (holder instanceof ThreeImageViewHolder) {
                ThreeImageViewHolder threeImageViewHolder = (ThreeImageViewHolder) holder;
                threeImageViewHolder.bindDataWithView(mReadHistoryList.get(position), position, mContext, mOnItemClickListener, theSameDayNews);
            }
            if (theSameDayNews) {
                holder.itemView.setTag(NONE_STICKY_VIEW);
            } else {
                holder.itemView.setTag(HAS_STICKY_VIEW);
            }
            holder.itemView.setContentDescription(DateUtil.formatMonth(mReadHistoryList.get(position).getReleaseTime()));
        }

        private boolean isTheSameDayNews(int position) {
            if (position == 0) return true;
            ReadHistory last = mReadHistoryList.get(position - 1);
            ReadHistory nex = mReadHistoryList.get(position);
            long createTime = last.getReleaseTime();
            long nexTime = nex.getReleaseTime();
            return DateUtil.isInThisDay(nexTime, createTime);
        }

        @Override
        public int getItemCount() {
            return mReadHistoryList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (!mReadHistoryList.isEmpty()) {
                ReadHistory readHistory = mReadHistoryList.get(position);
                List<String> readHistoryImgs = readHistory.getImgs();
                if (readHistoryImgs != null
                        && !readHistoryImgs.isEmpty()
                        && readHistoryImgs.size() > 2) {
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

            NoneOrSingleImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(ReadHistory item, int position, Context context, OnItemClickListener<ReadHistory> onItemClickListener, boolean theSameDayNews) {

                if (theSameDayNews) {
                    mAdsorbText.setVisibility(View.GONE);
                } else {
                    mAdsorbText.setVisibility(View.VISIBLE);
                    mAdsorbText.setText(DateUtil.formatNewsStyleTime(item.getReleaseTime()));
                }

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

            ThreeImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(ReadHistory item, int position, Context context, OnItemClickListener<ReadHistory> onItemClickListener, boolean theSameDayNews) {
                if (theSameDayNews) {
                    mAdsorbText.setVisibility(View.GONE);
                } else {
                    mAdsorbText.setVisibility(View.VISIBLE);
                    mAdsorbText.setText(DateUtil.formatNewsStyleTime(item.getReleaseTime()));
                }

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
