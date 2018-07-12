package com.sbai.bcnews.fragment.search;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.SearchActivity;
import com.sbai.bcnews.activity.ShareNewsFlashActivity;
import com.sbai.bcnews.activity.author.AuthorActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.BaseFragment;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.NewsFlash;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.model.author.AuthorArticle;
import com.sbai.bcnews.model.search.HistorySearch;
import com.sbai.bcnews.model.search.HotSearch;
import com.sbai.bcnews.model.search.SearchContent;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.ToastUtil;
import com.sbai.bcnews.utils.UmengCountEventId;
import com.sbai.bcnews.view.search.SearchContentLayout;
import com.sbai.bcnews.view.search.SearchEditText;
import com.sbai.bcnews.view.search.SearchLabelLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchSynthesizeFragment extends BaseFragment implements SearchLabelLayout.OnSearchLabelClickListener {

    Unbinder unbinder;

    @BindView(R.id.searchLabelLayout)
    SearchLabelLayout mSearchLabelLayout;
    @BindView(R.id.searchContentLayout)
    SearchContentLayout mSearchContentLayout;

    private ArrayList<String> mHotSearchList;

    private SearchActivity mSearchActivity;

    private String mSearchContent;

    private OnSearchLabelSelectListener mOnSearchLabelSelectListener;

    private SearchEditText.OnSearchContentResultListener mSearchContentResultListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchActivity) {
            mSearchActivity = (SearchActivity) context;
        }

        if (context instanceof OnSearchLabelSelectListener) {
            mOnSearchLabelSelectListener = (OnSearchLabelSelectListener) context;
        }

        if (context instanceof SearchEditText.OnSearchContentResultListener) {
            mSearchContentResultListener = (SearchEditText.OnSearchContentResultListener) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_synthesize, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
        requestHotSearchContent();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            if (!TextUtils.isEmpty(mSearchContent)) {
                requestSearchContent(mSearchContent);
            }
        }
    }


    private void requestHotSearchContent() {
        Apic.requestHotSearchContent()
                .tag(TAG)
                .callback(new Callback<ListResp<HotSearch>>() {
                    @Override
                    protected void onRespSuccess(ListResp<HotSearch> resp) {
                        List<HotSearch> hotSearchList = resp.getListData();
                        mHotSearchList.clear();
                        if (hotSearchList != null) {
                            for (HotSearch result : hotSearchList) {
                                mHotSearchList.add(result.getWord());
                            }
                        }
                        mSearchLabelLayout.setHotSearchLabel(mHotSearchList);
                    }
                })
                .fire();
    }

    private void init() {

        mHotSearchList = new ArrayList<String>();

        updateSearchLabel();
        mSearchLabelLayout.setOnSearchLabelClickListener(this);

        mSearchContentLayout.setOnSearchContentClickListener(new SearchContentLayout.OnSearchContentClickListener() {
            @Override
            public void onAuthorClick(Author author) {
                Launcher.with(getActivity(), AuthorActivity.class).putExtra(ExtraKeys.ID, author.getId()).execute();
            }

            @Override
            public void onAttentionAuthor(Author author, ImageView imageView) {
                attentionAuthor(author, imageView);
            }

            @Override
            public void onLookAllAuthor() {
                mSearchActivity.getViewPager().setCurrentItem(SearchActivity.FRAGMENT_AUTHOR_POSITION, false);
            }

            @Override
            public void onArticleClick(AuthorArticle authorArticle) {
                Launcher.with(getActivity(), NewsDetailActivity.class)
                        .putExtra(ExtraKeys.NEWS_ID, authorArticle.getId())
                        .putExtra(ExtraKeys.CHANNEL, (authorArticle.getChannel() == null || authorArticle.getChannel().isEmpty()) ? null : authorArticle.getChannel().get(0))
                        .executeForResult(NewsDetailActivity.REQ_CODE_CANCEL_COLLECT);
            }

            @Override
            public void onLookAllArticle() {
                mSearchActivity.getViewPager().setCurrentItem(SearchActivity.FRAGMENT_ARTICLE_POSITION, false);
            }

            @Override
            public void onShareNewsFlash(NewsFlash newsFlash) {
                MobclickAgent.onEvent(getActivity(), UmengCountEventId.NEWS_FLASH_SHARE);
                Launcher.with(getActivity(), ShareNewsFlashActivity.class)
                        .putExtra(ExtraKeys.NEWS_FLASH, newsFlash)
                        .execute();
            }

            @Override
            public void onLookAllNewsFlash() {
                mSearchActivity.getViewPager().setCurrentItem(SearchActivity.FRAGMENT_NEWS_FLASH_POSITION, false);
            }
        });
    }

    private void updateSearchLabel() {
        List<String> historySearchList = HistorySearch.getHistorySearchList();
        mSearchLabelLayout.setHistorySearchLabel(historySearchList);
        mSearchLabelLayout.updateHotSearch();
    }

    private void attentionAuthor(Author author, ImageView imageView) {
        if (!LocalUser.getUser().isLogin()) {
            Launcher.with(getContext(), LoginActivity.class).execute();
        } else {
            if (author != null) {
                final int attentionType = author.getIsConcern() == Author.AUTHOR_STATUS_SPECIAL ? Author.AUTHOR_IS_NOT_ATTENTION : Author.AUTHOR_IS_ALREADY_ATTENTION;
                Apic.attentionAuthor(author.getId(), attentionType)
                        .tag(TAG)
                        .callback(new Callback<Resp<Object>>() {
                            @Override
                            protected void onRespSuccess(Resp<Object> resp) {
                                author.setIsConcern(attentionType);
                                Intent intent = new Intent();
                                intent.putExtra(ExtraKeys.TAG, attentionType);
                                mSearchContentLayout.updateAttentionAuthor(author, imageView);
                                if (author.getIsConcern() == Author.AUTHOR_IS_ALREADY_ATTENTION) {
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

    public void setSearchContent(String searchContent) {
        mSearchContent = searchContent;
        if (getUserVisibleHint())
            requestSearchContent(searchContent);
    }

    private void requestSearchContent(String searchContent) {
        Api.cancel(TAG);
        Apic.requestSearchContent(Uri.encode(searchContent))
                .tag(TAG)
                .callback(new Callback2D<Resp<SearchContent>, SearchContent>() {
                    @Override
                    protected void onRespSuccessData(SearchContent data) {
                        if (data.isEmpty()) {
                            showSearchLabelView(true);
                        } else {
                            showSearchContentView();
                            if (mSearchContentResultListener != null) {
                                mSearchContentResultListener.onSearchFinish(searchContent, data);
                            }
                            mSearchContentLayout.setSearchContent(searchContent);
                            mSearchContentLayout.setSearchContentData(data);
                        }

//                        List<AuthorArticle> bitcoin = data.getBitcoin();
//
//                        if (bitcoin.size() > 2) {
//                            AuthorArticle a = bitcoin.get(0);
//                            bitcoin.remove(a);
//
//                            AuthorArticle authorArticle = bitcoin.get(1);
//                            bitcoin.remove(authorArticle);
//                        }


                    }
                })
                .fireFreely();
    }

    private void showSearchLabelView(boolean showNotSearchDataView) {
        mSearchContentLayout.setVisibility(View.GONE);
        mSearchLabelLayout.setVisibility(View.VISIBLE);
        if (showNotSearchDataView) {
            mSearchLabelLayout.showNotSearchView();
        }
    }

    private void showSearchContentView() {
        mSearchContentLayout.setVisibility(View.VISIBLE);
        mSearchLabelLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSearchLabelClick(String values) {
        if (mOnSearchLabelSelectListener != null) {
            mOnSearchLabelSelectListener.onSearchLabelSelect(values);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void updateSearchContent(String values) {
        if (TextUtils.isEmpty(values)) {
            if (mSearchLabelLayout.getVisibility() == View.GONE) {
                showSearchLabelView(false);
            }
            updateSearchLabel();
        } else {
//            if (mSearchContentLayout.getVisibility() == View.GONE) {
//                showSearchContentView();
//            }
        }
    }

    public interface OnSearchLabelSelectListener {
        void onSearchLabelSelect(String values);
    }

}
