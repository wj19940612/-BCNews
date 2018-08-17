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
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.sbai.bcnews.R;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ProjectGrade;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;
import com.sbai.bcnews.utils.OnItemClickListener;
import com.sbai.bcnews.view.TitleBar;
import com.zcmrr.swipelayout.foot.LoadMoreFooterView;
import com.zcmrr.swipelayout.header.RefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProjectGradeActivity extends RecycleViewSwipeLoadActivity {
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.swipe_refresh_header)
    RefreshHeaderView mSwipeRefreshHeader;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_load_more_footer)
    LoadMoreFooterView mSwipeLoadMoreFooter;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.rootView)
    LinearLayout mRootView;

    private List<ProjectGrade> mProjectGradeList;
    private GradeAdapter mGradeAdapter;
    private int mPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_grade);
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
        mProjectGradeList = new ArrayList<>();
        mGradeAdapter = new GradeAdapter(this, mProjectGradeList, new OnItemClickListener<ProjectGrade>() {
            @Override
            public void onItemClick(ProjectGrade projectGrade, int position) {
                Log.e("zzz","zzz");
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mGradeAdapter);
    }

    private void loadData(boolean refresh){

    }

    private void updateData(boolean refresh, List<ProjectGrade> data) {
        if (data == null || data.size() == 0) {
            mSwipeToLoadLayout.setLoadMoreEnabled(false);
            refreshFoot(0);
            mGradeAdapter.notifyDataSetChanged();
            return;
        }
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
        if (size < Apic.DEFAULT_PAGE_SIZE) {
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
                mName.setText(projectGrade.getName());
                mType.setText(projectGrade.getType());
                mGrade.setText(projectGrade.getGrade());
                mTime.setText(projectGrade.getUpdateTime());

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
