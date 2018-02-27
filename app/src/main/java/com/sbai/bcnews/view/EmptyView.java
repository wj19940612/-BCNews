package com.sbai.bcnews.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\2\1 0001.
 */

public class EmptyView extends RelativeLayout {
    @BindView(R.id.errorTxt)
    TextView mErrorTxt;
    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.btnRefresh)
    Button mBtnRefresh;
    @BindView(R.id.noData)
    TextView mNoData;

    private Context mContext;
    private OnRefreshButtonClickListener mOnRefreshButtonClickListener;

    public interface OnRefreshButtonClickListener {
        public void onRefreshClick();
    }

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.view_empty_error, this, true);
        ButterKnife.bind(this);
        setBackgroundColor(ContextCompat.getColor(mContext, R.color.background));
    }

    public void setRefreshButtonClickListener(OnRefreshButtonClickListener onRefreshButtonClickListener) {
        mOnRefreshButtonClickListener = onRefreshButtonClickListener;
    }

    public void setNoNet() {
        mErrorTxt.setVisibility(View.VISIBLE);
        mBtnRefresh.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);
    }

    public void setNoData(String tip) {
        mErrorTxt.setVisibility(View.GONE);
        mBtnRefresh.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
        mNoData.setText(tip);
    }


    @OnClick(R.id.btnRefresh)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRefresh:
                if (mOnRefreshButtonClickListener != null) {
                    mOnRefreshButtonClickListener.onRefreshClick();
                }
                break;
        }
    }
}
