package com.tuya.iotapp.common.utils;

import android.text.TextUtils;
import android.util.Log;
import com.tuya.smart.android.common.utils.L;

/**
 * 日志管理
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 8:15 PM
 */
public class LogUtils {

    private static String IOT_APP_TAG = "iot_app";

    public static void setLogSwitcher(boolean open) {
        L.setLogSwitcher(open);
    }

    public static void i(String tag, String msg) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(msg)) {
            L.i(IOT_APP_TAG, tag + " " + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(msg)) {
            L.d(IOT_APP_TAG, tag + " " + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(msg)) {
            L.e(IOT_APP_TAG, tag + " " + msg);
        }
    }
}
