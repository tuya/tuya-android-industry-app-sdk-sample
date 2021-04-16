package com.tuya.dev.network.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.tuya.dev.common.utils.LogUtils;
import com.tuya.dev.network.IotAppNetWork;

/**
 * TimeStampManager
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 3:09 PM
 */
public class TimeStampManager {

    /**
     * 0表示发送请求，还没有返回结果
     */
    public static final int TIME_FLAG_LOADING = 0;
    private static final String TAG = "TimeStampManager";
    private static final String TIMESTAMP_SP_NAME = "timestamp_difference";
    private static final String BASE_TIME_ELAPSE_KEY = "base_time_elapsed";
    private static final String BASE_SERVER_TIMESTAMP_KEY = "base_server_timestamp";
    /**
     * -1表示还没有初始化
     */
    private static final int TIME_FLAG_ERROR = -1;
    /**
     * 1表示已完成获取动作
     */
    private static final int TIME_FLAG_LOADED = 1;

    private volatile int timeFlag = -1;

    /**
     * 第一次获取服务端timestamp的本地基准时间
     */
    private volatile long baseTimeElapsed = SystemClock.elapsedRealtime();

    /**
     * 第一次获取服务端timestamp的值
     */
    private volatile long baseServerTimeStamp = System.currentTimeMillis() / 1000;

    private TimeStampManager() {
    }

    /**
     * 单例获取接口
     *
     * @return TimeStampManager的单例
     */
    public static TimeStampManager instance() {
        return SingletonHolder.instance;
    }

    /**
     * 更新当前记录的 基准服务端时间 和 基准app运行时间
     *
     * @param serverTimeStamp 服务端时间戳
     */
    public void updateTimeStamp(long serverTimeStamp) {
        baseTimeElapsed = SystemClock.elapsedRealtime();
        baseServerTimeStamp = serverTimeStamp;
        LogUtils.d(TAG, "update baseServerTimeStamp: " + baseServerTimeStamp
                + " | update baseTimeElapsed: " + baseTimeElapsed);
        timeFlag = TIME_FLAG_LOADED;
        save();
    }

    /**
     * 获取当前时间戳(经过服务端校准)
     * ------  严重警告！不要随意更改时间戳精度 --------
     *
     * @return 当前时间戳
     */
    public long getCurrentTimeStamp() {
        // 第一次进入从本地还原
        if (timeFlag == TIME_FLAG_ERROR) {
            LogUtils.d(TAG, "restore timestamp from local");
            restore(IotAppNetWork.getAppContext());
        }
        long timeDiff = SystemClock.elapsedRealtime() - baseTimeElapsed;
        long currentTimeStamp = baseServerTimeStamp + (timeDiff / 1000);
        // 如果当前系统启动时间小于存储的启动时间，说明手机重启后还未更新存储的启动时间
        // 此时直接采用本地的时间戳，等TimeManager.initTime接口请求成功后会更新时间
        if (timeDiff < 0) {
            baseTimeElapsed = SystemClock.elapsedRealtime();
            baseServerTimeStamp = System.currentTimeMillis() / 1000;
            currentTimeStamp = baseServerTimeStamp;
        }
        return currentTimeStamp;
    }

    /**
     * 将当前时间差存入本地文件
     */
    public void save() {
        SharedPreferences sp = IotAppNetWork.getAppContext().getSharedPreferences(TIMESTAMP_SP_NAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(BASE_TIME_ELAPSE_KEY, baseTimeElapsed);
        editor.putLong(BASE_SERVER_TIMESTAMP_KEY, baseServerTimeStamp);
        editor.apply();
    }

    /**
     * 恢复本地文件中时间差
     *
     * @param context context
     */
    private void restore(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TIMESTAMP_SP_NAME, 0);
        long base = sp.getLong(BASE_TIME_ELAPSE_KEY, -1);
        long sever = sp.getLong(BASE_SERVER_TIMESTAMP_KEY, -1);
        if (base > 0 && sever > 0) {
            baseTimeElapsed = base;
            baseServerTimeStamp = sever;
            timeFlag = TIME_FLAG_LOADED;
        }
    }

    public void onCreated() {

    }

    /**
     * 重置状态，对应app的onStop()
     */
    public void onStop() {
    }

    /**
     * 异步从服务端获取时间戳，可能不会发起网络请求
     *
     * @param isForce 是否强制重新获取(会取消前一次未完成的请求)
     * @return 是否发起网络请求 true : 发起   false : 未发起
     */
    public boolean pullTimeStamp(boolean isForce) {
        return true;
    }

    /**
     * 根据timeFlag来判断是否已经成功从server端获取过timestamp，否则发起网络请求
     *
     * @return 是否发起网络请求 true : 发起   false : 未发起
     */
    public boolean pullTimeStampIfNeeded() {
        //如果当前没有获取成功，则重新获取
        if (timeFlag == TIME_FLAG_ERROR) {
            return pullTimeStamp(false);
        }
        return false;
    }

    /**
     * 当前时间戳获取状态
     *
     * @return TIME_FLAG_ERROR = -1 表示还没有初始化
     * TIME_FLAG_LOADING = 0 表示发送请求，还没有返回结果
     * TIME_FLAG_LOADED = 1 表示已完成获取动作
     */
    public int getTimeStampState() {
        return timeFlag;
    }

    /**
     * 单例模型
     */
    private static class SingletonHolder {
        private static TimeStampManager instance = new TimeStampManager();
    }
}
