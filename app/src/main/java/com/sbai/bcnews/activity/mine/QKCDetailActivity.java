package com.sbai.bcnews.activity.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.model.mine.QKCDetails;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.view.EmptyRecyclerView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.recycleview.HeaderViewRecycleViewAdapter;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QKCDetailActivity extends RecycleViewSwipeLoadActivity {

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
    private QKCDetailAdapter mQKCDetailAdapter;

    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qkc_detail);
        ButterKnife.bind(this);

        initView();
        requestQksDetailsList();
    }


    private void initView() {
        mQKCDetailAdapter = new QKCDetailAdapter(getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mQKCDetailAdapter);

        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_qkc_detail_head, null);
        headView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mQKCDetailAdapter.addHeaderView(headView);
    }

    private void requestQksDetailsList() {
        Apic.requestQKCDetailsList(mPage)
                .tag(TAG)
                .callback(new Callback<ListResp<QKCDetails>>() {

                    @Override
                    protected void onRespSuccess(ListResp<QKCDetails> resp) {
                        updateQKCList(resp.getListData());
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

    private void updateQKCList(ArrayList<QKCDetails> data) {
        if (mPage == 0) {
            mQKCDetailAdapter.clear();
        }

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mQKCDetailAdapter.addAll(data);
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        requestQksDetailsList();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestQksDetailsList();
    }

    static class QKCDetailAdapter extends HeaderViewRecycleViewAdapter<QKCDetails, QKCDetailAdapter.ViewHolder> {

        private Context mContext;

        QKCDetailAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public ViewHolder onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_qkc_detail, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindContentViewHolder(@NonNull ViewHolder holder, QKCDetails data, int position) {
            holder.bindDataWithView(data, mContext, position);
        }


        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.qksNumber)
            TextView mQksNumber;
            @BindView(R.id.rootView)
            LinearLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bindDataWithView(QKCDetails data, Context context, int position) {
                mName.setText(data.getTypeStr());
                boolean plus = data.getChangeType() == 0;
                mRootView.setSelected(plus);
                mQksNumber.setSelected(plus);

                double number = data.getIntegral();
                String formatNumber;
                if (number == data.getIntegral()) {
                    formatNumber = String.valueOf(number);
                } else {
                    formatNumber = String.valueOf(data.getIntegral());
                }

                if (plus) {
                    mQksNumber.setText(context.getString(R.string.plus_qks, formatNumber));
                } else {
                    mQksNumber.setText(context.getString(R.string.minus_qks, formatNumber));
                }
                String format = DateUtil.format(data.getCreateTime(), DateUtil.FORMAT_SPECIAL_SLASH_NO_HOUR);
                String hour = DateUtil.format(data.getCreateTime(), DateUtil.FORMAT_HOUR_MINUTE);
                SpannableString spannableString = StrUtil.mergeTextWithRatio(format, "\n" + hour, 0.95f);
                mTime.setText(spannableString);
            }
        }
    }

}
