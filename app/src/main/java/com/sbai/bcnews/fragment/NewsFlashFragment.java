package com.sbai.bcnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
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
import com.sbai.bcnews.activity.RelatedNewsActivity;
import com.sbai.bcnews.activity.ShareNewsFlashActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.httplib.ReqError;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by john on 24/01/2018
 * <p>
 * Description: 快讯
 * <p>
 */
public class NewsFlashFragment extends RecycleViewSwipeLoadFragment {
    private static final int LESS_THAN_TIME = 0;
    private static final int GREATER_THAN_TIME = 1;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    Unbinder unbinder;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    private NewsAdapter mNewsAdapter;
    private long mFirstDataTime, mLastDataTime;

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
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
        initRecyclerView();
        requestNewsFlash(mFirstDataTime, GREATER_THAN_TIME);
    }

    @Override
    public void onResume() {
        super.onResume();
        startScheduleJob(60 * 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopScheduleJob();
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        requestNewsFlash(mFirstDataTime, GREATER_THAN_TIME);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            startScheduleJob(60 * 1000);
        } else {
            stopScheduleJob();
        }
    }


    private void scrollToFirstView() {
        int firstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition > 20) {
            mRecyclerView.scrollToPosition(0);
        } else {
            mRecyclerView.smoothScrollToPosition(0);
        }
        mNewsAdapter.refresh();
    }

    private void requestNewsFlash(long time, int status) {
        Apic.getNewsFlash(time, status).tag(TAG)
                .callback(new Callback2D<Resp<List<NewsFlash>>, List<NewsFlash>>() {
                    @Override
                    protected void onRespSuccessData(List<NewsFlash> data) {
                        if (data.size() > 0 && !mNewsAdapter.isEmpty()) {
                            if (data.get(0).getId() == mNewsAdapter.getFirst().getId()) {
                                refreshComplete(R.string.no_more_new_news);
                            } else {
                                refreshSuccess();
                            }
                        } else {
                            refreshSuccess();
                        }
                        updateNewsFlashData(data);
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
                }).fire();
    }

    private void updateNewsFlashData(List<NewsFlash> data) {
        int size = data.size();
        if (size == 0 && mNewsAdapter.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        if (size == 0) return;
        if (mFirstDataTime > 0 && data.get(size - 1).getReleaseTime() > mFirstDataTime) {
            mFirstDataTime = data.get(0).getReleaseTime();
            //定时刷新
            Collections.reverse(data);
            for (NewsFlash newsFlash : data) {
                mNewsAdapter.addFirst(newsFlash);
            }
        } else {
            if (mFirstDataTime == 0) {
                //重新刷新
                mFirstDataTime = data.get(0).getReleaseTime();
                mLastDataTime = data.get(size - 1).getReleaseTime();
                mNewsAdapter.clear();
                mNewsAdapter.addAllData(data);
            } else {
                //加载更多
                mLastDataTime = data.get(size - 1).getReleaseTime();
                mNewsAdapter.addAllData(data);
            }
            if (size < 30) {
                mSwipeToLoadLayout.setLoadMoreEnabled(false);
                mNewsAdapter.showFooterView(true);
            } else {
                mSwipeToLoadLayout.setLoadMoreEnabled(true);
                mNewsAdapter.showFooterView(false);
            }
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
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                mFirstDataTime = 0;
                requestNewsFlash(mFirstDataTime, GREATER_THAN_TIME);
            }
        });

        mRecyclerView.addItemDecoration(new SectionDecoration(mNewsAdapter.getDataList(), getActivity(), new SectionDecoration.DecorationCallback() {
            @Override
            public String getGroupName(int position) {
                NewsFlash newsFlash = mNewsAdapter.getDataList().get(position);
                return DateUtil.format(newsFlash.getReleaseTime(), DateUtil.FORMAT_YEAR_MONTH_DAY_NO_HOUR);
            }
        }));
    }

    @Override
    public void onLoadMore() {
        requestNewsFlash(mLastDataTime, LESS_THAN_TIME);
    }

    @Override
    public void onRefresh() {
        mFirstDataTime = 0;
        requestNewsFlash(mFirstDataTime, GREATER_THAN_TIME);
    }

    @Override
    public void triggerRefresh() {
        super.triggerRefresh();
        smoothScrollToTop();
        onRefresh();
    }

    @OnClick(R.id.titleBar)
    public void onViewClicked() {
        scrollToFirstView();
    }

    static class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private boolean showFooterView;
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

        public void addFirst(NewsFlash newsFlash) {
            dataList.add(0, newsFlash);
            notifyItemInserted(0);
        }

        public void clear() {
            dataList.clear();
            notifyDataSetChanged();
        }

        public void showFooterView(boolean isShow) {
            showFooterView = isShow;
            notifyDataSetChanged();
        }

        public List<NewsFlash> getDataList() {
            return dataList;
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        public boolean isEmpty() {
            return dataList.isEmpty();
        }

        public NewsFlash getFirst() {
            return dataList.isEmpty() ? null : dataList.get(0);
        }

        @Override

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_flash_news, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bindDataWithView(showFooterView && position == dataList.size() - 1, dataList.get(position), mContext, position, getItemCount());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.title)
            TextView mTitle;
            @BindView(R.id.smallLineLayout)
            RelativeLayout mSmallLineLayout;
            @BindView(R.id.timeIcon)
            ImageView mTimeIcon;
            @BindView(R.id.lineLayout)
            RelativeLayout mLineLayout;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.share)
            LinearLayout mShare;
            @BindView(R.id.content)
            TextView mContent;
//            @BindView(R.id.split)
//            View mSplit;
//            @BindView(R.id.footer)
//            TextView mFooter;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }

            private void bindDataWithView(boolean showFooterView, final NewsFlash newsFlash, final Context context, int position, int count) {
                String colorValue = "#0E418C";
                if (position == count - 1) {
                    mTimeIcon.setVisibility(View.GONE);
                    mSmallLineLayout.setVisibility(View.GONE);
                    mLineLayout.setVisibility(View.GONE);
                } else {
                    mTimeIcon.setVisibility(View.VISIBLE);
                    mSmallLineLayout.setVisibility(View.VISIBLE);
                    mLineLayout.setVisibility(View.VISIBLE);
                }
//                if (showFooterView) {
//                    mSplit.setVisibility(View.GONE);
//                    mFooter.setVisibility(View.VISIBLE);
//                } else {
//                    mSplit.setVisibility(View.VISIBLE);
//                    mFooter.setVisibility(View.GONE);
//                }
                mTime.setText(DateUtil.getFormatDayTime(newsFlash.getReleaseTime()));
                if (newsFlash.isImportant()) {
                    if (TextUtils.isEmpty(newsFlash.getTitle())) {
                        mTitle.setVisibility(View.GONE);
                        mContent.setText(newsFlash.getContent().trim());
                        mContent.setTextColor(Color.parseColor(colorValue));
                    } else {
                        mContent.setText(newsFlash.getContent().trim());
                        mContent.setTextColor(Color.parseColor(colorValue));
                        mTitle.setVisibility(View.VISIBLE);
                        mTitle.setText(newsFlash.getTitle().replace("【", "").replace("】", "").trim());
                        mTitle.setTextColor(Color.parseColor(colorValue));
                    }
                } else {
                    if (TextUtils.isEmpty(newsFlash.getTitle())) {
                        mTitle.setVisibility(View.GONE);
                        mContent.setText(newsFlash.getContent().trim());
                        mContent.setTextColor(Color.parseColor("#494949"));
                    } else {
                        mTitle.setVisibility(View.VISIBLE);
                        mTitle.setText(newsFlash.getTitle().replace("【", "").replace("】", "").trim());
                        mTitle.setTextColor(ContextCompat.getColor(context,R.color.text_4949));
                        mContent.setText(newsFlash.getContent());
                        mContent.setTextColor(ContextCompat.getColor(context,R.color.text_4949));
                    }
                }
                mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MobclickAgent.onEvent(context, UmengCountEventId.NEWS_FLASH_SHARE);
                        Launcher.with(context, ShareNewsFlashActivity.class)
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

    static class SectionDecoration extends RecyclerView.ItemDecoration {

        private DecorationCallback mCallback;
        private List<NewsFlash> mNewsFlashes;
        private Context mContext;
        private Paint mPaint;
        private TextPaint mTextPaint;

        private int topGap;
        private int alignBottom;
        private int mLeftMargin;


        public SectionDecoration(List<NewsFlash> newsFlashes, Context context, DecorationCallback callback) {
            mCallback = callback;
            mNewsFlashes = newsFlashes;
            mContext = context;
            mPaint = new Paint();
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.white));

            mTextPaint = new TextPaint(mTextPaint.FAKE_BOLD_TEXT_FLAG);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setTextSize(Display.sp2Px(18, context.getResources()));
            mTextPaint.setColor(ContextCompat.getColor(mContext, R.color.text_4a));
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            //决定悬浮栏的高度等
            topGap = context.getResources().getDimensionPixelSize(R.dimen.sectioned_top);
            //决定文本的显示位置等
            alignBottom = context.getResources().getDimensionPixelSize(R.dimen.sectioned_alignBottom);
            mLeftMargin = context.getResources().getDimensionPixelOffset(R.dimen.sectioned_left_margin);
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildAdapterPosition(view);
            if (pos == 0 || isFirstInGroup(pos)) {
                outRect.top = topGap;
            }
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int itemCount = state.getItemCount();
            final int childCount = parent.getChildCount();
            final int left = parent.getLeft() + parent.getPaddingLeft();
            final int right = parent.getRight() - parent.getPaddingRight();
            String preGroupName;      //标记上一个item对应的Group
            String currentGroupName = null;       //当前item对应的Group
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);
                preGroupName = currentGroupName;
                currentGroupName = mCallback.getGroupName(position);
                if (currentGroupName == null || TextUtils.equals(currentGroupName, preGroupName))
                    continue;
                int viewBottom = view.getBottom();
                float top = Math.max(topGap, view.getTop());//top 决定当前顶部第一个悬浮Group的位置
                if (position + 1 < itemCount) {
                    //获取下个GroupName
                    String nextGroupName = mCallback.getGroupName(position + 1);
                    //下一组的第一个View接近头部
                    if (!currentGroupName.equals(nextGroupName) && viewBottom < top) {
                        top = viewBottom;
                    }
                }
                //根据top绘制group
                c.drawRect(left, top - topGap, right, top, mPaint);
                Paint.FontMetrics fm = mTextPaint.getFontMetrics();
                //文字竖直居中显示
                float baseLine = top - (topGap - (fm.bottom - fm.top)) / 2 - fm.bottom;
                c.drawText(currentGroupName, left + mLeftMargin, baseLine, mTextPaint);
            }
        }


        /**
         * 判断是不是组中的第一个位置
         *
         * @param pos
         * @return
         */
        private boolean isFirstInGroup(int pos) {
            if (pos == 0) {
                return true;
            } else {

                NewsFlash pre = mNewsFlashes.get(pos - 1);
                NewsFlash next = mNewsFlashes.get(pos);
                long preTime = pre.getReleaseTime();
                long nextTime = next.getReleaseTime();
                return !DateUtil.isInThisDay(nextTime, preTime);

            }
        }

        //定义一个借口方便外界的调用
        interface DecorationCallback {
            String getGroupName(int position);
        }

    }
}
