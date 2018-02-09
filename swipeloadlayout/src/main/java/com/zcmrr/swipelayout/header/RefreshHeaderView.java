package com.zcmrr.swipelayout.header;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.zcmrr.swipelayout.R;

/**
 * Created by ${wangJie} on 2018/1/25.
 */

public class RefreshHeaderView extends SwipeRefreshHeaderLayout {

    private static final String TAG = "RefreshHeaderView";
    private ImageView ivArrow;

    private TextView tvRefresh;


    private int mHeaderHeight;

    private boolean rotated = false;

    private CharSequence mRefreshCompleteText;

    private OnStartRefreshListener mOnStartRefreshListener;

    public interface OnStartRefreshListener{
        public void onStartRefresh();
    }

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_header, this, false);

        tvRefresh = view.findViewById(R.id.tvRefresh);
        ivArrow = view.findViewById(R.id.ivArrow);
        addView(view);
        mRefreshCompleteText = getContext().getString(R.string.refresh_complete);
    }

    public void setStartRefreshListener(OnStartRefreshListener onStartRefreshListener){
        mOnStartRefreshListener = onStartRefreshListener;
    }


    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        ivArrow.clearAnimation();

        ivArrow.setImageResource(R.drawable.refreshing);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivArrow.getDrawable();
        animationDrawable.start();

        tvRefresh.setVisibility(VISIBLE);
        tvRefresh.setText(R.string.refreshing);
    }

    @Override
    public void onPrepare() {
        Log.d(TAG, "onPrepare()");
        reset();
        ivArrow.setImageResource(R.drawable.refresh_start);
        AnimationDrawable refreshStartDrawable = (AnimationDrawable) ivArrow.getDrawable();
        refreshStartDrawable.start();
        if(mOnStartRefreshListener!=null){
            mOnStartRefreshListener.onStartRefresh();
        }
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            if (y > mHeaderHeight) {
                if (!rotated) {
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    rotated = false;
                }

            }
        }
    }

    @Override
    public void onRelease() {
        Log.d(TAG, "onRelease: ");
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
        rotated = false;
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        tvRefresh.setText(mRefreshCompleteText);
    }

    @Override
    public void onReset() {
        Log.d(TAG, "onReset: ");
        rotated = false;
        reset();
    }

    private void reset() {
        ivArrow.setVisibility(VISIBLE);
        mRefreshCompleteText = "";
        tvRefresh.setVisibility(INVISIBLE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ivArrow.clearAnimation();
    }

    public void setRefreshCompleteText(@StringRes int resid) {
        setRefreshCompleteText(getContext().getString(resid));
    }

    public void setRefreshCompleteText(CharSequence text) {
        mRefreshCompleteText = text;
        tvRefresh.setText(mRefreshCompleteText);
    }

    public void refreshFail() {
        setRefreshCompleteText(R.string.refresh_fail);
    }

    public void refreshFail(String freshFailMsg) {
        setRefreshCompleteText(freshFailMsg);
    }

    public void refreshSuccess() {
        setRefreshCompleteText(R.string.refresh_complete);
    }

    public void refreshSuccess(String toast){
        setRefreshCompleteText(toast);
    }

    public void refreshSuccess(int toast){
        setRefreshCompleteText(toast);

    }
}
