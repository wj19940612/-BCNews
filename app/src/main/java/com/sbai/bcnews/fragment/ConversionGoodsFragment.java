package com.sbai.bcnews.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.ConversionResultActivity;
import com.sbai.bcnews.fragment.dialog.BottomDialogFragment;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.ScrollableViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class ConversionGoodsFragment extends BottomDialogFragment {
    public static final int PAGE_DIGITAL_COIN = 0;
    public static final int PAGE_ALIPAY = 1;
    public static final int PAGE_TELEPHONE_CHARGE = 2;
    public static final int PAGE_CONVERSION_DETAIL = 3;

    public static final int TAB_COUNT = 4;

    public static final int REQUEST_CODE_CONVERSION = 108;

    @BindView(R.id.digitalCurrency)
    TextView mDigitalCurrency;
    @BindView(R.id.aliPay)
    TextView mAliPay;
    @BindView(R.id.telephoneCharge)
    TextView mTelephoneCharge;
    @BindView(R.id.conversionDetail)
    TextView mConversionDetail;
    @BindView(R.id.viewPager)
    ScrollableViewPager mViewPager;
    @BindView(R.id.mentionCoinAddress)
    TextView mMentionCoinAddress;
    @BindView(R.id.modifyBtn)
    TextView mModifyBtn;
    @BindView(R.id.rechargeBtn)
    TextView mRechargeBtn;

    private Unbinder mBind;

    private PagerAdapter mPagerAdapter;

    private TextView[] mTabViews;

    public static ConversionGoodsFragment newInstance() {
        ConversionGoodsFragment conversionGoodsFragment = new ConversionGoodsFragment();
        return conversionGoodsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_conversion, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    private void initView() {
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.setOffscreenPageLimit(TAB_COUNT - 1);
        mViewPager.setScrollable(false);
        mViewPager.setCurrentItem(0, false);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void initData() {
        mTabViews = new TextView[]{mDigitalCurrency, mAliPay, mTelephoneCharge, mConversionDetail};
        setTabClick(mDigitalCurrency);
    }

    private void gotoResultActivity() {
        Launcher.with(ConversionGoodsFragment.this, ConversionResultActivity.class).excuteForResultFragment(REQUEST_CODE_CONVERSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CONVERSION && resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(PAGE_CONVERSION_DETAIL);
        }
    }

    @OnClick({R.id.conversionDetail, R.id.digitalCurrency, R.id.aliPay, R.id.telephoneCharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.digitalCurrency:
                setTabClick(mDigitalCurrency);
                break;
            case R.id.aliPay:
                setTabClick(mAliPay);
                break;
            case R.id.telephoneCharge:
                setTabClick(mTelephoneCharge);
                break;
            case R.id.conversionDetail:
                setTabClick(mConversionDetail);
                break;
        }
    }

    private void setTabClick(TextView clickView) {
        clickView.setSelected(true);
        clickView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        for (int i = 0; i < mTabViews.length; i++) {
            if (mTabViews[i] != clickView) {
                mTabViews[i].setSelected(false);
                mTabViews[i].setTextColor(ContextCompat.getColor(getContext(), R.color.text97));
            }
        }
    }

    public static class PagerAdapter extends FragmentPagerAdapter {

        private FragmentManager mFragmentManager;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
        }

        @Override
        public Fragment getItem(int position) {
            return ConversionContentFragment.newsInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        public Fragment getFragment(int position) {
            return mFragmentManager.findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + position);
        }
    }
}
