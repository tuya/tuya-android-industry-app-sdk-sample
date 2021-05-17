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

        //tn8ncrssaj8suallbsyq   8a7e8600d04b4090be3e3a7b589a00b5
        //4r84thwpag39ts6axiz5   19c2baba7f9042d79fd548249125eaec
        //pre:jrfqdtyrag1ujadbspoh  051d8a5606bf438e80c8d4e975b11c84
        //f37yxdnnakpg5j30w2dq   6aafc371d4a34cd8a33a0553e042181a
        TYNetworkManager.Companion.initialize(getApplicationContext(),
                "f37yxdnnakpg5j30w2dq",
                "6aafc371d4a34cd8a33a0553e042181a",
               RegionHostConst.REGION_HOST_CN);
    }
}
