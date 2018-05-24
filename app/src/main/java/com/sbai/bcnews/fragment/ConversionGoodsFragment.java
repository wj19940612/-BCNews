package com.sbai.bcnews.fragment;

import android.app.Dialog;
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
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.BindingAddressActivity;
import com.sbai.bcnews.activity.ConversionResultActivity;
import com.sbai.bcnews.fragment.dialog.BottomDialogFragment;
import com.sbai.bcnews.http.Apic;
import com.sbai.bcnews.http.Callback2D;
import com.sbai.bcnews.http.Resp;
import com.sbai.bcnews.model.ConversionAddress;
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
    @BindView(R.id.bindingAddress)
    TextView mBindingAddress;
    @BindView(R.id.bottomLayout)
    LinearLayout mBottomLayout;

    private Unbinder mBind;

    private PagerAdapter mPagerAdapter;

    private TextView[] mTabViews;

    private ConversionAddress mConversionAddress;
    private int mPosition;
    private boolean mCreated;

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
        mCreated = true;
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
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
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                updateConversionAddress(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setCurrentItem(0, false);
    }

    private void initData() {
        mTabViews = new TextView[]{mDigitalCurrency, mAliPay, mTelephoneCharge, mConversionDetail};
        setTabClick(mDigitalCurrency);
    }

    private void loadData() {
        Apic.requestConversionAddress().tag(TAG).callback(new Callback2D<Resp<ConversionAddress>, ConversionAddress>() {
            @Override
            protected void onRespSuccessData(ConversionAddress data) {
                mConversionAddress = data;
                updateConversionAddress(mViewPager.getCurrentItem());
            }
        }).fireFreely();
    }

    private void updateConversionAddress(int page) {
        if (mConversionAddress != null) {
            switch (page) {
                case PAGE_DIGITAL_COIN:
                    mBottomLayout.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(mConversionAddress.getExtractCoinAddress())) {
                        mRechargeBtn.setEnabled(false);
                        mMentionCoinAddress.setVisibility(View.GONE);
                        mModifyBtn.setVisibility(View.GONE);
                        mBindingAddress.setVisibility(View.VISIBLE);
                    } else {
                        mRechargeBtn.setEnabled(true);
                        mMentionCoinAddress.setVisibility(View.VISIBLE);
                        mModifyBtn.setVisibility(View.VISIBLE);
                        mBindingAddress.setVisibility(View.GONE);
                        mMentionCoinAddress.setText(getString(R.string.get_coin_address_x, getString(R.string.have_binding)));
                    }
                    break;
                case PAGE_ALIPAY:
                    mBottomLayout.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(mConversionAddress.getAliPay())) {
                        mRechargeBtn.setEnabled(false);
                        mMentionCoinAddress.setVisibility(View.GONE);
                        mModifyBtn.setVisibility(View.GONE);
                        mBindingAddress.setVisibility(View.VISIBLE);
                    } else {
                        mRechargeBtn.setEnabled(true);
                        mMentionCoinAddress.setVisibility(View.VISIBLE);
                        mModifyBtn.setVisibility(View.VISIBLE);
                        mBindingAddress.setVisibility(View.GONE);
                        mMentionCoinAddress.setText(getString(R.string.ali_pay_address_x, mConversionAddress.getAliPay()));
                    }
                    break;
                case PAGE_TELEPHONE_CHARGE:
                    mBottomLayout.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(mConversionAddress.getPhone())) {
                        mRechargeBtn.setEnabled(false);
                        mMentionCoinAddress.setVisibility(View.GONE);
                        mModifyBtn.setVisibility(View.GONE);
                        mBindingAddress.setVisibility(View.VISIBLE);
                    } else {
                        mRechargeBtn.setEnabled(true);
                        mMentionCoinAddress.setVisibility(View.VISIBLE);
                        mModifyBtn.setVisibility(View.VISIBLE);
                        mBindingAddress.setVisibility(View.GONE);
                        mMentionCoinAddress.setText(getString(R.string.ali_pay_address_x, mConversionAddress.getPhone()));
                    }
                    break;
                case PAGE_CONVERSION_DETAIL:
                    mBottomLayout.setVisibility(View.GONE);
                    break;
            }
        }
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

    @OnClick({R.id.conversionDetail, R.id.digitalCurrency, R.id.aliPay, R.id.telephoneCharge, R.id.modifyBtn, R.id.bindingAddress})
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
            case R.id.modifyBtn:
                Launcher.with(getActivity(), BindingAddressActivity.class).putExtra(ExtraKeys.BINDING_TYPE, mPosition).execute();
                break;
            case R.id.bindingAddress:
                Launcher.with(getActivity(), BindingAddressActivity.class).putExtra(ExtraKeys.BINDING_TYPE, mPosition).execute();
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
            } else {
                mViewPager.setCurrentItem(i, false);
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
            if (position == PAGE_CONVERSION_DETAIL) {
                return ConversionContentFragment.newsInstance(PAGE_DIGITAL_COIN);
            } else {
                return ConversionContentFragment.newsInstance(position);
            }
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
