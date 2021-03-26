package com.tuya.iotapp.network.request;

import com.tuya.iotapp.network.business.BusinessResponse;

/**
 * 结果回调监听接口
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 9:17 PM
 */
public interface ResultListener<T> {
    /**
     * 各种错误,统一调用此方法
     *
     * @param bizResponse
     * @param bizResult
     * @param apiName
     */
    void onFailure(BusinessResponse bizResponse, T bizResult, String apiName);

    /**
     * 接口status为ok时,调用此方法
     *
     * @param bizResponse
     * @param bizResult
     * @param apiName
     */
    void onSuccess(BusinessResponse bizResponse, T bizResult, String apiName);
}
