package com.tuya.dev.login.business;

import com.tuya.dev.common.utils.SHA256Util;
import com.tuya.dev.network.accessToken.bean.TokenBean;
import com.tuya.dev.network.business.Business;
import com.tuya.dev.network.request.IotApiParams;
import com.tuya.dev.network.request.ResultListener;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:12 PM
 */
public class LoginBusiness extends Business {

    private static final String LOGIN_API = "/v1.0/iot-03/users/login";

    /**
     * login
     * @param userName
     * @param password
     * @param listener
     */
    public void login(String countryCode, String userName, String password, ResultListener<TokenBean> listener) {
        IotApiParams params = new IotApiParams(LOGIN_API, "1.0", "POST", countryCode);
        params.putPostData("username", userName);
        params.putPostData("password", SHA256Util.sha256(password).toLowerCase());
        params.setSessionRequire(false);
        asyncRequest(params, TokenBean.class, listener);
    }

}
