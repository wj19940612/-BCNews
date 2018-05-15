package com.sbai.bcnews.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsDetail;
import com.sbai.bcnews.model.OtherArticle;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Modified by $nishuideyu$ on 2018/4/27
 * <p>
 * Description: 咨询详情webView下面的内容
 * </p>
 */
public class NewDetailBottomView extends LinearLayout implements View.OnClickListener {

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

    private OnNewDetailBottomViewClickListener mOnNewDetailBottomViewClickListener;

    public void setOnNewDetailBottomViewClickListener(OnNewDetailBottomViewClickListener onNewDetailBottomViewClickListener) {
        mOnNewDetailBottomViewClickListener = onNewDetailBottomViewClickListener;
    }

    private List<OtherArticle> mOtherArticles;

    public NewDetailBottomView(Context context) {
        this(context, null);
    }

    public NewDetailBottomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewDetailBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_news_details_bottom, this, false);
        ButterKnife.bind(this, view);

        mFirstArticle.setOnClickListener(this);
        mSecondArticle.setOnClickListener(this);
        mThirdArticle.setOnClickListener(this);
        mWxShare.setOnClickListener(this);
        mCircleShare.setOnClickListener(this);
        mPraiseLayout.setOnClickListener(this);

    }

    public void updateOtherData(final List<OtherArticle> data) {

    }



    public void updatePraiseCollect(NewsDetail newsDetail) {

    }

    public void setViewShow() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstArticle:
                if (mOnNewDetailBottomViewClickListener != null && mOtherArticles != null) {
                    mOnNewDetailBottomViewClickListener.onArticleClick(0, mOtherArticles.get(0));
                }
                break;
            case R.id.secondArticle:
                if (mOnNewDetailBottomViewClickListener != null) {
                    mOnNewDetailBottomViewClickListener.onArticleClick(1, mOtherArticles.get(1));
                }
                break;
            case R.id.thirdArticle:
                if (mOnNewDetailBottomViewClickListener != null) {
                    mOnNewDetailBottomViewClickListener.onArticleClick(2, mOtherArticles.get(2));
                }
                break;
            case R.id.wxShare:
                if (mOnNewDetailBottomViewClickListener != null) {
                    mOnNewDetailBottomViewClickListener.onShare(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.circleShare:
                if (mOnNewDetailBottomViewClickListener != null) {
                    mOnNewDetailBottomViewClickListener.onShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                }
                break;
            case R.id.praiseLayout:
                if (mOnNewDetailBottomViewClickListener != null) {
                    mOnNewDetailBottomViewClickListener.onPraise();
                }
                break;
        }
    }

    public interface OnNewDetailBottomViewClickListener {

        void onPraise();

        void onArticleClick(int position, OtherArticle otherArticle);

        void onCommentClick();

        void onShare(SHARE_MEDIA shareMedia);
    }
}
