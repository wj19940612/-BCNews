package com.sbai.bcnews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.fragment.swipeload.BaseDefaultSwipeLoadFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作为SwipeToLoadLayout的使用例子 后期删除
 */

public class RecycleViewFragment extends BaseDefaultSwipeLoadFragment {


    private ArrayList<String> mStrings;
    private RecycleViewAdapter mRecycleViewAdapter;

    int a;

    public RecycleViewFragment() {
        super(SWIPE_TARGET_RECYCLE_VIEW);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mStrings = new ArrayList<>();
        mRecycleViewAdapter = new RecycleViewAdapter(mStrings, getActivity());
        ((RecyclerView) mSwipeTargetView).setLayoutManager(new LinearLayoutManager(getActivity()));
        ((RecyclerView) mSwipeTargetView).setAdapter(mRecycleViewAdapter);
    }


    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    a++;
                    mRecycleViewAdapter.add("第 " + a + " 个数据");
                }

                stopFreshOrLoadAnimation();

            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        mSwipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                a = 0;

                stopFreshOrLoadAnimation();
                mRecycleViewAdapter.clear();
                for (int i = 0; i < 20; i++) {
                    a++;
                    mStrings.add("第 " + a + " 个数据");
                }
                mRecycleViewAdapter.addAll(mStrings);
            }
        }, 200);
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
            View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
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
            @BindView(android.R.id.text1)
            TextView mText;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
