package com.tuya.dev.network.accessToken;

import com.tuya.dev.network.accessToken.bean.TokenBean;

/**
 * Manage Access Token
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/19 9:44 AM
 */
public enum AccessTokenManager {
    INSTANCE;

    AccessTokenRepository accessTokenRepository = new AccessTokenRepository();

    /**
     * Refresh Access by code
     */
    public void refreshToken() {
        accessTokenRepository.refreshToken();
    }

    public String getUid() {
        return accessTokenRepository.getUid();
    }

    public void storeInfo(TokenBean tokenBean,
                          long t) {
        accessTokenRepository.storeInfo(tokenBean,
                t);
    }

    public void clearInfo() {
        accessTokenRepository.clearInfo();
    }

    public AccessTokenRepository getAccessTokenRepository() {
        return accessTokenRepository;
    }
}
