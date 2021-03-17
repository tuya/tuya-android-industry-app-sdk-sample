package com.tuya.iotapp.sample;

import android.text.method.TextKeyListener;

import com.alibaba.fastjson.JSONObject;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.common.utils.MD5Util;
import com.tuya.iotapp.network.business.Business;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;




/**
 * TODO feature
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/16 6:07 PM
 */
public class TestBusiness extends Business {

    private static final String LOGIN_API = "v1.0/iot-03/users/login";


    public void test() {
        IotApiParams params = new IotApiParams(LOGIN_API, "1.0", "POST");
        String password = MD5Util.md5AsBase64("Lbn123456");
        //用户名：15068975916  密码：Lbn123456
        params.putPostData("user_name", "18640825065");
        params.putPostData("password", "b8a762334ab0c2f25a0503a86b152f77");
        params.putPostData("project_code", "p1615796832753ggk9jt");
        asyncRequest(params, new ResultListener<JSONObject>() {
            @Override
            public void onFailure(BusinessResponse bizResponse, JSONObject bizResult, String apiName) {
                LogUtils.d("TestBusiness", "====-failure======code: " + bizResponse.getCode()+" msg:" +bizResponse.getMsg());
            }

            @Override
            public void onSuccess(BusinessResponse bizResponse,JSONObject bizResult, String apiName) {
                LogUtils.d("TestBusiness", "====-onSuccess======"+bizResult);
            }
        });
    }
}
