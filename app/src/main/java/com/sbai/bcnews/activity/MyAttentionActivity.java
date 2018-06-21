package com.sbai.bcnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.fragment.AttentionFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.LocalUser;
import com.sbai.bcnews.model.author.Author;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.EmptyView;
import com.sbai.bcnews.view.HasLabelLayout;
import com.sbai.bcnews.view.TitleBar;
import com.sbai.bcnews.view.dialog.AttentionDialog;
import com.sbai.glide.GlideApp;
import com.sbai.httplib.ReqError;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAttentionActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;

    private AttentionAdapter mAttentionAdapter;
    private List<Author> mNewsAuthorList;

    private int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_attention);
        ButterKnife.bind(this);
        initView();
        loadData(true);
    }

    @Override
    public View getContentView() {
        return mRootView;
    }

    @Override
    public void onLoadMore() {
        loadData(false);
    }

    @Override
    public void onRefresh() {
        mPage = 0;
        loadData(true);
    }

    private void initView() {
        mNewsAuthorList = new ArrayList<>();
        mAttentionAdapter = new AttentionAdapter(getActivity(), mNewsAuthorList, new AttentionFragment.OnItemClickListener() {
            @Override
            public void onItemClick() {

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
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mAttentionAdapter);
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
                mAttentionAdapter.notifyItemChanged(selectPosition);
            }
        }).fire();
    }


    private void loadData(final boolean refresh) {
        Apic.requestAttentionAuthor().tag(TAG).callback(new Callback2D<Resp<List<Author>>, List<Author>>() {
            @Override
            protected void onRespSuccessData(List<Author> data) {
                updateData(data, refresh);
            }
        }).fireFreely();
    }

    private void updateData(List<Author> data, boolean refresh) {
        if (refresh) {
            mNewsAuthorList.clear();
        }
        if (data == null && data.size() == 0) {
            if (mNewsAuthorList.size() > 0) {
                mEmptyView.setVisibility(View.GONE);
            } else {
                mEmptyView.setVisibility(View.VISIBLE);
            }
            mAttentionAdapter.notifyDataSetChanged();
            return;
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mNewsAuthorList.addAll(data);
        mAttentionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    static class AttentionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private AttentionFragment.OnItemClickListener mOnItemClickListener;
        private Context mContext;
        private List<Author> mNewsAuthorList;

        public AttentionAdapter(Context context, List<Author> newsAuthorList, AttentionFragment.OnItemClickListener onItemClickListener) {
            mOnItemClickListener = onItemClickListener;
            mContext = context;
            mNewsAuthorList = newsAuthorList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_attention, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bindingData(mContext, mNewsAuthorList.get(position), position, mOnItemClickListener);
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

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            private void bindingData(Context context, final Author newsAuthor, final int position, final AttentionFragment.OnItemClickListener onItemClickListener) {
                mHead.setImageSrc(newsAuthor.getUserPortrait());
                if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_OFFICIAL) {
                    mHead.setLabelSelected(true);
                } else if (newsAuthor.getRankType() == Author.AUTHOR_STATUS_SPECIAL) {
                    mHead.setLabelSelected(false);
                } else {
                    mHead.setLabelImageViewVisible(false);
                }

                mName.setText(newsAuthor.getUserName());
                if (!LocalUser.getUser().isLogin()) {
                    setNoAttentionBtn(mAttentionBtn, false, context);
                } else {
                    setNoAttentionBtn(mAttentionBtn, newsAuthor.getIsConcern() > 0, context);
                }
                mAttentionBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onAttention(newsAuthor, newsAuthor.getIsConcern() > 0, position);
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
