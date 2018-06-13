package com.sbai.bcnews.activity.mine;

import android.app.Dialog;
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
import android.widget.Space;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.ReadHistoryOrMyCollect;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.OnItemLongClickListener;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.ThreeImageLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.move.SwipeItemLayout;
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
    LinearLayout mRootView;

    private int mPage;
    private HashSet<String> mSet;
    private MyCollectAdapter mMyCollectAdapter;
    private ArrayList<ReadHistoryOrMyCollect> mReadHistoryOrMyCollectList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        ButterKnife.bind(this);
        initView();
        onRefresh();
    }

    private void initView() {
        mSet = new HashSet<>();
        mReadHistoryOrMyCollectList = new ArrayList<>();
        mMyCollectAdapter = new MyCollectAdapter(this, mReadHistoryOrMyCollectList);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(this));
        mSwipeTarget.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        mSwipeTarget.setAdapter(mMyCollectAdapter);

        mMyCollectAdapter.setOnItemLongClickListener(new OnItemLongClickListener<ReadHistoryOrMyCollect>() {
            @Override
            public boolean onLongClick(View v, final int position, final ReadHistoryOrMyCollect readHistoryOrMyCollect) {
                SmartDialog.with(getActivity(), R.string.sure_delete_this_content)
                        .setPositive(R.string.ok, new SmartDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog) {
                                dialog.dismiss();
                                cancelCollect(position, readHistoryOrMyCollect);
                            }
                        })
                        .show();

                return false;
            }
        });

        mMyCollectAdapter.setOnItemClickListener(new OnItemClickListener<ReadHistoryOrMyCollect>() {
            @Override
            public void onItemClick(ReadHistoryOrMyCollect item, int position) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, item.getDataId())
                        .putExtra(ExtraKeys.CHANNEL, (item.getChannel() == null || item.getChannel().isEmpty()) ? null : item.getChannel().get(0))
                        .executeForResult(NewsDetailActivity.REQ_CODE_CANCEL_COLLECT);
            }
        });
    }

    private void cancelCollect(final int position, final ReadHistoryOrMyCollect readHistoryOrMyCollect) {
        Apic.collectOrCancelCollect(readHistoryOrMyCollect.getDataId(), ReadHistoryOrMyCollect.CANCEL_COLLECT, ReadHistoryOrMyCollect.MESSAGE_TYPE_COLLECT)
                .callback(new Callback<Resp<Object>>() {

                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        mMyCollectAdapter.removeItem(position, readHistoryOrMyCollect);
                    }

                    @Override
                    protected void onRespFailure(Resp failedResp) {
                        super.onRespFailure(failedResp);
                        if (failedResp.getCode() == Resp.CODE_ARTICLE_ALREADY_SOLD_OUT) {
                            mMyCollectAdapter.removeItem(position, readHistoryOrMyCollect);
                        }
                    }
                })
                .fire();
    }

    private void requestCollectNews() {
        Apic.requestReadHistoryOrMyCollectData(ReadHistoryOrMyCollect.MESSAGE_TYPE_COLLECT, mPage)
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
            if (mSet.add(readHistoryOrMyCollect.getDataId())) {
                mMyCollectAdapter.add(readHistoryOrMyCollect);
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NewsDetailActivity.REQ_CODE_CANCEL_COLLECT:
                    String id = data.getStringExtra(ExtraKeys.TAG);
                    if (!TextUtils.isEmpty(id)) {
                        for (ReadHistoryOrMyCollect readHistoryOrMyCollect : mReadHistoryOrMyCollectList) {
                            if (id.equalsIgnoreCase(readHistoryOrMyCollect.getId())) {
                                mReadHistoryOrMyCollectList.remove(readHistoryOrMyCollect);
                                mMyCollectAdapter.notifyDataSetChanged();
                                break;
                            }
                        }
                    }
                    break;
            }
        }
    }

    public static class MyCollectAdapter extends RecyclerView.Adapter {

        private static final int ITEM_TYPE_NONE_OR_SINGLE = 0;
        private static final int ITEM_TYPE_THREE_IMAGE = 1;

        private ArrayList<ReadHistoryOrMyCollect> mReadHistoryOrMyCollectList;
        private Context mContext;
        private OnItemLongClickListener<ReadHistoryOrMyCollect> mOnItemLongClickListener;
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

        public void removeItem(int position, ReadHistoryOrMyCollect readHistoryOrMyCollect) {
            mReadHistoryOrMyCollectList.remove(position);
            notifyDataSetChanged();
        }

        public void setOnItemLongClickListener(OnItemLongClickListener<ReadHistoryOrMyCollect> onItemLongClickListener) {
            mOnItemLongClickListener = onItemLongClickListener;
        }

        public void setOnItemClickListener(OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case ITEM_TYPE_NONE_OR_SINGLE:
                    View view = LayoutInflater.from(mContext).inflate(R.layout.row_mycollect_single_or_none_image, parent, false);
                    return new NoneOrSingleImageViewHolder(view);
                case ITEM_TYPE_THREE_IMAGE:
                    View inflate = LayoutInflater.from(mContext).inflate(R.layout.row_mycollect_three_image, parent, false);
                    return new ThreeImageViewHolder(inflate);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NoneOrSingleImageViewHolder) {
                NoneOrSingleImageViewHolder noneOrSingleImageViewHolder = (NoneOrSingleImageViewHolder) holder;
                noneOrSingleImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemLongClickListener, mOnItemClickListener);
            } else if (holder instanceof ThreeImageViewHolder) {
                ThreeImageViewHolder threeImageViewHolder = (ThreeImageViewHolder) holder;
                threeImageViewHolder.bindDataWithView(mReadHistoryOrMyCollectList.get(position), position, mContext, mOnItemLongClickListener, mOnItemClickListener);
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
            @BindView(R.id.split)
            Space mSplit;
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.cover)
            ImageView mCover;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.main)
            RelativeLayout mMain;


            NoneOrSingleImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ReadHistoryOrMyCollect item, final int position, final Context context, final OnItemLongClickListener<ReadHistoryOrMyCollect> onItemLongClickListener, final OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {

                if (position == 0) {
                    mSplit.setVisibility(View.VISIBLE);
                } else {
                    mSplit.setVisibility(View.GONE);
                }

                mMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(item, position);
                        }
                    }
                });

                mMain.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onLongClick(v, position, item);
                        }
                        return false;
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
                            .circleCrop()
                            .into(mCover);
                } else {
                    mCover.setVisibility(View.GONE);
                }
            }
        }

        static class ThreeImageViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.firstSplit)
            Space mFirstSplit;
            @BindView(R.id.newsTitle)
            TextView mNewsTitle;
            @BindView(R.id.threeImageLayout)
            ThreeImageLayout mThreeImageLayout;
            @BindView(R.id.original)
            TextView mOriginal;
            @BindView(R.id.source)
            TextView mSource;
            @BindView(R.id.main)
            RelativeLayout mMain;

            ThreeImageViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final ReadHistoryOrMyCollect item, final int position, final Context context, final OnItemLongClickListener<ReadHistoryOrMyCollect> onItemLongClickListener, final OnItemClickListener<ReadHistoryOrMyCollect> onItemClickListener) {
                if (position == 0) {
                    mFirstSplit.setVisibility(View.VISIBLE);
                } else {
                    mFirstSplit.setVisibility(View.GONE);
                }

                mMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(item, position);
                        }
                    }
                });

                mMain.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onLongClick(v, position, item);
                        }
                        return false;
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
