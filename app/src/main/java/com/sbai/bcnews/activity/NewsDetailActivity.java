package com.sbai.bcnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sbai.bcnews.AppJs;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.Preference;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ShareUtils;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.view.DrawWebView;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.NewsScrollView;
import com.sbai.bcnews.view.ShareDialog;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.httplib.ReqError;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.sql.Struct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\25 0025.
 */

public class NewsDetailActivity extends BaseActivity {

    @BindView(R.id.webView)
    DrawWebView mWebView;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.subtitle)
    TextView mSubtitle;
    @BindView(R.id.titleLayout)
    RelativeLayout mTitleLayout;
    @BindView(R.id.scrollView)
    NewsScrollView mScrollView;
    @BindView(R.id.source)
    TextView mSource;
    @BindView(R.id.pubTime)
    TextView mPubTime;
    @BindView(R.id.readTime)
    TextView mReadTime;
    @BindView(R.id.praiseIcon)
    ImageView mPraiseIcon;
    @BindView(R.id.praiseCount)
    TextView mPraiseCount;
    @BindView(R.id.circleShare)
    TextView mCircleShare;
    @BindView(R.id.wxShare)
    TextView mWxShare;
    @BindView(R.id.titleBarLine)
    View mTitleBarLine;
    @BindView(R.id.titleLine)
    View mTitleLine;
    @BindView(R.id.praiseLayout)
    RelativeLayout mPraiseLayout;
    @BindView(R.id.statement)
    ImageView mStatement;
    @BindView(R.id.shareTo)
    TextView mShareTo;
    @BindView(R.id.shareLayout)
    RelativeLayout mShareLayout;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;

    private WebViewClient mWebViewClient;

    protected String mPageUrl;
    protected String mPureHtml;
    private AppJs mAppJs;

    private String mId;
    private NewsDetail mNewsDetail;

    private int mTitleHeight;
    private boolean mTitleVisible;
    private boolean mScrolling;

    public static final String INFO_HTML_META = "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\">";

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
    }

    private void initData() {
        mId = getIntent().getStringExtra(ExtraKeys.NEWS_ID);
        mNewsDetail = NewsCache.getCacheForId(mId);
    }

    private void initView() {
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShareDialog();
            }
        });
        mEmptyView.setRefreshButtonClickListener(new EmptyView.OnRefreshButtonClickListener() {
            @Override
            public void onRefreshClick() {
                requestDetailData();
            }
        });
    }

    protected void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setUserAgentString(webSettings.getUserAgentString()
//                + " ###" + getString(R.string.android_web_agent) + "/1.0");
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
                if (newProgress == 100) {
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
                "                  font-size: 17px !important;\n" +
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
        mTitleHeight = mTitleLayout.getMeasuredHeight();
        int webViewHeight = mWebView.getHeight();
        //Log.e("zzz", "starty:" + mNewsDetail.getReadHeight() + " and webview 高度:" + webViewHeight);
        //webView内资源异步加载，此时高度可能还未显示完全，需等资源完全显示或高度足够显示才可
        if (mNewsDetail != null && mNewsDetail.getReadHeight() > webViewHeight + mTitleHeight) {
            startScheduleJob(50);
            return;
        }
        if (mNewsDetail != null) {
            mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
        }
    }

    @Override
    public void onTimeUp(int count) {
        super.onTimeUp(count);
        //Log.e("zzz", "onTimeUp");
        mTitleHeight = mTitleLayout.getMeasuredHeight();
        int webViewHeight = mWebView.getHeight();
        //webView内资源异步加载，此时高度可能还未显示完全，需等资源完全显示或高度足够显示才可
        if (mNewsDetail != null && mNewsDetail.getReadHeight() <= webViewHeight + mTitleHeight) {
            stopScheduleJob();
            mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
            //Log.e("zzz", "starty:" + mNewsDetail.getReadHeight() + " and webview 高度:" + webViewHeight);
        }
    }

    private void initScrollView() {
        mScrollView.setOnScrollListener(new NewsScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
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

    private void requestDetailData() {
        if (mNewsDetail == null) {
            Apic.getNewsDetail(mId).tag(TAG).callback(new Callback2D<Resp<NewsDetail>, NewsDetail>() {
                @Override
                protected void onRespSuccessData(NewsDetail data) {
                    mNewsDetail = data;
                    updateData(data);
                    mEmptyView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(ReqError reqError) {
                    super.onFailure(reqError);
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }).fireFreely();
        } else {
            updateData(mNewsDetail);
        }
    }

    private void updateData(NewsDetail newsDetail) {
        mSubtitle.setText(newsDetail.getTitle());
        mSource.setText(newsDetail.getSource());
        mPubTime.setText(DateUtil.formatNewsStyleTime(newsDetail.getReleaseTime()));
        mReadTime.setText(String.format(getString(R.string.reader_time), newsDetail.getReaderTime()));
        updatePraiseCount(newsDetail.getPraiseCount());

        mPureHtml = mNewsDetail.getContent();
        loadPage();
    }

    private void updatePraiseCount(int praiseCount) {
        if (praiseCount == 0) {
            mPraiseCount.setText(R.string.news_praise);
            mPraiseIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_praise_not));
        } else {
            mPraiseCount.setText(String.format(getString(R.string.praise_count), praiseCount));
            mPraiseIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_praise));
        }
    }

    private void requestPraise() {
        if (mNewsDetail != null) {
            Apic.praiseNews(mNewsDetail.getId()).tag(TAG).callback(new Callback<Resp>() {
                @Override
                protected void onRespSuccess(Resp resp) {
                    updatePraiseCount(mNewsDetail.getPraiseCount() + 1);
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    super.onErrorResponse(volleyError);
                    ToastUtil.show(R.string.praise_error);
                }
            }).fireFreely();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mNewsDetail != null) {
            mNewsDetail.setReadHeight(mScrollView.getScrollY());
            //Log.e("zzz", "y:" + mScrollView.getScrollY());
            NewsCache.insertOrReplaceNews(mNewsDetail);
        }
    }

    @OnClick({R.id.wxShare, R.id.circleShare, R.id.praiseLayout, R.id.titleBar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wxShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.circleShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.praiseLayout:
                requestPraise();
                break;
            case R.id.titleBar:
                if (!mScrolling)
                    mScrollView.smoothScrollTo(0, 0);
                break;
        }
    }

    private void showShareDialog() {
        if (mNewsDetail == null) return;
        String shareThumbUrl = null;
        if (mNewsDetail.getImgs() != null && !mNewsDetail.getImgs().isEmpty()) {
            shareThumbUrl = mNewsDetail.getImgs().get(0);
        }
        updateSummaryData();
        ShareDialog.with(getActivity())
                .setTitleVisible(false)
                .setShareTitle(mNewsDetail.getTitle())
                .setShareDescription(mNewsDetail.getSummary())
                .setShareUrl(String.format(Apic.SHARE_NEWS_URL, mNewsDetail.getId()))
                .setShareThumbUrl(shareThumbUrl)
                .show();

    }

    private void shareToPlatform(SHARE_MEDIA platform) {
        if (mNewsDetail == null) return;
        UMWeb mWeb = new UMWeb(String.format(Apic.SHARE_NEWS_URL, mNewsDetail.getId()));
        mWeb.setTitle(mNewsDetail.getTitle());
        updateSummaryData();
        mWeb.setDescription(mNewsDetail.getSummary());
        UMImage thumb;
        if (mNewsDetail.getImgs() == null || mNewsDetail.getImgs().isEmpty()) {
            thumb = new UMImage(getActivity(), R.mipmap.ic_launcher);
        } else {
            thumb = new UMImage(getActivity(), mNewsDetail.getImgs().get(0));
        }
        mWeb.setThumb(thumb);
        new ShareAction(getActivity())
                .withMedia(mWeb)
                .setPlatform(platform)
                .setCallback(mUMShareListener)
                .share();

    }

    private void updateSummaryData() {
        if (TextUtils.isEmpty(mNewsDetail.getSummary())) {
            String content = new String(mNewsDetail.getContent());
            String imgTag = "<img\\s[^>]+>";
            Pattern pattern = Pattern.compile(imgTag);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll("");
            content = Html.fromHtml(content).toString();
            if (content.length() > 150) {
                mNewsDetail.setSummary(content.substring(0, 150));
            } else {
                mNewsDetail.setSummary(content);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private UMShareListener mUMShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_succeed);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            //   ToastUtil.show(R.string.share_failed);
            ToastUtil.show(throwable.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            ToastUtil.show(R.string.share_cancel);
        }
    };
}
