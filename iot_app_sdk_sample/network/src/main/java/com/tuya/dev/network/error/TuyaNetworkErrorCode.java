package com.tuya.dev.network.error;

/**
 * Network error code
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/3/25 11:48 AM
 */
interface TuyaNetworkErrorCode {
    int SYSTEM_ERROR = 500;

    int INVALID_SING = 1004;

    int TOKEN_INVALID = 1010;

    int PARAM_ERROR = 1100;

    int PERMISSION_DENY = 1106;

    int PARAM_ILLEGAL = 1109;


}
