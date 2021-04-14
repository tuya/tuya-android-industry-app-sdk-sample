package com.tuya.iotapp.activator.config;

import androidx.annotation.NonNull;

import com.tuya.smart.sdk.config.ble.TuyaBleWifiConfig;
import com.tuya.smart.sdk.config.ble.api.TuyaBleConfigListener;
import com.tuya.smart.sdk.config.ble.api.TuyaBleScanCallback;
import com.tuya.smart.sdk.config.ble.api.bean.ConfigParams;

/**
 * BleWifiConfigImpl
 *
 * bluetooth activator
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/13 11:37 AM
 */
public class BleWifiConfigImpl {

    /**
     * startScan
     * @param timeout
     * @param scanCallback
     */
    public static void startScan(int timeout, TuyaBleScanCallback scanCallback) {
        TuyaBleWifiConfig.INSTANCE.startScan(timeout, scanCallback);
    }

    /**
     * stopScan
     */
    public static void stopScan() {
        TuyaBleWifiConfig.INSTANCE.stopScan();
    }

    /**
     * startConfig
     * @param configParams
     * @param listener
     */
    public static void startConfig(ConfigParams configParams, TuyaBleConfigListener listener) {
        TuyaBleWifiConfig.INSTANCE.startConfig(configParams, listener);
    }

    /**
     * stopConfig
     * @param devUuid
     */
    public static void stopConfig(String devUuid) {
        TuyaBleWifiConfig.INSTANCE.stopConfig(devUuid);
    }

    /**
     * debug
     * @param flag
     */
    public static void debug(boolean flag) {
        TuyaBleWifiConfig.INSTANCE.debug(flag);
    }
}
