package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.CandyDetailActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.model.Candy;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.glide.GlideRoundAndCenterCropTransform;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CandyListFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    private Unbinder mBind;

    private List<Candy> mCandyList;
    private CandyAdapter mCandyAdapter;
    private int mPage;

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_candy_list, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadData(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    private void initView() {
        mCandyList = new ArrayList<>();
        mCandyAdapter = new CandyAdapter(mCandyList, getContext(), new OnItemClickListener<Candy>() {
            @Override
            public void onItemClick(Candy candy, int position) {
                Launcher.with(getActivity(), CandyDetailActivity.class).putExtra(ExtraKeys.CANDY,candy).execute();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mCandyAdapter);
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                loadData(true);
            }
        });
    }

    private void loadData(final boolean refresh) {
        Apic.requestCandyList(mPage).tag(TAG).callback(new Callback<ListResp<Candy>>() {
            @Override
            protected void onRespSuccess(ListResp<Candy> resp) {
                updateData(resp.getListData(), refresh);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                mEmptyView.setNoNet();
                if (mCandyList.size() == 0)
                    mEmptyView.setVisibility(View.VISIBLE);
                else
                    mEmptyView.setVisibility(View.GONE);
            }
        }).fireFreely();
    }

    private void updateData(List<Candy> data, boolean refresh) {
        if (refresh) {
            mCandyList.clear();
        }
        if (data == null || data.size() == 0) {
            if (mCandyList.size() > 0) {
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setNoData("");
                mEmptyView.setVisibility(View.VISIBLE);
            }
            mCandyAdapter.notifyDataSetChanged();
            return;
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mCandyList.addAll(data);
        mCandyAdapter.notifyDataSetChanged();
    }

    static class CandyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Candy> mCandyList;
        private Context mContext;
        private OnItemClickListener<Candy> mOnItemClickListener;

        public CandyAdapter(List<Candy> candyList, Context context, OnItemClickListener<Candy> onItemClickListener) {
            mCandyList = candyList;
            mContext = context;
            mOnItemClickListener = onItemClickListener;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_candy, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bindingData(mContext, mCandyList.get(position),position, mOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mCandyList == null ? 0 : mCandyList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.rootView)
            RelativeLayout mRootView;
            @BindView(R.id.head)
            ImageView mHead;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.getCount)
            TextView mGetCount;
            @BindView(R.id.introduce)
            TextView mIntroduce;
            @BindView(R.id.tip)
            TextView mTip;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(Context context, final Candy candy,final int position, final OnItemClickListener onItemClickListener) {
                mName.setText(candy.getName());
                mIntroduce.setText(candy.getIntro());
                mTip.setText(candy.getWelfare());
                if (candy.getClicks() <= 99999) {
                    mGetCount.setText(context.getString(R.string.x_have_get, candy.getClicks()));
                } else {
                    mGetCount.setText(context.getString(R.string.x_ten_thousand_have_get, candy.getClicks() / 10000));
                }
                GlideApp.with(context).load(candy.getPhoto())
                        .transform(new GlideRoundAndCenterCropTransform(context))
                        .placeholder(R.drawable.ic_default_news)
                        .centerCrop()
                        .into(mHead);

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(candy,position);
                        }
                    }
                });
            }
        }
    }
}
