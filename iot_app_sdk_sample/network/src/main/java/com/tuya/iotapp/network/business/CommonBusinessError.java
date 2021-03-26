package com.tuya.iotapp.network.business;

/**
 * 业务通用错误
 *
 * @author xiaoxiao <a href="mailto:developer@tuya.com"/>
 * @since 2021/3/15 5:24 PM
 */
public enum CommonBusinessError {
    /**
     * 业务通用解析错误
     */
    BUSINESS_NETWORK_UNKNOWN("101", "network exception"), //网络异常
    BUSINESS_JSON_EXCEPTION("102", "json error"), //json转义失败
    BUSINESS_NETWORK_ERROR("103", "network exception"), //网络错误，无法访问网络
    BUSINESS_TIME_ERROR("104", "time error"), //手机时钟不对 https证书过期
    BUSINESS_NEED_LOGIN("105", "need login Exception"), //登陆需要session信息
    BUSINESS_IO_EXCEPTION("106", "io exception"), //同步io错误
    BUSINESS_DECRYPT_EXCEPTION("107", "decrypt exception"), //解密信息失败
    BUSINESS_READ_RESPONSE_TIMEOUT("108", "read timeout"), //读取超时
    ERROR_CODE_NETWORK_JSON_PARSE_EXCEPTION("101001", "JSON PARSE EXCEPTION");

    private String errorCode;
    private String errorMsg;

    CommonBusinessError(String errorCode, String errorMsg) {
        this.setErrorCode(errorCode);
        this.setErrorMsg(errorMsg);
    }

    public java.lang.String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(java.lang.String errorCode) {
        this.errorCode = errorCode;
    }

    public java.lang.String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(java.lang.String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
