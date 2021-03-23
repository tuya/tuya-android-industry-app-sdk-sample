package com.tuya.iotapp.network.accessToken;

import com.tuya.iotapp.network.accessToken.bean.TokenBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.business.BusinessResult;
import com.tuya.iotapp.network.request.IRequest;
import com.tuya.iotapp.network.request.IotApiParams;

/**
 * Access token repository
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 5:15 PM
 */
public class AccessTokenRepository extends Business {
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
        IotApiParams apiParams = new IotApiParams(String.format("/v1.0/token/{%s}", refreshToken), "", IRequest.GET);
        synchronized (this) {
            BusinessResult<TokenBean> result = syncRequest(apiParams, TokenBean.class);
            if (result.getBizResponse().isSuccess()) {
                TokenBean tokenBean = result.getBizResult();

                storeInfo(tokenBean,
                        result.getBizResponse().getT());
            } else {
                //todo 抛异常
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
        //todo recover token info from sp

    }

    /**
     * @param tokenBean
     * @param t
     */
    void storeInfo(TokenBean tokenBean,
                   long t) {
        this.accessToken = tokenBean.getAccess_token();
        this.refreshToken = tokenBean.getRefresh_token();
        this.expireTime = tokenBean.getExpire();
        this.uid = tokenBean.getUid();
        this.lastRefreshTime = t;

        //todo store token info into sp
    }
}
