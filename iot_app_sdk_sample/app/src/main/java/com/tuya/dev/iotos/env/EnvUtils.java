package com.tuya.dev.iotos.env;

import android.content.Context;

import com.tuya.dev.common.utils.PreferenceUtil;

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
    private static String endpoint = Endpoint.AZ;

    public static void setEndpoint(Context context, String env) {
        endpoint = env;
        PreferenceUtil.getInstance(context, "env").set("sp_env", env);
    }

    public static String getEndpoint() {
        return endpoint;
    }
}
