package com.tuya.iotapp.login.bean;

import java.io.Serializable;

/**
 * TokenBean
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:29 PM
 */
public class TokenBean implements Serializable {
    private String uid;
    private String access_token;
    private String refresh_token;
    private int expire;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
