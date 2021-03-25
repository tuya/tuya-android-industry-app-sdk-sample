package com.tuya.iotapp.sample;

import android.app.Application;

import com.getkeepsafe.relinker.ReLinker;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.sample.env.Constant;
import com.tuya.iotapp.sample.env.EnvUrlProvider;
import com.tuya.iotapp.sample.env.EnvUtils;

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
        ReLinker.loadLibrary(this, Constant.TUYA_SIGN);

        EnvUtils.setEnv(this, EnvUtils.ENV_PRE); //环境区分
        IApiUrlProvider provider = new EnvUrlProvider(this);
        IotAppNetWork.initialize(getApplicationContext(),
                "qpgf3xycag1x81ferih3",
                "bd0ddd90c50341de9b7f55748cc2f7de",
                "Android",
                provider);
    }
}
