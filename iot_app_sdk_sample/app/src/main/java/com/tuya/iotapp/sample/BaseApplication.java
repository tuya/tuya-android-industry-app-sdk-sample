package com.tuya.iotapp.sample;

import android.app.Application;

import com.getkeepsafe.relinker.ReLinker;
import com.tuya.iotapp.network.api.RegionHostConst;
import com.tuya.iotapp.network.api.TYNetworkManager;
import com.tuya.iotapp.sample.env.Constant;
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

        EnvUtils.setEnv(this, EnvUtils.ENV_ONLINE); //环境区分

        //todo:replace appId and appSecret
        TYNetworkManager.Companion.initialize(getApplicationContext(),
                "",
                "",
               RegionHostConst.REGION_HOST_CN);
    }
}
