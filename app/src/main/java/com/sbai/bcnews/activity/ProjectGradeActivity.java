package com.sbai.bcnews.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback;
import com.sbai.bcnews.http.ListResp;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ProjectGrade;
import com.sbai.bcnews.swipeload.BaseSwipeLoadActivity;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.DateUtil;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectGradeActivity extends BaseSwipeLoadActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    RelativeLayout mRootView;
    @BindView(R.id.emptyView)
    LinearLayout mEmptyView;
    @BindView(R.id.swipe_target)
    RelativeLayout mSwipeTarget;

    private List<ProjectGrade> mProjectGradeList;
    private GradeAdapter mGradeAdapter;
    private int mPage;

    protected RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (!recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                    triggerLoadMore();
                }

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                boolean isTop = layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
                if(!isTop){
                    mSwipeToLoadLayout.setRefreshEnabled(false);
                }else{
                    mSwipeToLoadLayout.setRefreshEnabled(true);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_grade);
        ButterKnife.bind(this);
        initView();
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        loadData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @NonNull
    @Override
    public Object getSwipeTargetView() {
        return mSwipeTarget;
    }

    @NonNull
    @Override
    public SwipeToLoadLayout getSwipeToLoadLayout() {
        return mSwipeToLoadLayout;
    }

    @NonNull
    @Override
    public RefreshHeaderView getRefreshHeaderView() {
        return mSwipeRefreshHeader;
    }

    @NonNull
    @Override
    public LoadMoreFooterView getLoadMoreFooterView() {
        return mSwipeLoadMoreFooter;
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
        mProjectGradeList = new ArrayList<>();
        mGradeAdapter = new GradeAdapter(this, mProjectGradeList, new OnItemClickListener<ProjectGrade>() {
            @Override
            public void onItemClick(ProjectGrade projectGrade, int position) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mGradeAdapter);
    }

    private void loadData(boolean refresh) {
        Apic.requestProjectGrade(mPage).tag(TAG).callback(new Callback<ListResp<ProjectGrade>>() {
            @Override
            protected void onRespSuccess(ListResp<ProjectGrade> resp) {
                updateData(resp.getListData(), refresh);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                stopFreshOrLoadAnimation();
            }
        }).fireFreely();
    }

    private void updateData(List<ProjectGrade> data, boolean refresh) {
        if (data == null || data.size() == 0) {
            if (refresh) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            refreshFoot(0);
            mGradeAdapter.notifyDataSetChanged();
            return;
        }
        mEmptyView.setVisibility(View.GONE);
        if (refresh) {
            mProjectGradeList.clear();
        }
        if (data.size() < Apic.DEFAULT_PAGE_SIZE) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
        } else {
            mSwipeToLoadLayout.setLoadMoreEnabled(true);
        }
        if (data.size() != 0)
            mPage++;
        mProjectGradeList.addAll(data);
        refreshFoot(data.size());
        mGradeAdapter.notifyDataSetChanged();
    }

    private void refreshFoot(int size) {
        if (mProjectGradeList.size() >= Apic.DEFAULT_PAGE_SIZE && size < Apic.DEFAULT_PAGE_SIZE) {
            mGradeAdapter.setShowFoot(true);
        } else {
            mGradeAdapter.setShowFoot(false);
        }
    }

    public static class GradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private List<ProjectGrade> mProjectGrades;
        private boolean mShowFoot;
        private OnItemClickListener<ProjectGrade> mOnItemClickListener;

        public GradeAdapter(Context context, List<ProjectGrade> projectGrades, OnItemClickListener<ProjectGrade> onItemClickListener) {
            mContext = context;
            mProjectGrades = projectGrades;
            mOnItemClickListener = onItemClickListener;
        }

        public void setShowFoot(boolean isShowFoot) {
            mShowFoot = isShowFoot;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_project_grade, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ((ViewHolder) holder).bind(mProjectGrades.get(position), position, getItemCount(), mShowFoot, mOnItemClickListener);
        }

        @Override
        public int getItemCount() {
            return mProjectGrades.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.name)
            TextView mName;
            @BindView(R.id.type)
            TextView mType;
            @BindView(R.id.grade)
            TextView mGrade;
            @BindView(R.id.time)
            TextView mTime;
            @BindView(R.id.footer)
            LinearLayout mFooter;
            @BindView(R.id.rootView)
            LinearLayout mRootView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

            public void bind(ProjectGrade projectGrade, int position, int itemCount, boolean showFoot, OnItemClickListener<ProjectGrade> onItemClickListener) {
                mName.setText(projectGrade.getProjectName());
                mType.setText(projectGrade.getTypeStr());
                mGrade.setText(projectGrade.getGradeStr());
                mTime.setText(DateUtil.getFormatSpecialSlashNoHour(projectGrade.getUpdateTime()));

                mRootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(projectGrade, position);
                        }
                    }
                });

                if (showFoot && itemCount - 1 == position) {
                    mFooter.setVisibility(View.VISIBLE);
                } else {
                    mFooter.setVisibility(View.GONE);
                }
            }
        }
    }
}
