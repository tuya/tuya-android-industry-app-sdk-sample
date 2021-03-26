package com.tuya.iotapp.activator.config;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.smart.config.TuyaConfig;

/**
 * APConfigImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/18 3:52 PM
 */
public class APConfigImpl {

    /**
     * AP开始配网
     *
     * @param context
     * @param ssid
     * @param password
     * @param token
     */
    public static final void startConfig(Context context, String ssid, String password, String token) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password) || TextUtils.isEmpty(token)) {
            Toast.makeText(context, "params input can not null", Toast.LENGTH_SHORT).show();
            return;
        }

        LogUtils.d("ap startConfig", "ssid : password: token :" + ssid + "-" + password + "-" + token);
        TuyaConfig.getAPInstance().startConfig(context, ssid, password, token);
    }

    /**
     * AP停止配网
     */
    public static final void stopConfig() {
        TuyaConfig.getAPInstance().stopConfig();
    }
}
