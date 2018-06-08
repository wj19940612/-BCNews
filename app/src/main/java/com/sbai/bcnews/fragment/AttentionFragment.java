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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.model.NewsAuthor;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.wrap.NewsWrap;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.news.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.sbai.bcnews.ExtraKeys.CHANNEL;

public class AttentionFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.emptyView)
    LinearLayout mEmptyView;
    @BindView(R.id.emptyRecyclerView)
    RecyclerView mEmptyRecyclerView;
    private Unbinder mBind;

    private RecommendAdapter mRecommendAdapter;
    private List<NewsAuthor> mNewsAuthorList;

    private List<NewsWrap> mNewsWraps;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attention, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
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
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    private void initView() {
        mNewsAuthorList = new ArrayList<>();
        mEmptyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.HORIZONTAL, false));
        mRecommendAdapter = new RecommendAdapter(mNewsAuthorList, getActivity());
        mEmptyRecyclerView.setAdapter(mRecommendAdapter);

        mNewsWraps = new ArrayList<>();

        NewsAdapter newsAdapter = new NewsAdapter(getActivity(), mNewsWraps, new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(NewsDetail newsDetail) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, newsDetail.getId());
//                        .putExtra(CHANNEL, mChannel).execute();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    static class RecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<NewsAuthor> mNewsAuthors;
        private Context mContext;

        public RecommendAdapter(List<NewsAuthor> newsAuthors, Context context) {
            mNewsAuthors = newsAuthors;
            mContext = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_recommend_attention, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bindingData(mNewsAuthors.get(position), mContext);
        }

        @Override
        public int getItemCount() {
            return mNewsAuthors == null ? 0 : mNewsAuthors.size();
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.head)
            ImageView mHead;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.attentionBtn)
            TextView mAttentionBtn;
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(NewsAuthor newsAuthor, Context context) {

            }
        }
    }
}
