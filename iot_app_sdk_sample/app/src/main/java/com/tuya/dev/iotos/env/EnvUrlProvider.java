package com.tuya.dev.iotos.env;

import com.tuya.dev.network.api.IApiUrlProvider;

/**
 * Env Url Provider
 * todo redesign in the future
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 5:50 PM
 */
public class EnvUrlProvider implements IApiUrlProvider {
    private String env;


    public EnvUrlProvider() {
    }

    @Override
    public String getApiUrl() {
//        if (BuildConfig.DEBUG) {
//            return DevEndpoint.PRE;
//        }
        return EnvUtils.getEndpoint();
    }

    @Override
    public String getApiUrlByCountryCode(String countryCode) {
        return getApiUrl();
    }
}
