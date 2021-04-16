package com.tuya.dev.network.security;

import androidx.annotation.NonNull;

import com.tuya.dev.iot.sign.TuyaSign;
import com.tuya.dev.common.utils.LogUtils;
import com.tuya.dev.common.utils.SHA256Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * 加签拦截器
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/17 4:17 PM
 */
public class SignInterceptor implements Interceptor {
    private static final String TAG = "SignInterceptor";

    private static final String T = "t";
    private static final String SIGN_METHOD = "sign_method";
    private static final String SIGN = "sign";
    private static final String HMACSHA256 = "HMAC-SHA256";

    private static final String ACCESS_TOKEN_HEAD = "access_token";

    public SignInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        LogUtils.d("interceptor", "  before request");
        Request request = chain.request();

        LogUtils.d("interceptor", "  request and sign");
        Response response = chain.proceed(newRequestWithSign(request));
        LogUtils.d("interceptor", "  after response ");
        return response.newBuilder().build();
    }

    /**
     * sign 拼接
     *
     * @param request
     * @return
     */
    private String stringToSign(Request request) {
        StringBuilder builder = new StringBuilder();

        //------------- HTTPMethod ---------------//
        builder.append(request.method());
        builder.append("\n");

        //------------- Content-SHA256 -------------//
        String sha256Body = "";
        RequestBody body = request.body();

        try {
            sha256Body = SHA256Util.sha256(requestBodyToString(body));
            builder.append(sha256Body);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.d("sha256Body", sha256Body);


        builder.append("\n");

        //------------- Header --------------//
        builder.append("\n");

        //------------- URL -----------------//
        HttpUrl url = request.url();
        StringBuilder urlBuilder = new StringBuilder(url.encodedPath());

        if (url.encodedQuery() != null) {
            urlBuilder.append("?");

            StringBuilder queryBuilder = new StringBuilder();
            ArrayList<String> names = new ArrayList<>(url.queryParameterNames());
            Collections.sort(names, (lhs, rhs) -> lhs.compareTo(rhs));
            for (String name : names) {
                queryBuilder.append(name);
                queryBuilder.append("=");
                queryBuilder.append(url.queryParameter(name));
                queryBuilder.append("&");
            }
            urlBuilder.append(queryBuilder.subSequence(0, queryBuilder.length() - 1).toString());
        }

        builder.append(urlBuilder.toString());

        LogUtils.d("url", builder.toString());
        return builder.toString();
    }

    private String requestBodyToString(RequestBody requestBody) throws IOException {
        if (requestBody == null) {
            return "";
        }

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return buffer.readUtf8();
    }

    private Request newRequestWithSign(@NonNull Request request) {
        //todo:目前的验签方式，对齐的是小程序对接涂鸦云的方案，，带我们有了自己的方式后做特替换；其中部分为必要参数
        String t = String.valueOf(System.currentTimeMillis());

        Request.Builder builder = request.newBuilder();

        builder.addHeader(T, t);
        builder.addHeader(SIGN_METHOD, HMACSHA256);

        StringBuilder toSignBuilder = new StringBuilder();

        String accessToken = request.header(ACCESS_TOKEN_HEAD);
        if (accessToken != null) {
            toSignBuilder.append(accessToken);
        }
        toSignBuilder.append(t);

        try {
            String stringToSign = stringToSign(request);
            LogUtils.d("stringToSign", stringToSign);
            String sign = TuyaSign.sign(toSignBuilder.toString(), stringToSign);
            LogUtils.d("sign", sign);
            builder.addHeader(SIGN, sign);
        } catch (IOException e) {
            LogUtils.e(TAG, e.getMessage());
        }

        return builder.build();
    }
}
