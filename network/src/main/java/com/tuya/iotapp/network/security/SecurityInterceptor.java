package com.tuya.iotapp.network.security;

import com.alibaba.fastjson.JSON;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.common.utils.SHA256Util;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *  加签拦截器
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 4:17 PM
 */
public class SecurityInterceptor implements Interceptor {
    private String mClientId;
    private String mSecret;

    public SecurityInterceptor(String appId, String appSecret) {
        mClientId = appId;
        mSecret = appSecret;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        LogUtils.d("interceptor", "  获取request之前");
        Request request = chain.request();
        //构造加密字符串
        String stringToSign = stringToSign(request);

        LogUtils.d("interceptor", "  获取request 并处理加密");
        Response response = chain.proceed(request);
        LogUtils.d("interceptor", "  获取response 后");
        return response.newBuilder().build();
    }

    /**
     * sign 拼接
     * @param request
     * @return
     */
    private String stringToSign(Request request) {
        RequestBody body = request.body();
        String sha256Body = SHA256Util.sha256(JSON.toJSONBytes(body));
        StringBuilder builder = new StringBuilder();
        builder.append(request.method());
        builder.append("\n");
        builder.append(sha256Body);
        builder.append("\n");
        builder.append(request.headers().toString());
        builder.append(request.url().toString());

        LogUtils.d("url", builder.toString());
        return builder.toString();
    }
}
