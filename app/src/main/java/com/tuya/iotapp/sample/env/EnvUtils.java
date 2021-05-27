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

    private static final String PRE_REGION_HOST_US = "https://openapi-us.wgine.com"; //America
    private static final String PRE_REGION_HOST_CN = "https://openapi-cn.wgine.com"; //China
    private static final String PRE_REGION_HOST_EU = "https://openapi-eu.wgine.com"; //Europe
    private static final String PRE_REGION_HOST_IN = "https://openapi-ind.wgine.com"; //India
    private static final String PRE_REGION_HOST_UE = "https://openapi-ueaz.wgine.com"; //EasternAmerica
    private static final String PRE_REGION_HOST_WE =  "https://openapi-weaz.wgine.com"; //WesternEurope


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
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_US);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_US);
                }
                break;
            case 1:
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_CN);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_CN);
                }
                break;
            case 2:
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_EU);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_EU);
                }
                break;
            case 3:
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_IN);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_IN);
                }
                break;
            case 4:
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_UE);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_UE);
                }
                break;
            case 5:
                if (ENV_ONLINE == mEnv) {
                    TYNetworkManager.setRegionHost(RegionHostConst.REGION_HOST_WE);
                } else if (ENV_PRE == mEnv) {
                    TYNetworkManager.setRegionHost(PRE_REGION_HOST_WE);
                }
                break;
        }
    }
}
