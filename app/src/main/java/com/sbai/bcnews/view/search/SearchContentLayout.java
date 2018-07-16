package com.sbai.bcnews.view.search;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.model.search.SearchContent;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.view.AutoSplitTextView;
import com.sbai.bcnews.view.HasLabelLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Modified by $nishuideyu$ on 2018/7/9
 * <p>
 * Description: 搜索界面 内容界面
 * </p>
 */
public class SearchContentLayout extends LinearLayout implements View.OnClickListener {

    //作者
    private HasLabelLayout mFirstHasLabelLayout;
    private TextView mFirstAuthorName;
    private TextView mFirstAuthorIntroduce;
    TextView mFirstAttentionAuthor;
    ConstraintLayout mFirstAuthor;
    HasLabelLayout mSecondHasLabelLayout;
    TextView mSecondAuthorName;
    TextView mSecondAuthorIntroduce;
    TextView mSecondAttentionAuthor;
    ConstraintLayout mSecondAuthor;
    TextView mLookAllAuthor;

    //文章
    private SearchArticleContent mFirstArticle;
    private SearchArticleContent mSecondArticle;
    private SearchArticleContent mThirdArticle;
    private TextView mLookAllArticle;
    private View mArticleSplit;
    private View mArticleFirstSplit;
    private View mArticleSecondSplit;
    private View mArticleThirdSplit;


    @BindView(R.id.newsSplit)
    View mNewsSplit;
    @BindView(R.id.firstPoint)
    View mFirstPoint;
    @BindView(R.id.firstTimeLine)
    TextView mFirstTimeLine;
    @BindView(R.id.firstShare)
    TextView mFirstShare;
    @BindView(R.id.firstTitle)
    TextView mFirstTitle;
    @BindView(R.id.firstContent)
    AutoSplitTextView mFirstContent;
    @BindView(R.id.firstSplit)
    View mFirstSplit;
    @BindView(R.id.firstNews)
    ConstraintLayout mFirstNews;
    @BindView(R.id.secondPoint)
    View mSecondPoint;
    @BindView(R.id.secondTimeLine)
    TextView mSecondTimeLine;
    @BindView(R.id.secondShare)
    TextView mSecondShare;
    @BindView(R.id.secondTitle)
    TextView mSecondTitle;
    @BindView(R.id.secondContent)
    AutoSplitTextView mSecondContent;
    @BindView(R.id.secondSplit)
    View mSecondSplit;
    @BindView(R.id.secondNews)
    ConstraintLayout mSecondNews;
    @BindView(R.id.thirdPoint)
    View mThirdPoint;
    @BindView(R.id.thirdTimeLine)
    TextView mThirdTimeLine;
    @BindView(R.id.thirdShare)
    TextView mThirdShare;
    @BindView(R.id.thirdTitle)
    TextView mThirdTitle;
    @BindView(R.id.thirdContent)
    AutoSplitTextView mThirdContent;
    @BindView(R.id.thirdNews)
    ConstraintLayout mThirdNews;
    @BindView(R.id.lookAllNews)
    TextView mLookAllNews;
    @BindView(R.id.searchNewsLayout)
    LinearLayout mSearchNewsLayout;


    private View mAuthorView;
    private View mArticleView;
    private View mNewsView;

    private List<Author> mAuthorList;
    private List<AuthorArticle> mArticleList;
    private List<NewsFlash> mFlashList;

    private String mSearchContent;
    private int mSearchTextColor;

    private OnSearchContentClickListener mOnSearchContentClickListener;

    public void setOnSearchContentClickListener(OnSearchContentClickListener onSearchContentClickListener) {
        mOnSearchContentClickListener = onSearchContentClickListener;
    }

    public SearchContentLayout(Context context) {
        this(context, null);
    }

    public SearchContentLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchContentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        mAuthorView = layoutInflater.inflate(R.layout.layout_search_author, this, false);
        mArticleView = layoutInflater.inflate(R.layout.layout_search_article, this, false);
        mNewsView = layoutInflater.inflate(R.layout.layout_search_news, this, false);

        ButterKnife.bind(this, mNewsView);

        mSearchTextColor = ContextCompat.getColor(getContext(), R.color.text_FFC10C);

        addView(mAuthorView);
        addView(mArticleView);
        addView(mNewsView);

        initArticleView();
        initAuthorView();
    }


    private void initArticleView() {
        mArticleSplit = mArticleView.findViewById(R.id.articleSplit);
        mFirstArticle = mArticleView.findViewById(R.id.firstArticle);
        mSecondArticle = mArticleView.findViewById(R.id.secondArticle);
        mThirdArticle = mArticleView.findViewById(R.id.thirdArticle);
        mLookAllArticle = mArticleView.findViewById(R.id.lookAllArticle);
        mArticleFirstSplit = mArticleView.findViewById(R.id.firstSplit);
        mArticleSecondSplit = mArticleView.findViewById(R.id.secondSplit);
        mArticleThirdSplit = mArticleView.findViewById(R.id.thirdSplit);

        mFirstArticle.setOnClickListener(this);
        mSecondArticle.setOnClickListener(this);
        mThirdArticle.setOnClickListener(this);
        mLookAllArticle.setOnClickListener(this);

        mFirstArticle.setHighlightColor(mSearchTextColor);
        mSecondArticle.setHighlightColor(mSearchTextColor);
        mThirdArticle.setHighlightColor(mSearchTextColor);
    }

    private void initAuthorView() {
        mFirstHasLabelLayout = mAuthorView.findViewById(R.id.firstHasLabelLayout);
        mFirstAuthorName = mAuthorView.findViewById(R.id.firstAuthorName);
        mFirstAuthorIntroduce = mAuthorView.findViewById(R.id.firstAuthorIntroduce);
        mFirstAttentionAuthor = mAuthorView.findViewById(R.id.firstAttentionAuthor);
        mFirstAuthor = mAuthorView.findViewById(R.id.firstAuthor);
        mSecondHasLabelLayout = mAuthorView.findViewById(R.id.secondHasLabelLayout);
        mSecondAuthorName = mAuthorView.findViewById(R.id.secondAuthorName);
        mSecondAuthorIntroduce = mAuthorView.findViewById(R.id.secondAuthorIntroduce);
        mSecondAttentionAuthor = mAuthorView.findViewById(R.id.secondAttentionAuthor);
        mSecondAuthor = mAuthorView.findViewById(R.id.secondAuthor);
        mLookAllAuthor = mAuthorView.findViewById(R.id.lookAllAuthor);

        mFirstAuthor.setOnClickListener(this);
        mFirstAttentionAuthor.setOnClickListener(this);
        mSecondAuthor.setOnClickListener(this);
        mSecondAttentionAuthor.setOnClickListener(this);
        mLookAllAuthor.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (mOnSearchContentClickListener == null) return;
        switch (v.getId()) {
            case R.id.firstArticle:
                mOnSearchContentClickListener.onArticleClick(mArticleList.get(0));
                break;
            case R.id.secondArticle:
                mOnSearchContentClickListener.onArticleClick(mArticleList.get(1));
                break;
            case R.id.thirdArticle:
                mOnSearchContentClickListener.onArticleClick(mArticleList.get(2));
                break;
            case R.id.lookAllArticle:
                mOnSearchContentClickListener.onLookAllArticle();
                break;
            case R.id.firstAttentionAuthor:
                mOnSearchContentClickListener.onAttentionAuthor(mAuthorList.get(0), mFirstAttentionAuthor);
                break;
            case R.id.firstAuthor:
                mOnSearchContentClickListener.onAuthorClick(mAuthorList.get(0));
                break;
            case R.id.secondAttentionAuthor:
                mOnSearchContentClickListener.onAttentionAuthor(mAuthorList.get(1), mSecondAttentionAuthor);
                break;
            case R.id.secondAuthor:
                mOnSearchContentClickListener.onAuthorClick(mAuthorList.get(1));
                break;
            case R.id.lookAllAuthor:
                mOnSearchContentClickListener.onLookAllAuthor();
                break;
        }
    }

    @OnClick({R.id.firstShare, R.id.firstNews, R.id.secondShare, R.id.secondNews, R.id.thirdShare, R.id.thirdNews, R.id.lookAllNews})
    public void onViewClicked(View view) {
        if (mOnSearchContentClickListener == null) return;
        switch (view.getId()) {
            case R.id.firstShare:
                mOnSearchContentClickListener.onShareNewsFlash(mFlashList.get(0));
                break;
            case R.id.firstNews:
                break;
            case R.id.secondShare:
                mOnSearchContentClickListener.onShareNewsFlash(mFlashList.get(1));
                break;
            case R.id.secondNews:
                break;
            case R.id.thirdShare:
                mOnSearchContentClickListener.onShareNewsFlash(mFlashList.get(2));
                break;
            case R.id.thirdNews:
                break;
            case R.id.lookAllNews:
                mOnSearchContentClickListener.onLookAllNewsFlash();
                break;
        }
    }

    public void setSearchContentData(SearchContent data) {
        setAuthorList(data.getAuthor());
        setArticleList(data.getBitcoin());
        setFlashList(data.getInformation());
    }

    public void setSearchContent(String searchContent) {
        mSearchContent = searchContent;
    }

    public void setAuthorList(List<Author> authorList) {
        mAuthorList = authorList;
        updateAuthorView();
    }

    public void setArticleList(List<AuthorArticle> articleList) {
        mArticleList = articleList;
        updateArticle(articleList);
    }

    public void setFlashList(List<NewsFlash> flashList) {
        mFlashList = flashList;
        updateNewsFlash(flashList);
    }

    private void updateAuthorView() {
        if (notHasAuthor()) {
            mAuthorView.setVisibility(GONE);
            mArticleSplit.setVisibility(GONE);
            return;
        } else {
            mArticleSplit.setVisibility(VISIBLE);
            mAuthorView.setVisibility(VISIBLE);
        }

        updateAuthorInfo(mAuthorList.get(0), mFirstHasLabelLayout, mFirstAuthorName, mFirstAuthorIntroduce, mFirstAttentionAuthor);
        if (mAuthorList.size() > 1) {
            mSecondAuthor.setVisibility(VISIBLE);
            mLookAllAuthor.setVisibility(VISIBLE);
            updateAuthorInfo(mAuthorList.get(1), mSecondHasLabelLayout, mSecondAuthorName, mSecondAuthorIntroduce, mSecondAttentionAuthor);
        } else {
            mSecondAuthor.setVisibility(GONE);
            mLookAllAuthor.setVisibility(GONE);

        }
    }

    private void updateAuthorInfo(Author author, HasLabelLayout hasLabelLayout, TextView authorName, TextView authorIntroduce, TextView attentionAuthor) {
        hasLabelLayout.setImageSrc(author.getUserPortrait());
        hasLabelLayout.setLabelImageViewVisible(author.isAuthor());

        boolean isOfficialAuthor = author.getRankType() == Author.AUTHOR_STATUS_OFFICIAL;
        hasLabelLayout.setLabelSelected(isOfficialAuthor);

        authorName.setText(StrUtil.changeSpecialTextColor(author.getUserName(), mSearchContent, mSearchTextColor));
        if (!TextUtils.isEmpty(author.getAuthInfo())) {
            authorIntroduce.setVisibility(VISIBLE);
            authorIntroduce.setText(author.getAuthInfo());
        } else {
            authorIntroduce.setVisibility(GONE);
        }

        updateAttentionAuthor(author, attentionAuthor);

    }

    public void updateAttentionAuthor(Author author, TextView attentionAuthor) {
        if (author.getIsConcern() == Author.AUTHOR_IS_ALREADY_ATTENTION) {
            attentionAuthor.setSelected(true);
            attentionAuthor.setTextColor(ContextCompat.getColor(getContext(), R.color.text_d9));
            attentionAuthor.setText(R.string.has_attention);
        } else {
            attentionAuthor.setSelected(false);
            attentionAuthor.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            attentionAuthor.setText(R.string.attention);
        }
    }

    private void updateArticle(List<AuthorArticle> articleList) {
        if (notHasArticle()) {
            mArticleView.setVisibility(GONE);
            return;
        } else {
            mArticleView.setVisibility(VISIBLE);
        }

        int showViewSize;
        if (articleList.size() < 4) {
            showViewSize = articleList.size();
        } else {
            showViewSize = 3;
        }

        updateArticleInfo(articleList.get(0), mFirstArticle);

        if (showViewSize == 1) {
            mSecondArticle.setVisibility(GONE);
            mThirdArticle.setVisibility(GONE);
            mLookAllArticle.setVisibility(GONE);
            mArticleFirstSplit.setVisibility(GONE);
            mArticleSecondSplit.setVisibility(GONE);
            mArticleThirdSplit.setVisibility(GONE);
        } else if (showViewSize == 2) {
            mSecondArticle.setVisibility(VISIBLE);
            mThirdArticle.setVisibility(GONE);
            updateArticleInfo(articleList.get(1), mSecondArticle);
            mArticleFirstSplit.setVisibility(VISIBLE);
            mArticleSecondSplit.setVisibility(GONE);
            mArticleThirdSplit.setVisibility(GONE);
            mLookAllArticle.setVisibility(GONE);
        } else {
            mLookAllArticle.setVisibility(VISIBLE);
            mSecondArticle.setVisibility(VISIBLE);
            mThirdArticle.setVisibility(VISIBLE);
            mArticleFirstSplit.setVisibility(VISIBLE);
            mArticleSecondSplit.setVisibility(VISIBLE);
            mArticleThirdSplit.setVisibility(VISIBLE);
            updateArticleInfo(articleList.get(1), mSecondArticle);
            updateArticleInfo(articleList.get(2), mThirdArticle);
        }
    }

    private void updateArticleInfo(AuthorArticle authorArticle, SearchArticleContent searchArticleContent) {
        searchArticleContent.setHighlightText(mSearchContent);
        searchArticleContent.setAuthorArticle(authorArticle);
    }

    private void updateNewsFlash(List<NewsFlash> flashList) {
        if (notHasNewsFlash()) {
            mNewsView.setVisibility(GONE);
            return;
        } else {
            mNewsView.setVisibility(VISIBLE);
        }
        switch (flashList.size()) {
            case 1:
                mFirstSplit.setVisibility(GONE);
                mSecondSplit.setVisibility(GONE);
                mLookAllNews.setVisibility(GONE);
                mSecondNews.setVisibility(GONE);
                mThirdNews.setVisibility(GONE);
                updateNewsFlashInfo(mFlashList.get(0), mFirstTitle, mFirstTimeLine, mFirstContent);
                break;
            case 2:
                mFirstSplit.setVisibility(VISIBLE);
                mSecondSplit.setVisibility(GONE);
                mLookAllNews.setVisibility(GONE);
                mSecondNews.setVisibility(VISIBLE);
                mThirdNews.setVisibility(GONE);
                updateNewsFlashInfo(mFlashList.get(0), mFirstTitle, mFirstTimeLine, mFirstContent);
                updateNewsFlashInfo(mFlashList.get(1), mSecondTitle, mSecondTimeLine, mSecondContent);
                break;
            default:
                mFirstSplit.setVisibility(VISIBLE);
                mSecondSplit.setVisibility(VISIBLE);
                mLookAllNews.setVisibility(VISIBLE);
                mSecondNews.setVisibility(VISIBLE);
                mThirdNews.setVisibility(VISIBLE);
                updateNewsFlashInfo(mFlashList.get(0), mFirstTitle, mFirstTimeLine, mFirstContent);
                updateNewsFlashInfo(mFlashList.get(1), mSecondTitle, mSecondTimeLine, mSecondContent);
                updateNewsFlashInfo(mFlashList.get(2), mThirdTitle, mThirdTimeLine, mThirdContent);
                break;

        }
    }

    private void updateNewsFlashInfo(NewsFlash newsFlash, TextView title, TextView timeLine, AutoSplitTextView content) {
        if (TextUtils.isEmpty(newsFlash.getContent())) {
            content.setVisibility(GONE);
        } else {
            content.setVisibility(VISIBLE);
            content.setMaxLines(6);
            content.setAutoSplitEnabled(true);
            content.setEllipsize(TextUtils.TruncateAt.END);
        }

        timeLine.setText(DateUtil.formatDefaultStyleTime(newsFlash.getReleaseTime()));

        if (!TextUtils.isEmpty(newsFlash.getTitle())) {
            String trim = newsFlash.getTitle()
                    .replace("【", "")
                    .replace("】", "")
                    .replaceAll("\r", "")
                    .replaceAll("\n", "").trim();
            title.setText(StrUtil.changeSpecialTextColor(trim, mSearchContent, mSearchTextColor));
            title.setVisibility(VISIBLE);
        } else {
            title.setVisibility(GONE);
        }

        content.setText(newsFlash.getContent());

        content.setOnClickListener(new OnClickListener() {
            boolean flag = true;

            @Override
            public void onClick(View v) {
                int lineCount = content.getLineCount();
                if (lineCount != 0 && lineCount >= 6)
                    if (flag) {
                        flag = false;
                        content.setMaxLines(Integer.MAX_VALUE);
                        content.setEllipsize(null);
                    } else {
                        flag = true;
                        content.setMaxLines(6);
                        content.setEllipsize(TextUtils.TruncateAt.END);
                    }
            }
        });
    }


    private boolean notHasAuthor() {
        return mAuthorList == null || mAuthorList.isEmpty();
    }

    private boolean notHasArticle() {
        return mArticleList == null || mArticleList.isEmpty();
    }

    private boolean notHasNewsFlash() {
        return mFlashList == null || mFlashList.isEmpty();
    }




    public interface OnSearchContentClickListener {

        void onAuthorClick(Author author);

        void onAttentionAuthor(Author author, TextView textView);

        void onLookAllAuthor();

        void onArticleClick(AuthorArticle authorArticle);

        void onLookAllArticle();

        void onShareNewsFlash(NewsFlash newsFlash);

        void onLookAllNewsFlash();
    }
}
