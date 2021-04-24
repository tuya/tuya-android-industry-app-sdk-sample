package com.tuya.dev.network.accessToken;

import androidx.annotation.NonNull;

import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.dev.network.business.BusinessResponse;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Interceptor for manage access_token
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/18 3:40 PM
 */
public class AccessTokenInterceptor implements Interceptor {
    //Default pre expire time, unit second
    private static final long DEFAULT_EXPIRE_TIME_PRE = 60;

    private static final String ACCESS_TAG = "ACCESS";
    private static final String ACCESS_TOKEN_HEAD = "access_token";

    private final AccessTokenRepository accessTokenRepository;

    public AccessTokenInterceptor(AccessTokenRepository accessTokenRepository) {
        this.accessTokenRepository = accessTokenRepository;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();

        //------------ token relative request -------------//
        if (ACCESS_TAG.equals(request.tag(String.class))) {
            return chain.proceed(request);
        }

        //------------ service management operations -------------//
        checkAuth();
        Response response = chain.proceed(newRequestWithAccessToken(request));
        return handleTokenError(response);
    }

    private void checkAuth() {
        if (accessTokenRepository.getLastRefreshTime() + accessTokenRepository.getExpireTime() * 1000 < System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_PRE * 1000) {
            accessTokenRepository.refreshToken();
        }
    }

    private Request newRequestWithAccessToken(@NonNull Request request) {
        String accessToken = accessTokenRepository.getAccessToken();
        return request.newBuilder()
                .addHeader(ACCESS_TOKEN_HEAD, accessToken)
                .build();
    }

    private Response handleTokenError(Response response) {
        try {
            ResponseBody responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            String strBody = responseBodyCopy.string();

            BusinessResponse bizResponse = JsonParser.parseObject(strBody, BusinessResponse.class);
            if (!bizResponse.isSuccess() && bizResponse.getCode() == 1010) {
                accessTokenRepository.refreshToken();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
