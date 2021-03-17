package com.tuya.iotapp.network.business;
import com.alibaba.fastjson.JSON;

import com.tuya.iotapp.network.response.IotApiResponse;

/**
 * BusinessResponse
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 5:47 PM
 */
public class BusinessResponse extends IotApiResponse {

    //时间校验失败,需要重新请求
    public static final String RESULT_TIME_INVALID = "TIME_VALIDATE_FAILED";
    // session失效，需要重新登陆
    public static final String RESULT_SESSION_INVALID = "USER_SESSION_INVALID";
    //session丢失，需要重新登陆
    public static final String RESULT_SESSION_LOSS = "USER_SESSION_LOSS";


    //api name
    private String apiName;

    //api version
    private String apiVersion;

    public BusinessResponse() {
        success = false;
    }

    public static BusinessResponse Builder(byte[] bytedata) {
        return JSON.parseObject(bytedata, BusinessResponse.class);
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
