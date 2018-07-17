package com.sbai.bcnews.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.MyAttentionActivity;
import com.sbai.bcnews.activity.author.AuthorActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadFragment;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.utils.StrUtil;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.dialog.AttentionDialog;
import com.sbai.bcnews.view.search.SearchEditText;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AuthorSearchFragment extends RecycleViewSwipeLoadFragment {

    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;

    private Unbinder mBind;

    private int mPage;
    private String mSearchContent;
    private List<Author> mData;
    private AuthorAdapter mAuthorAdapter;

    private SearchEditText.OnSearchContentResultListener mSearchContentResultListener;


    public static AuthorSearchFragment newsInstance(String searchContent) {
        AuthorSearchFragment authorSearchFragment = new AuthorSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ExtraKeys.SEARCH_CONTENT, searchContent);
        authorSearchFragment.setArguments(bundle);
        return authorSearchFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchContent = getArguments().getString(ExtraKeys.SEARCH_CONTENT);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchEditText.OnSearchContentResultListener) {
            mSearchContentResultListener = (SearchEditText.OnSearchContentResultListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        requestAuthor(true);
    }

    private void initView() {
        mData = new ArrayList<>();
        mAuthorAdapter = new AuthorAdapter(getActivity(), mData, new AttentionFragment.OnItemClickListener() {
            @Override
            public void onItemClick(Author author) {
                Launcher.with(getActivity(), AuthorActivity.class).putExtra(ExtraKeys.ID, author.getId()).execute();
            }

            @Override
            public void onAttention(final Author newsAuthor, boolean isAttention, final int position) {
                if (isAttention) {
                    AttentionDialog.with(getActivity()).setOnSureClickListener(new AttentionDialog.OnClickListener() {
                        @Override
                        public void onClick() {
                            cancelOrAttention(false, newsAuthor, position);
                        }
                    }).setTitle(R.string.sure_cancel_attention).show();
                } else if (LocalUser.getUser().isLogin()) {
                    cancelOrAttention(true, newsAuthor, position);
                } else {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                }
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAuthorAdapter);
        mSwipeToLoadLayout.setLoadMoreEnabled(false);
    }

    @Override
    public void onLoadMore() {
        requestAuthor(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        requestAuthor(true);
    }

    private void cancelOrAttention(final boolean isAttention, final Author selectAuthor, final int selectPosition) {
        int type = isAttention ? 1 : 0;
        Apic.requestConcernAuthor(selectAuthor.getId(), type).tag(TAG).callback(new Callback<Resp>() {
            @Override
            protected void onRespSuccess(Resp resp) {
                if (!isAttention) {
                    selectAuthor.setIsConcern(0);
                } else {
                    selectAuthor.setIsConcern(1);
                }
                mAuthorAdapter.notifyItemChanged(selectPosition);
            }
        }).fire();
    }

    public void setSearchContent(String searchContent) {
        mSearchContent = searchContent;
        requestAuthor(true);
    }

    private void requestAuthor(final boolean isRefresh) {
        if (!TextUtils.isEmpty(mSearchContent)) {
            String searchContent = Uri.encode(mSearchContent);
            Apic.requestSearchAuthor(searchContent, mPage).tag(TAG).callback(new Callback<ListResp<Author>>() {
                @Override
                protected void onRespSuccess(ListResp<Author> resp) {
                    updateData(mSearchContent, resp.getListData(), isRefresh);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    stopFreshOrLoadAnimation();
                }

            }).fireFreely();
        }else {
            refreshSuccess();
            stopFreshOrLoadAnimation();
        }
    }

    private void updateData(String searchContent, List<Author> data, boolean isRefresh) {
        if (isRefresh) {
            mData.clear();
        }
        if (TextUtils.isEmpty(searchContent) || data == null) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            mAuthorAdapter.notifyDataSetChanged();
            return;
        }

        if (mSearchContentResultListener != null) {
            mSearchContentResultListener.onSearchFinish(searchContent, data);
        }

        mAuthorAdapter.setSearchContent(searchContent);
        mData.addAll(data);
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0) {
            mPage++;
        }
//        if (mData.size() >= Apic.DEFAULT_PAGE_SIZE && data.size() < Apic.DEFAULT_PAGE_SIZE) {
//            mAuthorAdapter.showFooterView(true);
//        } else {
//            mAuthorAdapter.showFooterView(false);
//        }
        mAuthorAdapter.notifyDataSetChanged();
    }

    static class AuthorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private AttentionFragment.OnItemClickListener mOnItemClickListener;
        private Context mContext;
        private List<Author> mNewsAuthorList;
        private String mSearchContent;

        public AuthorAdapter(Context context, List<Author> newsAuthorList, AttentionFragment.OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
            mContext = context;
            mNewsAuthorList = newsAuthorList;
        }

        public void setSearchContent(String searchContent) {
            mSearchContent = searchContent;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_attention, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bindingData(mContext, mNewsAuthorList.get(position), position, mOnItemClickListener, mSearchContent);
        }

        @Override
        public int getItemCount() {
            return mNewsAuthorList == null ? 0 : mNewsAuthorList.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.head)
            HasLabelLayout mHead;
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.introduce)
            TextView mIntroduce;
            @BindView(R.id.attentionBtn)
            TextView mAttentionBtn;
            @BindView(R.id.rootView)
            RelativeLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(Context context, final Author newsAuthor, final int position, final AttentionFragment.OnItemClickListener onItemClickListener, String searchContent) {
                mHead.setImageSrc(newsAuthor.getUserPortrait());
                if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_OFFICIAL) {
                    mHead.setLabelImageViewVisible(true);
                    mHead.setLabelSelected(true);
                } else if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_SPECIAL) {
                    mHead.setLabelImageViewVisible(true);
                    mHead.setLabelSelected(false);
                } else {
                    mHead.setLabelImageViewVisible(false);
                }

                mName.setText(StrUtil.changeSpecialTextColor(newsAuthor.getUserName().trim(), searchContent, ContextCompat.getColor(context, R.color.colorPrimary)));
                if (!LocalUser.getUser().isLogin()) {
                    setNoAttentionBtn(mAttentionBtn, false, context);
                } else {
                    setNoAttentionBtn(mAttentionBtn, newsAuthor.getIsConcern() > 0, context);
                }

                if (!TextUtils.isEmpty(newsAuthor.getAuthInfo().trim())) {
                    mIntroduce.setText(newsAuthor.getAuthInfo().trim());
                } else {
                    mIntroduce.setText("");
                }

                mAttentionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onAttention(newsAuthor, newsAuthor.getIsConcern() > 0, position);
                        }
                    }
                });

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(newsAuthor);
                        }
                    }
                });
            }

            private void setNoAttentionBtn(TextView attentionBtn, boolean hasAttention, Context context) {
                attentionBtn.setSelected(hasAttention);
                if (hasAttention) {
                    attentionBtn.setTextColor(ContextCompat.getColor(context, R.color.text_d9));
                    attentionBtn.setText(R.string.has_attention);
                } else {
                    attentionBtn.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    attentionBtn.setText(R.string.attention);
                }
            }
        }
    }
}
