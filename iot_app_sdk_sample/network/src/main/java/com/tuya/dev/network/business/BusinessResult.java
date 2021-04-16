package com.tuya.dev.network.business;

/**
 * 业务返回封装
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 5:19 PM
 */
public class BusinessResult<T> {

    private String apiName;

    private BusinessResponse bizResponse;

    private T bizResult;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public BusinessResponse getBizResponse() {
        return bizResponse;
    }

    public void setBizResponse(BusinessResponse bizResponse) {
        this.bizResponse = bizResponse;
    }

    public T getBizResult() {
        return bizResult;
    }

    public void setBizResult(T bizResult) {
        this.bizResult = bizResult;
    }

}
