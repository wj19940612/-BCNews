package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.mine.RobRedPacketInfo;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hour_welfare);
        ButterKnife.bind(this);
        mRobRedPacketInfo = getIntent().getParcelableExtra(ExtraKeys.DATA);

        initView();
    }

    private void initView() {
        initHeadView();

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

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

}
