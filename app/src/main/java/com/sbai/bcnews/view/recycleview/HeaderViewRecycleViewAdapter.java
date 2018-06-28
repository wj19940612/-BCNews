package com.sbai.bcnews.view.recycleview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.view.FooterView;

/**
 * Modified by $nishuideyu$ on 2018/4/19
 * <p>
 * Description: 添加头部和尾部的adapter
 * 暂时只支持添加一个头部和一个尾部
 * </p>
 */
public abstract class HeaderViewRecycleViewAdapter<T, K extends RecyclerView.ViewHolder> extends BaseRecycleViewAdapter<T, K> implements HeaderViewController {

    private View mHeaderView;

    private View mFooterView;

    private View mEmptyView;

    private final Object mLock = new Object();

    @NonNull
    public abstract K onContentCreateViewHolder(@NonNull ViewGroup parent, int viewType);

    public abstract void onBindContentViewHolder(@NonNull K holder, T data, int position);

    @SuppressWarnings("unchecked")
    @Override
    @NonNull
    public K onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                if (mHeaderView != null) {
                    return (K) new HeaderViewViewHolder(mHeaderView);
                }
            case FOOTER_VIEW_TYPE:
                if (mFooterView != null) {
                    return (K) new FooterViewViewHolder(mFooterView);
                }
            default:
                return onContentCreateViewHolder(parent, viewType);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case HEADER_VIEW_TYPE:
                break;
            case FOOTER_VIEW_TYPE:
                break;
            default:
                T itemData = getItemData(position - getHeaderViewsCount());
                if (itemData != null) {
                    onBindContentViewHolder((K) holder, itemData, position - getHeaderViewsCount());
                }
        }
    }


    @Override
    public int getItemCount() {
        return getDataList().size() + getFooterViewsCount() + getHeaderViewsCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (getHeaderViewsCount() != 0 && position == 0) {
            return HEADER_VIEW_TYPE;
        }
        if (getFooterViewsCount() != 0 && position == getDataList().size() + getHeaderViewsCount()) {
            return FOOTER_VIEW_TYPE;
        }
        return getContentItemViewType(position - getHeaderViewsCount());
    }

    public int getContentItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void addHeaderView(View view) {
        synchronized (mLock) {
            mHeaderView = view;
            notifyDataSetChanged();
        }
    }


    @Override
    public int getHeaderViewsCount() {
        if (mHeaderView != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean removeHeaderView() {
        if (mHeaderView != null) {
            mHeaderView = null;
            notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public void addFooterView(View view) {
        mFooterView = view;
        notifyDataSetChanged();
    }


    @Override
    protected int getFooterViewsCount() {
        if (mFooterView != null) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean removeFooterView() {
        if (mFooterView != null) {
            mFooterView = null;
            notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public boolean hasFooterView() {
        return mFooterView != null;
    }

    public class HeaderViewViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class FooterViewViewHolder extends RecyclerView.ViewHolder {

        public FooterViewViewHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * 好多页面需要加尾部
     *
     * @return
     */
    public View createDefaultFooterView(Context context) {
        FooterView footerView = new FooterView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) Display.dp2Px(30, context.getResources()));
        layoutParams.gravity = Gravity.CENTER;
        footerView.setLayoutParams(layoutParams);
        return footerView;
    }
}
