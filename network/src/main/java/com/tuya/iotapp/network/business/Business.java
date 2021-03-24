package com.tuya.iotapp.network.business;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.tuya.dev.json_parser.api.JsonParser;
import com.tuya.iotapp.common.utils.LogUtils;
import com.tuya.iotapp.network.IotAppNetWork;
import com.tuya.iotapp.network.http.IotAppNetWorkExecutorManager;
import com.tuya.iotapp.network.http.SimpleResponseCallback;
import com.tuya.iotapp.network.request.IRequest;
import com.tuya.iotapp.network.request.IotApiParams;
import com.tuya.iotapp.network.request.ResultListener;
import com.tuya.iotapp.network.utils.BusinessUtil;
import com.tuya.iotapp.network.utils.ParseHelper;
import com.tuya.iotapp.network.utils.TimeStampManager;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.tuya.iotapp.network.business.CommonBusinessError.BUSINESS_IO_EXCEPTION;
import static com.tuya.iotapp.network.business.CommonBusinessError.BUSINESS_JSON_EXCEPTION;
import static com.tuya.iotapp.network.business.CommonBusinessError.BUSINESS_NETWORK_UNKNOWN;

/**
 * Business
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 5:18 PM
 */
public class Business {
    public static final String TAG = "Business";

    private final Handler mHandler;
    private String tagRequest = toString();
    private boolean isCanceled = false;

    public Business() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mHandler = new Handler();
        } else {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    public Business(Handler handler) {
        mHandler = handler;
    }

    /**
     * OKHTTP pares params
     *
     * @param apiParams
     * @param requestHeaders
     * @param tagRequest
     * @return
     */
    public static Request newOKHttpRequest(IotApiParams apiParams, Map<String, String> requestHeaders, String tagRequest) {
        Request.Builder builder = new Request.Builder();
        if (!TextUtils.isEmpty(tagRequest)) {
            builder.tag(tagRequest);
        }

        for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }

        HttpUrl url = HttpUrl.parse(apiParams.getRequestUrl())
                .newBuilder()
                .encodedPath(apiParams.getApiName())
                .build();

        String requestUrl = url.toString();

        LogUtils.d(TAG, businessLog(apiParams));
        builder.url(requestUrl);

        //follow ResultfulApi protocal
        if (IRequest.GET.equals(apiParams.getMethod())) {
            LogUtils.d(TAG, "newokhttpRequest method : get");
            builder.get();
        } else if (IRequest.PUT.equals(apiParams.getMethod())) {
            LogUtils.d(TAG, "newokhttpRequest method : put");
            builder.put(getRequestBody(apiParams));
        } else if (IRequest.DELETE.equals(apiParams.getMethod())) {
            LogUtils.d(TAG, "newokhttpRequest method : delete");
            builder.delete(null);
        } else {
            if (apiParams.getDataBytes() != null) {
                RequestBody audio = RequestBody.create(MediaType.parse("audio"), apiParams.getDataBytes());
                builder.post(audio);
            } else {
                LogUtils.d(TAG, "newokhttpRequest method : post");
                builder.post(getRequestBody(apiParams));
            }
        }

        // If not require session, mean access_token not need
        if (!apiParams.isSessionRequire()) {
            builder.tag(String.class, "ACCESS");
        }

        return builder.build();
    }

    private static RequestBody getRequestBody(IotApiParams apiParams) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
                apiParams.getPostDataString());
        return body;
    }

    /**
     * 解析为指定对象
     *
     * @param apiParams
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void asyncRequest(IotApiParams apiParams, Class<T> clazz, ResultListener<T> listener) {
        asyncRequest(apiParams, clazz, null, listener);
    }

    /**
     * 异步请求操作
     *
     * @param apiParams
     * @param clazz
     * @param key
     * @param listener
     * @param <T>
     */
    public <T> void asyncRequest(IotApiParams apiParams, final Class<T> clazz, final String key, ResultListener<T> listener) {
        runRequestTask(new RequestTask<T>(apiParams, listener) {
            @Override
            public T onParser(BusinessResponse bizResponse) {
                return ParseHelper.parser(bizResponse, clazz, key);
            }
        });
    }

    /**
     * 解析为指定列表对象
     * todo 优化请求方式
     *
     * @param apiParams
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void asyncRequestList(IotApiParams apiParams, Class<T> clazz, ResultListener<List<T>> listener) {
        asyncRequestList(apiParams, clazz, null, listener);
    }

    /**
     * 解析为指定立碑对象
     *
     * @param apiParams
     * @param clazz
     * @param listener
     * @param <T>
     */
    public <T> void asyncRequestList(IotApiParams apiParams, Class<T> clazz, final String key, ResultListener<List<T>> listener) {
        runRequestTask(new RequestTask<List<T>>(apiParams, listener) {
            @Override
            public List<T> onParser(BusinessResponse bizResponse) {
                return ParseHelper.parserList(bizResponse, clazz, key);
            }
        });
    }

    /**
     * 同步请求
     *
     * @param apiParams
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> BusinessResult<T> syncRequest(final IotApiParams apiParams, final Class<T> clazz) {
        return syncRequestByOkhttp(apiParams, clazz);
    }

    private <T> BusinessResult<T> syncRequestByOkhttp(IotApiParams apiParams, Class<T> clazz) {
        LogUtils.d(TAG, "syncRequestByOkhttp");
        BusinessResult<T> result = new BusinessResult<>();
        result.setApiName(apiParams.getApiName());
        try {
            Request request = newOKHttpRequest(apiParams, IotAppNetWork.getRequestHeaders(), tagRequest);
            Response response = IotAppNetWork.getOkHttpClient().newCall(request).execute();
            int code = response.code();
            if (response.isSuccessful()) {
                try {
                    BusinessResponse bizResponse;
                    ResponseBody body = response.body();
                    if (body == null) {
                        return onFailureResponse(result, BUSINESS_NETWORK_UNKNOWN.getErrorCode(), BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(), String.valueOf(code)));
                    }
                    String bodyString = body.string();
                    bizResponse = JsonParser.parseObject(bodyString, BusinessResponse.class);
                    if (bizResponse.isSuccess()) {
                        T bizResult = ParseHelper.parser(bizResponse, clazz, null);
                        if (bizResult != null) {
                            result.setBizResult(bizResult);
                        }
                    }
                    result.setBizResponse(bizResponse);
                    return result;
                } catch (Exception e) {
                    return onFailureResponse(result, BUSINESS_JSON_EXCEPTION.getErrorCode(), "json error");
                }
            } else {
                return onFailureResponse(result, BUSINESS_NETWORK_UNKNOWN.getErrorCode(), BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(), String.valueOf(code)));
            }
        } catch (IOException e) {
            LogUtils.d(TAG, "handling response error: " + e);
            return onFailureResponse(result, BUSINESS_IO_EXCEPTION.getErrorCode(), BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(), e.toString()));
        }
    }

    <T> BusinessResult<T> onFailureResponse(BusinessResult<T> result, String errorCode, String errorMessage) {
        BusinessResponse bizResponse = new BusinessResponse();
        bizResponse.setCode(Integer.valueOf(errorCode));
        bizResponse.setMsg(errorMessage);
        result.setBizResponse(bizResponse);
        return result;
    }


    public void runRequestTask(Runnable r) {
        IotAppNetWorkExecutorManager.getBusinessExecutor().execute(r);
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void onDestroy() {
        cancelAll();
    }

    /**
     * 取消当前Business对象的所有请求操作
     */
    public void cancelAll() {
        if (TextUtils.isEmpty(tagRequest)) return;
        for (Call call : IotAppNetWork.getOkHttpClient().dispatcher().queuedCalls()) {
            Object tag = call.request().tag();
            if (tag == null) {
                continue;
            }
            if (tag.equals(tagRequest)) {
                call.cancel();
            }
        }
        for (Call call : IotAppNetWork.getOkHttpClient().dispatcher().runningCalls()) {
            Object tag = call.request().tag();
            if (tag == null) {
                continue;
            }
            if (tag.equals(tagRequest)) {
                call.cancel();
            }
        }
        isCanceled = true;
    }

    private static String businessLog(IotApiParams apiParams) {
        try {
            return "\n" + "RequestUrl: " + apiParams.getRequestUrl()  + "\n"
                    + "api:" + apiParams.getApiName() + "\n"
                    + "GetData:" + JsonParser.toJsonString(apiParams.getParams()) + "\n"
                    + "PostData: " + apiParams.getPostDataString() + "\n"
                    + "apiParams: " + JsonParser.toJsonString(apiParams.getUrlParams());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public abstract class RequestTask<T> implements Runnable, Callback, SimpleResponseCallback {

        private static final String TAG = "request_task";
        protected IotApiParams apiParams;

        protected ResultListener<T> listener;

        protected boolean isRetryMode = false;
        protected int retryTime = 1;

        public RequestTask(IotApiParams params, ResultListener<T> listener) {
            this.apiParams = params;
            this.listener = listener;
        }

        @Override
        public void run() {
            //todo：参数校验  判断session是否失效
            onRequest();
        }

        private void onRequest() {
            if (isCanceled) {
                return;
            }
            apiParams.setRequestTime(System.currentTimeMillis());
            LogUtils.d(TAG, "requestByOkhttp");

            Request request = Business.newOKHttpRequest(apiParams, IotAppNetWork.getRequestHeaders()
                    , tagRequest);

            IotAppNetWork.getOkHttpClient().newCall(request).enqueue(this);
        }

        public void onRetry() {
            isRetryMode = true;
            if (retryTime-- > 0) {
                apiParams.updateRequestId();
                onRequest();
            }
        }

        /**
         * 解析成功返回Result的数据格式
         *
         * @param bizResponse
         * @return
         */
        public abstract T onParser(BusinessResponse bizResponse);

        @Override
        public void onFailure(Call call, IOException e) {
            String errorMsg = e.toString();
            LogUtils.d(TAG, "request task onFailure" + errorMsg);
            if (listener == null) {
                return;
            }

            if (call.isCanceled()) {
                return;
            }

            String host = call.request().url().host();
            failed(errorMsg, host);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (listener == null) {
                return;
            }

            if (call.isCanceled()) {
                return;
            }

            int responseCode = response.code();
            if (response.isSuccessful()) {
                try {
                    ResponseBody body = response.body();
                    if (body == null) {
                        handlingFailed(BUSINESS_NETWORK_UNKNOWN.getErrorCode(),
                                BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(),
                                        BUSINESS_NETWORK_UNKNOWN.getErrorCode()));
                        return;
                    }
                    String bodyString = body.string();
                    HttpUrl httpUrl = call.request().url();
                    handlingResponse(responseCode, bodyString, httpUrl);
                } catch (SocketTimeoutException e) {
                    handlingFailed(CommonBusinessError.BUSINESS_READ_RESPONSE_TIMEOUT.getErrorCode()
                            , CommonBusinessError.BUSINESS_READ_RESPONSE_TIMEOUT.getErrorMsg());
                } catch (Exception e) {
                    handlingFailed(CommonBusinessError.BUSINESS_JSON_EXCEPTION.getErrorCode(),
                            CommonBusinessError.BUSINESS_JSON_EXCEPTION.getErrorMsg());
                }
            } else {
                handlingFailed(BUSINESS_NETWORK_UNKNOWN.getErrorCode(),
                        BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(),
                                BUSINESS_NETWORK_UNKNOWN.getErrorCode()));
            }
        }

        @Override
        public void failed(String errorMessage, String host) {
            BusinessResponse bizResponse = new BusinessResponse();
            String errorCode = BusinessUtil.getErrorCode(IotAppNetWork.getAppContext(), errorMessage);

            bizResponse.setCode(Integer.valueOf(errorCode));
            String errorMsg = BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(), errorMessage);
            bizResponse.setMsg(errorMsg);
            onFailure(bizResponse, null, apiParams.getApiName());

        }

        private void onFailure(BusinessResponse bizResponse, T bizResult, String appName) {
            if (mHandler != null) {
                LogUtils.d(TAG, "apiName: " + appName + " errorCode:" +
                        bizResponse.getCode() + "  errorMsg:" + bizResponse.getMsg());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onFailure(bizResponse, bizResult, appName);
                        }
                    }
                });
            } else {
                if (listener != null) {
                    listener.onFailure(bizResponse, bizResult, appName);
                }
            }
        }

        @Override
        public void handlingResponse(int responseCode, String bodyString, HttpUrl httpUrl) {
            BusinessResponse bizResponse;
            long time = apiParams.getRequestTime();
            String decryptResponse = bodyString;

            LogUtils.d(TAG, "api: " + this.apiParams.getApiName() + " " + decryptResponse
                    + " " + (System.currentTimeMillis() - time) + " ms");

            bizResponse = JsonParser.parseObject(decryptResponse, BusinessResponse.class);

            onSuccessResponse(bizResponse, this.apiParams.getApiName());
        }

        @Override
        public void handlingFailed(String errorCode, String errorMessage) {
            BusinessResponse bizResponse = new BusinessResponse();
            bizResponse.setCode(Integer.valueOf(errorCode));
            bizResponse.setMsg(BusinessUtil.checkNetwork(IotAppNetWork.getAppContext(),
                    errorCode));
            onFailure(bizResponse, null, apiParams.getApiName());

        }

        private void onSuccessResponse(BusinessResponse bizResponse, String apiName) {
            if (bizResponse.isSuccess()) {
                onSuccessResult(bizResponse);
            } else if (!isRetryMode) {
                //重试逻辑
                onSuccessResponseWithResultFailure(bizResponse, apiName);
            } else {
                onFailure(bizResponse, null, bizResponse.getApiName());
            }
        }

        private void onSuccessResult(BusinessResponse bizResponse) {
            T bizResult = onParser(bizResponse);
            if (bizResponse.isSuccess()) {
                onSuccess(bizResponse, bizResult, bizResponse.getApiName());
            } else {
                onFailure(bizResponse, bizResult, bizResponse.getApiName());
            }
        }

        private void onSuccessResponseWithResultFailure(BusinessResponse bizResponse, String apiName) {
            if (BusinessResponse.RESULT_TIME_INVALID.equals(bizResponse.getCode())) {
                TimeStampManager.instance().updateTimeStamp(bizResponse.getT());//更新时间戳
                onRetry();
            } else if (BusinessResponse.RESULT_SESSION_INVALID.equals(bizResponse.getCode()) ||
                    BusinessResponse.RESULT_SESSION_LOSS.equals(bizResponse.getCode())) {
                LogUtils.d(TAG, "session is not exist");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO:需要重新登录，，
                        //TuyaSmartNetWork.needLogin("request_failure");
                    }
                });
                bizResponse.setCode(Integer.valueOf(CommonBusinessError.BUSINESS_NEED_LOGIN.getErrorCode()));
                onFailure(bizResponse, null, apiName);
            } else {
                onFailure(bizResponse, null, apiName);
            }
        }


        private void onSuccess(final BusinessResponse bizResponse, final T bizResult, final String apiName) {
            if (mHandler != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onSuccess(bizResponse, bizResult, apiName);
                        }
                    }
                });
            } else {
                if (listener != null) {
                    listener.onSuccess(bizResponse, bizResult, apiName);
                }
            }
        }

    }

}
