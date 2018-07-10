package com.sbai.bcnews.fragment.search;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.BaseFragment;
import com.sbai.bcnews.http.Api;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.search.HistorySearch;
import com.sbai.bcnews.model.search.HotSearch;
import com.sbai.bcnews.model.search.SearchContent;
import com.sbai.bcnews.view.search.SearchContentLayout;
import com.sbai.bcnews.view.search.SearchLabelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchSynthesizeFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.searchLabelLayout)
    SearchLabelLayout mSearchLabelLayout;
    @BindView(R.id.searchContentLayout)
    SearchContentLayout mSearchContentLayout;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    private ArrayList<String> mHotSearchList;

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
        setSearchContent("数字货币");
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

        ArrayList<String> data = new ArrayList<>();
        data.add("22222");
        data.add("867447jfkldj");
        data.add("888");
        data.add("754155445jisjkfsjkfsdjkfdssjk健康更健康的风格的进口量给大家看两个房间开两个房间开两个房间空间管控尽快两个接口规范健康规范健康健康了规范健康规范健康规范健康克己奉公即可将");
        data.add("33");
        data.add("kjfjklfjkfjkfjk");

        List<String> historySearchList = HistorySearch.getHistorySearchList();

        mSearchLabelLayout.setHistorySearchLabel(historySearchList);


        mSearchLabelLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchLabelLayout.setHotSearchLabel(null);
            }
        }, 500);


        mSearchLabelLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchLabelLayout.setHotSearchLabel(data);
            }
        }, 2000);

        mSearchLabelLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchLabelLayout.setVisibility(View.GONE);
            }
        }, 5000);


//        AuthorArticle authorArticle = new AuthorArticle();
//        authorArticle.setTitle("第一天哈哈哈哈哈哈哈");
//        mFirstArticle.setAuthorArticle(authorArticle);
//
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=311241833,2213359007&fm=173&app=25&f=JPEG?w=640&h=456&s=27161DC7033197F51474DCAE0300F013");
//        AuthorArticle authorArticle2 = new AuthorArticle();
//        authorArticle2.setTitle("第一天哈哈哈哈哈哈哈空间的数据库附近的康师傅尽快和基督教高房价和好几个房间和高房价和高房价巨化股份借古讽今和");
//        authorArticle2.setImgs(strings);
//        mSecondArticle.setAuthorArticle(authorArticle2);
//
//        strings.clear();
//
//        strings.add("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1553888185,2759920345&fm=173&app=25&f=JPEG?w=640&h=426&s=B1F0E9376872D19E81955DC203007030");
//        strings.add("https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=524274293,2410873114&fm=173&app=25&f=JPEG?w=640&h=427&s=7EE18B54B5106E7D1C973FE80300E03C");
//        strings.add("https://hiphotos.baidu.com/feed/pic/item/1e30e924b899a9014eda61d511950a7b0308f561.jpg");
//        AuthorArticle authorArticle3 = new AuthorArticle();
//        authorArticle3.setImgs(strings);
//        authorArticle3.setTitle("第一天哈哈哈哈哈哈哈");
//        mThirdArticle.setAuthorArticle(authorArticle3);
    }

    public void setSearchContent(String searchContent) {
        Api.cancel(TAG);
        Apic.requestSearchContent(Uri.encode(searchContent))
                .tag(TAG)
                .callback(new Callback2D<Resp<SearchContent>, SearchContent>() {
                    @Override
                    protected void onRespSuccessData(SearchContent data) {
                        mSearchContentLayout.setSearchContent(searchContent);
                        mSearchContentLayout.setSearchContentData(data);
                    }
                })
                .fireFreely();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
