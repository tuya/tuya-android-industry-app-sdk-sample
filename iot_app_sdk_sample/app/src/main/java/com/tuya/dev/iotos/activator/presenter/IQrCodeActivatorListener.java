package com.tuya.dev.iotos.activator.presenter;

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
