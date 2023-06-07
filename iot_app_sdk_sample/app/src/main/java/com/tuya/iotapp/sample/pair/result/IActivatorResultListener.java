package com.tuya.iotapp.sample.pair.result;


import android.graphics.Bitmap;

import com.thingclips.iotapp.device.api.IDevice;

import java.util.List;

/**
 * IActivatorResultListener
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 6:13 PM
 */
public interface IActivatorResultListener {
    void onActivatorSuccessDevice(List<IDevice> successDevices);


    void onQRCodeSuccess(Bitmap qrBitmap);
}
