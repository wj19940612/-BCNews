package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.model.Candy;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.glide.GlideRoundAndCenterCropTransform;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.glide.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sbai.bcnews.activity.WebActivity.INFO_HTML_META;

public class CandyDetailActivity extends BaseActivity {


    @BindView(R.id.head)
    ImageView mHead;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.getCount)
    TextView mGetCount;
    @BindView(R.id.tip)
    TextView mTip;
    @BindView(R.id.headLayout)
    LinearLayout mHeadLayout;
    @BindView(R.id.welfare)
    TextView mWelfare;
    @BindView(R.id.welfareIntroduce)
    WebView mWelfareIntroduce;
    @BindView(R.id.split1)
    View mSplit1;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.contentIntroduce)
    WebView mContentIntroduce;
    @BindView(R.id.getCandyBtn)
    TextView mGetCandyBtn;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

    private Candy mCandy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candy_detail);
        ButterKnife.bind(this);
        initData(getIntent());
        initWebView(mContentIntroduce);
        initWebView(mWelfareIntroduce);
        requestClick();
        initViewData();
    }

    private void initData(Intent intent) {
        mCandy = intent.getParcelableExtra(ExtraKeys.CANDY);
        if (mCandy == null) {
            ToastUtil.show(R.string.no_this_candy);
            finish();
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

    private void requestClick() {
        Apic.requestClickCandy(mCandy.getId()).tag(TAG).fireFreely();
    }

    private void initViewData() {
        mTitleBar.setTitle(mCandy.getName());
        mTitleBar.setOnRightViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Launcher.with(getActivity(), ShareCandyActivity.class).putExtra(ExtraKeys.CANDY, mCandy).execute();
            }
        });

        GlideApp.with(getActivity()).load(mCandy.getPhoto())
                .transform(new GlideRoundAndCenterCropTransform(getActivity()))
                .placeholder(R.drawable.ic_default_news)
                .into(mHead);

        mName.setText(mCandy.getName());
        if (mCandy.getClicks() <= 99999) {
            mGetCount.setText(getString(R.string.x_have_get, mCandy.getClicks()));
        } else {
            mGetCount.setText(getString(R.string.x_ten_thousand_have_get, mCandy.getClicks() / 10000));
        }
        mTip.setText(mCandy.getWelfare());

        openWebView(mCandy.getSweetIntroduce(), mContentIntroduce);
        openWebView(mCandy.getIntroduce(), mWelfareIntroduce);
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
                "                  margin-top: 13px !important;\n" +
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

    @OnClick({R.id.getCandyBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.getCandyBtn:
                if (LocalUser.getUser().isLogin()) {
                    getCandy();
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
                break;
        }
    }

    private void getCandy() {
        Apic.receiveCandy(mCandy.getId()).tag(TAG).fireFreely();

        Launcher.with(getActivity(), WebActivity.class)
                .putExtra(WebActivity.EX_URL, mCandy.getUrl())
                .execute();
    }
}
