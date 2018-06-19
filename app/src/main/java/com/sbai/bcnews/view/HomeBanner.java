package com.sbai.bcnews.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.Banner;
import com.sbai.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeBanner extends FrameLayout {

    @BindView(R.id.viewPager)
    InfiniteViewPager mViewPager;
    @BindView(R.id.pageIndicator)
    PageIndicator mPageIndicator;

    private AdvertisementAdapter mAdapter;
    private int mInnerCounter;
    private boolean mIsRectIndicator;

    public interface OnViewClickListener {
        void onBannerClick(Banner information);
    }

    private OnViewClickListener mOnViewClickListener;

    public void setOnViewClickListener(OnViewClickListener onViewClickListener) {
        mOnViewClickListener = onViewClickListener;
    }

    public HomeBanner(Context context) {
        super(context);
        init();
    }

    public HomeBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttrs(attrs);
        init();
    }

    private void processAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HomeBanner);

        mIsRectIndicator = typedArray.getBoolean(R.styleable.HomeBanner_isRectIndicator, false);

        typedArray.recycle();
    }

    protected void init() {
        //需要方形的indicator
        if (mIsRectIndicator) {
            LayoutInflater.from(getContext()).inflate(R.layout.new_home_banner, this, true);
        } else {
            LayoutInflater.from(getContext()).inflate(R.layout.home_banner, this, true);
        }
        ButterKnife.bind(this);
        mInnerCounter = 1;
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        if (mViewPager != null) {
//            mViewPager.clearOnPageChangeListeners();
//        }
//    }

    public void detachFromWindow() {
        mViewPager.setHasDestroy(true);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mPageIndicator != null) {
                mPageIndicator.move(position);
            }

            onPageSwitched(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setInnerCounter(1);
            }
        }
    };

    protected void onPageSwitched(int position){

    }

    public void nextAdvertisement() {
        if (mAdapter != null && mAdapter.getCount() > 1) {
            //ViewPager还在窗口执行这个动作
            if (!mViewPager.isDetachFromWindow()) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

            }
        }
    }

    public int getInnerCounter() {
        return mInnerCounter;
    }

    public void setInnerCounter(int innerCounter) {
        mInnerCounter = innerCounter;
        if (mInnerCounter % 6 == 0) { // 5 seconds (innerCounter: 1~6)
            nextAdvertisement();
        }
    }

    public void setHomeAdvertisement(List<Banner> informationList) {
        if (informationList.size() == 0) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        filterEmptyInformation(informationList);

        if (!informationList.isEmpty()) {
            int size = informationList.size();
            if (size < 2) {
                mPageIndicator.setVisibility(INVISIBLE);
            } else {
                mPageIndicator.setVisibility(VISIBLE);
            }
            mPageIndicator.setCount(size);

            if (mAdapter == null) {
                mAdapter = new AdvertisementAdapter(getContext(), informationList, mOnViewClickListener);
                mViewPager.addOnPageChangeListener(mOnPageChangeListener);
                mViewPager.setAdapter(mAdapter);
            } else {
                mAdapter.setNewAdvertisements(informationList);
            }
        }
    }

    public void setHomeAdvertisement(List<Banner> informationList,int ImageStyle) {
        if (informationList.size() == 0) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        filterEmptyInformation(informationList);

        if (!informationList.isEmpty()) {
            int size = informationList.size();
            if (size < 2) {
                mPageIndicator.setVisibility(INVISIBLE);
            } else {
                mPageIndicator.setVisibility(VISIBLE);
            }
            mPageIndicator.setCount(size);

            if (mAdapter == null) {
                mAdapter = new AdvertisementAdapter(getContext(), informationList, mOnViewClickListener,ImageStyle);
                mViewPager.addOnPageChangeListener(mOnPageChangeListener);
                mViewPager.setAdapter(mAdapter);
            } else {
                mAdapter.setNewAdvertisements(informationList);
            }
        }
    }

    private void filterEmptyInformation(List<Banner> informationList) {
        List<Banner> removeList = new ArrayList<>();
        for (int i = 0; i < informationList.size(); i++) {
            Banner information = informationList.get(i);
            if (TextUtils.isEmpty(information.getCover())) {
                removeList.add(information);
            }
        }
        for (int i = 0; i < removeList.size(); i++) {
            informationList.remove(removeList.get(i));
        }
    }

    public static class AdvertisementAdapter extends PagerAdapter {

        public static final int IMAGE_CENTER_INSIDE = 0;
        public static final int IMAGE_CENTER_CROP = 1;

        private List<Banner> mList;
        private Context mContext;
        private OnViewClickListener mListener;
        private int mImageStyle ;

        public AdvertisementAdapter(Context context, List<Banner> informationList, OnViewClickListener listener) {
            mContext = context;
            mList = informationList;
            mListener = listener;
        }

        public AdvertisementAdapter(Context context, List<Banner> informationList, OnViewClickListener listener,int imageStyle) {
            mContext = context;
            mList = informationList;
            mListener = listener;
            mImageStyle = imageStyle;
        }

        public void setNewAdvertisements(List<Banner> informationList) {
            mList = informationList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int pos = position;
            ImageView imageView = new ImageView(mContext);
            final Banner information = mList.get(pos);

            container.addView(imageView);
            if (!TextUtils.isEmpty(information.getCover())) {
                if(mImageStyle == IMAGE_CENTER_INSIDE){
                    GlideApp.with(mContext).load(information.getCover())
                            .centerInside().into(imageView);
                }else{
                    GlideApp.with(mContext).load(information.getCover())
                            .centerCrop().into(imageView);
                }
            }
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onBannerClick(information);
                    }
                }
            });
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
