package com.tuya.iotapp.network.request;

import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.tuya.iotapp.common.utils.IotAppUtil;
import com.tuya.iotapp.common.utils.IotCommonUtil;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.utils.IotApiUrlManager;
import com.tuya.iotapp.network.utils.TimeStampManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Iot api params
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 4:54 PM
 */
public class IotApiParams implements IRequest{

    private static final String TAG = "iotApiParams";

    public static final String KEY_VERSION = "v";
    public static final String KEY_API = "a";
    public static final String KEY_POST = "postData";
    public static final String KEY_TIMESTAMP = "time";
    public static final String KEY_SESSION = "sid";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_DEVICEID = "deviceId";
    public static final String KEY_TTID = "ttid";
    public static final String KEY_OS_SYSTEM = "osSystem";
    public static final String KEY_APP_OS = "os";
    public static final String KEY_APP_LANG = "lang";
    public static final String KEY_APP_SIGN = "sign";
    public static final String KEY_APP_ID = "clientId";
    public static final String KEY_REQUEST_ID = "requestId";
    public static final String KEY_PLATFORM = "platform";
    public static final String KEY_TIME_ZONE_ID = "timeZoneId";
    public static final String KEY_GID = "gid";

    protected ConcurrentHashMap<String, String> urlGETParams = new ConcurrentHashMap<>();

    private JSONObject postData;
    private String apiName;
    private String apiVersion = "*";
    private String session;
    private String encode;
    private boolean sessionRequire = true;
    private boolean locationRequire = true;
    private boolean shouldCache = false;
    private boolean throwCache = false;
    private byte[] dataBytes;
    private long requestTime;

    private Object cacheDefaultData;

    private String serverHostUrl;
    private String method;

    private ResultListener listener = null;
    private boolean spRequest;
    private long gid;

    public IotApiParams() {
        initUrlParams(null);
    }

    public IotApiParams(String apiName, String apiVersion, String method) {
        this.apiName = apiName;
        this.apiVersion = apiVersion;
        this.method = method;
        initUrlParams(null);
    }

    public void initUrlParams(String countryCode) {
        urlGETParams.put(KEY_VERSION, "*");
        urlGETParams.put(KEY_APP_ID, IotAppNetWork.mAppId);
        urlGETParams.put(KEY_APP_OS, "Android");
        urlGETParams.put(KEY_APP_LANG, IotAppUtil.getLang(IotAppNetWork.mAppContext));
        urlGETParams.put(KEY_TTID, IotAppNetWork.getTtid());
        urlGETParams.put(KEY_OS_SYSTEM, Build.VERSION.RELEASE);
        urlGETParams.put(KEY_REQUEST_ID, UUID.randomUUID().toString());
        urlGETParams.put(KEY_PLATFORM, Build.MODEL);
        urlGETParams.put(KEY_TIME_ZONE_ID, IotCommonUtil.getTimeZoneId());
        if (TextUtils.isEmpty(countryCode)) {
            serverHostUrl = IotAppNetWork.getServiceHostUrl();
        } else {
            serverHostUrl = IotAppNetWork.getServiceHostUrl(countryCode);
        }
    }

    public Map<String, String> getUrlParams() {
        ConcurrentHashMap<String, String> urlParams = new ConcurrentHashMap<>(urlGETParams);

        urlParams.put(KEY_API, getApiName());
        if (gid != 0) {
            urlParams.put(KEY_GID, String.valueOf(gid));
        }
        urlParams.put(KEY_VERSION, getApiVersion());

        if (isSessionRequire()) {
            if (!TextUtils.isEmpty(getSession())) {
                urlParams.put(KEY_SESSION, getSession());
            } else {
                LogUtils.d(TAG, "Need Login");
                urlParams.remove(KEY_SESSION);
            }
        } else {
            urlParams.remove(KEY_SESSION);
        }
        return urlParams;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public JSONObject getPostData() {
        return postData;
    }

    public String getPostDataString() {
        return postData.toString();
    }

    public void setPostData(JSONObject postData) {
        this.postData = postData;
    }

    public IotApiParams putPostData(String key, Object value) {
        if (postData == null) {
            postData = new JSONObject();
        }
        postData.put(key, value);
        return this;
    }

    public boolean hasPostData() {
        return postData != null;
    }

    public byte[] getDataBytes() {
        return dataBytes;
    }

    public void setDataBytes(byte[] dataBytes) {
        this.dataBytes = dataBytes;
    }

    public void updateRequestId() {
        urlGETParams.put(KEY_REQUEST_ID, UUID.randomUUID().toString());
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public boolean isSessionRequire() {
        return sessionRequire;
    }

    public void setSessionRequire(boolean sessionRequire) {
        this.sessionRequire = sessionRequire;
    }

    public String getServerHostUrl() {
        return serverHostUrl;
    }

    public void setServerHostUrl(String serverHostUrl) {
        this.serverHostUrl = serverHostUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getRequestUrl() {
        return IotApiUrlManager.getUrlWithQueryString(true, getServerHostUrl(), new HashMap<String, String>());
    }

    @Override
    public Map<String, String> getRequestBody() {
        Map<String, String> postData = getPostBody();
        if (isSessionRequire()) {
            if (!TextUtils.isEmpty(getSession())) {
                postData.put(KEY_SESSION, getSession());
            } else {
                postData.remove(KEY_SESSION);
            }
        } else {
            postData.remove(KEY_SESSION);
        }
        ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>(getUrlParams());
        data.put(KEY_TIMESTAMP, String.valueOf(TimeStampManager.instance().getCurrentTimeStamp()));
        data.putAll(postData);
        data.remove(KEY_POST);
        postData.putAll(data);
        return postData;
    }

    public Map<String, String> getPostBody() {
        ConcurrentHashMap<String, String> postData = new ConcurrentHashMap<>();
        if (hasPostData()) {
            postData.put(KEY_POST, getPostDataString());
        }
        return postData;
    }

    @Override
    public String getRequestKey() {
        Map<String, String> params = getUrlParams();
        postData.put(KEY_POST, getPostDataString());

        return IotApiUrlManager.getRequestKeyBySorted(params);
    }

    @Override
    public boolean shouldCache() {
        return shouldCache;
    }

    @Override
    public boolean throwCache() {
        return throwCache;
    }
}
