package com.zcmrr.swipelayout.header;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshHeaderLayout;
import com.zcmrr.swipelayout.R;

/**
 * Created by ${wangJie} on 2018/1/25.
 */

public class RefreshHeaderView extends SwipeRefreshHeaderLayout {
    private ImageView ivArrow;

    private ImageView ivSuccess;

    private TextView tvRefresh;

    private ProgressBar progressBar;

    private int mHeaderHeight;

    private Animation rotateUp;

    private Animation rotateDown;

    private boolean rotated = false;

    public RefreshHeaderView(Context context) {
        this(context, null);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
        rotateUp = AnimationUtils.loadAnimation(context, R.anim.rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.rotate_down);


        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_header, this, false);

        addView(view);
        tvRefresh = view.findViewById(R.id.tvRefresh);
        ivArrow = view.findViewById(R.id.ivArrow);
        ivSuccess = view.findViewById(R.id.ivSuccess);
        progressBar = view.findViewById(R.id.progressbar);
    }


    @Override
    public void onRefresh() {
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        tvRefresh.setText(R.string.refreshing);
    }

    @Override
    public void onPrepare() {
        Log.d("TwitterRefreshHeader", "onPrepare()");
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivArrow.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
            ivSuccess.setVisibility(GONE);
            if (y > mHeaderHeight) {
                tvRefresh.setText(R.string.release_to_refresh);
                if (!rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (y < mHeaderHeight) {
                if (rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }

                tvRefresh.setText("SWIPE TO REFRESH");
            }
        }
    }

    @Override
    public void onRelease() {
        Log.d("TwitterRefreshHeader", "onRelease()");
    }

    @Override
    public void onComplete() {
        rotated = false;
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        tvRefresh.setText(R.string.refresh_complete);
    }

    @Override
    public void onReset() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        progressBar.setVisibility(GONE);
    }
}
