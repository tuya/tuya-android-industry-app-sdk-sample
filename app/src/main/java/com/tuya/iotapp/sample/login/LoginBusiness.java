package com.tuya.iotapp.sample.login;

import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.sample.login.bean.LoginBean;

/**
 * 测试类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 6:07 PM
 */
public class LoginBusiness extends Business {

    private static final String LOGIN_API = "v1.0/iot-03/users/login";

    /**
     * 登录接口
     *
     * @param account
     * @param password
     * @param listener
     */
    public void login(String account,
                      String password,
                      ResultListener<LoginBean> listener) {
        IotApiParams params = new IotApiParams(LOGIN_API, "1.0", "POST");
//        String password = MD5Util.md5AsBase64("Lbn123456");

        //用户名：15068975916  密码：Lbn123456
        params.putPostData("user_name", account);
        params.putPostData("password", password);
        params.putPostData("project_code", "p1615796832753ggk9jt");
        asyncRequest(params, LoginBean.class, listener);
    }
}
