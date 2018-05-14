package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.Message;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.recycleview.HeaderViewRecycleViewAdapter;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
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
    RelativeLayout mRootView;

    private long mStartTime;
    private MessageAdapter mMessageAdapter;
    private TextView mFooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        int notReadMessageCount = getIntent().getIntExtra(ExtraKeys.DATA, 0);
        initView(notReadMessageCount);
        requestMessage();
    }

    private void initView(int notReadMessageCount) {
        if (notReadMessageCount == 0) {
            mTitleBar.setRightVisible(false);
            mTitleBar.setRightViewEnable(false);
        } else {
            mTitleBar.setRightVisible(true);
            mTitleBar.setRightViewEnable(true);
        }
        mMessageAdapter = new MessageAdapter(getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mMessageAdapter);
        mSwipeTarget.setEmptyView(mEmptyView);
        mMessageAdapter.setOnItemClickListener(new OnItemClickListener<Message>() {
            @Override
            public void onItemClick(Message message, int position) {
                readMessage(message, position);
                if (message.isReview())
                    Launcher.with(getActivity(), ReviewActivity.class).execute();
                else if (message.getType() == Message.MESSAGE_TYPE_FEED_BACK_REVIEW)
                    Launcher.with(getActivity(), FeedbackActivity.class).execute();
            }
        });

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMessageRead();
            }
        });

        createFootView();
    }

    private void createFootView() {
        mFooterView = new TextView(getActivity());
        mFooterView.setText(R.string.there_is_no_more_data);
        mFooterView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        mFooterView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_666));
        mFooterView.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(40, getResources()));
        layoutParams.gravity = Gravity.CENTER;
        mFooterView.setLayoutParams(layoutParams);
    }

    private void readMessage(final Message message, final int position) {
        Apic.readMessage(message.getId())
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {
                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        message.setStatus(Message.MESSAGE_IS_READ);
                        mMessageAdapter.notifyItemChanged(position, message);
                    }
                })
                .fire();
    }

    private void allMessageRead() {
        Apic.postAllMessageRead()
                .tag(TAG)
                .callback(new Callback<Resp<Object>>() {
                    @Override
                    protected void onRespSuccess(Resp<Object> resp) {
                        mMessageAdapter.setAllMessageIsRead(true);
                        mMessageAdapter.notifyDataSetChanged();
                    }
                })
                .fire();
    }

    private void requestMessage() {
        Apic.requestMessage(mStartTime)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<Message>>, List<Message>>() {
                    @Override
                    protected void onRespSuccessData(List<Message> data) {
                        refreshSuccess();
                        updateMessageList(data);
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                        refreshFailure();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    private void updateMessageList(List<Message> data) {

        if (mStartTime == 0) {
            mMessageAdapter.setAllMessageIsRead(false);
            mMessageAdapter.clear();
        }

        mMessageAdapter.addAll(data);

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            if (!mMessageAdapter.isEmpty()) {
                if (!mMessageAdapter.hasFooterView()) {
                    mMessageAdapter.addFooterView(mFooterView);
                }
            }
        } else {
            mStartTime = data.get(data.size() - 1).getCreateTime();
        }


        if (mMessageAdapter.isEmpty()) {
            mSwipeTarget.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mSwipeTarget.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        requestMessage();
    }

    @Override
    public void onRefresh() {
        mStartTime = 0;
        mMessageAdapter.removeFooterView();
        requestMessage();
    }

    static class MessageAdapter extends HeaderViewRecycleViewAdapter<Message, MessageAdapter.ViewHolder> {

        private OnItemClickListener<Message> mOnItemClickListener;
        private Context mContext;
        private boolean mAllMessageIsRead;

        public MessageAdapter(Context context) {
            mContext = context;
        }

        public void setOnItemClickListener(OnItemClickListener<Message> onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
        }

        public void setAllMessageIsRead(boolean allMessageIsRead) {
            mAllMessageIsRead = allMessageIsRead;
        }

        @NonNull
        @Override
        public ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bindDataWithView(getItemData(position), position, mContext, mOnItemClickListener, mAllMessageIsRead);
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

            public void bindDataWithView(final Message message, final int position, Context context, final OnItemClickListener<Message> onItemClickListener, boolean allMessageIsRead) {
                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(message, position);
                        }
                    }
                });

                if (allMessageIsRead || message.getStatus() == Message.MESSAGE_IS_READ) {
                    mNotReadHint.setVisibility(View.GONE);
                } else {
                    mNotReadHint.setVisibility(View.VISIBLE);
                }

                GlideApp.with(context)
                        .load(message.getSourceUserPortrait())
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .circleCrop()
                        .into(mPortrait);

                mUserName.setText(message.getSourceUserName());
                mUserBehavior.setText(message.getTitle());
                mContent.setText(message.getMsg());
                mTimeLine.setText(DateUtil.formatDefaultStyleTime(message.getCreateTime()));

            }
        }
    }
}
