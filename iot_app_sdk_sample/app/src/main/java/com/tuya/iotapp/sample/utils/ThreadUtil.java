package com.tuya.iotapp.sample.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtil {
    private static final Handler handler = new Handler(Looper.getMainLooper());

    public static boolean isUIThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void runOnUIThread(Runnable runnable) {
        handler.post(runnable);
    }
}
