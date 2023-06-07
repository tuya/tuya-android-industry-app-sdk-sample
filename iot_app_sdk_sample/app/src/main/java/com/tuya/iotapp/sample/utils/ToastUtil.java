package com.tuya.iotapp.sample.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.thingclips.iotapp.common.IndustryCallBack;

public class ToastUtil {

    public static void show(Context context, @StringRes int resId) {
        if (ThreadUtil.isUIThread()) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        } else {
            ThreadUtil.runOnUIThread(() -> {
                Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static void show(Context context, String message) {
        if (ThreadUtil.isUIThread()) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        } else {
            ThreadUtil.runOnUIThread(() -> {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            });
        }
    }
}
