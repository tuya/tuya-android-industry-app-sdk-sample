package com.tuya.dev.activator.config;

import android.text.TextUtils;

import com.tuya.dev.common.utils.LogUtils;
import com.tuya.smart.config.TuyaConfig;

/**
 * EZConfigImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/18 3:52 PM
 */
public class EZConfigImpl {

    /**
     * EZ开始配网
     *
     * @param ssid
     * @param password
     * @param token
     */
    public static final void startConfig(String ssid, String password, String token) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password) || TextUtils.isEmpty(token)) {
            return;
        }

        LogUtils.d("ez  startConfig", "ssid : password: token :" + ssid + "-" + password + "-" + token);
        TuyaConfig.getEZInstance().startConfig(ssid, password, token);
    }

    /**
     * AP停止配网
     */
    public static final void stopConfig() {
        TuyaConfig.getEZInstance().stopConfig();
    }
}
