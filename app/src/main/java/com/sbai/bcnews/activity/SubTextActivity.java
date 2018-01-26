package com.sbai.bcnews.activity;

import android.content.Intent;
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
import com.sbai.bcnews.fragment.NewsFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.news.NewsCache;
import com.sbai.bcnews.view.NewsScrollView;
import com.sbai.bcnews.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\1\25 0025.
 */

public class SubTextActivity extends BaseActivity {
    public static final String text = "<p>财联社11月2日讯，随着威创股份的定增、红黄蓝的美股上市等，上市公司快速扩张，竞争激烈的幼教市场一直在跑马圈地。</p>\r\n\r\n<p>10月27日，威创股份发布公告称，已募集到9.18亿元资金，其中扣除发行费用后的募集资金净额9.06亿元，将全部投入到儿童艺体培训中心建设项目，占总投资额的比例为75.43%。</p>\r\n\r\n<p><strong>威创的幼教利润占其总利润的份额超过70%。</strong></p>\r\n\r\n<p>威创股份的前三季度报显示，招商证券、华创证券、中泰证券、天风证券等均给予推荐，或者买入的评级，这是在A股教育上市公司的评级中不多见的。</p>\r\n\r\n<p>不过，&ldquo;园所数量增长不及预期&rdquo;&ldquo; 外延收购不及预期&rdquo;等风险不容忽视，那么，威创积极并购幼教的后遗症是什么?对于跨界幼教的上市公司有哪些启示?</p>\r\n\r\n<p><strong>券商唱好背后，加盟模式是否健康</strong></p>\r\n\r\n<p>自2015年开始，威创股份从大屏幕数字显示系统专业供应商，通过收购红缨教育和金色摇篮切入幼教，仅收购幼儿园就已累计投入达18.68亿元。</p>\r\n\r\n<p><img alt=\"\" src=\"https://image.cailianpress.com/cailianpress/default/20171102/103839_59fa852f8aa15.jpg\" style=\"width: 640px; height: 411px;\" />&nbsp;</p>\r\n\r\n<p>通过快速并购，威创股份实现了净利润快速增长;线下并购当地有号召力的幼教品牌，投资线上企业幼师口袋，布局和培养幼教师资，为威创旗下其他品牌输出师资和管理人才。从收购类型来看，威创股份收购标的企业均偏资源型，旗下都有很多实体。</p>\r\n\r\n<p><img height=\"1\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABBJREFUCB0BBQD6/wAAAAAAAAUAAbqJEIoAAAAASUVORK5CYII=\" width=\"1\" />&nbsp;<img alt=\"\" src=\"https://image.cailianpress.com/cailianpress/default/20171102/103904_59fa8548cf406.jpg\" style=\"width: 640px; height: 144px;\" /></p>\r\n\r\n<p>威创股份的四起并购均设置了业绩承诺。2015年和2016年，金色摇篮和红缨教育的实际业绩完成情况均高于业绩承诺。红缨教育表现最好，2015年实际完成5648万元，2016年实际完成1.02亿元。</p>\r\n\r\n<p><strong>部分核心员工因利益分布不均，以及并购后分红减少而离开了红缨教育</strong></p>\r\n\r\n<p>一旦超额完成业绩达到一定水平，威创股份需要对核心管理层进行额外奖励。不过，一位红缨教育的中层员工向财联社透露，威创通过相关的资本力量促成了对红缨的收购，缺乏核心人才直指幼教的要害，尤其对于加盟园来说，核心的经营管理人才很重要。但是，加盟模式本身才是症灶。</p>\r\n\r\n<p><img height=\"1\" src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAAOxAAADsQBlSsOGwAAABBJREFUCB0BBQD6/wAAAAAAAAUAAbqJEIoAAAAASUVORK5CYII=\" width=\"1\" />&nbsp;<img alt=\"\" src=\"https://image.cailianpress.com/cailianpress/default/20171102/103923_59fa855bc35d7.jpg\" style=\"width: 544px; height: 44px;\" /></p>\r\n\r\n<p>加盟模式及其良性扩张是资本市场关注的。瑞金基金Z姓高管向财联社指出，加盟园所不需要获得幼儿园的所有权，只需要交加盟费，不涉及运营。虽然实现了轻资产的扩张并且扩张速度极快，但是很多加盟园没有进一步消化加盟品牌和管理方式。</p>\r\n\r\n<p>无独有偶，一位和君系合伙人认为，幼儿园是资本市场的优质资源，但是威创的园所大多采取加盟模式，如何提高加盟品牌的黏性以实现良好扩张，这是威创在并购后亟待考量的。</p>\r\n\r\n<p>同时，绝大多数的幼儿园资源原有的经营模式较弱，主要有两大风险，一个是并购后提高了幼儿园的管理成本;第二个在于幼儿园需要增强经营成本和收益，来反哺自身的管理成本。</p>\r\n\r\n<p>面对业绩承诺大考，并购后的幼儿园要&ldquo;自负盈亏&rdquo;，这对幼儿园的经营来说是个不小的考验。此外，上市公司也要考虑募资的成本、加盟园的投资回报率以及长期投资价值。</p>\r\n\r\n<p><strong>&ldquo;加盟模式基本进入尾声阶段。虽然短期内死不了，但是加盟模式是危险的。&rdquo;</strong></p>\r\n\r\n<p>阳光雨露的董事长徐玲表示，&ldquo;加盟模式有共性。首先，目前的连锁式园所规模大，单体园所数量少，加盟的市场空间变少;第二，品牌在加盟后的支持力度有待提高;另外，加盟还面临续费的问题。&rdquo;</p>\r\n\r\n<p>值得注意的是，曾被威创列入投资目标的幼教公司创始人告诉财联社，&ldquo;之所以没有接受威创主要在于，虽然威创表示发力成为文化教育平台，但是威创未来的主营业务并不清晰。另外，威创现在是双主业，原主业营收有所增长，一旦原主营业务增长快，会不会造成教育业务的边缘化。&rdquo;</p>\r\n\r\n<p><strong>剥离警示：跨界幼教不容易</strong></p>\r\n\r\n<p>纵观整个A股，财联社发现，涉及教育业务的公司对教育的态度不一。仅从剥离业务的角度来看，上市公司有三种主要行为：一类是剥离原有业务，转而发力教育;第二类是剥离教育业务，发力原有业务;第三类是一直从事教育业务，但会随市场需求对业务线进行调整。</p>\r\n\r\n<p><img alt=\"\" src=\"https://image.cailianpress.com/cailianpress/default/20171102/103951_59fa857758289.jpg\" style=\"width: 640px; height: 468px;\" /></p>\r\n\r\n<p>整体来看，威创的幼教行业盈利能力优于电子视像业务，幼教行业的收入增长成为带动公司营收增长的主要动力。</p>\r\n\r\n<p>除了威创股份，LED行业还有勤上股份、长方集团等跨界进入幼教。长方集团在2016年并购线下幼儿园、开设教育子公司，寻求&ldquo;LED+教育&rdquo;双主业发展。而勤上股份迈步最大，主业涉及幼教、K12课外辅导、国际教育以及民办学校等领域。2016年，勤上股份前董秘胡绍安曾向媒体表示，未来不排除剥离LED资产的可能性。</p>\r\n\r\n<p>&ldquo;透过与威创股份董事长何正宇沟通，可以感受到他对原主业的热爱，我个人认为他不会剥离原主业，这也是对并入之后的一个担心&rdquo;，曾参与并购谈判的幼教公司创始人如是说。</p>\r\n\r\n<p><strong>引入招商局和擅长</strong></p>\r\n\r\n<p>也有资深基金人士预测，同时，原主营虽然没有大幅度提升，但其业务能力仍表现良好，剥离原有主业的意义不大。</p>\r\n\r\n<p>威创股份董秘李亦争曾对财联社表示，上市公司跨界或者转型需要投资并购、投后管理、运营管理、战略提升四项能力，目前威创在沿着这四个方面发力，但很多公司在投资并购能力这方面不过关。威创股份投资并购非常重视两点，首先，不仅仅着眼于一个并购，而是着力于并购背后的投融资体系的整体规划;其次，更重视通过战略梳理、激励制度设计等手段激发并购标的的发展潜力。在财务报表利润外，更重视现金流的管理，由此建立投融资的良性循环。</p>\r\n\r\n<p>近年来教育行业呈现了良好的发展态势，有望成为中国社会经济领域重要的增长点。幼儿教育行业普遍存在连锁幼儿园集中度低，优质教育内容较少，缺乏高效、系统和先进的管理理念和体系等问题，上市公司垂青教育资产，需要提高优质教育资源整合和教育服务供给的能力。</p>\r\n\r\n<p><strong>所以跨界教育，必须做好长期经营的打算。</strong></p>\r\n\r\n<p>快速扩张的后遗症便是当资本涌入后，如果没有看到所期望的收益，一旦资本撤出，可能会造成行业的大片伤亡，</p>\r\n\r\n<p>此外，上市公司在并购教育标的、进入教育领域前，应做好前期调研并时时关注新主业发展的动态，学习教育行业运营管理经验以及人才的引进。</p>\r\n\r\n<p>有证券分析师对财联社表示，收购其实只是第一步，最难的是收购标的如何与主业相协同匹配。收购也让很多企业陷入泥潭、放弃主业，因为人都是贪婪的，都想去做轻松的生意。所以，在收购的同时，一定还要做透主业。</p>\r\n";

    @BindView(R.id.webView)
    WebView mWebView;
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
        setContentView(R.layout.activity_subtext);
        ButterKnife.bind(this);
        initData();
        initWebView();
        initScrollView();
        requestDetailData();
    }

    private void initData() {
        mId = getIntent().getStringExtra(ExtraKeys.NEWS_ID);
        mId = "5a6841148940b041d86256da";
        mNewsDetail = NewsCache.getCahceNewsForId(mId);
//        mPureHtml = text;
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
        //webView加载完高度才完全展示出来，这时候scrollView才能滑动到记忆的位置
        if (mNewsDetail != null)
            mScrollView.scrollTo(0, mNewsDetail.getReadHeight());
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
//        if (mNewsDetail == null) {
//            Apic.getNewsDetail(mId).tag(TAG).callback(new Callback2D<Resp<NewsDetail>, NewsDetail>() {
//                @Override
//                protected void onRespSuccessData(NewsDetail data) {
//                    mNewsDetail = data;
//                    updateData(data);
//                }
//            }).fireFreely();
//        }else{
//            updateData(mNewsDetail);
//        }
        if (mNewsDetail == null) {
            mNewsDetail = new NewsDetail();
            mNewsDetail.setContent("<p>彭博称，北欧最大的银行已经告诉其员工，不要交易比特币和其他<u>加密货币</u>。这可能导致欧洲银行联合会评估其对<u>加密货币</u>的官方立场。</p> \n<p>北欧联合银行发言人Afroditi Kellberg通过电话说，该行将于2月28日实施这一禁令，之前该行董事会同意采取这一立场，因为<u>加密货币</u>市场“无监管的性质”。截至第三季度末，该行约有3.15万名员工。</p> \n<p>北欧联合银行在发给彭博的电子邮件中表示，其政策“包括对现有持币人员的过渡性规定，并允许某些例外”。已经拥有比特币的员工“被允许保留现有持仓”。</p> \n<div>\n  北欧第二大银行丹麦丹斯克银行说，不鼓励员工交易比特币，但尚未决定是否需要全面禁止。丹斯克银行发言人Kenni Leth在一封电子邮件中表示：“我们对 \n <u>加密货币</u>持怀疑态度，并建议我们的员工不要交易，但我们并没有实施实际的禁令。我们正在分析情况，时间会告诉我们是否会有正式的禁令。” \n <p></p> \n <p>丹斯克银行不向客户提供<u>加密货币</u>交易。“由于各种<u>加密货币</u>不成熟、缺乏透明度，我们决定在我们的平台上不提供这种证券交易，”Leth说。</p> \n <p>北欧联合银行在电邮中表示，对客户的政策与对员工不一样，但也要强调不推荐客户投资于此。</p> \n <p>（原文标题：《北欧联合银行全员禁止比特币 因市场“无监管”》）</p> \n</div> \n<div></div> \n<div>\n  来源：新浪财经 \n</div> \n<div>\n  文章地址：http://finance.sina.com.cn/stock/usstock/c/2018-01-23/doc-ifyqwiqi5439089.shtml \n</div>");
            mNewsDetail.setCreateTime(1516781844505L);
            mNewsDetail.setDetail("比特币资讯");
            mNewsDetail.setId("5a6841148940b041d86256da");
            mNewsDetail.setImgs(new ArrayList<String>());
            mNewsDetail.setPraiseCount(0);
            mNewsDetail.setReaderCount(1);
            mNewsDetail.setReleaseTime(1516636800000L);
            mNewsDetail.setSecondDetail("行业解读");
            mNewsDetail.setSecondType(12);
            mNewsDetail.setSource("BTCif中文网");
            mNewsDetail.setSummary("彭博称，北欧最大的银行已经告诉其员工，不要交易比特币和其他加密货币。这可能导致欧洲银行联合会评估其对加密货币的官方立场。");
            List<String> tags = new ArrayList<>();
            tags.add("银行");
            tags.add("员工");
            tags.add("北欧");
            tags.add("加密");
            tags.add("货币");
            mNewsDetail.setTags(tags);
            mNewsDetail.setTitle("北欧联合银行全员禁止比特币，因加密货币市场“无监管”");
            mNewsDetail.setType(1);
        }
        updateData(mNewsDetail);

        mPureHtml = mNewsDetail.getContent();
        loadPage();
    }

    private void updateData(NewsDetail newsDetail) {
        mSubtitle.setText(newsDetail.getTitle());
        mSource.setText(newsDetail.getSource());
        mPubTime.setText(DateUtil.formatDefaultStyleTime(newsDetail.getReleaseTime()));
        mReadTime.setText(String.format(getString(R.string.reader_time),newsDetail.getReaderTime()));
        updatePraiseCount(newsDetail.getPraiseCount());
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
        mNewsDetail.setReadHeight(mScrollView.getScrollY());
        NewsCache.insertOrReplaceNews(mNewsDetail);
        Log.e("zzz", "scY:" + mScrollView.getScrollY());
    }

    @OnClick({R.id.icWxShare, R.id.icCircleShare,R.id.praiseLayout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icWxShare:
                break;
            case R.id.icCircleShare:
                break;
            case R.id.praiseLayout:
                requestPraise();
                break;
        }
    }
}
