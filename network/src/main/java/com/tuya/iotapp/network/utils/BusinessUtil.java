package com.tuya.iotapp.network.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tuya.iotapp.common.utils.LogUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 业务工具类
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 8:15 PM
 */
public class BusinessUtil {

    /**
     * certificate pinning failure
     */
    public static final String ERROR_CODE_CERTIFICATE_FAILURE = "50502";//ssl handshake、certpathvalidatorexception、extcertpathvalidatorexception对应的证书错误
    public static final String ERROR_CODE_CERTIFICATE_UNACCEPTABLE_FAILER = "206008";//Unacceptable certificate
    public static final String ERROR_CODE_CERTIFICATE_EXPIRED = "206001";//Chain validation failed证书过期

    /**
     * 网络错误
     */
    public static final String ERROR_CODE_NETWORK_UNREACHABLE = "206002";//	Network is unreachable

    /**
     * 请求错误
     */
    public static final String ERROR_CODE_REQUEST_PINNING_FAILURE = "50516";
    public static final String ERROR_CODE_REQUEST_SOCKET_OPERATION_ON_NONE = "206003";//Socket operation on non-socket
    public static final String ERROR_CODE_REQUEST_SOCKET_CLOSED = "206004";//socket closed
    public static final String ERROR_CODE_REQUEST_STREAM_RESETED = "206005";//stream reseted
    public static final String ERROR_CODE_REQUEST_CONNECTION_ABORTED = "206006";//Software caused connection abort
    public static final String ERROR_CODE_REQUEST_RESOURCE_REDIRECT = "206007";//URL redirects to another url

    //证书错误错误码集合
    public static final List<String> ERROR_CODES_CERTIFICATE_PINNING = Arrays.asList(ERROR_CODE_CERTIFICATE_FAILURE, ERROR_CODE_CERTIFICATE_EXPIRED
            , ERROR_CODE_CERTIFICATE_UNACCEPTABLE_FAILER);
    //网络错误错误码集合
    public static final List<String> ERROR_CODES_NETWORK = Arrays.asList("50501", "103", "50408", "50504", "50503", "108", "50505", "50506", "50507"
            , "50508", ERROR_CODE_NETWORK_UNREACHABLE);
    //请求错误错误码集合
    public static final List<String> ERROR_CODES_REQUEST = Arrays.asList("50509", "50510", "50511", "50515", ERROR_CODE_REQUEST_PINNING_FAILURE, "50500", "101", "106"
            , ERROR_CODE_REQUEST_SOCKET_OPERATION_ON_NONE, ERROR_CODE_REQUEST_SOCKET_CLOSED, ERROR_CODE_REQUEST_STREAM_RESETED, ERROR_CODE_REQUEST_CONNECTION_ABORTED
            , ERROR_CODE_REQUEST_RESOURCE_REDIRECT);

    /**
     * 根据错误码归类出三种错误类型，UI层根据错误类型分别给出弹窗提示
     */
    public static final int ERROR_TYPE_NETWORK = 1;//网络错误
    public static final int ERROR_TYPE_REQUEST = 2;//请求错误
    public static final int ERROR_TYPE_CERTIFICATE_PINNING = 3;//证书错误

    public static int getErrorTypeByCode(String code) {
        if (ERROR_CODES_NETWORK.contains(code)) {
            return ERROR_TYPE_NETWORK;
        } else if (ERROR_CODES_REQUEST.contains(code)) {
            return ERROR_TYPE_REQUEST;
        } else if (ERROR_CODES_CERTIFICATE_PINNING.contains(code)) {
            return ERROR_TYPE_CERTIFICATE_PINNING;
        }
        return -1;
    }

    public static String checkNetwork(Context context, String message) {
        String network_error_message;
        if (message == null) {
            message = "";
        } else {
            message = message.toLowerCase();
        }
        String errorCode = getErrorCode(context, message);
        network_error_message = getString(context, "ty_network_error", "Network error, please retry.");
        if (ERROR_CODE_CERTIFICATE_FAILURE.equals(errorCode)) {
            network_error_message = getString(context, "ty_time_error", "Local clock is not accurate,please repair in time");
        } else if ("50501".equals(errorCode)) {
            return network_error_message;
        }

        LogUtils.d("network_error", message);
        return network_error_message;
    }


    public static String getString(Context context, String resId, String defaultValue) {
        try {
            return context.getString(context.getResources().getIdentifier(resId, "string", context.getPackageName()));
        } catch (Exception e) {
            return defaultValue;
        }
    }


    public static String getErrorCode(Context context, String message) {
        //其他错误码
        String errorCode = "50500";
        if (message == null) {
            message = "";
        } else {
            message = message.toLowerCase();
        }

        if (!TextUtils.isEmpty(message) && (message.contains("javax.net.ssl.sslhandshakeexception") || message.contains("java.security.cert.certpathvalidatorexception")
                || message.contains("com.android.org.bouncycastle.jce.exception.extcertpathvalidatorexception") || message.contains("SSL handshake aborted"))) {
            errorCode = ERROR_CODE_CERTIFICATE_FAILURE;
        } else if (message.contains("Chain validation failed")) {//证书过期
            errorCode = ERROR_CODE_CERTIFICATE_EXPIRED;
        } else if (message.contains("Unacceptable certificate")) {//手机本地时间戳和证书时间戳相差很大
            errorCode = ERROR_CODE_CERTIFICATE_UNACCEPTABLE_FAILER;
        } else {
            if (!NetworkUtil.isNetworkAvailable(context)) {
                /*
                网络不可用
                 */
                errorCode = "50501";
            } else {
                if ("timeout".equals(message)) {
                    errorCode = "50408";
                } else if (message.startsWith("unable to resolve host")) {
                    errorCode = "50504";
                } else if (message.startsWith("read timed out")) {
                    errorCode = "50503";
                } else if (message.startsWith("failed to connect to")) {
                    errorCode = "50505";
                } else if (message.startsWith("no route to host")) {
                    errorCode = "50506";
                } else if (message.startsWith("connect timed out")) {
                    errorCode = "50507";
                } else if (message.startsWith("ssl handshake timed out")) {
                    errorCode = "50508";
                } else if (message.startsWith("connection closed by peer")) {
                    errorCode = "50509";
                } else if (message.startsWith("stream was reset: protocol_error")) {
                    errorCode = "50510";
                } else if (message.startsWith("canceled")) {
                    errorCode = "50511";
                } else if (message.startsWith("502")) {
                    errorCode = "50512";
                } else if (message.startsWith("503")) {
                    errorCode = "50513";
                } else if (message.startsWith("json error")) {
                    errorCode = "50514";
                } else if (message.startsWith("Hostname") && message.contains("not verified")) {
                    // DNS 使用的IP是错误的，验证不通过
                    // 例子：
                    // Hostname a1.tuyacn.com not verified:
                    //    certificate: sha256/p59OrIQ4kFOSwyYwMXdkufL62igTJ+TnRPGUNW0lxiI=
                    //    DN: CN=*.newcapec.cn,OU=信息系统管理部,O=新开普电子股份有限公司,L=郑州,ST=河南,C=CN
                    //    subjectAltNames: [*.newcapec.cn, newcapec.cn]
                    //
                    errorCode = ERROR_CODE_REQUEST_PINNING_FAILURE;
                } else if (message.contains("certificate pinning failure")) {
                    /*
                    证书校验失败
                    例如：
                    Certificate pinning failure!
                          Peer certificate chain:
                            sha256/sryuXYKz7YiCYwjvt7axKDGJhCuAVXMnaxE1Q1Ga7QQ=: CN=*.tuyacn.com,O=杭州涂鸦信息技术有限公司,L=杭州市,C=CN
                            sha256/y0nWqsMxaCAR7VQ+sv7wp9hDLPMDR+Or1XLdu0n3Vlg=: C=NZ,ST=Auckland,L=Auckland,O=XK72 Ltd,OU=https://charlesproxy.com/ssl,CN=Charles Proxy CA (3 十二月 2018\, xushundeMacBook-Pro.local)
                          Pinned certificates for a1.tuyacn.com:
                            sha256/YhNNie7EoILoelAxSWD9rlGeQCILjsfs4E1RaoC1x90=
                     */

                    /*
                     *  javax.net.ssl.sslpeerunverifiedexception:
                     * certificate pinning failure!\n  peer certificate chain:
                     *     sha256/sryuxykz7yicywjvt7axkdgjhcuavxmnaxe1q1ga7qq=: cn=*.tuyacn.com,o=杭州涂鸦信息技术有限公司,l=杭州市,c=cn
                     *     sha256/y0nwqsmxacar7vq+sv7wp9hdlpmdr+or1xldu0n3vlg=: c=nz,st=auckland,l=auckland,o=xk72 ltd,ou=https://charlesproxy.com/ssl,cn=charles proxy ca (3 十二月 2018\\, xushundemacbook-pro.local)
                     *   pinned certificates for a1.tuyacn.com:
                     *     sha256/yhnnie7eoiloelaxswd9rlgeqciljsfs4e1raoc1x90=
                     *     sha256/8rw90ej3ttt8rrkrg+wyds9n7is03bk5bjp/uxptay8=
                     *     sha256/ko8tivdrejiy90ygasp6zpbu4jwxvhqvvqi0gs3gnda=
                     *     sha256/vjlze/p3w/pjnd6ll8jvnbcgqbzynfldzstiqco0sj8="
                     */
                    errorCode = ERROR_CODE_REQUEST_PINNING_FAILURE;
                } else if (message.contains("Network is unreachable")) {
                    errorCode = ERROR_CODE_NETWORK_UNREACHABLE;
                } else if (message.contains("Socket closed") || message.contains("Socket is closed")) {
                    errorCode = ERROR_CODE_REQUEST_SOCKET_CLOSED;
                } else if (message.contains("stream was reset")) {
                    errorCode = ERROR_CODE_REQUEST_STREAM_RESETED;
                } else if (message.contains("Software caused connection abort")) {
                    errorCode = ERROR_CODE_REQUEST_CONNECTION_ABORTED;
                } else if (message.contains("Unexpected response code for CONNECT: 302") || message.contains("Unexpected response code 302")) {
                    errorCode = ERROR_CODE_REQUEST_RESOURCE_REDIRECT;
                } else if (message.contains("Socket operation on non-socket")) {
                    errorCode = ERROR_CODE_REQUEST_SOCKET_OPERATION_ON_NONE;
                }
            }
        }
        LogUtils.d("BusinessUtil", "errorCode: " + errorCode);
        return errorCode;
    }

}
