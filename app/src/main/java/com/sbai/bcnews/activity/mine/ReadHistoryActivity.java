package com.sbai.bcnews.activity.mine;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

        initView();
    }

    private void initView() {
        mTitleBar.setTitle(R.string.read_history);


    }


    @Override
    protected int getContentViewId() {
        return R.layout.activity_read_history;
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
