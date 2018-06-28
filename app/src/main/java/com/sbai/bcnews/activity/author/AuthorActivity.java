package com.sbai.bcnews.activity.author;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.BuildConfigUtils;
import com.sbai.bcnews.utils.ClipboardUtils;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.NumberUtils;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.adapter.AuthorArticleAdapter;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.move.LinearItemDecoration;
import com.sbai.bcnews.view.share.NewsShareAndSetDialog;
import com.sbai.bcnews.view.share.NewsShareDialog;
import com.sbai.bcnews.view.share.ShareDialog;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthorActivity extends RecycleViewSwipeLoadActivity {

    public static final int REQ_CODE_ATTENTION_AUTHOR = 14882;

    HasLabelLayout mHasLabelLayout;
    TextView mAuthorName;
    TextView mAuthorIdentity;
    TextView mFansNumber;
    View mFirstSplit;
    TextView mReadNumber;
    View mSecondSplit;
    TextView mPraiseNumber;
    ImageView mAttentionAuthor;
    TextView mAuthorIntroduce;
    TextView mMyArticleTotal;
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
    CoordinatorLayout mRootView;
    @BindView(R.id.emptyView)
    TextView mEmptyView;
    private int mPage;
    private AuthorArticleAdapter mAuthorArticleAdapter;


    private int mAuthorId;
    private Author mAuthor;

    private View mFooterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author);
        ButterKnife.bind(this);
        translucentStatusBar();

        mAuthorId = getIntent().getIntExtra(ExtraKeys.ID, -1);

        initView();

        requestAuthorArticle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAuthorInfo();
    }

    private void initView() {

        initAdapter();

        initHeadView();

        if (BuildConfigUtils.isProductFlavor()) {
            mFansNumber.setVisibility(View.GONE);
            mFirstSplit.setVisibility(View.GONE);
            ViewGroup.MarginLayoutParams layoutParams = (ConstraintLayout.LayoutParams) mReadNumber.getLayoutParams();
            layoutParams.leftMargin = 0;
            mReadNumber.setLayoutParams(layoutParams);
        }

        mFooterView = mAuthorArticleAdapter.createDefaultFooterView(getActivity());
    }

    private void initAdapter() {
        mAuthorArticleAdapter = new AuthorArticleAdapter(getActivity());
        mAuthorArticleAdapter.setPageType(AuthorArticleAdapter.PAGE_TYPE_AUTHOR_INFO);
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.addItemDecoration(new LinearItemDecoration(ContextCompat.getColor(getActivity(), R.color.bg_F5F5)));
        mSwipeTarget.setAdapter(mAuthorArticleAdapter);


        mAuthorArticleAdapter.setItemClickListener(new OnItemClickListener<AuthorArticle>() {
            @Override
            public void onItemClick(AuthorArticle item, int position) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, item.getId())
                        .putExtra(ExtraKeys.CHANNEL, (item.getChannel() == null || item.getChannel().isEmpty()) ? null : item.getChannel().get(0))
                        .executeForResult(NewsDetailActivity.REQ_CODE_CANCEL_COLLECT);
            }
        });
    }

    private void initHeadView() {
        View view = getLayoutInflater().inflate(R.layout.row_author_head_view, mSwipeTarget, false);
        mHasLabelLayout = view.findViewById(R.id.hasLabelLayout);
        mAuthorName = view.findViewById(R.id.authorName);
        mAuthorIdentity = view.findViewById(R.id.authorIdentity);
        mFansNumber = view.findViewById(R.id.fansNumber);
        mFirstSplit = view.findViewById(R.id.firstSplit);

        mReadNumber = view.findViewById(R.id.readNumber);
        mSecondSplit = view.findViewById(R.id.secondSplit);
        mPraiseNumber = view.findViewById(R.id.praiseNumber);
        mAttentionAuthor = view.findViewById(R.id.attentionAuthor);
        mAuthorIntroduce = view.findViewById(R.id.authorIntroduce);
        mMyArticleTotal = view.findViewById(R.id.myArticleTotal);
        mTitleBar = view.findViewById(R.id.titleBar);
        mAuthorArticleAdapter.addHeaderView(view);

        mAttentionAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocalUser.getUser().isLogin())
                    attentionAuthor();
                else
                    Launcher.with(getActivity(), LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
            }
        });

        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
    }


    private void showShareDialog() {
        if (mAuthor == null) return;
        String shareThumbUrl = mAuthor.getUserPortrait();
        String shareTitle = getString(R.string.recommend_author, getString(R.string.app_name), getString(R.string.app_author));
        String shareDescription = !TextUtils.isEmpty(mAuthor.getAuthInfo()) ? mAuthor.getAuthInfo() : mAuthor.getUserName();
        final String shareUrl = String.format(Apic.url.SHARE_AUTHOR, mAuthor.getId());
        NewsShareDialog.with(getActivity())
                .setOnNewsLinkCopyListener(new NewsShareAndSetDialog.OnNewsLinkCopyListener() {
                    @Override
                    public void onCopyLink() {
                        ClipboardUtils.clipboardText(getActivity(), shareUrl, R.string.copy_success);
                    }
                }).setTitleVisible(false)
                .setShareTitle(shareTitle)
                .setShareDescription(shareDescription)
                .setListener(new ShareDialog.OnShareDialogCallback() {
                    @Override
                    public void onSharePlatformClick(ShareDialog.SHARE_PLATFORM platform) {
//                        Apic.share(mNewsDetail.getId(), 0).tag(TAG).fireFreely();
                    }
                })
                .setShareUrl(shareUrl)
                .setShareThumbUrl(shareThumbUrl)
                .show();
    }


//    @Override
//    protected void onRecycleViewScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onRecycleViewScrolled(recyclerView, dx, dy);
//        if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
//            triggerLoadMore();
//        }
//    }

//    @Override
//    public boolean isUseDefaultLoadMoreConditions() {
//        return false;
//    }

    private void requestAuthorInfo() {
        Apic.requestAuthorInfo(mAuthorId)
                .tag(TAG)
                .callback(new Callback2D<Resp<Author>, Author>() {
                    @Override
                    protected void onRespSuccessData(Author data) {
                        mAuthor = data;
                        updateAuthorInfo(data);
                    }
                })
                .fire();
    }

    private void requestAuthorArticle() {
        Apic.requestAuthorArticle(mPage, mAuthorId)
                .tag(TAG)
                .callback(new Callback<ListResp<AuthorArticle>>() {
                    @Override
                    protected void onRespSuccess(ListResp<AuthorArticle> resp) {
                        if (resp.getListData() != null)
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

    private void updateArticle(List<AuthorArticle> data) {

        if (mPage == 0) {
            mAuthorArticleAdapter.clear();
        }

        if (!data.isEmpty() || !mAuthorArticleAdapter.isEmpty()) {
            if (mSwipeToLoadLayout.getVisibility() != View.VISIBLE) {
                mSwipeToLoadLayout.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
        } else {
            if (mEmptyView.getVisibility() != View.VISIBLE) {
                mEmptyView.setVisibility(View.VISIBLE);
                mSwipeToLoadLayout.setVisibility(View.GONE);
            }
        }

        mAuthorArticleAdapter.addAll(data);

        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);

            if (!mAuthorArticleAdapter.hasFooterView() && mAuthorArticleAdapter.getDataList().size() > 3) {
                mAuthorArticleAdapter.addFooterView(mFooterView);
            }
        } else {
            mPage++;
            if (mAuthorArticleAdapter.hasFooterView()) {
                mAuthorArticleAdapter.removeFooterView();
            }
        }
    }

    private void updateAuthorInfo(Author author) {
        mHasLabelLayout.setImageSrc(author.getUserPortrait());
        mHasLabelLayout.setLabelImageViewVisible(author.isAuthor());

        boolean isOfficialAuthor = author.getRankType() == Author.AUTHOR_STATUS_OFFICIAL;
        mHasLabelLayout.setLabelSelected(isOfficialAuthor);
        mAuthorIdentity.setText(author.getRankTypeStr());


        mAuthorName.setText(author.getUserName());
        String introduce = TextUtils.isEmpty(author.getAuthInfo()) ? "--" : author.getAuthInfo();
        mAuthorIntroduce.setText(getString(R.string.author_check_introduce_s, introduce));

        initAuthorAttentionNumber(author);

        mAttentionAuthor.setSelected(author.getIsConcern() == Author.AUTHOR_IS_ALREADY_ATTENTION);

        setMyArticleTotal(author.getArticleCount());
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

    @OnClick({ R.id.emptyView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.emptyView:
                onRefresh();
                break;
        }
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


    private void attentionAuthor() {
        if (mAuthor != null) {
            final int attentionType = mAuthor.getIsConcern() == Author.AUTHOR_STATUS_SPECIAL ? Author.AUTHOR_IS_NOT_ATTENTION : Author.AUTHOR_IS_ALREADY_ATTENTION;
            Apic.attentionAuthor(mAuthor.getId(), attentionType)
                    .tag(TAG)
                    .callback(new Callback<Resp<Object>>() {
                        @Override
                        protected void onRespSuccess(Resp<Object> resp) {
                            mAuthor.setIsConcern(attentionType);
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKeys.TAG, attentionType);
                            setResult(RESULT_OK, intent);
                            mAttentionAuthor.setSelected(mAuthor.getIsConcern() == Author.AUTHOR_IS_ALREADY_ATTENTION);

                            if (mAuthor.getIsConcern() == Author.AUTHOR_IS_ALREADY_ATTENTION) {
                                ToastUtil.show(R.string.attention_success);
                            } else {
                                ToastUtil.show(R.string.cancel_attention_success);
                            }
                        }
                    })
                    .fire();
        }
    }


}
