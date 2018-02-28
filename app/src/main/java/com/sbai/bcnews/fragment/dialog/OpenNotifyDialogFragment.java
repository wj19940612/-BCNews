package com.sbai.bcnews.fragment.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.TextView;

import com.sbai.bcnews.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 开启通知
 */

public class OpenNotifyDialogFragment extends DialogFragment {
    @BindView(R.id.dialogDelete)
    AppCompatImageView mDialogDelete;
    @BindView(R.id.button)
    TextView mButton;
    private Unbinder mBind;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_open_notify, container, false);
        mBind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.BaseDialog);
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

    @OnClick({R.id.dialogDelete, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialogDelete:
                dismissAllowingStateLoss();
                break;
            case R.id.button:
                dismissAllowingStateLoss();
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                break;
        }
    }
}
