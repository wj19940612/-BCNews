package com.sbai.bcnews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.fragment.dialog.BottomDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CommonBottomDialogFragment extends BottomDialogFragment {

    @BindView(R.id.title)
    AppCompatTextView mTitle;
    @BindView(R.id.firstBtn)
    AppCompatTextView mFirstBtn;
    @BindView(R.id.secondBtn)
    AppCompatTextView mSecondBtn;
    private Unbinder mBind;

    private String mTitleString;

    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        public void onFirstClick();

        public void onSecondClick();
    }

    public static CommonBottomDialogFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(ExtraKeys.TITLE, title);
        CommonBottomDialogFragment commonBottomDialogFragment = new CommonBottomDialogFragment();
        commonBottomDialogFragment.setArguments(bundle);
        return commonBottomDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnClickListener) {
            mOnClickListener = (OnClickListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitleString = getArguments().getString(ExtraKeys.TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_common_bottom, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!TextUtils.isEmpty(mTitleString)) {
            mTitle.setText(mTitleString);
        }
    }

    @OnClick({R.id.firstBtn, R.id.secondBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.firstBtn:
                if(mOnClickListener!=null){
                    mOnClickListener.onFirstClick();
                }
                break;
            case R.id.secondBtn:
                if(mOnClickListener!=null){
                    mOnClickListener.onSecondClick();
                }
                break;
        }
    }
}
