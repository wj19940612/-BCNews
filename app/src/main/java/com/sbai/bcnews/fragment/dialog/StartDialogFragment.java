package com.sbai.bcnews.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.sbai.bcnews.R;
import com.sbai.bcnews.model.Pop;
import com.sbai.bcnews.view.StartBanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by ${wangJie} on 2017/9/12.
 * 用户注册即送300元宝
 */

public class StartDialogFragment extends DialogFragment {
    public static final String ACTIVITY = "activity";
    public static final String ACTION_DISMISS = "11";
    @BindView(R.id.dialogDelete)
    AppCompatImageView mDialogDelete;
    @BindView(R.id.startBanner)
    StartBanner mStartBanner;
    private Unbinder mBind;
    private List<Pop> mPopList;

    public static StartDialogFragment newInstance(List<Pop> popList) {
        ArrayList<Pop> arrayList = new ArrayList<Pop>();
        arrayList.addAll(popList);
        StartDialogFragment startDialogFragment = new StartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("pop", arrayList);
        startDialogFragment.setArguments(bundle);
        return startDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_start, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BaseDialog);
        if (getArguments() != null) {
            mPopList = getArguments().getParcelableArrayList("pop");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setGravity(Gravity.CENTER);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            window.setLayout((int) (dm.widthPixels * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
        }
        initView();
    }

    private void initView() {
        if (mPopList == null) return;
        mStartBanner.setPopList(mPopList);
        mStartBanner.setOnViewClickListener(null);
        mStartBanner.setOnButtonClickListener(new StartBanner.OnButtonClickListener() {
            @Override
            public void onButtonClick() {
                dismissAllowingStateLoss();
            }
        });
    }

    public void show(FragmentManager manager) {
        if (!isAdded()) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, this.getClass().getSimpleName());
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBind.unbind();
    }

    @OnClick({R.id.dialogDelete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogDelete:
                dismissAllowingStateLoss();
        }
    }
}
