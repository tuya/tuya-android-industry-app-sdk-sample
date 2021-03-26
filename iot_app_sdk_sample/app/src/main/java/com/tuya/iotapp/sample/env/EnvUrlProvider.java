package com.tuya.iotapp.sample.env;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.api.IApiUrlProvider;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 5:50 PM
 */
public class EnvUrlProvider implements IApiUrlProvider {
    private Context mContext;
    private String URL_ONLINE_CN = "https://openapi.tuyacn.com";

    public EnvUrlProvider(Context context) {
        mContext = context;
    }

    @Override
    public String getApiUrl() {
        return URL_ONLINE_CN;
    }

    @Override
    public String getApiUrlByCountryCode(String countryCode) {
        if (TextUtils.isEmpty(countryCode)) {
            return getApiUrl();
        }
        return null;
    }
}
