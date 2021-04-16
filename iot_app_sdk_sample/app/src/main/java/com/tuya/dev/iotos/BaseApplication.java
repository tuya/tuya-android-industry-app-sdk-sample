package com.tuya.dev.iotos;

import android.app.Application;

import com.getkeepsafe.relinker.ReLinker;
import com.tuya.dev.common.utils.LogUtils;
import com.tuya.dev.iotos.env.Constant;

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

        LogUtils.setLogSwitcher(true);

    }
}
