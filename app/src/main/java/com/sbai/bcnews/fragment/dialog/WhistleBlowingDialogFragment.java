package com.sbai.bcnews.fragment.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.BottomDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Modified by $nishuideyu$ on 2018/4/26
 * <p>
 * Description:
 * </p>
 */
public class WhistleBlowingDialogFragment extends BottomDialogFragment implements View.OnClickListener {

    public static final int WHISTLE_BLOWING_TYPE_ARTICLE = 0; //文章
    public static final int WHISTLE_BLOWING_TYPE_COMMENT = 1; //评论

    @BindView(R.id.first)
    TextView mFirst;
    @BindView(R.id.seventh)
    TextView mSeventh;
    @BindView(R.id.eighth)
    TextView mEighth;
    @BindView(R.id.ninth)
    TextView mNinth;
    @BindView(R.id.tenth)
    TextView mTenth;


    public interface OnWhistleBlowingReasonListener {
        void onChooseReason(int position, String content, int type,String id);
    }

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.second)
    TextView mSecond;
    @BindView(R.id.third)
    TextView mThird;
    @BindView(R.id.forth)
    TextView mForth;
    @BindView(R.id.fifth)
    TextView mFifth;
    @BindView(R.id.sixth)
    TextView mSixth;
    @BindView(R.id.cancel)
    TextView mCancel;
    Unbinder unbinder;
    @BindView(R.id.rootView)
    LinearLayout mRootView;
    private String[] mStringArray;

    public OnWhistleBlowingReasonListener mOnWhistleBlowingReasonListener;

    private int mWhistleBlowingType;
    private String mDataId;

    public WhistleBlowingDialogFragment() {

    }


    public static WhistleBlowingDialogFragment newInstance(int type, String id) {
        Bundle args = new Bundle();
        args.putInt(ExtraKeys.TAG, type);
        args.putString(ExtraKeys.DATA, id);
        WhistleBlowingDialogFragment fragment = new WhistleBlowingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWhistleBlowingType = getArguments().getInt(ExtraKeys.TAG);
            mDataId = getArguments().getString(ExtraKeys.DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_whistle_blowing, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mOnWhistleBlowingReasonListener = (OnWhistleBlowingReasonListener) context;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mStringArray = getResources().getStringArray(R.array.whistle_blowing_content);

        for (int i = 0; i < mStringArray.length; i++) {
            if (i < mRootView.getChildCount() - 2) {
                TextView child = (TextView) mRootView.getChildAt(i + 1);
                child.setText(mStringArray[i]);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {

    }

    @OnClick({R.id.first, R.id.second, R.id.third, R.id.forth, R.id.fifth, R.id.sixth, R.id.cancel,
            R.id.seventh, R.id.eighth, R.id.ninth, R.id.tenth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.first:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(0, mStringArray[0], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.second:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(1, mStringArray[1], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.third:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(2, mStringArray[2], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.forth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(3, mStringArray[3], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.fifth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(4, mStringArray[4], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.sixth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(5, mStringArray[5], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.seventh:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(6, mStringArray[6], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.eighth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(7, mStringArray[7], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.ninth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(8, mStringArray[8], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.tenth:
                if (mOnWhistleBlowingReasonListener != null) {
                    mOnWhistleBlowingReasonListener.onChooseReason(9, mStringArray[9], mWhistleBlowingType,mDataId);
                }
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }
}
