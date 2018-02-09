package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.Feedback;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.ThumbTransform;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sbai.bcnews.utils.DateUtil.FORMAT_HOUR_MINUTE;

/**
 * 意见反馈`
 */

public class FeedbackActivity extends RecycleViewSwipeLoadActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.commentContent)
    EditText mCommentContent;
    @BindView(R.id.addPic)
    ImageButton mAddPic;
    @BindView(R.id.send)
    TextView mSend;
    private FeedbackAdapter mFeedbackAdapter;
    int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initViews();
        requestFeedbackData(true);
    }

    private void initViews() {
        mFeedbackAdapter = new FeedbackAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFeedbackAdapter);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    private void requestFeedbackData(final boolean needScrollToLast) {
        Apic.requestFeedbackList(mPage)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<Feedback>>, List<Feedback>>() {
                    @Override
                    protected void onRespSuccessData(List<Feedback> data) {
                      //  updateFeedbackData(data);
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }


    static class FeedbackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<Feedback> dataList;
        private Context mContext;

        public FeedbackAdapter(Context context) {
            super();
            mContext = context;
            dataList = new ArrayList<>();
        }

        public void addAllData(List<Feedback> newsList) {
            dataList.addAll(newsList);
            notifyDataSetChanged();
        }

        public void addFirst(Feedback newsFlash) {
            dataList.add(0, newsFlash);
            notifyItemInserted(0);
        }

        public void clear() {
            dataList.clear();
            notifyDataSetChanged();
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return dataList.isEmpty();
        }


        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof UserViewHolder) {
                ((UserViewHolder) holder).bindingData(dataList.get(position), needTitle(position), mContext);
            } else if (holder instanceof CustomerViewHolder) {
                ((CustomerViewHolder) holder).bindingData(dataList.get(position), needTitle(position), mContext);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case Feedback.TYPE_USER:
                    View userView = LayoutInflater.from(mContext).inflate(R.layout.row_feedback_user, parent, false);
                    return new UserViewHolder(userView);
                case Feedback.TYPE_CUSTOMER:
                    View customerView = LayoutInflater.from(mContext).inflate(R.layout.row_feedback_customer, parent, false);
                    return new CustomerViewHolder(customerView);
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return dataList.get(position).getType();
        }

        private boolean needTitle(int position) {
            if (position == 0) {
                return true;
            }
            if (position < 0) {
                return false;
            }
            Feedback pre = dataList.get(position - 1);
            Feedback next = dataList.get(position);
            //判断两个时间在不在一天内  不是就要显示标题
            long preTime = pre.getCreateDate();
            long nextTime = next.getCreateDate();
            return !DateUtil.isToday(nextTime, preTime);
        }

        static class UserViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.endLineTime)
            TextView mEndLineTime;
            @BindView(R.id.timeLayout)
            RelativeLayout mTimeLayout;
            @BindView(R.id.contentWrapper)
            RelativeLayout mWrapper;
            @BindView(R.id.timestamp)
            TextView mTimestamp;
            @BindView(R.id.image)
            ImageView mImage;
            @BindView(R.id.text)
            TextView mText;
            @BindView(R.id.headImage)
            ImageView mHeadImage;


            public UserViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void bindingData(final Feedback feedback, boolean needTitle, final Context context) {
                if (needTitle) {
                    mTimeLayout.setVisibility(View.VISIBLE);
                    if (feedback.getCreateDate() > 0) {
                        mEndLineTime.setText(DateUtil.getFeedbackFormatTime(feedback.getCreateDate()));
                    } else {
                        long time = System.currentTimeMillis();
                        if (!TextUtils.isEmpty(feedback.getCreateTime())) {
                            time = DateUtil.convertString2Long(feedback.getCreateTime(), DateUtil.DEFAULT_FORMAT);
                        }
                        mEndLineTime.setText(DateUtil.getFeedbackFormatTime(time));
                    }
                } else {
                    mTimeLayout.setVisibility(View.GONE);
                }
                if (feedback.getCreateDate() > 0) {
                    mTimestamp.setText(DateUtil.format(feedback.getCreateDate(), FORMAT_HOUR_MINUTE));
                } else {
                    long time = System.currentTimeMillis();
                    if (!TextUtils.isEmpty(feedback.getCreateTime())) {
                        time = DateUtil.convertString2Long(feedback.getCreateTime(), DateUtil.DEFAULT_FORMAT);
                    }
                    mTimestamp.setText(DateUtil.format(time, FORMAT_HOUR_MINUTE));
                }

                //判断是否图片
                if (feedback.getContentType() == Feedback.CONTENT_TYPE_TEXT) {
                    mText.setVisibility(View.VISIBLE);
                    mImage.setVisibility(View.GONE);
                    mText.setText(feedback.getContent());
                } else {
                    mText.setVisibility(View.GONE);
                    mImage.setVisibility(View.VISIBLE);
                    GlideApp.with(context).load(feedback.getContent())
                            .transform(new ThumbTransform(context))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mImage);
                }
                if (!TextUtils.isEmpty(feedback.getUserPortrait())) {
                    GlideApp.with(context).load(feedback.getUserPortrait())
                            .circleCrop()
                            .placeholder(R.drawable.ic_avatar_feedback)
                            .into(mHeadImage);
                } else {
                    GlideApp.with(context).load(feedback.getPortrait())
                            .circleCrop()
                            .placeholder(R.drawable.ic_avatar_feedback)
                            .into(mHeadImage);
                }
                if (mImage != null) {
                    mImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (feedback.getContentType() == CONTENT_TYPE_PICTURE) {
//                                PreviewDialogFragment.newInstance(feedback.getContent())
//                                        .show(((FeedbackActivity) context).getSupportFragmentManager());
//                            }
                        }
                    });
                }
            }

        }

        static class CustomerViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.endLineTime)
            TextView mEndLineTime;
            @BindView(R.id.timeLayout)
            RelativeLayout mTimeLayout;
            @BindView(R.id.headImage)
            ImageView mHeadImage;
            @BindView(R.id.timestamp)
            TextView mTimestamp;
            @BindView(R.id.text)
            TextView mText;

            public CustomerViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(Feedback feedback, boolean needTitle, Context context) {
                if (needTitle) {
                    mTimeLayout.setVisibility(View.VISIBLE);
                    //不是很清楚时间戳为何要用两个。。。。。。。。。。。。。。。。。。。。。。。。。。。。
                    if (feedback.getCreateDate() > 0) {
                        mEndLineTime.setText(DateUtil.getFeedbackFormatTime(feedback.getCreateDate()));
                    } else {
                        long time = System.currentTimeMillis();
                        if (!TextUtils.isEmpty(feedback.getCreateTime())) {
                            time = DateUtil.convertString2Long(feedback.getCreateTime(), DateUtil.DEFAULT_FORMAT);
                        }
                        mEndLineTime.setText(DateUtil.getFeedbackFormatTime(time));
                    }
                } else {
                    mTimeLayout.setVisibility(View.GONE);
                }
                if (feedback.getCreateDate() > 0) {
                    mTimestamp.setText(DateUtil.format(feedback.getCreateDate(), FORMAT_HOUR_MINUTE));
                } else {
                    long time = System.currentTimeMillis();
                    if (!TextUtils.isEmpty(feedback.getCreateTime())) {
                        time = DateUtil.convertString2Long(feedback.getCreateTime(), DateUtil.DEFAULT_FORMAT);
                    }
                    mTimestamp.setText(DateUtil.format(time, FORMAT_HOUR_MINUTE));
                }
                mText.setText(feedback.getContent());
                GlideApp.with(context).load(R.drawable.ic_feedback_service)
                        .circleCrop()
                        .into(mHeadImage);
            }
        }
    }
}
