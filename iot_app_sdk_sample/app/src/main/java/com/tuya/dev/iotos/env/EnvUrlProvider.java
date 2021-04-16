package com.tuya.dev.iotos.env;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.dev.common.utils.LogUtils;
import com.tuya.dev.network.api.IApiUrlProvider;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 5:50 PM
 */
public class EnvUrlProvider implements IApiUrlProvider {
    private static final String CN = "cn"; //中国
    private static final String US = "us"; //美洲区
    private static final String EU = "eu"; //欧洲
    private static final String IN = "in"; //印度区

    private int env;
    private Context mContext;
    private String URL_ONLINE_CN = "https://openapi.tuyacn.com";  //中国
    private String URL_PRE_CN = "https://openapi-cn.wgine.com";
    private String URL_DEV_CN = "https://openapi-daily.tuya-inc.cn";

    private String URL_ONLINE_US = "https://openapi.tuyaus.com";  //美洲区

    private String URL_ONLINE_EU = "https://openapi.tuyaeu.com";  //欧洲

    private String URL_ONLINE_IN = "https://openapi.tuyain.com";  //印度区

    public EnvUrlProvider(Context context) {
        mContext = context;
    }

    @Override
    public String getApiUrl() {
        env = EnvUtils.getCurrentEnv(mContext);
        LogUtils.d("env", "current env : " + env);
        if (env == EnvUtils.ENV_PRE) {
            return URL_PRE_CN;
        } else if (env == EnvUtils.ENV_DAILY) {
            return URL_DEV_CN;
        } else {
            return URL_ONLINE_CN;
        }
    }

    @Override
    public String getApiUrlByCountryCode(String countryCode) {
        if (TextUtils.isEmpty(countryCode)) {
            return getApiUrl();
        }
        String code = countryCode.toLowerCase();
        if (CN.equals(code)) {
            return getApiUrl();
        } else if (US.equals(code)) {
            return URL_ONLINE_US;
        } else if (EU.equals(code)) {
            return URL_ONLINE_EU;
        } else if (IN.equals(code)) {
            return URL_ONLINE_IN;
        }
        return null;
    }
}
