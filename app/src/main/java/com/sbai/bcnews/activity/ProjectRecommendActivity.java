package com.sbai.bcnews.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.text.Html;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ProjectCommend;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.FinanceUtil;
import com.sbai.bcnews.view.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.activity.WebActivity.INFO_HTML_META;

public class ProjectRecommendActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView mWebView;

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.btnLookMore)
    TextView mBtnLookMore;
    @BindView(R.id.introduce)
    TextView mIntroduce;
    @BindView(R.id.type)
    TextView mType;
    @BindView(R.id.publishTime)
    TextView mPublishTime;
    @BindView(R.id.researcher)
    TextView mResearcher;
    @BindView(R.id.publishPrice)
    TextView mPublishPrice;
    @BindView(R.id.publishCount)
    TextView mPublishCount;
    @BindView(R.id.turnOver)
    TextView mTurnOver;
    @BindView(R.id.coreAlgorithm)
    TextView mCoreAlgorithm;
    @BindView(R.id.rewardSystem)
    TextView mRewardSystem;
    @BindView(R.id.baseInfoLayout)
    LinearLayout mBaseInfoLayout;

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (!judgeIsMax() && TextViewCompat.getMaxLines(mIntroduce) <= 5) {
                mBtnLookMore.setVisibility(View.INVISIBLE);
            } else {
                mBtnLookMore.setVisibility(View.VISIBLE);
            }
            mIntroduce.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_recommend);
        ButterKnife.bind(this);
        initWebView(mWebView);
        initView();
        loadData();
    }

    private void initView() {
        mBtnLookMore.setTag(false);
    }

    private void expandOrStopContent(boolean hasExpand) {
        if (hasExpand) {
            //已经展开,收起
            mBtnLookMore.setText(R.string.look_more);
            mIntroduce.setMaxLines(5);
            mIntroduce.setEllipsize(TextUtils.TruncateAt.END);
            mBtnLookMore.setTag(false);
        } else if (judgeIsMax()) {
            mBtnLookMore.setText(R.string.retract);
            mIntroduce.setMaxLines(Integer.MAX_VALUE);
            mIntroduce.setEllipsize(null);
            mBtnLookMore.setTag(true);
        }
    }

    //查看这个TextView
    private boolean judgeIsMax() {
        Layout layout = mIntroduce.getLayout();
        if (layout != null) {
            int lines = layout.getLineCount();
            if (lines > 0) {
                if (layout.getEllipsisCount(4) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void loadData() {
        Apic.requestProjectRecommend().tag(TAG).callback(new Callback2D<Resp<ProjectCommend>, ProjectCommend>() {
            @Override
            protected void onRespSuccessData(ProjectCommend data) {
                updateData(data);
            }
        }).fireFreely();
    }

    private void updateData(ProjectCommend data) {
        mType.setText(data.getProjectName());
        mPublishTime.setText(DateUtil.getFormatSpecialSlashNoHour(data.getPublishTime()));
        if (data.getPublishPrice() != 0) {
            mPublishPrice.setText("$" + String.valueOf(data.getPublishPrice()));
        }
        mPublishCount.setText(FinanceUtil.trimTrailingZero(data.getPublishTotal()));
        mTurnOver.setText(String.valueOf(data.getCirculateCount()));
        mRewardSystem.setText(data.getIncentiveSystem());

        mIntroduce.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
        mIntroduce.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);
        if (!TextUtils.isEmpty(data.getInfo())) {
            mIntroduce.setText(Html.fromHtml(data.getIntro()));
        }

        if (!TextUtils.isEmpty(data.getInfo())) {
            openWebView(data.getInfo(), mWebView);
        }
    }


    @OnClick({R.id.btnLookMore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnLookMore:
                boolean hasExpand = (boolean) view.getTag();
                expandOrStopContent(hasExpand);
                break;
        }
    }

    protected void initWebView(WebView webView) {
        WebSettings webSettings = webView.getSettings();
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
        webView.clearHistory();
        webView.clearCache(true);
        webView.clearFormData();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //硬件加速 有些API19手机不支持
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else {
//            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        webView.setLayerType(View.LAYER_TYPE_NONE, null);
        // mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 以下 默认同时加载http和https
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setDrawingCacheEnabled(true);
        webView.setBackgroundColor(0);
    }

    private void openWebView(String urlData, WebView webView) {
        String content;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            content = INFO_HTML_META + "<body>" + urlData + "</body>";
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            content = getHtmlData(urlData);
        }
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
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
                "                  margin-top: 38px !important;\n" +
                "                  margin-left: 20px !important;\n" +
                "                  margin-right: 20px !important;\n" +
                "                }\n" +
                "                * {\n" +
                "                  text-align:justify;\n" +
                "                  font-size: 13px !important;\n" +
                "                  font-family: 'PingFangSC-Regular' !important;\n" +
                "                  color: #4a4a4a !important;\n" +
                "                  background-color: white !important;\n" +
                "                  letter-spacing: 1px !important;\n" +
                "                  line-height: 18px !important;\n" +
                "                  white-space: break-all !important;\n" +
                "                  word-wrap: break-word;\n" +
                "                }\n" +
                "                img{max-width:100% !important; width:auto; height:auto;}\n" +
                "            </style>\n" + "</head>";
        return textHead;
    }
}
