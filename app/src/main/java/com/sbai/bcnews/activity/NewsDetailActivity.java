package com.sbai.bcnews.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sbai.bcnews.AppJs;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.comment.NewsShareOrCommentBaseActivity;
import com.sbai.bcnews.activity.dialog.WriteCommentActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.dialog.OpenNotifyDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.OtherArticle;
import com.sbai.bcnews.model.news.NewViewPointAndReview;
import com.sbai.bcnews.model.news.NewsViewpoint;
import com.sbai.bcnews.model.news.WriteComment;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.PermissionUtil;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.utils.news.NewsReadCache;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.NewsScrollView;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.fragment.HomeNewsFragment.SCROLL_STATE_GONE;
import static com.sbai.bcnews.fragment.HomeNewsFragment.SCROLL_STATE_NORMAL;

/**
 * Created by Administrator on 2018\1\25 0025.
 */

public class NewsDetailActivity extends NewsShareOrCommentBaseActivity {

    public static final int TIME_SECOND = 1000;
    public static final int TIME_COUNT_GET_RATE = 30;
    public static final int REQ_CODE_CANCEL_COLLECT = 2265;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.titleBarLine)
    View mTitleBarLine;
    @BindView(R.id.subtitle)
    TextView mSubtitle;
    @BindView(R.id.source)
    TextView mSource;
    @BindView(R.id.pubTime)
    TextView mPubTime;
    @BindView(R.id.readTime)
    TextView mReadTime;
    @BindView(R.id.titleLine)
    View mTitleLine;
    @BindView(R.id.titleLayout)
    RelativeLayout mTitleLayout;
    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.praiseIcon)
    ImageView mPraiseIcon;
    @BindView(R.id.praiseCount)
    TextView mPraiseCount;
    @BindView(R.id.praiseLayout)
    RelativeLayout mPraiseLayout;
    @BindView(R.id.statement)
    ImageView mStatement;
    @BindView(R.id.shareTo)
    TextView mShareTo;
    @BindView(R.id.wxShare)
    TextView mWxShare;
    @BindView(R.id.circleShare)
    TextView mCircleShare;
    @BindView(R.id.shareLayout)
    RelativeLayout mShareLayout;
    @BindView(R.id.split)
    View mSplit;
    @BindView(R.id.otherArticleTip)
    TextView mOtherArticleTip;
    @BindView(R.id.firstImg)
    ImageView mFirstImg;
    @BindView(R.id.firstTitle)
    TextView mFirstTitle;
    @BindView(R.id.firstOriginal)
    TextView mFirstOriginal;
    @BindView(R.id.firstSource)
    TextView mFirstSource;
    @BindView(R.id.firstTime)
    TextView mFirstTime;
    @BindView(R.id.firstArticle)
    RelativeLayout mFirstArticle;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.secondImg)
    ImageView mSecondImg;
    @BindView(R.id.secondTitle)
    TextView mSecondTitle;
    @BindView(R.id.secondOriginal)
    TextView mSecondOriginal;
    @BindView(R.id.secondSource)
    TextView mSecondSource;
    @BindView(R.id.secondTime)
    TextView mSecondTime;
    @BindView(R.id.secondArticle)
    RelativeLayout mSecondArticle;
    @BindView(R.id.secondLine)
    View mSecondLine;
    @BindView(R.id.thirdImg)
    ImageView mThirdImg;
    @BindView(R.id.thirdTitle)
    TextView mThirdTitle;
    @BindView(R.id.thirdOriginal)
    TextView mThirdOriginal;
    @BindView(R.id.thirdSource)
    TextView mThirdSource;
    @BindView(R.id.thirdTime)
    TextView mThirdTime;
    @BindView(R.id.thirdArticle)
    RelativeLayout mThirdArticle;
    @BindView(R.id.thirdLine)
    View mThirdLine;
    @BindView(R.id.scrollView)
    NewsScrollView mScrollView;
    @BindView(R.id.writeComment)
    TextView mWriteComment;
    @BindView(R.id.commentCount)
    TextView mCommentCount;
    @BindView(R.id.collectIcon)
    ImageView mCollectIcon;
    @BindView(R.id.bottomShareIcon)
    ImageView mBottomShareIcon;
    @BindView(R.id.collectAndShareLayout)
    LinearLayout mCollectAndShareLayout;
    @BindView(R.id.defaultImg)
    ImageView mDefaultImg;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    @BindView(R.id.allPoint)
    TextView mAllPoint;
    @BindView(R.id.firstPortrait)
    ImageView mFirstPortrait;
    @BindView(R.id.firstName)
    TextView mFirstName;
    @BindView(R.id.firstContent)
    TextView mFirstContent;
    @BindView(R.id.firstPraiseCount)
    TextView mFirstPraiseCount;
    @BindView(R.id.firstPointCircle)
    View mFirstPointCircle;
    @BindView(R.id.firstReviewCount)
    TextView mFirstReviewCount;
    @BindView(R.id.firstPoint2Circle)
    View mFirstPoint2Circle;
    @BindView(R.id.firstPointTime)
    TextView mFirstPointTime;
    @BindView(R.id.firstPoint)
    ConstraintLayout mFirstPoint;
    @BindView(R.id.secondPortrait)
    ImageView mSecondPortrait;
    @BindView(R.id.secondName)
    TextView mSecondName;
    @BindView(R.id.secondContent)
    TextView mSecondContent;
    @BindView(R.id.secondPraiseCount)
    TextView mSecondPraiseCount;
    @BindView(R.id.secondPointCircle)
    View mSecondPointCircle;
    @BindView(R.id.secondReviewCount)
    TextView mSecondReviewCount;
    @BindView(R.id.secondPoint2Circle)
    View mSecondPoint2Circle;
    @BindView(R.id.secondPointTime)
    TextView mSecondPointTime;
    @BindView(R.id.secondPoint)
    ConstraintLayout mSecondPoint;
    @BindView(R.id.allComment)
    TextView mAllComment;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;


    private WebViewClient mWebViewClient;

    protected String mPageUrl;
    protected String mPureHtml;
    private AppJs mAppJs;

    private String mId;
    private NewsDetail mNetNewsDetail;//保证是网络更新的详情
    private String mChannel;
    private String mTag;

    private int mTitleHeight;
    private boolean mTitleVisible;
    private boolean mScrolling;
    private int mScrollY;
    private boolean mAnimating;
    private int mTitleScrollState;  //0-默认 1-已经滚下去了
    private List<OtherArticle> mOtherArticleList;

    public static final String INFO_HTML_META = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\">";


    @Override
    protected int getPageType() {
        return PAGE_TYPE_NEWS_DETAIL;
    }


    @Override
    protected void onCollectSuccess(NewsDetail newsDetail) {
        //收藏页面刷新
        if (newsDetail.getCollect() == 0) {
            Intent intent = new Intent();
            intent.putExtra(ExtraKeys.TAG, newsDetail.getId());
            setResult(RESULT_OK, intent);
        }
        updatePraiseCollect(mNetNewsDetail);
    }


    @Override
    protected void onReceiveBroadcast(Context context, Intent intent) {
        if (!TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                case ACTION_WEB_TEXT_SIZE_HAS_CHANGE:
                    mWebTextSize = Preference.get().getLocalWebTextSize();
                    openWebView(mPureHtml);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initData();
        initView();
        initWebView();
        initScrollView();
        requestDetailData();
        requestOtherArticle();
        requestNewsViewpoint();
        startScheduleJob(TIME_SECOND);
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

    private void initData() {
        mId = getIntent().getStringExtra(ExtraKeys.NEWS_ID);
        mChannel = getIntent().getStringExtra(ExtraKeys.CHANNEL);
        mTag = getIntent().getStringExtra(ExtraKeys.TAG);
        mNewsDetail = NewsCache.getCacheForId(mId);
        NewsReadCache.markNewsRead(mId);

    }

    private void initView() {
        mTitleBar.setBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                saveDetailCache();
            }
        });
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                requestDetailData();
            }
        });


        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewsDetail != null) {
                    mNewsDetail.setReadHeight(mScrollView.getScrollY());
                }
                showNewsShareAndSettingDialog();
            }
        });

    }

    private void requestNewsViewpoint() {
        Apic.requestNewsViewpoint(mId)
                .tag(TAG)
                .callback(new Callback<ListResp<NewsViewpoint>>() {
                    @Override
                    protected void onRespSuccess(ListResp<NewsViewpoint> resp) {
                        if (resp.getData() != null && resp.getListData() != null) {
                            updateNewsViewpoint(resp.getListData(), resp.getData().getTotalComment());
                        }
                    }
                })
                .fire();

    }

    private void updateNewsViewpoint(final List<NewsViewpoint> data, int size) {
        if (!data.isEmpty()) {
            mAllPoint.setVisibility(View.VISIBLE);
            mAllComment.setVisibility(View.VISIBLE);
        } else {
            mAllComment.setVisibility(View.GONE);
            mAllPoint.setVisibility(View.GONE);
        }

        if (size != 0) {
            mCommentCount.setText(String.valueOf(size));
        }

        if (size < 10) {
            mAllComment.setText(R.string.all_comment);
        } else {
            mAllComment.setText(getString(R.string.all_point_number, " " + size + " "));
        }

        if (data.size() > 0) {
            mFirstPoint.setVisibility(View.VISIBLE);
            final NewsViewpoint newsViewpoint = data.get(0);
            updateViewpoint(mFirstPortrait, mFirstName, mFirstContent, mFirstPraiseCount, mFirstPointTime, mFirstReviewCount, newsViewpoint);
            mFirstPoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCommentDetailPage(newsViewpoint);
                }
            });
            if (data.size() > 1) {
                final NewsViewpoint secondNewsViewpoint = data.get(1);
                mSecondPoint.setVisibility(View.VISIBLE);
                updateViewpoint(mSecondPortrait, mSecondName, mSecondContent, mSecondPraiseCount, mSecondPointTime, mSecondReviewCount, secondNewsViewpoint);
                mSecondPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCommentDetailPage(secondNewsViewpoint);
                    }
                });
            }
        }
    }

    private void openCommentDetailPage(NewsViewpoint newsViewpoint) {
        if (mNewsDetail != null)
            Launcher.with(getActivity(), CommentDetailActivity.class)
                    .putExtra(ExtraKeys.DATA, NewViewPointAndReview.getNewViewPointAndReview(newsViewpoint))
                    .putExtra(ExtraKeys.NEWS_DETAIL, mNewsDetail)
                    .execute();
    }

    private void updateViewpoint(ImageView portrait, TextView userName, TextView content, TextView praiseCount, TextView pointTime, TextView reviewCount, NewsViewpoint newsViewpoint) {
        GlideApp.with(getActivity())
                .load(newsViewpoint.getUserPortrait())
                .circleCrop()
                .placeholder(R.drawable.ic_default_head_portrait)
                .into(portrait);
        userName.setText(newsViewpoint.getUsername());
        content.setText(newsViewpoint.getContent());
        praiseCount.setText(getString(R.string.praise_count_, newsViewpoint.getPraiseCount()));
        pointTime.setText(DateUtil.formatDefaultStyleTime(newsViewpoint.getReplayTime()));
        reviewCount.setText(getString(R.string.review_count, newsViewpoint.getReplayCount()));
    }


    @OnClick({R.id.titleBar, R.id.writeComment, R.id.commentCount, R.id.collectIcon,
            R.id.bottomShareIcon, R.id.wxShare, R.id.circleShare, R.id.praiseLayout,
            R.id.firstArticle, R.id.secondArticle, R.id.thirdArticle,
            R.id.firstPoint, R.id.secondPoint, R.id.allComment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.titleBar:
                if (!mScrolling)
                    mScrollView.smoothScrollTo(0, 0);
                break;
            case R.id.writeComment:
                if (mNewsDetail != null) {
                    if (!LocalUser.getUser().isLogin()) {
                        Launcher.with(getActivity(), LoginActivity.class).execute();
                        return;
                    }
                    Launcher.with(getActivity(), WriteCommentActivity.class)
                            .putExtra(ExtraKeys.DATA, WriteComment.getWriteComment(mNewsDetail))
                            .executeForResult(WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_NEWS);
                }
                break;

            case R.id.collectIcon:
                collect(mNetNewsDetail);
                break;
            case R.id.emptyView:
                break;
            case R.id.bottomShareIcon:
                showShareDialog();
                break;
            case R.id.wxShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.circleShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.praiseLayout:
                requestNewsPraise(mNetNewsDetail);
                break;
            case R.id.firstArticle:
                openNewsDetailsPage(0);
                break;
            case R.id.secondArticle:
                openNewsDetailsPage(1);
                break;
            case R.id.thirdArticle:
                openNewsDetailsPage(2);
                break;
            case R.id.firstPoint:
                break;
            case R.id.secondPoint:
                break;
            case R.id.allComment:
            case R.id.commentCount:
                NewsDetail newsDetail = null;
                if (mNetNewsDetail != null) {
                    newsDetail = mNetNewsDetail;
                } else {
                    newsDetail = mNewsDetail;
                }
                if (newsDetail != null) {
                    Launcher.with(getActivity(), NewsViewPointListActivity.class)
                            .putExtra(ExtraKeys.DATA, newsDetail)
                            .executeForResult(NewsViewPointListActivity.REQ_CODE_COMMENT);
                }
                break;

        }
    }

    protected void requestNewsPraise(final NewsDetail newsDetail) {
        if (!PermissionUtil.isNotificationEnabled(getActivity()) && Preference.get().isFirstPraise()) {
            new OpenNotifyDialogFragment().show(getSupportFragmentManager());
            Preference.get().setFirstPraise(false);
        }
        if (newsDetail != null && LocalUser.getUser().isLogin()) {
            int praiseWant = newsDetail.getPraise() == 0 ? 1 : 0;
            Apic.praiseNews(newsDetail.getId(), praiseWant)
                    .tag(TAG)
                    .callback(new Callback<Resp>() {
                        @Override
                        protected void onRespSuccess(Resp resp) {
                            if (newsDetail.getPraise() == 0) {
                                newsDetail.setPraise(1);
                                newsDetail.setPraiseCount(newsDetail.getPraiseCount() + 1);
                                umengEventCount(UmengCountEventId.NEWS04);
                            } else {
                                newsDetail.setPraiseCount(newsDetail.getPraiseCount() - 1);
                                newsDetail.setPraise(0);
                            }
                            updatePraise(newsDetail);
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            super.onErrorResponse(volleyError);
                            ToastUtil.show(R.string.praise_error);
                        }
                    }).fireFreely();
        } else if (newsDetail != null) {
            Launcher.with(this, LoginActivity.class).executeForResult(LoginActivity.REQ_CODE_LOGIN);
        }
    }

    @Override
    protected void addTextSize(int textSize) {
        super.addTextSize(textSize);
        openWebView(mPureHtml);
    }

    @Override
    protected void subTextSize(int textSize) {
        super.subTextSize(textSize);
        openWebView(mPureHtml);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateOtherData(mOtherArticleList);
        requestNewsViewpoint();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlideApp.with(getActivity()).onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            GlideApp.with(getActivity()).onDestroy();
        } catch (Exception e) {
            Log.d(TAG, "onDestroy: " + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        saveDetailCache();
        super.onBackPressed();
    }

    private void openNewsDetailsPage(int position) {
        if (mOtherArticleList != null && position < mOtherArticleList.size()) {
            OtherArticle data = mOtherArticleList.get(position);
            if (!TextUtils.isEmpty(mChannel)) {
                Launcher.with(NewsDetailActivity.this, NewsDetailActivity.class).putExtra(ExtraKeys.CHANNEL, mChannel).putExtra(ExtraKeys.NEWS_ID, data.getId()).execute();
            } else if (!TextUtils.isEmpty(mTag)) {
                Launcher.with(NewsDetailActivity.this, NewsDetailActivity.class).putExtra(ExtraKeys.TAG, mTag).putExtra(ExtraKeys.NEWS_ID, data.getId()).execute();
            }
        }
    }

    protected void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setUserAgentString(webSettings.getUserAgentString()
                + " ###" + getString(R.string.android_web_agent) + "/2.0");
        //mWebView.getSettings().setAppCacheEnabled(true);l
        //webSettings.setAppCachePath(getExternalCacheDir().getPath());
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        // performance improve
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setEnableSmoothTransition(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBlockNetworkImage(false);//解决图片不显示
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.clearFormData();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //硬件加速 有些API19手机不支持
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        mWebView.setLayerType(View.LAYER_TYPE_NONE, null);
        // mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以下 默认同时加载http和https
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                initScrollViewLocation();
                mWebView.loadUrl("javascript:(function(){"
                        + "var objs = document.getElementsByTagName(\"img\"); "
                        + "for(var i=0;i<objs.length;i++) " + "{" + " objs[i].onclick=function() "
                        + " { " + " window.AppJs.openImage(this.src); " + " } " + "}" + "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
        };
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setBackgroundColor(0);
        mAppJs = new AppJs(this);
        mWebView.addJavascriptInterface(mAppJs, "AppJs");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 20) {
                    if (mDefaultImg.getVisibility() != View.GONE) {
                        mPraiseLayout.setVisibility(View.VISIBLE);
                        mShareLayout.setVisibility(View.VISIBLE);
                        mStatement.setVisibility(View.VISIBLE);
                        mDefaultImg.setVisibility(View.GONE);
                    }
//                    mProgress.setVisibility(View.GONE);
                } else {
//                    if (mProgress.getVisibility() == View.GONE) {
//                        mProgress.setVisibility(View.VISIBLE);
//                    }
//                    mProgress.setProgress(newProgress);
                }
            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = mWebView.getHitTestResult();
                if (result != null) {
                    int type = result.getType();
                    if (type == WebView.HitTestResult.IMAGE_TYPE) {
//                        showSaveImageDialog(result);
                    }
                }
                return false;
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition,
                                        String mimeType, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    protected void loadPage() {
        if (!TextUtils.isEmpty(mPageUrl)) {
            mWebView.loadUrl(mPageUrl);
        } else if (!TextUtils.isEmpty(mPureHtml)) {
            openWebView(mPureHtml);
        }
    }

    private void openWebView(String urlData) {
        String content;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getWebView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            content = INFO_HTML_META + "<body>" + mPureHtml + "</body>";
        } else {
            getWebView().getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            content = getHtmlData(urlData);
        }
        getWebView().loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
    }

    public WebView getWebView() {
        return mWebView;
    }

    private String getHtmlData(String bodyHTML) {
        String head = getTextHead();
        return "<html>" + head + "<body style:'height:auto;max-width: 100%; width:auto;'>" + bodyHTML + "</body></html>";
    }

    private String getTextHead() {
        Log.d(TAG, "getTextHead: " + mWebTextSize);
        String textHead = "<head>" +
                "            <meta charset='utf-8'>\n" +
                "            <meta name='viewport' content='width=device-width, user-scalable=no, initial-scale=1.0001, minimum-scale=1.0001, maximum-scale=1.0001, shrink-to-fit=no'>\n" +
                "            <style type='text/css'>\n" +
                "                body {\n" +
                "                  margin-top: 20px !important;\n" +
                "                  margin-left: 20px !important;\n" +
                "                  margin-right: 20px !important;\n" +
                "                }\n" +
                "                * {\n" +
                "                  text-align:justify;\n" +
                "                  font-size: " + mWebTextSize + "px !important;\n" +
                "                  font-family: 'PingFangSC-Regular' !important;\n" +
                "                  color: #494949 !important;\n" +
                "                  background-color: white !important;\n" +
                "                  letter-spacing: 1px !important;\n" +
                "                  line-height: 26px !important;\n" +
                "                  white-space: break-all !important;\n" +
                "                  word-wrap: break-word;\n" +
                "                }\n" +
                "                img{max-width:100% !important; width:auto; height:auto;}\n" +
                "            </style>\n" + "</head>";
        return textHead;
    }

    private void initScrollViewLocation() {
//        mTitleHeight = mTitleLayout.getMeasuredHeight();
//        int webViewHeight = mWebView.getHeight();
//        //webView内资源异步加载，此时高度可能还未显示完全，需等资源完全显示或高度足够显示才可
//        if (mNewsDetail != null && mNewsDetail.getReadHeight() > webViewHeight + mTitleHeight) {
//            startScheduleJob(300);
//            return;
//        }
//        if (mNewsDetail != null) {
//            mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
//        }
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
            }
        });
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        if (count % TIME_COUNT_GET_RATE == 0) {
            //TODO 调用获取算力接口
        }
//        mTitleHeight = mTitleLayout.getMeasuredHeight();
//        int webViewHeight = mWebView.getHeight();
//        //webView内资源异步加载，此时高度可能还未显示完全，需等资源完全显示或高度足够显示才可
//        Log.d(TAG, "onTimeUp: " + count);
//        if (mNewsDetail != null) {
//            if (mNewsDetail.getReadHeight() <= webViewHeight + mTitleHeight) {
//                mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
//                stopScheduleJob();
//            } else {
//                stopScheduleJob();
//                mScrollView.smoothScrollTo(0, mNetNewsDetail.getReadHeight());
//            }
//
//        }
    }

    private void showRateTip() {
        View view =  LayoutInflater.from(this).inflate(R.layout.toast_get_calculate_rate, null);
        TextView textView = view.findViewById(R.id.message);
        String message = "恭喜获得持续阅读奖励+3算力";
        ToastUtil.show(view,textView, message, mTitleBar.getHeight());
    }


    private void initScrollView() {
        mScrollView.setOnScrollListener(new NewsScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                //底部按钮的交互
                upOrDownBottomBar(scrollY);
                if (scrollY > mTitleHeight && mNewsDetail != null) {
                    if (!mTitleVisible) {
                        mTitleBar.setTitle(mNewsDetail.getTitle());
                        mTitleVisible = true;
                    }
                } else {
                    if (mTitleVisible) {
                        mTitleBar.setTitle(R.string.news_detail);
                        mTitleVisible = false;
                    }
                }
            }

            @Override
            public void onScrollState(boolean scrolling) {
                mScrolling = scrolling;
            }
        });
    }

    private void upOrDownBottomBar(int scrollY) {
        //最底部那部分只滑出不滑入
        int scrollAddScreenHeight = scrollY + mScrollView.getHeight();
        int scrollViewExpandHeight = mScrollView.getChildAt(0).getMeasuredHeight();
        if (scrollAddScreenHeight > scrollViewExpandHeight || Math.abs(scrollAddScreenHeight - scrollViewExpandHeight) < 60) {
            scrollBottomBar(false);
        } else if (mScrollView.getScrollY() < 1000) {
            scrollBottomBar(false);
        } else if (mScrollY != 0 && mScrollY != scrollY) {
            scrollBottomBar(scrollY - mScrollY > 0);
        }
        mScrollY = scrollY;
    }

    private void scrollBottomBar(final boolean down) {
        if (mAnimating || (mTitleScrollState == SCROLL_STATE_GONE && down) || (mTitleScrollState == SCROLL_STATE_NORMAL && !down)) {
            return;
        }
        int titleHeight = mCollectAndShareLayout.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(down ? 0 : -titleHeight, down ? -titleHeight : 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mCollectAndShareLayout.getLayoutParams();
                lp.setMargins(0, 0, 0, y);
                mCollectAndShareLayout.setLayoutParams(lp);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimating = false;
                mTitleScrollState = down ? SCROLL_STATE_GONE : SCROLL_STATE_NORMAL;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setDuration(200);
        mAnimating = true;
        valueAnimator.start();
    }

    private void requestDetailData() {
        if (mNewsDetail == null) {
            requestData(true);
        } else {
            updateData(mNewsDetail);
            requestData(false);
        }
    }

    private void requestData(final boolean refresh) {
        Apic.getNewsDetail(mId).tag(TAG).callback(new Callback2D<Resp<NewsDetail>, NewsDetail>() {
            @Override
            protected void onRespSuccessData(NewsDetail data) {
                if (refresh) {
                    mNewsDetail = data;
                    mNetNewsDetail = data;
                    updateData(data);
                    updatePraiseCollect(data);
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mNetNewsDetail = data;
                    updatePraiseCollect(data);
                }
            }

            @Override
            protected void onRespFailure(Resp failedResp) {
                super.onRespFailure(failedResp);
                //收藏文章没有查到
                if (failedResp.getCode() == Resp.CODE_MSG_NOT_FIND) {
                    finish();
                }
            }

            @Override
            public void onFailure(ReqError reqError) {
                super.onFailure(reqError);
                if (refresh)
                    mEmptyView.setVisibility(View.VISIBLE);
            }
        }).fireFreely();
    }

    private void updateData(NewsDetail newsDetail) {
        mSubtitle.setText(newsDetail.getTitle());
        mSource.setText(newsDetail.getAuthor());
        mPubTime.setText(DateUtil.formatNewsStyleTime(newsDetail.getReleaseTime()));
        mReadTime.setText(String.format(getString(R.string.reader_time), newsDetail.getReaderTime()));
        mPureHtml = mNewsDetail.getContent();
        loadPage();
        if (mChannel == null && mTag == null && newsDetail.getChannel().size() > 0) {
            mChannel = newsDetail.getChannel().get(0);
            requestOtherArticle();
        }
    }

    private void requestOtherArticle() {
        if (!TextUtils.isEmpty(mChannel)) {
            requestOtherArticleWithChannel();
        } else if (!TextUtils.isEmpty(mTag)) {
            requestOtherArticleWithTag();
        }
    }

    private void requestOtherArticleWithChannel() {
        String encodeChannel = Uri.encode(mChannel);
        Apic.getOtherArticles(encodeChannel, mId).tag(TAG).callback(new Callback2D<Resp<List<OtherArticle>>, List<OtherArticle>>() {
            @Override
            protected void onRespSuccessData(List<OtherArticle> data) {
                updateOtherData(data);
            }
        }).fireFreely();
    }

    private void requestOtherArticleWithTag() {
        String encodeTag = Uri.encode(mTag);
        Apic.getRelatedNewsRecommend(encodeTag, mId).tag(TAG).callback(new Callback2D<Resp<List<OtherArticle>>, List<OtherArticle>>() {
            @Override
            protected void onRespSuccessData(List<OtherArticle> data) {
                updateOtherData(data);
            }
        }).fireFreely();
    }

    private void updateOtherData(List<OtherArticle> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        mOtherArticleList = data;
        mSplit.setVisibility(View.VISIBLE);
        mOtherArticleTip.setVisibility(View.VISIBLE);
        OtherArticle otherArticle = data.get(0);
        if (otherArticle != null)
            updateArticle(otherArticle, mFirstArticle, mFirstTitle, mFirstOriginal, mFirstSource, mFirstTime, mFirstImg);

        if (data.size() > 1) {
            OtherArticle otherArticle1 = data.get(1);
            if (otherArticle1 != null)
                updateArticle(otherArticle1, mSecondArticle, mSecondTitle, mSecondOriginal, mSecondSource, mSecondTime, mSecondImg);
        }

        if (data.size() > 2) {
            OtherArticle otherArticle2 = data.get(2);
            if (otherArticle2 != null)
                updateArticle(otherArticle2, mThirdArticle, mThirdTitle, mThirdOriginal, mThirdSource, mThirdTime, mThirdImg);
        }
    }

    private void updateArticle(OtherArticle data, RelativeLayout articleRl, TextView articleTitle, TextView articleOriginal, TextView articleSource, TextView articleTime, ImageView articleImg) {
        articleRl.setVisibility(View.VISIBLE);
        articleTitle.setText(data.getTitle());
        articleTitle.setEnabled(!NewsReadCache.isRead(data.getId()));
        articleOriginal.setVisibility(data.getOriginal() > 0 ? View.VISIBLE : View.GONE);
        articleSource.setText(data.getSource());
//        articleSource.setVisibility(TextUtils.isEmpty(data.getSource()) ? View.GONE : View.VISIBLE);
        articleSource.setVisibility(View.GONE);
        articleTime.setText(DateUtil.formatNewsStyleTime(data.getReleaseTime()));
        if (data.getImgs() != null && data.getImgs().size() > 0) {
            articleImg.setVisibility(View.VISIBLE);
            GlideApp.with(getActivity()).load(data.getImgs().get(0))
                    .placeholder(R.drawable.ic_default_news)
                    .centerCrop()
                    .into(articleImg);
        } else {
            articleImg.setVisibility(View.GONE);
        }
    }

    private void updatePraiseCollect(NewsDetail newsDetail) {
        updatePraise(newsDetail);
        updateCollect(newsDetail);
    }


    @Override
    protected void updateCollect(NewsDetail newsDetail) {
        super.updateCollect(newsDetail);
        if (newsDetail.getCollect() > 0) {
            mCollectIcon.setSelected(true);
        } else {
            mCollectIcon.setSelected(false);
        }
    }

    protected void updatePraise(NewsDetail newsDetail) {
        int praiseCount = newsDetail.getPraiseCount();
        //如果自己已经点赞过，但是服务器那边点赞数量是0，置成1
        if (newsDetail.getPraise() > 0 && newsDetail.getPraiseCount() == 0)
            praiseCount = 1;
        if (praiseCount == 0) {
            mPraiseCount.setText(R.string.news_praise);
        } else {
            mPraiseCount.setText(String.format(getString(R.string.praise_count), praiseCount));
        }
        if (newsDetail.getPraise() > 0) {
            mPraiseIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.btn_praise_selected));
        } else {
            mPraiseIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.btn_praise_normal));
        }
    }


    public void openBigImage(String img) {
        //可能是base64位，过长无法通过Intent传递
        if (img.contains("base64")) {
            Preference.get().setBigImage(img);
            Launcher.with(this, LookBigPictureActivity.class).execute();
        } else {
            //普通的http图片地址直接通过参数传递，否则效率太差
            Launcher.with(this, LookBigPictureActivity.class).putExtra(ExtraKeys.PORTRAIT, img).execute();
        }
    }


    private void saveDetailCache() {
        if (mNewsDetail != null && mNetNewsDetail != null && mNewsDetail.getUpdateTime() != mNetNewsDetail.getUpdateTime()) {
            mNetNewsDetail.setReadTime(System.currentTimeMillis());
            new CacheThread(mNetNewsDetail).start();
        } else if (mNewsDetail != null) {
            mNewsDetail.setReadHeight(mScrollView.getScrollY());
            mNewsDetail.setReadTime(System.currentTimeMillis());
            new CacheThread(mNewsDetail).start();
        }
    }


    static class CacheThread extends Thread {
        NewsDetail mNewsDetail;

        public CacheThread(NewsDetail newsDetail) {
            mNewsDetail = newsDetail;
        }

        public void run() {
            NewsCache.insertOrReplaceNews(mNewsDetail);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LoginActivity.REQ_CODE_LOGIN:
                    requestDetailData();
                    break;
                case WriteCommentActivity.REQ_CODE_WRITE_VIEWPOINT_FOR_NEWS:
                    ToastUtil.show(R.string.publish_success);
                    break;
                case NewsViewPointListActivity.REQ_CODE_COMMENT:
                    if (data != null) {
                        int collectStatus = data.getIntExtra(ExtraKeys.TAG, -1);
                        if (collectStatus != -1) {
                            if (mNetNewsDetail != null) {
                                mNetNewsDetail.setCollect(collectStatus);
                                updateCollect(mNetNewsDetail);
                            } else if (mNewsDetail != null) {
                                mNewsDetail.setCollect(collectStatus);
                                updateCollect(mNewsDetail);
                            }
                        }
                    }
                    break;
            }
        }

    }


}
