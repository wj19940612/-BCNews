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
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.mine.RedPacketInfo;
import com.sbai.bcnews.model.mine.RobRedPacketInfo;
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

import java.util.ArrayList;
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

    private RobRedPacketInfo mRobRedPacketInfo;
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
        mRobRedPacketInfo = getIntent().getParcelableExtra(ExtraKeys.DATA);

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
        ImageView portrait = mHeadView.findViewById(R.id.portrait);
        TextView robRedPacketNumber = mHeadView.findViewById(R.id.robRedPacketNumber);
        TextView robRedPacketPeopleNumber = mHeadView.findViewById(R.id.robRedPacketPeopleNumber);

        GlideApp.with(getActivity())
                .load(LocalUser.getUser().getUserInfo().getUserPortrait())
                .placeholder(R.drawable.ic_default_head_portrait)
                .circleCrop()
                .into(portrait);
        if (mRobRedPacketInfo != null) {
            SpannableString spannableString = StrUtil.mergeTextWithColor(getString(R.string.congrats_you_rob_number_red_packet),
                    getString(R.string.number_qks, String.valueOf(mRobRedPacketInfo.getMoney())), ContextCompat.getColor(getActivity(), R.color.text_222), getString(R.string.save_account));
            robRedPacketNumber.setText(spannableString);

            robRedPacketPeopleNumber.setText(getString(R.string.total_number_people_rob_red_packet, mRobRedPacketInfo.getTotalPeople()));
        }
    }

    private void requestHourWelfareList() {
        Apic.requestHourWelfareList(mPage, mPageSize)
                .tag(TAG)
                .callback(new Callback2D<Resp<List<RedPacketInfo>>, List<RedPacketInfo>>() {
                    @Override
                    protected void onRespSuccessData(List<RedPacketInfo> data) {
                        updateHourWelfareList(data);
                    }

                    @Override
                    public void onFailure(ReqError reqError) {
                        super.onFailure(reqError);
                        // TODO: 2018/5/18 模拟数据
                        ArrayList<RedPacketInfo> redPacketInfos = new ArrayList<>();
                        for (int i = 0; i < mPageSize; i++) {
                            RedPacketInfo redPacketInfo = new RedPacketInfo();
                            redPacketInfo.setMoney(i);
                            redPacketInfo.setPortrait("https://t10.baidu.com/it/u=2039509812,1027111384&fm=173&app=25&f=JPEG?w=500&h=330&s=B2377084CA603C86E6AA4A810300B09A");
                            redPacketInfo.setTime(System.currentTimeMillis());
                            redPacketInfo.setUserName("第 " + i + " 抢到的人");
                            redPacketInfos.add(redPacketInfo);
                        }
                        updateHourWelfareList(redPacketInfos);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
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
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        mPageSize = 50;
    }

    static class HourWelfareAdapter extends HeaderViewRecycleViewAdapter<RedPacketInfo, HourWelfareAdapter.ViewHolder> {

        private Context mContext;

        public HourWelfareAdapter(Context context) {
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
                        .load(data.getPortrait())
                        .circleCrop()
                        .placeholder(R.drawable.ic_default_head_portrait)
                        .into(mPortrait);
                mUserName.setText(data.getUserName());
                mTimeLine.setText(DateUtil.format(data.getTime(), DateUtil.FORMAT_MINUTE_SECOND));
                mRobRedPacketNumber.setText(context.getString(R.string.get_rad_packet_number, String.valueOf(data.getMoney())));
            }
        }
    }

}
