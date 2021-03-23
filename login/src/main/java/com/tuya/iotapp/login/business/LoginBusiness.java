package com.tuya.iotapp.login.business;

import com.tuya.iotapp.network.accessToken.bean.TokenBean;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;

/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/20 2:12 PM
 */
public class LoginBusiness extends Business {

    private static final String LOGIN_API = "/v1.0/iot-03/users/login";

    /**
     * old :  user_name:18640825065   password:（b8a762334ab0c2f25a0503a86b152f77）Lbn123456  project-code：p1615796832753ggk9jt
     * new:  xiaoxiao.li@tuya.com  c679a76bc4315372c57ad1ba0a8e59f6
     *
     * @param userName
     * @param password
     * @param listener
     */
    public void login(String countryCode, String userName, String password, ResultListener<TokenBean> listener) {
        //todo:password 密码需要做一次md5加密  加密规则是否直接md5就好，待确认
        IotApiParams params = new IotApiParams(LOGIN_API, "1.0", "POST", countryCode);
        //用户名：15068975916  密码：Lbn123456
        params.putPostData("user_name", userName);
        params.putPostData("password", password);
        params.setSessionRequire(false);
        asyncRequest(params, TokenBean.class, listener);
    }

}
