package com.tuya.iotapp.sample.env;

import android.content.Context;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 2:31 PM
 */
public class EnvUtils {
    public static final int ENV_ONLINE = 0;
    public static final int ENV_PRE = 1;
    public static final int ENV_DAILY = 2;

    private static int mEnv;

    public static void setEnv(Context context, int env) {
        mEnv = env;
    }

    public static int getCurrentEnv(Context context) {
        return mEnv;
    }
}
