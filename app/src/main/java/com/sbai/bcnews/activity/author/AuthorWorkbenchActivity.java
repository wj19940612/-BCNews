package com.sbai.bcnews.activity.author;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.PersonalDataActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.UserInfo;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.NumberUtils;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.utils.adapter.AuthorArticleAdapter;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthorWorkbenchActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;


    HasLabelLayout mHasLabelLayout;
    TextView mNickName;
    TextView mModifyInfo;
    TextView mYesterdayRedNumber;
    TextView mTotalRedNumber;
    TextView mYesterdayPraiseNumber;
    TextView mTotalPraiseNumber;
    TextView mYesterdayFansNumber;
    TextView mTotalFansNumber;
    TextView mMyArticleTotal;

    private AuthorArticleAdapter mAuthorArticleAdapter;

    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_workbench);
        ButterKnife.bind(this);

        initView();

        requestAuthorInfo();
        requestAuthorArticle();
    }

    private void requestAuthorInfo() {
        Apic.requestWorkbenchAuthorInfo()
                .tag(TAG)
                .callback(new Callback2D<Resp<Author>, Author>() {
                    @Override
                    protected void onRespSuccessData(Author data) {
                        updateAuthorData(data);
                    }
                })
                .fire();
    }

    private void requestAuthorArticle() {
        Apic.requestAuthorWorkbenchArticle(mPage)
                .tag(TAG)
                .callback(new Callback<ListResp<AuthorArticle>>() {
                    @Override
                    protected void onRespSuccess(ListResp<AuthorArticle> resp) {
                        if (resp != null && resp.getListData() != null)
                            updateArticle(resp.getListData());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    private void initView() {


        mAuthorArticleAdapter = new AuthorArticleAdapter(getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mAuthorArticleAdapter);

        initHeadView();

    }

    private void initHeadView() {
        View mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_author_workbench_head, mSwipeTarget, false);
        mAuthorArticleAdapter.addHeaderView(mHeadView);
        mHasLabelLayout = mHeadView.findViewById(R.id.hasLabelLayout);
        mNickName = mHeadView.findViewById(R.id.nickName);
        mModifyInfo = mHeadView.findViewById(R.id.modifyInfo);
        mYesterdayRedNumber = mHeadView.findViewById(R.id.yesterdayRedNumber);
        mTotalRedNumber = mHeadView.findViewById(R.id.totalRedNumber);
        mYesterdayPraiseNumber = mHeadView.findViewById(R.id.yesterdayPraiseNumber);
        mTotalPraiseNumber = mHeadView.findViewById(R.id.totalPraiseNumber);
        mYesterdayFansNumber = mHeadView.findViewById(R.id.yesterdayFansNumber);
        mTotalFansNumber = mHeadView.findViewById(R.id.totalFansNumber);
        mMyArticleTotal = mHeadView.findViewById(R.id.myArticleTotal);

        UserInfo userInfo = LocalUser.getUser().getUserInfo();
        boolean isAuthor = userInfo.getAuthorType() != Author.AUTHOR_STATUS_ORDINARY;
        mHasLabelLayout.setLabelImageViewVisible(isAuthor);
        if (isAuthor) {
            boolean isOfficialAuthor = userInfo.getAuthorType() == Author.AUTHOR_STATUS_OFFICIAL;
            mHasLabelLayout.setLabelSelected(isOfficialAuthor);
        }


        mModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.with(getActivity(), PersonalDataActivity.class).execute();
            }
        });
    }

    public void updateAuthorData(Author author) {
        mHasLabelLayout.setImageSrc(author.getUserPortrait());
        mNickName.setText(author.getUserName());

        setYesterdayData(mYesterdayRedNumber, author.getYesterdayReadCount());
        setYesterdayData(mYesterdayFansNumber, author.getYesterdayFansCount());
        setYesterdayData(mYesterdayPraiseNumber, author.getYesterdayPraiseCount());

        setTotalData(mTotalRedNumber, author.getShowReadCount());
        setTotalData(mTotalPraiseNumber, author.getPraiseCount());
        setTotalData(mTotalFansNumber, author.getFansCount());

        setMyArticleTotal(author.getArticleCount());
    }

    private void setYesterdayData(TextView textView, int data) {
        String formatNumber = NumberUtils.formatNumber(data);
        SpannableString spannableString = StrUtil.mergeTextWithRatio(formatNumber, " " + getString(R.string.yesterday), 0.55f);
        textView.setText(spannableString);
    }

    private void setTotalData(TextView textView, int data) {
        String formatNumber = NumberUtils.formatNumber(data);
        SpannableString spannableString = StrUtil.mergeTextWithRatio(formatNumber, " " + getString(R.string.total), 0.55f);
        textView.setText(spannableString);
    }

    public void setMyArticleTotal(int articleNumber) {
        mMyArticleTotal.setText(getString(R.string.my_article_number, articleNumber));
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        requestAuthorArticle();
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestAuthorArticle();
    }

    private void updateArticle(List<AuthorArticle> data) {

        if (mPage == 0) {
            mAuthorArticleAdapter.clear();
        }

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mAuthorArticleAdapter.addAll(data);
    }
}
