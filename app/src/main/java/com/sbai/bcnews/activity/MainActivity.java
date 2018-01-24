package com.sbai.bcnews.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.sbai.bcnews.R;
import com.sbai.bcnews.RecycleViewFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // TODO: 2018/1/23 测试数据

        MainFragmentPager mainFragmentPager = new MainFragmentPager(getSupportFragmentManager());
        mViewPager.setAdapter(mainFragmentPager);

    }


    static class MainFragmentPager extends FragmentPagerAdapter {

        public MainFragmentPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (0) {
                case 0:
                    return new RecycleViewFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
