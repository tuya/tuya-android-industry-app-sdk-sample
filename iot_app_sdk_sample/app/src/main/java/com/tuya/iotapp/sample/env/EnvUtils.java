package com.tuya.iotapp.sample.env;

import android.content.Context;

import com.tuya.iotapp.network.api.RegionHostConst;
import com.tuya.iotapp.network.api.TYNetworkManager;

/**
 * EnvUtils
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 2:31 PM
 */
public class EnvUtils {
    public static final int ENV_ONLINE = 0;
    public static final int ENV_PRE = 1;
    public static final int ENV_DAILY = 2;

    private static int mEnv;

    public static void setEnv(Context context, int env) {
        mEnv = env;
    }

    public static int getCurrentEnv(Context context) {
        return mEnv;
    }

    public static void ChooseRegionHost(Context context, int position) {
        switch (position) {
            case 0:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_US);
                break;
            case 1:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_CN);
                break;
            case 2:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_EU);
                break;
            case 3:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_IN);
                break;
            case 4:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_UE);
                break;
            case 5:
                TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_WE);
                break;
        }
    }
}
