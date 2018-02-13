package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.News;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.view.EmptyView;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RelatedNewsActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;

    private View mView;

    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_related_news, null);
        setContentView(mView);
        ButterKnife.bind(this);

        initViews();

        requestRelatedNews();
    }

    private void initViews() {

    }

    private void requestRelatedNews() {
//        Apic.reqRelatedNews(tag);
        // TODO: 13/02/2018 使用tag 获取相关文章
        Apic.getChannels().tag(TAG)
                .callback(new Callback2D<Resp<List<String>>, List<String>>() {
                    @Override
                    protected void onRespSuccessData(List<String> data) {
                        if (!data.isEmpty()) {
                            getNews(data.get(0));
                        }
                    }
                }).fireFreely();
    }

    private void getNews(String chanel) {
        Apic.requestNewsListWithChannel(chanel, mPage).tag(TAG)
                .callback(new Callback2D<Resp<News>, News>() {
                    @Override
                    protected void onRespSuccessData(News data) {

                    }
                }).fireFreely();
    }

    @Override
    public View getContentView() {
        return mView;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }
}
