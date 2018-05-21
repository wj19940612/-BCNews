package com.sbai.bcnews.view.dialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sbai.bcnews.R;
import com.sbai.bcnews.view.SmartDialog;

public class ConversionGoodsDialog {

    private Activity mActivity;
    private SmartDialog mSmartDialog;
    private View mView;

    private TextView mContent;
    private TextView mConversionBtn;
    private OnClickListener mOnClickListener;

    public interface OnClickListener {
        void onClick();
    }

    public static ConversionGoodsDialog with(Activity activity) {
        ConversionGoodsDialog conversionGoodsDialog = new ConversionGoodsDialog();
        conversionGoodsDialog.mActivity = activity;
        conversionGoodsDialog.mSmartDialog = SmartDialog.single(activity);
        conversionGoodsDialog.mView = LayoutInflater.from(activity).inflate(R.layout.dialog_conversion_goods, null);
        conversionGoodsDialog.mSmartDialog.setCustomView(conversionGoodsDialog.mView);
        conversionGoodsDialog.init();
        return conversionGoodsDialog;
    }

    private void init() {
        mContent = mView.findViewById(R.id.content);
        mConversionBtn = mView.findViewById(R.id.conversionBtn);

        mConversionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick();
                }
            }
        });
    }

    public void show(){
        mSmartDialog.setWidthScale(1).show();
    }
}
