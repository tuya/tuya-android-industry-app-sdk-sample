package com.tuya.iotapp.devices.bean;

/**
 * AuthKeyBean
 *
 * @author xiaoxiao <a href="xiaoxiao.li@tuya.com"/>
 * @since 2021/4/13 3:53 PM
 */
public class AuthKeyBean {
    private String random;
    private String auth_key;


    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public String getAuthKey() {
        return auth_key;
    }

    public void setAuthKey(String auth_key) {
        this.auth_key = auth_key;
    }
}
