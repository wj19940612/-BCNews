package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.mine.RedPacketInfo;
import com.sbai.bcnews.model.mine.RedPacketListInfo;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
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

public class HourWelfareActivity extends RecycleViewSwipeLoadActivity {

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
    @BindView(R.id.rootView)
    RelativeLayout mRootView;

    private RedPacketInfo mRedPacketInfo;
    private View mHeadView;
    private HourWelfareAdapter mHourWelfareAdapter;

    private int mPageSize = 50;
    private int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_welfare);
        ButterKnife.bind(this);
        translucentStatusBar();
        mRedPacketInfo = getIntent().getParcelableExtra(ExtraKeys.DATA);

        initView();
        requestHourWelfareList();
    }


    private void initView() {
        initHeadView();
        mHourWelfareAdapter = new HourWelfareAdapter(getActivity());
        mHourWelfareAdapter.addHeaderView(mHeadView);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mHourWelfareAdapter);
    }

    private void initHeadView() {
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_hour_welfare_head, null);
        RedPacketListInfo redPacketListInfo = new RedPacketListInfo();
        if (mRedPacketInfo != null) {
            redPacketListInfo.setIntegral(mRedPacketInfo.getIntegral());
            redPacketListInfo.setUserName(mRedPacketInfo.getUserName());
            redPacketListInfo.setUserPortrait(mRedPacketInfo.getUserPortrait());
        }
        setHeadView(redPacketListInfo);
    }

    private void setHeadView(RedPacketListInfo redPacketListInfo) {
        ImageView portrait = mHeadView.findViewById(R.id.portrait);
        TextView robRedPacketNumber = mHeadView.findViewById(R.id.robRedPacketNumber);
        TextView robRedPacketPeopleNumber = mHeadView.findViewById(R.id.robRedPacketPeopleNumber);
        if (redPacketListInfo != null) {
            GlideApp.with(getActivity())
                    .load(redPacketListInfo.getUserPortrait())
                    .placeholder(R.drawable.ic_default_head_portrait)
                    .circleCrop()
                    .into(portrait);
            SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.congrats_you_rob_number_red_packet),
                    getString(R.string.number_qks, String.valueOf(redPacketListInfo.getIntegral())), ContextCompat.getColor(getActivity(), R.color.text_222), getString(R.string.save_account));
            robRedPacketNumber.setText(spannableString);

            robRedPacketPeopleNumber.setText(getString(R.string.total_number_people_rob_red_packet, redPacketListInfo.getTotal()));
        }
    }

    private void requestHourWelfareList() {
        Apic.requestHourWelfareList(mPage, mPageSize)
                .tag(TAG)
                .callback(new Callback2D<Resp<RedPacketListInfo>, RedPacketListInfo>() {
                    @Override
                    protected void onRespSuccessData(RedPacketListInfo data) {
                        setHeadView(data);
                        updateHourWelfareList(data.getData());
                        stopFreshOrLoadAnimation();
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    private void updateHourWelfareList(List<RedPacketInfo> data) {
        if (mPage == 0) {
            mHourWelfareAdapter.clear();
        }
        if (data.size() < mPageSize) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mHourWelfareAdapter.addAll(data);

    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        mPageSize = 30;
        requestHourWelfareList();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mPageSize = 50;
        requestHourWelfareList();
    }

    static class HourWelfareAdapter extends HeaderViewRecycleViewAdapter<RedPacketInfo, HourWelfareAdapter.ViewHolder> {

        private Context mContext;

        HourWelfareAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_hour_welfare, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(@NonNull ViewHolder holder, RedPacketInfo data, int position) {
            holder.bindDataWithView(data, position, mContext);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.split)
            View mSplit;
            @BindView(R.id.portrait)
            ImageView mPortrait;
            @BindView(R.id.userName)
            TextView mUserName;
            @BindView(R.id.timeLine)
            TextView mTimeLine;
            @BindView(R.id.robRedPacketNumber)
            TextView mRobRedPacketNumber;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(RedPacketInfo data, int position, Context context) {
                if (position < 3) {
                    mRobRedPacketNumber.setTextColor(ContextCompat.getColor(context, R.color.text_F5A6));
                } else {
                    mRobRedPacketNumber.setTextColor(ContextCompat.getColor(context, R.color.text_9B9B));
                }
                GlideApp.with(context)
                        .load(data.getUserPortrait())
                        .circleCrop()
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .into(mPortrait);
                mUserName.setText(data.getUserName());
                mTimeLine.setText(DateUtil.format(data.getCreateTime(), DateUtil.FORMAT_MINUTE_SECOND));
                mRobRedPacketNumber.setText(context.getString(R.string.get_rad_packet_number, String.valueOf(data.getIntegral())));
            }
        }
    }

}
