package com.sbai.bcnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.model.ChannelCacheModel;
import com.sbai.bcnews.utils.news.ChannelAdapter;
import com.sbai.bcnews.utils.news.ChannelCache;
import com.sbai.bcnews.utils.news.ItemDragHelperCallback;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ChannelActivity extends AppCompatActivity {

    public static final int SPAN_COUNT = 4;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.finish)
    ImageView mFinish;

    private ChannelAdapter mChannelAdapter;

    private ChannelCacheModel mChannelCacheModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mChannelCacheModel != null && mChannelCacheModel.getMyChannelEntities() != null && mChannelCacheModel.getMyChannelEntities().size() != 0)
            ChannelCache.modifyChannel(mChannelCacheModel.getMyChannelEntities(), mChannelCacheModel.getOtherChannelEntities());
    }

    private void initData() {
        mChannelCacheModel = getIntent().getParcelableExtra(ExtraKeys.CHANNEL);
        if (mChannelCacheModel.getOtherChannelEntities() == null) {
            mChannelCacheModel.setOtherChannelEntities(new ArrayList<String>());
        }
    }

    private void initView() {

        GridLayoutManager manager = new GridLayoutManager(this, SPAN_COUNT);
        mRecyclerView.setLayoutManager(manager);

        ItemDragHelperCallback callback = new ItemDragHelperCallback();
        final ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(mRecyclerView);

        mChannelAdapter = new ChannelAdapter(this, helper, mChannelCacheModel);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = mChannelAdapter.getItemViewType(position);
                return viewType == ChannelAdapter.TYPE_MY || viewType == ChannelAdapter.TYPE_OTHER ? 1 : SPAN_COUNT;
            }
        });
        mRecyclerView.setAdapter(mChannelAdapter);
    }

    @OnClick({R.id.finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish:
                returnActivityModel();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        returnActivityModel();
    }

    private void returnActivityModel() {
        Intent intent = new Intent();
        if (mChannelCacheModel != null) {
            intent.putExtra(ExtraKeys.CHANNEL, mChannelCacheModel);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
