package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.swipeload.RecycleViewSwipeLoadActivity;

import butterknife.BindView;

public class ReadHistoryActivity extends RecycleViewSwipeLoadActivity {

    @BindView(R.id.adsorb_text)
    TextView mAdsorbText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_read_history);

        initView();
    }

    @Override
    public View getContentView() {
        return null;
    }

    private void initView() {
//        mTitleBar.setTitle(R.string.read_history);

    }


    @Override
    public void onLoadMore() {

    }

    @Override
    public void onRefresh() {

    }

    public static  class ReadHistoryAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
