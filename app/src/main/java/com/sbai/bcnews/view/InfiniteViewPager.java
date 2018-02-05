package com.sbai.bcnews.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.utils.ToastUtil;

public class InfiniteViewPager extends ViewPager {

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    /**
     * 创建点击事件接口
     * @author wanpg
     *
     */
    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }

    private InnerAdapter mInnerAdapter;

    public InfiniteViewPager(Context context) {
        super(context);
    }

    public InfiniteViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mInnerAdapter = new InnerAdapter(adapter);
        super.setAdapter(mInnerAdapter);
        setCurrentItem(1);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(new InnerPageChangeListener(listener));
    }

    private class InnerPageChangeListener implements OnPageChangeListener {

        private OnPageChangeListener mExternalListener;
        private int mInnerPosition;

        public InnerPageChangeListener(OnPageChangeListener externalListener) {
            mExternalListener = externalListener;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int pos = calExternalPosition(position, mInnerAdapter);
            if (mExternalListener != null) {
                mExternalListener.onPageScrolled(pos, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            mInnerPosition = position;
            int pos = calExternalPosition(position, mInnerAdapter);
            if (mExternalListener != null) {
                mExternalListener.onPageSelected(pos); // FIXME: 15/11/2017 this will be called twice, since setCurrentItem to real item.
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mExternalListener != null) {
                mExternalListener.onPageScrollStateChanged(state);
            }

            if (state == ViewPager.SCROLL_STATE_IDLE) { // When viewpager is settled
                if (mInnerPosition == 0) { // fake obj: head, move to last second
                    setCurrentItem(mInnerAdapter.getCount() - 2, false);
                } else if (mInnerPosition == mInnerAdapter.getCount() - 1) { // fake obj: tail, move to the second
                    setCurrentItem(1, false);
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //每次进行onTouch事件都记录当前的按下的坐标
        curP.x = arg0.getX();
        curP.y = arg0.getY();

        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
            //记录按下时候的坐标
            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
            downP.x = arg0.getX();
            downP.y = arg0.getY();
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
            getParent().requestDisallowInterceptTouchEvent(true);
        }

        if(arg0.getAction() == MotionEvent.ACTION_UP){
            //在up时判断是否按下和松手的坐标为一个点
            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
            if(downP.x==curP.x && downP.y==curP.y){
                onSingleTouch();
                return true;
            }
        }

        return super.onTouchEvent(arg0);
    }

    /**
     * 单击
     */
    public void onSingleTouch() {
        Log.e("zzz","dianji");
        if (onSingleTouchListener!= null) {
            onSingleTouchListener.onSingleTouch();
        }
    }

    private class InnerAdapter extends PagerAdapter {

        private PagerAdapter mExternalAdapter;

        public InnerAdapter(PagerAdapter adapter) {
            mExternalAdapter = adapter;
            mExternalAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    notifyDataSetChanged();
                }
            });
        }

        public PagerAdapter getExternalAdapter() {
            return mExternalAdapter;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            if (mExternalAdapter.getCount() > 1) {
                return mExternalAdapter.getCount() + 2; // fake object at head and tail
            }
            return mExternalAdapter.getCount();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return mExternalAdapter.isViewFromObject(view, object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mExternalAdapter.destroyItem(container, position, object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = calExternalPosition(position, this);
            return mExternalAdapter.instantiateItem(container, position);
        }
    }

    private int calExternalPosition(int innerPos, InnerAdapter innerAdapter) {
        if (innerPos == 0) { // first -> external last
            return innerAdapter.getExternalAdapter().getCount() - 1;
        } else if (innerPos == innerAdapter.getCount() - 1) { // last -> external 0
            return 0;
        } else {
            return innerPos - 1;
        }
    }
}
