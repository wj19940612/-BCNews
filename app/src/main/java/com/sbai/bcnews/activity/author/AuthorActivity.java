package com.sbai.bcnews.activity.author;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.NumberUtils;
import com.sbai.bcnews.utils.adapter.AuthorArticleAdapter;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.move.LinearItemDecoration;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthorActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.hasLabelLayout)
    HasLabelLayout mHasLabelLayout;
    @BindView(R.id.authorName)
    TextView mAuthorName;
    @BindView(R.id.authorIdentity)
    TextView mAuthorIdentity;
    @BindView(R.id.fansNumber)
    TextView mFansNumber;
    @BindView(R.id.firstSplit)
    View mFirstSplit;
    @BindView(R.id.readNumber)
    TextView mReadNumber;
    @BindView(R.id.secondSplit)
    View mSecondSplit;
    @BindView(R.id.praiseNumber)
    TextView mPraiseNumber;
    @BindView(R.id.attentionAuthor)
    ImageView mAttentionAuthor;
    @BindView(R.id.authorIntroduce)
    TextView mAuthorIntroduce;
    @BindView(R.id.myArticleTotal)
    TextView mMyArticleTotal;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    CoordinatorLayout mRootView;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.layout)
    LinearLayout mLayout;
    private int mPage;
    private AuthorArticleAdapter mAuthorArticleAdapter;
    private int mTotalScrollRange;

    private View mCustomView;
    private TextView mTitleBarAuthorName;
    private HasLabelLayout mTitleBarHasLabelLayout;
    private int mAuthorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        ButterKnife.bind(this);
        translucentStatusBar();

        mAuthorId = getIntent().getIntExtra(ExtraKeys.ID, -1);

        initView();

        requestAuthorInfo();
        requestAuthorArticle();
    }

    private void initView() {
        initTitleBar();
        mAuthorArticleAdapter = new AuthorArticleAdapter(getActivity());
        mAuthorArticleAdapter.setPageType(AuthorArticleAdapter.PAGE_TYPE_AUTHOR_INFO);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.addItemDecoration(new LinearItemDecoration(ContextCompat.getColor(getActivity(), R.color.bg_F5F5)));
        mSwipeTarget.setAdapter(mAuthorArticleAdapter);

        mAppBarLayout.addOnOffsetChangedListener(mOnOffsetChangedListener);

        mAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                mTotalScrollRange = mAppBarLayout.getTotalScrollRange() - 20;
            }
        });
    }

    private void initTitleBar() {
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });

        mCustomView = mTitleBar.getCustomView();
        mCustomView.setVisibility(View.GONE);
        mTitleBarAuthorName = mCustomView.findViewById(R.id.titleBarAuthorName);
        mTitleBarHasLabelLayout = mCustomView.findViewById(R.id.titleBarHasLabelLayout);
    }

    private void showShareDialog() {
//        NewsShareDialog.with(getActivity())
//                .setOnNewsLinkCopyListener(new NewsShareAndSetDialog.OnNewsLinkCopyListener() {
//                    @Override
//                    public void onCopyLink() {
//                        ClipboardUtils.clipboardText(getActivity(), shareUrl, R.string.copy_success);
//                    }
//                }).setTitleVisible(false)
//                .setShareTitle(mNewsDetail.getTitle())
//                .setShareDescription(getSummaryData())
//                .setListener(new ShareDialog.OnShareDialogCallback() {
//                    @Override
//                    public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
//                        Apic.share(mNewsDetail.getId(), 0).tag(TAG).fireFreely();
//                    }
//                })
//                .setShareUrl(shareUrl)
//                .setShareThumbUrl(shareThumbUrl)
//                .show();
    }

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (verticalOffset != 0)
                if (verticalOffset > -mTotalScrollRange) {
                    if (mCustomView.getVisibility() == View.VISIBLE) {
                        mCustomView.setVisibility(View.GONE);
                        if (mLayout.getVisibility() == View.INVISIBLE) {
                            mLayout.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    if (mCustomView.getVisibility() == View.GONE) {
                        mCustomView.setVisibility(View.VISIBLE);
                        if (mLayout.getVisibility() == View.VISIBLE) {
                            mLayout.setVisibility(View.INVISIBLE);
                        }
                    }

                }
        }
    };

    private void requestAuthorInfo() {
        Apic.requestAuthorInfo(mAuthorId)
                .tag(TAG)
                .callback(new Callback2D<Resp<Author>, Author>() {
                    @Override
                    protected void onRespSuccessData(Author data) {
                        updateAuthorInfo(data);
                    }
                })
                .fire();
    }

    private void requestAuthorArticle() {
        Apic.requestAuthorArticle(mPage, mAuthorId)
                .tag(TAG)
                .callback(new Callback2D<List<AuthorArticle>, List<AuthorArticle>>() {
                    @Override
                    protected void onRespSuccessData(List<AuthorArticle> data) {
                        updateArticle(data);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        stopFreshOrLoadAnimation();
                    }
                })
                .fire();
    }

    private void updateArticle(List<AuthorArticle> data) {

        if (mPage == 0) mAuthorArticleAdapter.clear();

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mPage++;
        }

        mAuthorArticleAdapter.addAll(data);
    }

    private void updateAuthorInfo(Author author) {
        mHasLabelLayout.setImageSrc(author.getUserPortrait());

        boolean isOfficialAuthor = author.getRankType() == Author.AUTHOR_STATUS_OFFICIAL;
        mHasLabelLayout.setLabelSelected(isOfficialAuthor);
        if (isOfficialAuthor) {
            mAuthorIdentity.setText(R.string.official_author);
        } else {
            mAuthorIdentity.setText(R.string.special_author);
        }
        mTitleBarHasLabelLayout.setLabelSelected(isOfficialAuthor);

        mAuthorName.setText(author.getUserName());
        mAuthorIntroduce.setText(getString(R.string.author_check_introduce, author.getAuthInfo()));

        initAuthorAttentionNumber(author);

        mTitleBarHasLabelLayout.setImageSrc(author.getUserPortrait());
        mTitleBarAuthorName.setText(author.getUserName());
    }

    private void initAuthorAttentionNumber(Author author) {
        String fans = NumberUtils.formatNumber(author.getFansCount());
        String s = fans + "\n" + getString(R.string.fans);
        mFansNumber.setText(s);

        String read = NumberUtils.formatNumber(author.getShowReadCount());
        String readNumber = read + "\n" + getString(R.string.read);
        mReadNumber.setText(readNumber);

        String praise = NumberUtils.formatNumber(author.getPraiseCount());
        String s1 = praise + "\n" + getString(R.string.praise_);
        mPraiseNumber.setText(s1);
    }

    public void setMyArticleTotal(int articleNumber) {
        mMyArticleTotal.setText(getString(R.string.his_article_number, articleNumber));
    }


    @Override
    public View getContentView() {
        return mRootView;
    }

    @OnClick(R.id.attentionAuthor)
    public void onViewClicked() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppBarLayout.removeOnOffsetChangedListener(mOnOffsetChangedListener);
    }
}
