package com.sbai.bcnews.utils;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sbai.bcnews.App;
import com.sbai.bcnews.R;


public class ToastUtil {

    private static Toast sToast;
    private static Toast sViewToast;
    private static long sFirstTime;
    private static long sSecondTime;
    private static String sMessage;

    public static void show(int messageId) {
        show(App.getAppContext().getString(messageId));
    }

    public static void show(String message) {
        if (sToast == null) {
            sToast = Toast.makeText(App.getAppContext(), message, Toast.LENGTH_SHORT);
            sToast.show();
            sFirstTime = System.currentTimeMillis();
        } else {
            sSecondTime = System.currentTimeMillis();
            if (message.equals(sMessage)) {
                if (sSecondTime - sFirstTime > Toast.LENGTH_SHORT) {
                    sToast.show();
                }
            } else {
                sMessage = message;
                sToast.setText(message);
                sToast.show();
            }
        }
        sFirstTime = sSecondTime;
    }


    public static void show(Activity activity, CharSequence message, int yOffset) {
        show(activity,message,Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, yOffset);
    }

    public static void show(Activity activity, CharSequence message, int gravity, int xOffset, int yOffset) {
        View view = LayoutInflater.from(activity).inflate(R.layout.toast_get_calculate_rate, null);
        TextView textView = view.findViewById(R.id.message);
        textView.setText(message);
        show(activity, view, gravity, xOffset, yOffset);
    }

    public static void show(Activity activity, View view, int gravity, int xOffset, int yOffset) {
        if (sViewToast == null) {
            sViewToast = new Toast(activity);
        }
        sViewToast.setDuration(Toast.LENGTH_SHORT);
        sViewToast.setView(view);
        sViewToast.setGravity(gravity, xOffset, yOffset);
        sViewToast.show();
    }
}