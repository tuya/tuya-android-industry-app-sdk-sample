package com.tuya.dev.network.request;

/**
 * 网络入参接口
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:44 PM
 */
public interface IRequest {

    public static final String POST = "POST";
    public static final String GET = "GET";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";

    /**
     * 获取请求url
     *
     * @return
     */
    String getRequestUrl();

    /**
     * 是否需要缓存
     *
     * @return
     */
    boolean shouldCache();

    /**
     * 是否抛出缓存异常
     *
     * @return
     */
    boolean throwCache();

}
