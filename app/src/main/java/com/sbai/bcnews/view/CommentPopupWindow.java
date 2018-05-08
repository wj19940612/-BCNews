package com.sbai.bcnews.view;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.utils.Display;

/**
 * Modified by $nishuideyu$ on 2018/5/3
 * <p>
 * Description:
 * </p>
 */
public class CommentPopupWindow implements View.OnClickListener {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mAnchor;
    private View mContentView;

    private OnItemClickListener mOnItemClickListener;

    public CommentPopupWindow setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copy:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onCopy();
                }
                mPopupWindow.dismiss();
                break;
            case R.id.review:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.oReview();
                }
                mPopupWindow.dismiss();
                break;
            case R.id.whistleBlowing:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onWhistleBlowing();
                }
                mPopupWindow.dismiss();
                break;
        }
    }

    public interface OnItemClickListener {

        void onCopy();

        void oReview();

        void onWhistleBlowing();
    }

    public static CommentPopupWindow with(View view, Context context) {
        CommentPopupWindow commentPopupWindow = new CommentPopupWindow(context);
        commentPopupWindow.mAnchor = view;
        return commentPopupWindow;
    }

    public CommentPopupWindow(Context context) {
        mContext = context;

    }

    public void showPopupWindow() {
        if (mPopupWindow == null) {
            createPopupWindow();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPopupWindow.showAsDropDown(mAnchor, (Display.getScreenWidth())/5, -mAnchor.getMeasuredHeight(), Gravity.TOP);
        }

    }

    private void createPopupWindow() {
        mContentView = LayoutInflater.from(mContext).inflate(R.layout.popuwindow_comment, null);
        mPopupWindow = new PopupWindow();
        mPopupWindow.setContentView(mContentView);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);
        TextView copy = mContentView.findViewById(R.id.copy);
        TextView whistleBlowing = mContentView.findViewById(R.id.whistleBlowing);
        TextView review = mContentView.findViewById(R.id.review);
        copy.setOnClickListener(this);
        whistleBlowing.setOnClickListener(this);
        review.setOnClickListener(this);
    }

}
