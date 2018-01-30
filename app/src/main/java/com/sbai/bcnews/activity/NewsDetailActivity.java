package com.sbai.bcnews.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.sbai.bcnews.AppJs;
import com.sbai.bcnews.ExtraKeys;
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
import com.sbai.bcnews.view.NewsScrollView;
import com.sbai.bcnews.view.ShareDialog;
import com.sbai.bcnews.view.TitleBar;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\25 0025.
 */

public class NewsDetailActivity extends BaseActivity {
    public static final String text = "<section data-color=\"rgb(245, 245, 244)\" data-custom=\"rgb(245, 245, 244)\" data-id=\"708\" data-tools=\"135编辑器\"> \n <section> \n  <section>\n    在未来，每个成功的区块链应用都是一家生态型“未来公司”。 \n   <br> &nbsp; \n   <br> 目前，包括“比特币”、“ 以太币”在内的区块链应用，市值都超过了1000亿美元。 \n   <br> \n   <br> \n   <br> 但是，主流经济学家及投资人都认为这些数字资产毫无价值，纯粹是“泡沫”、“投机”。 \n   <br> &nbsp; \n   <br> \n   <br> 断言区块链技术能创造“未来公司”，似乎为时尚早。 \n   <br> \n   <br> \n   <br> 2016年，第一个完全区块链化的公司项目就是“The DAO”，其融资额已高达1.6亿美元，很受追捧，但是因黑客攻击而饱受争议。 \n   <br> &nbsp; \n   <br> 不过，400年前股份公司、证券交易所诞生之初，也是如此受到质疑。 \n   <br> \n   <br> \n   <br> 那么，区块链技术和Token经济模型，如何促进公司新的演变？ \n   <br> \n   <br> \n   <br> 也许，答案就在华为虚拟股权制度里。 \n  </section> \n </section> \n</section> &nbsp; \n<br> \n<br> \n<strong>区块链技术：能否谱写未来公司法</strong> \n<br> \n<br> \n<br> 区块链，本质上是一种分布式的共识与价值激励的技术，一方面通过数学和算法实现了整个系统的共识与信任（规则与交易），另一方面通过代币（token）保证了生态体系的价值激励（财富记录）。 \n<br> \n<br> \n<br> 他有三个特点： \n<strong>分布式去中心化、无须第三方信任体系、数据不可以篡改。</strong> \n<br> \n<br> \n<br> \n<br> 我们可以看看比特币区块链、 以太坊区块链是什么。 \n<br> &nbsp; \n<br> \n<strong>比</strong>特币区块链，目前最大和最成功的区块链应用，其白皮书开明宗义指出其本质是“一种点对点的电子现金系统” 。 \n<br> &nbsp; \n<br> 在这个区块链系统中，根据一套协议（共识），“比特币”可以不依赖特定的中心机构发行，它是节点（矿工）通过工作量证明机制（POW，Proof of Work）获得参与到系统的权利（创建区块、验证交易），根据“挖矿”而产生，并把它记录在去中心化的“账本系统”中。 \n<br> \n<br> \n<br> \n<strong>&nbsp;“比特币”只是比特币区块链上的一条记录，一种可信的数字凭证。</strong> \n<strong>比特币区块链是“去中心化的数字货币公司”，创造“数字货币”是<strong>其核心价值</strong>。</strong> \n<br> &nbsp; \n<br> \n<strong>以</strong>太坊区块链，其定义是下一代智能合约和去中心化应用平台。简单地说， \n<strong>以太坊 = “数字货币” + 智能合约</strong>。 \n<br> \n<br> \n<br> 以太坊能让用户创建并管理一个去中心化的应用程序商店，是“区块链行业的苹果应用商店”公司。 \n<br> &nbsp; \n<br> 以太坊区块链系统的价值激励，发行 “ 以太币”，也是通过POW机制发行。 \n<br> \n<br> \n<br> 可见，其在比特币基础上进行了升级，其不仅是“去中心化的 数字货币公司”功能，同时还构建了一个创建区块链应用的功能，同时，其“ 数字货币”发行构成与比特币更复杂，其设置了一部分Pre-mine（矿前），解决了 以太坊区块链本身运营的资金问题。 \n<br> &nbsp; \n<br> 2014年8月， 以太坊众筹大约发行了7200万 以太币（“矿前”），之后，每年通过POW机制产量在7200万 以太币的25%（每年 以太币的矿产量，不高于1800万，有限制）。 \n<br> \n<br> \n<br> \n<strong>“The DAO”项目，就是基于以太坊的应用。</strong> \n<br> \n<br> \n<div style=\"text-align: center;\"> \n <img alt=\"未来的公司：区块链技术+Token经济\" data-=\"\" data-copyright=\"0\" data-fail=\"0\" data-ratio=\"0.88984375\" data-s=\"300,640\" data-type=\"png\" data-w=\"1280\" src=\"https://esongtest.oss-cn-shanghai.aliyuncs.com/news/20180129/lm1517209233500.jpg\"> \n</div> &nbsp; \n<br> 以上两款区块链应用，会发现其本质上与公司具有某种相似性： \n<br> &nbsp; \n<br> \n<strong>无论比特币区块链还是以太坊区块链，根本上是一个网络协议（规则）的产物，网络协议规定了价值（比特币、以太币）生成规则、交易规则、数据交换格式等。</strong> \n<br> \n<br> \n<br> 网络协议只规定使用这个协议的用户的行为。系统中任何节点只要在行为上满足这个协议的要求，就可以被网络接受，而这些行为可以由任何代码实现，并不需要某个特定的代码。 \n<br> &nbsp; \n<br> \n<strong>而关于公司的性质，新古典经济学认为其“乃一系列合约的连结”。</strong> \n<br> &nbsp; \n<br> 这“一系列合约关系”，包括与原材料或服务的卖方签订的供应合同，与向企业提供劳动力的个人签订的雇佣合同，与债券持有人、银行及其他资本供应方签订的借贷合同以及与企业产品的购买方签订的销售合同等。 \n<br> &nbsp; \n<br> \n<strong>区块链是一个开放式的标准合同，类似《公司法》，而比特币区块链、以太坊区块链等应用，类似一家基于区块链的公司。</strong> \n<br> \n<br> \n<br> 公司的起源，是人类协作的需要，特别是大规模的协作，股份公司则是人类的一项成就，它改写了人与人协作的秩序，国与国竞争的规则。 \n<br> &nbsp; \n<br> 股份公司发展今天，一方面在自由市场中创造了巨额财富，另一方面由于其建立在资本为中心和资本市场基础上，资本天然追求集中（垄断），造成了世界财富分配的极端不均。 \n<br> \n<br> \n<br> \n<section> \n <section> \n  <section> \n   <section> \n    <strong>驯服：财富创造与分配的怪兽？</strong> \n   </section> \n  </section> \n </section> \n</section> \n<br> \n<br> 人类所有的财富创造经济活动，从原始发展到现代，归功于自由市场经济机制，过去40年中国经济成就也是证明。 \n<br> &nbsp; \n<br> 自由市场体系或许是人类已知的最好的财富创造制度，但有着相当的脆弱性质。一方面是来自国家层面中心化共识的脆弱，另外一方面则是来自市场主体本身的中心化（垄断）趋势。 \n<br> &nbsp; \n<br> \n<strong>公司，天然追求资本集中和控制。</strong> \n<br> \n<br> \n<br> 2017年《财富》世界500强排行榜企业总营业收入为27.6万亿美元，占全球GDP总量37.3%（74万亿美元），超过了1/3。 \n<br> &nbsp; \n<br> 在互联网时代，这一特征尤为明显，美国有五巨头“ FAMGA”中国有“BAT”，未来全球市值最大的10家公司将均为互联网科技公司。 \n<br> \n<br> 结果显而易见，以美国为例，根据美国国会预算办公室报告显示： \n<br> \n<br> \n<br> 2013年，全美前10%最富有就家庭拥有占有了全美家庭财富总和的76%，第51%-第90%阶层的家庭占有23%，而全美最底层的民众仅占有全美家庭财富的1%。 \n<br> &nbsp; \n<br> \n<strong>公司导致的集中化，本质原因是资本的一元化，即现行公司是以货币资本为中心的，作为人的劳动并不能作为资本成为公司资产。</strong> \n<br> &nbsp; \n<br> 现阶段无法靠消灭“市场”与“公司”解决这一问题。市场配置资源，实现财富创造；而公司是在企业家主导下，作为产权载体与降低市场自由交易成本的最好的工具。 \n<br> \n<br> \n<br> \n<strong>区块链技术的出现，将改变当前以资本为中心的“股份公司”现状，进化成为货币资本、人力资本以及其他要素资本融合的组织</strong>。 \n<br> \n<br> &nbsp; \n<br> 这对现行法律制度和监管体系是巨大挑战，不过 \n<strong>先行者已有成功的实验</strong>。 \n<br> \n<br> \n<br> \n<section label=\"Copyright Reserved by ipaiban.com.\"> \n <section> \n  <strong><strong><strong>华为虚拟股权：成功运行的 Token ？</strong></strong></strong> \n </section> \n</section> \n<br> 我们可以看看华为的成长历程。 \n<br> \n<br> \n<br> 30年前，华为从2万注册资本起步，成长为中国科技企业的名片，预计2017年的收入将超过6000亿元人民币。 \n<br> &nbsp; \n<br> 1987年，任正非与5位合伙人共同投资成立深圳市华为技术有限公司(即华为公司前身)，注册资本仅2万元。 \n<br> &nbsp; \n<br> 3年后，华为公司即自称实行广泛的“员工持股制度”。 \n<br> &nbsp; \n<br> 2001年7月，华为股东大会通过了股票期权计划，推出了《华为技术有限公司虚拟股票期权计划暂行管理办法》。 \n<br> &nbsp; \n<br> 推出虚拟受限股之后，华为公司员工所持有的原股票被逐步消化吸收转化成虚拟股，原本就不具实质意义的实体股明确变为虚拟股。 \n<br> &nbsp; \n<br> 华为2016年报显示， \n<strong>员工持股计划参与人数为8.2万人</strong>，其中任正非占公司总股本的比例约1.4%。 \n<br> &nbsp; \n<br> 华为虚拟股权，和区块链项目有些类似： \n<br> \n<strong>1.&nbsp;&nbsp; 新产权框架：共享制，非资本主导</strong> \n<br> \n<strong>2.&nbsp;&nbsp; 虚拟股权制：类似Token的虚拟股权，而非《公司法》下的股权</strong> \n<br> \n<strong>3.&nbsp;&nbsp; 配股制度：类似发行机制，实现人力资本的转化</strong> \n<br> &nbsp; \n<br> 将华为虚拟股权作为一个区块链项目来看，可类比之处主要是以上三个关键点，但并非完全意义上的区块链项目。 \n<br> &nbsp; \n<br> \n<strong>主要是华为虚拟股权没有去中心化的记账，也是饱受争议之处：</strong> \n<br> &nbsp; \n<br> \n<br> 《金融时报》在一篇报道中这样描述华为虚拟股权登记—— \n<br> \n<br> \n<br> \n<em>“在华为深圳总部的一间密室里，有一个玻璃橱柜，里面放了10本蓝色的册子，这些厚达数公分的册子里记录着约80000名员工的姓名、身份证号码以及其他个人信息……向外国记者首次展示持股簿册，是华为所做努力的一部分，目的是反驳有关华为在股权问题上一直不够透明的批评。”</em> \n<br> &nbsp; \n<br> 《财经》杂志中对华为虚拟股权登记这样描述—— \n<br> \n<br> \n<br> \n<em>“每年此时，表现优异的华为技术有限公司（下称华为公司）员工们会被主管叫到办公室里去，这是他们一年当中最期待的时刻。这些华为公司的“奋斗者”们会得到一份合同，告知他们今年能够认购多少数量公司股票。这份合同不能被带出办公室，签字完成之后，必须交回公司保管，没有副本，也不会有持股凭证，但员工通过一个内部账号，可以查询自己的持股数量。”</em> \n<br> &nbsp; \n<br> \n<strong>但是，这完全不影响华为虚拟股权制度，视为伟大的区块链项目实验。</strong> \n<br> \n<br> \n<br> \n<section label=\"Copyright Reserved by ipaiban.com.\"> \n <section> \n  <strong>公司2.0：区块链 技术+Token 经济时代</strong> \n </section> \n</section> \n<br> \n<br> 公司进化的大幕已经开启。 \n<br> \n<br> \n<br> \n<strong>华为的实验，说明通过发行类似Token的权利证明，将人力资本作为财富创造与财富分配的要素，是一个有效的机制。</strong> \n<br> &nbsp; \n<br> 经济学家科斯认为，公司（企业）存在的意义，在于降低市场交易成本。 \n<br> &nbsp; \n<br> \n<strong>区块链技术和Token经济模型，可以对公司进行升级，解决市场交易成本问题，在数字世界建立一套，以去中心化的方式实现财富证明、财富流动、资源配置与分工协作的价值激励系统。</strong> \n<br> \n<br> \n<br> 现在，有了 以太坊及其订立的 ERC20标准，任何人都可以在 以太坊上发行自定义的 token，这个token 可以代表任何权益和价值。 \n<br> &nbsp; \n<br> \n<strong>这种基于区块链技术发行Token的新物种，就是公司的2.0版本</strong>。 \n<br> &nbsp; \n<br> 不过，我国尚未建立起“ 虚拟货币”及ICO（Initial CoinOffering）的监管体系，中国人民银行等七部委联合发布了《关于防范代币发行融资风险的公告》，禁止了ICO、禁止交易平台从事代币、“ 虚拟货币”兑换买卖业务。 \n<br> \n<br> \n<br> 本文重新审视华为虚拟股权制度，以区块链技术和Token经济模型，将人力资本引入现行公司框架，形成股权+Token的二元结构，建立一种更好的公司系统，实现整个社会财富创造的风险共担，利益共享。 \n<br> \n<br> \n<br> \n<strong>或再诞生十个或百个华为。<br> <br> 来源微信公众号：法务VC</strong> \n<br>";

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
    @BindView(R.id.icCircleShare)
    ImageView mIcCircleShare;
    @BindView(R.id.icWxShare)
    ImageView mIcWxShare;
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

    private WebViewClient mWebViewClient;

    protected String mPageUrl;
    protected String mPureHtml;
    private AppJs mAppJs;

    private String mId;
    private NewsDetail mNewsDetail;

    private int mTitleHeight;

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
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width:100% !important; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body style:'height:auto;max-width: 100%; width:auto;'>" + bodyHTML + "</body></html>";
    }

    private void initScrollViewLocation() {
        mTitleHeight = mTitleLayout.getMeasuredHeight();
        int webViewHeight = mWebView.getHeight();
        Log.e("zzz", "starty:" + mNewsDetail.getReadHeight() + " and webview 高度:" + webViewHeight);
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
        Log.e("zzz", "onTimeUp");
        mTitleHeight = mTitleLayout.getMeasuredHeight();
        int webViewHeight = mWebView.getHeight();
        //webView内资源异步加载，此时高度可能还未显示完全，需等资源完全显示或高度足够显示才可
        if (mNewsDetail != null && mNewsDetail.getReadHeight() <= webViewHeight + mTitleHeight) {
            stopScheduleJob();
            mScrollView.smoothScrollTo(0, mNewsDetail.getReadHeight());
            Log.e("zzz", "starty:" + mNewsDetail.getReadHeight() + " and webview 高度:" + webViewHeight);
        }
    }

    private void initScrollView() {
        mScrollView.setOnScrollListener(new NewsScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > mTitleHeight && mNewsDetail != null) {
                    mTitleBar.setTitle(mNewsDetail.getTitle());
                } else {
                    mTitleBar.setTitle(R.string.news);
                }
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
        Launcher.with(this, LookBigPictureActivity.class).putExtra(ExtraKeys.PORTRAIT, img).execute();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mNewsDetail != null) {
            mNewsDetail.setReadHeight(mScrollView.getScrollY());
            Log.e("zzz", "y:" + mScrollView.getScrollY());
            NewsCache.insertOrReplaceNews(mNewsDetail);
        }
    }

    @OnClick({R.id.icWxShare, R.id.icCircleShare, R.id.praiseLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icWxShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.icCircleShare:
                shareToPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.praiseLayout:
                requestPraise();
                break;
        }
    }

    private void showShareDialog() {
        if (mNewsDetail == null) return;
        String shareThumbUrl = null;
        if (mNewsDetail.getImgs() != null && !mNewsDetail.getImgs().isEmpty()) {
            shareThumbUrl = mNewsDetail.getImgs().get(0);
        }
        ShareDialog.with(getActivity())
                .setTitleVisible(false)
                .setShareTitle(mNewsDetail.getTitle())
//                .setShareDescription(mNewsDetail.getc)
                .setShareUrl(String.format(Apic.SHARE_NEWS_URL, mNewsDetail.getId()))
                .setShareThumbUrl(shareThumbUrl)
                .show();

    }

    private void shareToPlatform(SHARE_MEDIA platform) {
        if (mNewsDetail == null) return;
        if (ShareUtils.canShare(getActivity(), platform)) {
            String text = mNewsDetail.getTitle() + String.format(Apic.SHARE_NEWS_URL, mNewsDetail.getId());
            UMImage image;
            if (mNewsDetail.getImgs() == null || mNewsDetail.getImgs().isEmpty()) {
                image = new UMImage(getActivity(), R.mipmap.ic_launcher);
            } else {
                image = new UMImage(getActivity(), mNewsDetail.getImgs().get(0));
            }
            new ShareAction(getActivity())
                    .withText(text)
                    .withMedia(image)
                    .setPlatform(platform)
                    .setCallback(mUMShareListener)
                    .share();
        }
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
