package com.tuya.iotapp.sample;

import android.app.Application;

import com.tuya.iotapp.componet.TuyaIoTSDK;
import com.tuya.iotapp.network.api.RegionHostConst;

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

//        EnvUtils.setEnv(this, EnvUtils.ENV_ONLINE); //环境区分

        //tn8ncrssaj8suallbsyq   8a7e8600d04b4090be3e3a7b589a00b5
        //4r84thwpag39ts6axiz5   19c2baba7f9042d79fd548249125eaec
        //pre:jrfqdtyrag1ujadbspoh  051d8a5606bf438e80c8d4e975b11c84
        //f37yxdnnakpg5j30w2dq   6aafc371d4a34cd8a33a0553e042181a
        TuyaIoTSDK.builder().init(getApplicationContext(), "f37yxdnnakpg5j30w2dq", "6aafc371d4a34cd8a33a0553e042181a")
                .hostConfig(RegionHostConst.REGION_HOST_CN)
                .debug(true)
                .build();

    }
}
