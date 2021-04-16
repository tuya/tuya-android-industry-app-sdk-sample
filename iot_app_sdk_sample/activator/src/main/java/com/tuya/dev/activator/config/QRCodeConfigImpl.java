package com.tuya.dev.activator.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.tuya.dev.activator.util.QrCodeUtil;
import com.tuya.dev.common.utils.LogUtils;

/**
 * QRCodeConfigImpl
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/18 4:29 PM
 */
public class QRCodeConfigImpl {
    private static final String TAG = "QRCodeConfigImpl";

    public static void createQrCode(Context context, String ssid, String password, String token,
                                    IQrCodeActivatorListener listener) {
        if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(password) || TextUtils.isEmpty(token)) {
            Toast.makeText(context, "params input can not null", Toast.LENGTH_SHORT).show();
            return;
        }
        LogUtils.d(TAG, "createQrCode : ");
        String wifiSsid = ssid;
        String wifiPass = password;
        if (ssid.contains("\\")) {
            wifiSsid = ssid.replaceAll("\\\\", "\\\\\\\\");
        }
        if (password.contains("\\")) {
            wifiPass = password.replaceAll("\\\\", "\\\\\\\\");
        }

        if (ssid.contains("\"")) {
            wifiSsid = wifiSsid.replaceAll("\"", "\\\\\"");
        }
        if (password.contains("\"")) {
            wifiPass = wifiPass.replaceAll("\"", "\\\\\"");
        }

        String url = "{\"p\"" + ":\"" + wifiPass + "\"," + "\"s\"" + ":\"" + wifiSsid + "\"," + "\"t\"" + ":\"" + token + "\"}";
        try {
            Bitmap bitmap = QrCodeUtil.createQRCode(url, 300);
            if (listener != null) {
                listener.onQrCodeSuccess(bitmap);
            }
        } catch (Exception e) {
            LogUtils.d(TAG, "createQrCode : ");
        }
    }
}
