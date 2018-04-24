package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.sbai.bcnews.model.mine.Message;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageActivity extends RecycleViewSwipeLoadActivity {

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
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    private Long mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        requestMessage();
    }

    private void requestMessage() {
        Apic.requestMessage(mStartTime)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<Message>>, List<Message>>() {
                    @Override
                    protected void onRespSuccessData(List<Message> data) {

                    }
                })
                .fire();
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {
        mStartTime = null;
    }


    static class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

        private OnItemClickListener<Message> mOnItemClickListener;
        private Context mContext;
        private List<Message> mMessageList;

        public MessageAdapter(Context context, List<Message> messageList) {
            mContext = context;
            mMessageList = messageList;
        }

        public void setOnItemClickListener(OnItemClickListener<Message> onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(mMessageList.get(position), position, mContext, mOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mMessageList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.notReadHint)
            ImageView mNotReadHint;
            @BindView(R.id.portrait)
            ImageView mPortrait;
            @BindView(R.id.userName)
            TextView mUserName;
            @BindView(R.id.userBehavior)
            TextView mUserBehavior;
            @BindView(R.id.content)
            TextView mContent;
            @BindView(R.id.timeLine)
            TextView mTimeLine;
            @BindView(R.id.rootView)
            CardView mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(final Message message, final int position, Context context, final OnItemClickListener<Message> onItemClickListener) {
                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(message, position);
                        }
                    }
                });

                GlideApp.with(context)
                        .load(message.getSourceUserPortrait())
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .circleCrop()
                        .into(mPortrait);

                mUserName.setText(message.getSourceUserName());
                mUserBehavior.setText(message.getTitle());
                mContent.setText(message.getMsg());
                mTimeLine.setText(DateUtil.formatNewsStyleTime(message.getCreateTime()));

            }
        }
    }
}
