package com.tuya.iotapp.network.accessToken.bean;

/**
 * Token Bean
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 5:26 PM
 */
public class TokenBean {

    private String uid;
    private String access_token;
    private Integer expire_time;
    private String refresh_token;

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

    public Integer getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Integer expire_time) {
        this.expire_time = expire_time;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
