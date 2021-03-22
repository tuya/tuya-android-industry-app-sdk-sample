package com.tuya.iotapp.network.accessToken;

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

    public void storeInfo(String accessToken,
                          String refreshToken,
                          long t,
                          long expireTime) {
        accessTokenRepository.storeInfo(accessToken,
                refreshToken,
                t,
                expireTime);
    }

    public AccessTokenRepository getAccessTokenRepository() {
        return accessTokenRepository;
    }
}
