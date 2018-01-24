package com.sbai.bcnews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.zcmrr.swipelayout.GoogleCircleProgressView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RecycleViewFragment extends Fragment {


    @BindView(R.id.googleProgress)
    GoogleCircleProgressView mGoogleProgress;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    //    @BindView(R.id.googleProgress)
//    GoogleCircleProgressView mGoogleProgress;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout mSwipeToLoadLayout;
    private Unbinder mBind;
    private ArrayList<String> mStrings;
    private RecycleViewAdapter mRecycleViewAdapter;

    private int a;

    public RecycleViewFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_view, container, false);
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

        mStrings = new ArrayList<>();
        mRecycleViewAdapter = new RecycleViewAdapter(mStrings, getActivity());
        mSwipeTarget.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeTarget.setAdapter(mRecycleViewAdapter);

        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        a = 0;
                        mSwipeToLoadLayout.setRefreshing(false);

                        mRecycleViewAdapter.clear();
                        for (int i = 0; i < 20; i++) {
                            a++;
                            mStrings.add("第 " + a + " 个数据");
                        }
                        mRecycleViewAdapter.addAll(mStrings);
                    }
                }, 200);
            }
        });

        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 20; i++) {
                            a++;
                            mRecycleViewAdapter.add("第 " + a + " 个数据");
                        }

                        mSwipeToLoadLayout.setLoadingMore(false);
                    }
                }, 2000);
            }
        });

        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        mSwipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
    }


    static class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

        private ArrayList<String> mData;
        private Context mContext;

        public RecycleViewAdapter(ArrayList<String> data, Context context) {
            mData = data;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.row_simple_recycleview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String s = mData.get(position);
            holder.mText.setText(s);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        public void clear() {
            mData.clear();
            notifyDataSetChanged();
        }

        public void addAll(ArrayList<String> strings) {
            mData.addAll(strings);
            notifyDataSetChanged();
        }

        public void add(String s) {
            mData.add(s);
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.text)
            TextView mText;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
