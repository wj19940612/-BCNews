package com.sbai.bcnews.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.sbai.bcnews.ExtraKeys;
import com.sbai.bcnews.R;
import com.sbai.bcnews.activity.mine.FeedbackActivity;
import com.sbai.bcnews.utils.Display;
import com.sbai.bcnews.utils.Launcher;
import com.sbai.glide.GlideApp;

/**
 * 截图预览
 */

public class ScreenShotView {

    public static void show(final Context context, final String path, int duration) {
        final View rootView = LayoutInflater.from(context).inflate(R.layout.view_screenn_shot, null);
        ImageView imageView = (ImageView) rootView.findViewById(R.id.img);
        //为控件设置属性
        GlideApp.with(context).load(path).into(imageView);
        final Dialog dialog = new Dialog(context, R.style.DialogTheme_NoTitle);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.RIGHT | Gravity.TOP);
        dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialogWindow.setDimAmount(0f);//去掉遮罩
        dialogWindow.requestFeature(Window.FEATURE_NO_TITLE);
        dialogWindow.setContentView(rootView);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = (int) Display.dp2Px(80, context.getResources());
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        rootView.postDelayed(new Runnable() {
            public void run() {
                if (dialog != null) {
                    dialog.hide();
                }
            }
        }, duration);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.hide();
                }
                Launcher.with(context, FeedbackActivity.class)
                        .putExtra(ExtraKeys.IMAGE_PATH, path)
                        .execute();
            }
        });
    }
}
