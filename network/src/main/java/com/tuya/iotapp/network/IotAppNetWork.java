package com.tuya.iotapp.network;

import android.content.Context;

import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.common.utils.SHA256Util;
import com.tuya.iotapp.network.accessToken.AccessTokenInterceptor;
import com.tuya.iotapp.network.accessToken.AccessTokenManager;
import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.http.IotAppNetWorkConfig;
import com.tuya.iotapp.network.http.IotAppNetWorkExecutorManager;
import com.tuya.iotapp.network.security.SignInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 * IotAppNetWork
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:43 PM
 */
public class IotAppNetWork {

    private static final String HMACSHA256 = "HMAC-SHA256";
    private static final String CLIENT_ID = "client_id";
    private static final String T = "t";
    private static final String SIGN_METHOD = "sign_method";
    private static final String SIGN = "sign";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";


    private volatile static OkHttpClient sOkHttpClient;

    private static IotAppNetWorkConfig iotAppNetWorkConfig = new IotAppNetWorkConfig();

    public static Context mAppContext;
    public static String mAppId;
    public static String mAppSecret;
    public static String mTtid;
    public static IApiUrlProvider mApiUrlProvider;
    public static String mAccessToken = "";

    private static boolean mAppDebug = false;

    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (IotAppNetWork.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = newOkHttpClient();
                }
            }
        }
        return sOkHttpClient;
    }

    private static OkHttpClient newOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        //builder.eventListenerFactory(HttpEventListener)

        // Access Token Interceptor
        builder.addInterceptor(new AccessTokenInterceptor(AccessTokenManager.INSTANCE.getAccessTokenRepository()));
        // Sign Interceptor
        builder.addInterceptor(new SignInterceptor(mAppId, mAppSecret));


        ExecutorService networkExecutor = IotAppNetWorkExecutorManager.getNetWorkExecutor();
        if (networkExecutor != null) {
            builder.dispatcher(new Dispatcher(networkExecutor));
        }

        return builder.build();
    }

    public static void initialize(Context context, String appId, String appSecret, String ttid, IApiUrlProvider provider) {
        mAppContext = context;
        mAppId = appId;
        mAppSecret = appSecret;
        mTtid = ttid;

        if (provider != null) {
            mApiUrlProvider = provider;
        }
        //设置debug模式
        setDebugMode(mAppDebug);
    }

    private static void setDebugMode(boolean mode) {
        mAppDebug = mode;
    }


    public static void setIotAppNetWorkConfig(IotAppNetWorkConfig config) {
        iotAppNetWorkConfig = config;
    }

    public static IotAppNetWorkConfig getIotAppNetWorkConfig() {
        return iotAppNetWorkConfig;
    }

    public static Map<String, String> getRequestHeaders() {
        Map<String, String> header = new HashMap<>();

        //todo:目前的验签方式，对齐的是小程序对接涂鸦云的方案，，带我们有了自己的方式后做特替换；其中部分为必要参数
        long t = System.currentTimeMillis();
        header.put(CLIENT_ID, mAppId);
        header.put(T, t + "");
        header.put(SIGN_METHOD, HMACSHA256);
        header.put("access_token", mAccessToken);

        LogUtils.d("IoaAppNetWork", " mAccessToken :" + mAccessToken);
        try {
            String sign = SHA256Util.HMACSHA256(mAppId + mAccessToken + t, mAppSecret).toUpperCase();
            header.put(SIGN, sign);
        } catch (Exception e) {
            LogUtils.d(HMACSHA256, e.getMessage());
        }
        header.put(CONTENT_TYPE, CONTENT_TYPE_VALUE);

        return header;
    }

    public static Context getAppContext() {
        return mAppContext.getApplicationContext();
    }

    public static void setAppContext(Context mAppContext) {
        IotAppNetWork.mAppContext = mAppContext;
    }

    public static String getTtid() {
        return mTtid;
    }

    public static void setTtid(String mTtid) {
        IotAppNetWork.mTtid = mTtid;
    }

    public static String getServiceHostUrl() {
        return mApiUrlProvider.getApiUrl();
    }

    public static String getServiceHostUrl(String countryCode) {
        return mApiUrlProvider.getApiUrlByCountryCode(countryCode);
    }

    public static String getAccessToken() {
        return mAccessToken;
    }

    public static void setAccessToken(String accessToken) {
        IotAppNetWork.mAccessToken = accessToken;
    }

    /**
     * 监听业务接口
     *
     * @param <T>
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
}
