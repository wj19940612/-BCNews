package com.sbai.bcnews.utils.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.ChannelCacheModel;
import com.sbai.bcnews.model.ChannelEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 拖拽排序 + 增删
 */
public class ChannelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnItemMoveListener {
    // 我的频道 标题部分
    public static final int TYPE_MY_CHANNEL_HEADER = 0;
    // 我的频道
    public static final int TYPE_MY = 1;
    // 其他频道 标题部分
    public static final int TYPE_OTHER_CHANNEL_HEADER = 2;
    // 其他频道
    public static final int TYPE_OTHER = 3;

    // 我的频道之前的header数量  该demo中 即标题部分 为 1
    private static final int COUNT_PRE_MY_HEADER = 1;
    // 其他频道之前的header数量  该demo中 即标题部分 为 COUNT_PRE_MY_HEADER + 1
    private static final int COUNT_PRE_OTHER_HEADER = COUNT_PRE_MY_HEADER + 1;

    private static final long ANIM_TIME = 360L;

    // touch 点击开始时间
    private long startTime;
    // touch 间隔时间  用于分辨是否是 "点击"
    private static final long SPACE_TIME = 100;

    private LayoutInflater mInflater;
    private ItemTouchHelper mItemTouchHelper;

    private List<String> mMyChannelItems, mOtherChannelItems;
    private ChannelCacheModel mChannelCacheModel;

    // 我的频道点击事件
    private OnMyChannelItemClickListener mChannelItemClickListener;

//    //是否正在执行动画
//    private boolean mAnimationing;

    public ChannelAdapter(Context context, ItemTouchHelper helper, ChannelCacheModel channelCacheModel) {
        this.mInflater = LayoutInflater.from(context);
        this.mItemTouchHelper = helper;
        this.mMyChannelItems = channelCacheModel.getMyChannelEntities();
        this.mOtherChannelItems = channelCacheModel.getOtherChannelEntities();
        mChannelCacheModel = channelCacheModel;
        mChannelCacheModel.setModified(false);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {    // 我的频道 标题部分
            return TYPE_MY_CHANNEL_HEADER;
        } else if (position == mMyChannelItems.size() + 1) {    // 其他频道 标题部分
            return TYPE_OTHER_CHANNEL_HEADER;
        } else if (position > 0 && position < mMyChannelItems.size() + 1) {
            return TYPE_MY;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view;
        switch (viewType) {
            case TYPE_MY_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_my_channel_header, parent, false);
                final MyChannelHeaderViewHolder holder = new MyChannelHeaderViewHolder(view);
                setMyHeaderTouch(holder);
                return holder;

            case TYPE_MY:
                view = mInflater.inflate(R.layout.item_my_channel, parent, false);
                final MyViewHolder myHolder = new MyViewHolder(view);
                setMyTextTouch(myHolder, parent);
                return myHolder;

            case TYPE_OTHER_CHANNEL_HEADER:
                view = mInflater.inflate(R.layout.item_other_channel_header, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };

            case TYPE_OTHER:
                view = mInflater.inflate(R.layout.item_other_channel, parent, false);
                final OtherViewHolder otherHolder = new OtherViewHolder(view);
                setOtherTouch(otherHolder, parent);
                return otherHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myHolder = (MyViewHolder) holder;
            myHolder.mChannel.setText(mMyChannelItems.get(position - COUNT_PRE_MY_HEADER));
            if (position == COUNT_PRE_MY_HEADER|| position == COUNT_PRE_OTHER_HEADER) {//0,1 item都不进行处理
                myHolder.mDelete.setVisibility(View.INVISIBLE);
            } else {
                myHolder.mDelete.setVisibility(View.VISIBLE);
            }
        } else if (holder instanceof OtherViewHolder) {
            ((OtherViewHolder) holder).mTextView.setText(mOtherChannelItems.get(position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER));
        } else if (holder instanceof MyChannelHeaderViewHolder) {
            MyChannelHeaderViewHolder headerHolder = (MyChannelHeaderViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        // 我的频道  标题 + 我的频道.size + 其他频道 标题 + 其他频道.size
        return mMyChannelItems.size() + mOtherChannelItems.size() + COUNT_PRE_OTHER_HEADER;
    }

    private void setMyHeaderTouch(final MyChannelHeaderViewHolder holder) {

    }

    //“我的栏目”相关触摸
    private void setMyTextTouch(final MyViewHolder myHolder, final ViewGroup parent) {
        myHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int position = myHolder.getAdapterPosition();
                if (position != COUNT_PRE_MY_HEADER && position != COUNT_PRE_OTHER_HEADER) {
                    moveMyToOther(myHolder);
                }
            }
        });

        myHolder.mRootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                int position = myHolder.getAdapterPosition();
                if (position != COUNT_PRE_MY_HEADER && position != COUNT_PRE_OTHER_HEADER) {
                    mItemTouchHelper.startDrag(myHolder);
                }
                return true;
            }
        });

        //        myHolder.textView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (MotionEventCompat.getActionMasked(event)) {
//                    case MotionEvent.ACTION_DOWN:
//                        startTime = System.currentTimeMillis();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        if (System.currentTimeMillis() - startTime > SPACE_TIME) {
//                            mItemTouchHelper.startDrag(myHolder);
//                        }
//                        break;
//                    case MotionEvent.ACTION_CANCEL:
//                    case MotionEvent.ACTION_UP:
//                        startTime = 0;
//                        break;
//                }
//
//                return false;
//            }
//        });
    }

    //“其他栏目”相关触摸事件
    private void setOtherTouch(final OtherViewHolder otherHolder, final ViewGroup parent) {
        otherHolder.mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveOtherToMy(otherHolder);
            }
        });
    }

    /**
     * 我的频道 移动到 其他频道
     *
     * @param myHolder
     */
    private void moveMyToOther(MyViewHolder myHolder) {
        int position = myHolder.getAdapterPosition();

        int startPosition = position - COUNT_PRE_MY_HEADER;
        if (startPosition > mMyChannelItems.size() - 1) {
            return;
        }
        String item = mMyChannelItems.get(startPosition);
        mMyChannelItems.remove(startPosition);
        mOtherChannelItems.add(0, item);

        notifyItemMoved(position, mMyChannelItems.size() + COUNT_PRE_OTHER_HEADER);
    }

    /**
     * 其他频道 移动到 我的频道
     *
     * @param otherHolder
     */
    private void moveOtherToMy(OtherViewHolder otherHolder) {

        int position = processItemRemoveAdd(otherHolder);
        if (position == -1) {
            return;
        }
        notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
    }

    /**
     * 其他频道 移动到 我的频道 伴随延迟
     *
     * @param otherHolder
     */
//    private void moveOtherToMyWithDelay(OtherViewHolder otherHolder) {
//        final int position = processItemRemoveAdd(otherHolder);
//        if (position == -1) {
//            return;
//        }
//        delayHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                notifyItemMoved(position, mMyChannelItems.size() - 1 + COUNT_PRE_MY_HEADER);
//            }
//        }, ANIM_TIME);
//    }

//    private Handler delayHandler = new Handler();
    private int processItemRemoveAdd(OtherViewHolder otherHolder) {
        int position = otherHolder.getAdapterPosition();

        int startPosition = position - mMyChannelItems.size() - COUNT_PRE_OTHER_HEADER;
        if (startPosition > mOtherChannelItems.size() - 1 || startPosition < 0) {
            return -1;
        }
        String item = mOtherChannelItems.get(startPosition);
        mOtherChannelItems.remove(startPosition);
        mMyChannelItems.add(item);
        return position;
    }


    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (!judgeCanMove(fromPosition, toPosition)) {
            return;
        }
        String item = mMyChannelItems.get(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.remove(fromPosition - COUNT_PRE_MY_HEADER);
        mMyChannelItems.add(toPosition - COUNT_PRE_MY_HEADER, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    private boolean judgeCanMove(int fromPosition, int toPosition) {
        if (toPosition == COUNT_PRE_MY_HEADER) {
            return false;
        }
        if (fromPosition - COUNT_PRE_MY_HEADER < 0 || fromPosition - COUNT_PRE_MY_HEADER > mMyChannelItems.size() - 1) {
            return false;
        }
        if (toPosition - COUNT_PRE_MY_HEADER < 0 || toPosition - COUNT_PRE_MY_HEADER > mMyChannelItems.size()) {
            return false;
        }
        return true;
    }


    public interface OnMyChannelItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnMyChannelItemClickListener(OnMyChannelItemClickListener listener) {
        this.mChannelItemClickListener = listener;
    }

    /**
     * 我的频道
     */
    class MyViewHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        @BindView(R.id.delete)
        ImageView mDelete;
        @BindView(R.id.channel)
        TextView mChannel;
        @BindView(R.id.rootView)
        RelativeLayout mRootView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
            mChannel.setBackgroundResource(R.drawable.bg_channel_pressed);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
            mChannel.setBackgroundResource(R.drawable.bg_channel);
        }
    }

    /**
     * 其他频道
     */
    class OtherViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv)
        TextView mTextView;
        @BindView(R.id.rootView)
        RelativeLayout mRootView;

        public OtherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 我的频道  标题部分
     */
    class MyChannelHeaderViewHolder extends RecyclerView.ViewHolder {

        public MyChannelHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
