package com.sbai.bcnews.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sbai.bcnews.App;


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

    public static void show(View view,TextView textView, String message, int marginTop) {
        if (sViewToast == null) {
            sViewToast = new Toast(App.getAppContext());
            sViewToast.setDuration(Toast.LENGTH_SHORT);
            textView.setText(message);
            sViewToast.setView(view);
            sViewToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, marginTop);
            sViewToast.show();
            sFirstTime = System.currentTimeMillis();
        } else {
            sSecondTime = System.currentTimeMillis();
            if (message.equals(sMessage)) {
                if (sSecondTime - sFirstTime > Toast.LENGTH_SHORT) {
                    sViewToast.show();
                }
            } else {
                sMessage = message;
                textView.setText(message);
                sViewToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, marginTop);
                sViewToast.setView(view);
                sViewToast.show();
            }
        }
        sFirstTime = sSecondTime;
    }
}