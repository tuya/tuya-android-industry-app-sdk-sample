package com.tuya.iotapp.network.response;

/**
 * 网络回参接口
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:56 PM
 */
public interface IResponse {

    /**
     * 响应码
     *
     * @return
     */
    int getCode();

    /**
     * 返回的数据
     *
     * @return
     */
    String getData();
}
