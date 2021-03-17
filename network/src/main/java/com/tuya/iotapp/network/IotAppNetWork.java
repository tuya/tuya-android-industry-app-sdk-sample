package com.tuya.iotapp.network;

import android.content.Context;

import com.tuya.iotapp.network.api.IApiUrlProvider;
import com.tuya.iotapp.network.business.BusinessResponse;
import com.tuya.iotapp.network.http.IotAppNetWorkConfig;
import com.tuya.iotapp.network.http.IotAppNetWorkExecutorManager;
import com.tuya.iotapp.network.security.SecurityInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;

/**
 *  IotAppNetWork
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:43 PM
 */
public class IotAppNetWork {

    private volatile static OkHttpClient sOkHttpClient;

    private static IotAppNetWorkConfig iotAppNetWorkConfig = new IotAppNetWorkConfig();

    private static String USER_AGENT = "TY-UA=APP/Android";

    public static Context mAppContext;
    public static String mAppId;
    public static String mAppSecret;
    public static String mTtid;
    public static IApiUrlProvider mApiUrlProvider;

    private static boolean mAppDebug = false;

    public static OkHttpClient getOkHttpClient() {
        if(sOkHttpClient == null) {
            synchronized (IotAppNetWork.class) {
                if (sOkHttpClient == null) {
                    sOkHttpClient = newOkHttpClient();
                }
            }
        }
        return sOkHttpClient;
    }

    private static OkHttpClient newOkHttpClient() {
        OkHttpClient.Builder builder  = new OkHttpClient.Builder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        //builder.eventListenerFactory(HttpEventListener)
        builder.addInterceptor(new SecurityInterceptor(mAppId, mAppSecret));
        ExecutorService networkExecutor = IotAppNetWorkExecutorManager.getNetWorkExecutor();
        if (networkExecutor != null) {
            builder.dispatcher(new Dispatcher(networkExecutor));
        }

        return builder.build();
    }

    public static void initialize(Context context, String appId, String appSecret, String ttid,IApiUrlProvider provider) {
        mAppContext = context;
        mAppId = appId;
        mAppSecret = appSecret;
        mTtid = ttid;

        if(provider != null) {
            mApiUrlProvider = provider;
        }

        //设置debug模式
        setDebugMode(mAppDebug);
        //基本的安全校验  是否还需要，应该是跟appId&appSecret&图片 校验等

        //todo:加解密相关
        //TuyaNetworkSecurityInit.initJNI(context);
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
        Map<String, String> header = new HashMap<>(1);
        header.put("User-Agent", USER_AGENT); //todo:不拼接后续版本号，直接使用是否有问题
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
