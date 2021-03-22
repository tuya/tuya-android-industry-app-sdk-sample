package com.tuya.iotapp.network.http;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.IotAppNetWork;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 8:06 PM
 */
public class IotAppNetWorkExecutorManager {

    private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    /**
     * 业务专用
     *
     * @return
     */
    public static ExecutorService getBusinessExecutor() {
        ExecutorService businessExecutor = IotAppNetWork.getIotAppNetWorkConfig().getBusinessExecutor();
        if (businessExecutor == null) {
            businessExecutor = executorService;
        }
        LogUtils.d("TuyaSmartNetWorkExecutorManager", "getBusinessExecutor: " + executorService);
        return businessExecutor;
    }

    /**
     * OKHttp 线程池专用
     *
     * @return
     */
    public static ExecutorService getNetWorkExecutor() {
        ExecutorService networkExecutor = IotAppNetWork.getIotAppNetWorkConfig().getNetWorkExecutor();
        if (networkExecutor == null) {
            networkExecutor = executorService;
        }

        return networkExecutor;
    }
}
