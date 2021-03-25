package com.tuya.iotapp.common.kv;

import android.app.Application;
import android.content.Context;

import com.tencent.mmkv.MMKV;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * K-v Manager
 * todo need adpater
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/23 10:02 AM
 */
public class KvManager {
    private static final String GLOBAL_KEY = "key_iotos_cache";
    private static volatile MMKV sMMKVManager;

    public static MMKV getMMKVManager() {
        if (sMMKVManager == null) {
            synchronized (KvManager.class) {
                if (sMMKVManager == null) {
                    Context appContext = getSystemApp();
                    MMKV.initialize(appContext);

                    sMMKVManager = MMKV.mmkvWithID("KvId", MMKV.SINGLE_PROCESS_MODE, GLOBAL_KEY);
                }
            }
        }
        return sMMKVManager;
    }


    public static String getString(String key) {
        return getMMKVManager().getString(key, "");
    }

    public static String getString(String key, String defValue) {
        return getMMKVManager().getString(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getMMKVManager().getLong(key, defValue);
    }

    public static void set(String key, String b) {
        getMMKVManager().putString(key, b);
    }

    public static void set(String key, long value) {
        getMMKVManager().putLong(key, value);
    }

    public static boolean getBoolean(String key, boolean defValue) {
        return getMMKVManager().getBoolean(key, defValue);
    }

    public static void putBoolean(String key, boolean b) {
        getMMKVManager().putBoolean(key, b);
    }


    public static void remove(String key) {
        getMMKVManager().remove(key);

    }

    public static void clear() {
        getMMKVManager().clear();
    }


    public static void reset() {
        synchronized (KvManager.class) {
            sMMKVManager = null;
        }
    }

    private static Application getSystemApp() {
        Application application = null;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
            Field initialApplicationField = activityThread.getDeclaredField("mInitialApplication");
            initialApplicationField.setAccessible(true);
            Object current = currentActivityThread.invoke(null);
            application = (Application) initialApplicationField.get(current);
        } catch (Throwable ignore) {
        }
        try {
            if (application == null) {
                application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null, (Object[]) null);
            }
        } catch (Throwable ignore) {
        }
        try {
            if (application == null) {
                application = (Application) Class.forName("android.app.AppGlobals")
                        .getMethod("getInitialApplication").invoke(null, (Object[]) null);
            }
        } catch (Throwable ignore) {
        }
        return application;
    }
}
