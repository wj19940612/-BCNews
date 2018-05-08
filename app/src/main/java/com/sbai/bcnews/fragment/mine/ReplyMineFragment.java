package com.sbai.bcnews.fragment.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.news.ViewPointComment;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.view.recycleview.BaseRecycleViewAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ReplyMineFragment extends RecycleViewSwipeLoadFragment {

    private Unbinder mBind;

    private int mPageType;

    public static ReplyMineFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(ExtraKeys.TAG, type);
        ReplyMineFragment fragment = new ReplyMineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageType = getArguments().getInt(ExtraKeys.TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_minek, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestViewpointList();
    }

    private void requestViewpointList() {
//        Apic.requestMineReplyOrCommentViewpointList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    static class ReplyMineAdapter extends BaseRecycleViewAdapter<ViewPointComment, ReplyMineAdapter.ViewHolder> {

        private Context mContext;
        private int mPageType;

        public ReplyMineAdapter(Context context, int pageType) {
            mContext = context;
            mPageType = pageType;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_review_mine, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(getItemData(position), position, mContext, mPageType);
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.portrait)
            ImageView mPortrait;
            @BindView(R.id.userName)
            TextView mUserName;
            @BindView(R.id.praiseCount)
            TextView mPraiseCount;
            @BindView(R.id.pointContent)
            TextView mPointContent;
            @BindView(R.id.timeLine)
            TextView mTimeLine;
            @BindView(R.id.review)
            TextView mReview;
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.newsImage)
            ImageView mNewsImage;
            @BindView(R.id.content)
            TextView mContent;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(ViewPointComment itemData, int position, Context context, int pageType) {

            }
        }
    }
}
