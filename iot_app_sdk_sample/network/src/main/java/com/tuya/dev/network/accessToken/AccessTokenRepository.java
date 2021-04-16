package com.tuya.dev.network.accessToken;

import android.text.TextUtils;

import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.dev.common.kv.KvManager;
import com.tuya.dev.network.accessToken.bean.TokenBean;
import com.tuya.dev.network.business.Business;
import com.tuya.dev.network.business.BusinessResult;
import com.tuya.dev.network.request.IRequest;
import com.tuya.dev.network.request.IotApiParams;

/**
 * Access token repository
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 5:15 PM
 */
public class AccessTokenRepository extends Business {
    private static final String AT_KEY = "atKey";
    private static final String AT_TIME = "atTime";


    private String refreshToken;
    private String accessToken;
    private String uid;

    /**
     * last access token refresh time
     */
    private long lastRefreshTime;
    /**
     * access token expire time
     */
    private long expireTime;

    public AccessTokenRepository() {
        recoverInfo();
    }

    public void refreshToken() {
        IotApiParams apiParams = new IotApiParams(String.format("/v1.0/token/%s",
                TextUtils.isEmpty(refreshToken) ? "" : refreshToken),
                "",
                IRequest.GET);
        apiParams.setSessionRequire(false);
        synchronized (this) {
            BusinessResult<TokenBean> result = syncRequest(apiParams, TokenBean.class);
            if (result.getBizResponse().isSuccess()) {
                TokenBean tokenBean = result.getBizResult();

                storeInfo(tokenBean,
                        result.getBizResponse().getT());
            } else {
                //todo Throw Exception
            }
        }

    }

    String getRefreshToken() {
        return refreshToken;
    }

    String getAccessToken() {
        return accessToken;
    }

    String getUid() {
        return uid;
    }

    long getLastRefreshTime() {
        return lastRefreshTime;
    }

    long getExpireTime() {
        return expireTime;
    }

    private void recoverInfo() {
        String strToken = KvManager.getString(AT_KEY);
        if (!"".equals(strToken)) {
            TokenBean bean = JsonParser.parseObject(strToken, TokenBean.class);
            long t = KvManager.getLong(AT_TIME, 0L);

            cacheInfo(bean, t);
        }
    }

    private void cacheInfo(TokenBean tokenBean,
                           long t) {
        this.accessToken = tokenBean.getAccess_token();
        this.refreshToken = tokenBean.getRefresh_token();
        this.expireTime = tokenBean.getExpire();
        this.uid = tokenBean.getUid();
        this.lastRefreshTime = t;
    }

    /**
     * @param tokenBean
     * @param t
     */
    void storeInfo(TokenBean tokenBean,
                   long t) {
        cacheInfo(tokenBean, t);

        KvManager.set(AT_KEY, JsonParser.toJsonString(tokenBean));
        KvManager.set(AT_TIME, t);
    }

    void clearInfo() {
        this.accessToken = "";
        this.refreshToken = "";
        this.expireTime = 0L;
        this.uid = "";
        this.lastRefreshTime = 0L;

        KvManager.set(AT_KEY, "");
        KvManager.set(AT_TIME, 0L);
    }
}
