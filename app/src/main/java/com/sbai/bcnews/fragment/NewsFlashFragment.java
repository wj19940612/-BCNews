package com.sbai.bcnews.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ShareNewsFlashActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.httplib.ReqCallback;

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
public class NewsFlashFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private NewsAdapter mNewsAdapter;
    private int mPage;

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
        Apic.getNewsFlash(mPage).tag(TAG)
                .callback(new Callback2D<Resp<List<NewsFlash>>, List<NewsFlash>>() {
                    @Override
                    protected void onRespSuccessData(final List<NewsFlash> data) {
                        updateNewsFlashData(data);
                    }
                }).fireFreely();
    }

    private void updateNewsFlashData(List<NewsFlash> data) {
        mNewsAdapter.clear();
        mNewsAdapter.addAllData(data);
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
            }
        }
    }
}
