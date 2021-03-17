package com.tuya.iotapp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *  PreferenceUtils 方法封装
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 2:46 PM
 */
public class PreferenceUtil {

    private static volatile PreferenceUtil util;
    private static SharedPreferences sp;

    private PreferenceUtil(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    /**
     * 初始化SharedPreferencesUtil,只需要初始化一次，建议在Application中初始化
     *
     * @param context 上下文对象
     * @param name    SharedPreferences Name
     */
    public static PreferenceUtil getInstance(Context context, String name) {
        if (util == null) {
            synchronized (PreferenceUtil.class) {
                if (util == null) {
                    util = new PreferenceUtil(context, name);
                }
            }
        }
        return util;
    }

    /**
     *  存储  Integer
     * @param key
     * @param value
     */
    public void set(String key, Integer value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 取值 Integer
     * @param key
     * @param defal
     * @return
     */
    public Integer get(String key, Integer defal) {
        return  sp.getInt(key, defal);
    }


    /**
     *  存储  Integer
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 取值 String
     * @param key
     * @param defal
     * @return
     */
    public String get(String key, String defal) {
        return  sp.getString(key, defal);
    }

    /**
     *  存储  String
     * @param key
     * @param value
     */
    public void set(String key, Boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 取值 Integer
     * @param key
     * @param defal
     * @return
     */
    public Boolean get(String key, Boolean defal) {
        return  sp.getBoolean(key, defal);
    }
}
