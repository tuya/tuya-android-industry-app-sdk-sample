package com.tuya.dev.activator.config;

import android.graphics.Bitmap;

/**
 * IQrCodeActivatorListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/18 4:33 PM
 */
public interface IQrCodeActivatorListener {
    void onQrCodeSuccess(Bitmap bitmap);
}
