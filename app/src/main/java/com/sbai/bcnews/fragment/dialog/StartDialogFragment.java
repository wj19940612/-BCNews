package com.sbai.bcnews.fragment.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.NewsDetailActivity;
import com.sbai.bcnews.activity.WebActivity;
import com.sbai.bcnews.activity.mine.LoginActivity;
import com.sbai.bcnews.model.Pop;
import com.sbai.bcnews.model.StartWindow;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.bcnews.view.SmartDialog;
import com.sbai.bcnews.view.StartBanner;
import com.sbai.glide.GlideApp;

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
    @BindView(R.id.window)
    ImageView mWindow;
    private Unbinder mBind;
    private StartWindow mStartWindow;

    private SmartDialog.OnDismissListener mOnDismissListener;

    public static StartDialogFragment newInstance(StartWindow startWindow) {
        StartDialogFragment startDialogFragment = new StartDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ExtraKeys.START_WINDOW, startWindow);
        startDialogFragment.setArguments(bundle);
        return startDialogFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof SmartDialog.OnDismissListener){
            mOnDismissListener = (SmartDialog.OnDismissListener) context;
        }
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
            mStartWindow = getArguments().getParcelable(ExtraKeys.START_WINDOW);
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
        GlideApp.with(getContext())
                .load(mStartWindow.getWindowUrl())
                .centerCrop()
                .placeholder(R.drawable.ic_default_image)
                .into(mWindow);
        mWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartWindow.getLinkType() == StartWindow.START_TYPE_MODULE) {
                    Launcher.with(getActivity(), LoginActivity.class).execute();
                } else if (mStartWindow.getLinkType() == StartWindow.START_TYPE_ARTICLE) {
                    Launcher.with(getActivity(), NewsDetailActivity.class).putExtra(ExtraKeys.NEWS_ID, mStartWindow.getLink()).execute();
                } else if (mStartWindow.getLinkType() == StartWindow.START_TYPE_H5) {
                    Launcher.with(getActivity(), WebActivity.class)
                            .putExtra(WebActivity.EX_URL, mStartWindow.getLink())
                            .execute();
                }
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnDismissListener!=null){
            mOnDismissListener.onDismiss(null);
        }
    }
}
