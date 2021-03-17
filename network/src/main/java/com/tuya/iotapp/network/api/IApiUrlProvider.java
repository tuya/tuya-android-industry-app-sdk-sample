package com.tuya.iotapp.network.api;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 8:27 PM
 */
public interface IApiUrlProvider {
    String getApiUrl();

    String getApiUrlByCountryCode(String countryCode);
}
