package com.tuya.iotapp.sample;

import android.app.Application;

import com.thingclips.iotapp.IndustryLinkSDK;

/**
 * Base Application
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/24 11:05 AM
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        new IndustryLinkSDK.Builder(this)
                .initialize("", "")
                .setHost("")
                .setDeviceSDKKey("")
                .setDeviceSDKSecret("")
                .setDebugMode(true)
                .build();
    }
}
