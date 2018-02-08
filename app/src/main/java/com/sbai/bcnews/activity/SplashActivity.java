package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.utils.Launcher;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {

    private static final int SPLASH_PAGE_STAY_TIME = 1500;

    @BindView(R.id.bgSplash)
    ImageView mBgSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        mBgSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                goMain();
            }
        }, SPLASH_PAGE_STAY_TIME);

    }

    private void goMain() {
        Launcher.with(getActivity(), MainActivity.class).execute();
        supportFinishAfterTransition();
    }
}
