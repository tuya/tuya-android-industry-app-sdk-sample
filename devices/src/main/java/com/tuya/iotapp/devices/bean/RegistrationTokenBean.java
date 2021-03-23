package com.tuya.iotapp.devices.bean;

/**
 * RegistrationTokenBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/22 3:26 PM
 */
public class RegistrationTokenBean {
    private long expire_time;
    private String region;
    private String token;
    private String secret;
    private Object extension;

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Object getExtension() {
        return extension;
    }

    public void setExtension(Object extension) {
        this.extension = extension;
    }

    public String toString() {
        return "expire_time : " + expire_time + " region : " + region + " token : " + token
                + " secret : " + secret + " extension : " + extension;
    }
}
