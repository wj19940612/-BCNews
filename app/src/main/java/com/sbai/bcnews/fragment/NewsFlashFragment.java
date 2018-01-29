package com.sbai.bcnews.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ShareNewsFlashActivity;
import com.sbai.bcnews.fragment.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 快讯
 * <p>
 */
public class NewsFlashFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    Unbinder unbinder;
    private NewsAdapter mNewsAdapter;
    private long mTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_flash, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        requestNewsFlash();
    }

    private void requestNewsFlash() {
        Apic.getNewsFlash(mTime).tag(TAG)
                .callback(new Callback2D<Resp<List<NewsFlash>>, List<NewsFlash>>() {
                    @Override
                    protected void onRespSuccessData( List<NewsFlash> data) {
                        updateNewsFlashData(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                }).fireFreely();
    }

    private void updateNewsFlashData(List<NewsFlash> data) {
        if (data.size() < 30) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (mTime == 0) {
            mNewsAdapter.clear();
            mNewsAdapter.addAllData(data);
        } else {
            mNewsAdapter.addAllData(data);
        }
        if (data.size() > 0) {
            mTime = data.get(0).getReleaseTime();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initRecyclerView() {
        mNewsAdapter = new NewsAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @Override
    public void onLoadMore() {
        requestNewsFlash();
    }

    @Override
    public void onRefresh() {
        mTime = 0;
        requestNewsFlash();
    }

    static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<NewsFlash> dataList;
        private Context mContext;

        public NewsAdapter(Context context) {
            super();
            mContext = context;
            dataList = new ArrayList<>();
        }

        public void addAllData(List<NewsFlash> newsList) {
            dataList.addAll(newsList);
            notifyDataSetChanged();
        }

        public void addData(NewsFlash newsFlash) {
            dataList.add(newsFlash);
            notifyDataSetChanged();
        }

        public void clear() {
            dataList.clear();
            notifyDataSetChanged();
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_news_flash, parent, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(dataList.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.share)
            TextView mShare;
            @BindView(R.id.content)
            TextView mContent;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(itemView);
            }

            private void bindDataWithView(final NewsFlash newsFlash, Context context) {
                mTime.setText(DateUtil.getFormatTime(newsFlash.getReleaseTime()).concat(" ").concat(context.getString(R.string.news_flash)));
                if (newsFlash.isImportant()) {
                    mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                            Color.parseColor("#476E92"), Color.parseColor("#476E92")));
                } else {
                    mContent.setText(StrUtil.mergeTextWithRatioColorBold(newsFlash.getTitle(), newsFlash.getContent(), 1.0f,
                            Color.parseColor("#494949"), Color.parseColor("#494949")));
                }
                mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Launcher.with(mContext, ShareNewsFlashActivity.class)
                                .putExtra(ExtraKeys.NEWS_FLASH, newsFlash)
                                .execute();
                    }
                });

                mContent.setMaxLines(6);
                mContent.setEllipsize(TextUtils.TruncateAt.END);
                mContent.setOnClickListener(new View.OnClickListener() {
                    boolean flag = true;

                    @Override
                    public void onClick(View v) {
                        if (flag) {
                            flag = false;
                            mContent.setMaxLines(Integer.MAX_VALUE);
                            mContent.setEllipsize(null);
                        } else {
                            flag = true;
                            mContent.setMaxLines(6);
                            mContent.setEllipsize(TextUtils.TruncateAt.END);
                        }
                    }
                });
            }
        }
    }
}
